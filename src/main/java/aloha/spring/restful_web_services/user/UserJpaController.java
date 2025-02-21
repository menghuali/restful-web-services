package aloha.spring.restful_web_services.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import aloha.spring.restful_web_services.jpa.PostRepo;
import aloha.spring.restful_web_services.jpa.UserRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "JPA User API", description = "Operations related to users in db.")
@RestController
public class UserJpaController {

    private static Link allUsersLink = linkTo(methodOn(UserController.class).allUsers()).withRel("all-users");

    private UserRepo userRepo;

    private PostRepo postRepo;

    public UserJpaController(UserRepo userRepo, PostRepo postRepo) {
        this.userRepo = userRepo;
        this.postRepo = postRepo;
    }

    @PostMapping("/jpa/users/{id}/posts")
    public EntityModel<Post> createPost(@PathVariable(name = "id") Integer id, @Valid @RequestBody Post post) {
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent()) {
            throw new UserNotFoundException();
        }

        post.setUser(user.get());
        post = postRepo.save(post);
        return postEntityModel(post);
    }

    @Operation(summary = "Get all users", description = "Get all users", responses = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping(path = "/jpa/users", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE })
    public List<User> allUsers() {
        return userRepo.findAll();
    }

    @Parameter(name = "id", description = "User id", required = true, example = "123")
    @Operation(summary = "Get a user by ID", description = "Returns user details for a given ID", responses = {
            @ApiResponse(responseCode = "200", description = "User found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = User.class)),
                    @Content(mediaType = MediaType.APPLICATION_YAML_VALUE, schema = @Schema(implementation = User.class))
            })
    })
    @GetMapping(path = "/jpa/users/{id}", produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE })
    public EntityModel<User> getUser(@PathVariable("id") int id) {
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent()) {
            // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            throw new UserNotFoundException();
        } else {
            return userEntityModel(user.get());
        }
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to be created", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @Operation(summary = "Create a user", description = "Create a user", responses = {
            @ApiResponse(responseCode = "201", description = "User created", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class)),
            })
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/jpa/users")
    public EntityModel<User> createUser(
            @Valid @RequestBody User user) {
        User savedUser = userRepo.save(user);
        return userEntityModel(savedUser);
    }

    @Parameter(name = "id", description = "User id", required = true, example = "123")
    @Operation(summary = "Delete a user by ID", description = "Delete a user by ID", responses = {
            @ApiResponse(responseCode = "204", description = "User deleted")
    })
    @DeleteMapping("/jpa/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(204));
    }

    @Parameter(name = "id", description = "User id", required = true, example = "123")
    @Operation(summary = "Get all posts of a user", description = "Returns all posts of a user found by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = User.class)),
                    @Content(mediaType = MediaType.APPLICATION_YAML_VALUE, schema = @Schema(implementation = User.class))
            })
    })
    @GetMapping("/jpa/users/{id}/posts")
    public List<Post> getUserPosts(@PathVariable Integer id) {
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent()) {
            // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            throw new UserNotFoundException();
        } else {
            return user.get().getPosts();
        }
    }

    /**
     * Generate an entity model as API response from User object. The generated
     * entity model will also have HATEOAS links.
     * 
     * @param user The User object from which entity model will be generated.
     * @return Entity model generted from User object.
     */
    private EntityModel<User> userEntityModel(User user) {
        EntityModel<User> entityModel = EntityModel.of(user);
        Link userLink = linkTo(methodOn(UserController.class).getUser(user.getId())).withRel("get-user");
        Link deleteLink = linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete-user");
        entityModel.add(allUsersLink, userLink, deleteLink);
        return entityModel;
    }

    private EntityModel<Post> postEntityModel(Post post) {
        EntityModel<Post> entityModel = EntityModel.of(post);
        Link postsLink = linkTo(methodOn(UserJpaController.class).getUserPosts(post.getUser().getId()))
                .withRel("get-user-posts");
        // Link deleteLink =
        // linkTo(methodOn(UserJpaController.class).deleteUser(user.getId())).withRel("delete-user");
        entityModel.add(postsLink);
        return entityModel;
    }

}
