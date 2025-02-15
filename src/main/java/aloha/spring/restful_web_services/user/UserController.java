package aloha.spring.restful_web_services.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "User API", description = "Operations related to users")
@RestController
public class UserController {

    private static Link allUsersLink = linkTo(methodOn(UserController.class).allUsers()).withRel("all-users");
    private UserDaoService dao;

    public UserController(UserDaoService dao) {
        this.dao = dao;
    }

    @Operation(summary = "Get all users", description = "Get all users", responses = {
            @ApiResponse(responseCode = "200", description = "Successful response"),
            @ApiResponse(responseCode = "404", description = "Users not found")
    })
    @GetMapping(path = "/users", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE })
    public List<User> allUsers() {
        return dao.findAll();
    }

    @Parameter(name = "id", description = "User id", required = true, example = "123")
    @Operation(summary = "Get a user by ID", description = "Returns user details for a given ID", responses = {
            // @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "200", description = "User found", content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class)),
                    @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = User.class)),
                    @Content(mediaType = MediaType.APPLICATION_YAML_VALUE, schema = @Schema(implementation = User.class))
            }),
            // @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping(path = "/users/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE })
    public EntityModel<User> getUser(@PathVariable("id") int id) {
        User user = dao.findOne(id);
        if (user == null) {
            // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            throw new UserNotFoundException();
        } else {
            return entityModel(user);
        }
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to be created", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
    @Operation(summary = "Create a user", description = "Create a user", responses = {
            @ApiResponse(responseCode = "201", description = "User created", content = {
                @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class)),
            })
    })
    @PostMapping("/users")
    public EntityModel<User> createUser(
            @Valid @RequestBody User user) {
        User savedUser = dao.save(user);
        return entityModel(savedUser);
    }
    // public ResponseEntity<User> createUser(
    // @Valid @RequestBody User user) {
    // User savedUser = dao.save(user);
    // // Return the location URI of a created resource.
    // URI location =
    // ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
    // .toUri();
    // return ResponseEntity.created(location).build();
    // }

    private EntityModel<User> entityModel(User user) {
        EntityModel<User> entityModel = EntityModel.of(user);
        Link userLink = linkTo(methodOn(UserController.class).getUser(user.getId())).withRel("get-user");
        Link deleteLink = linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete-user");
        entityModel.add(allUsersLink, userLink, deleteLink);
        return entityModel;
    }

    @Parameter(name = "id", description = "User id", required = true, example = "123")
    @Operation(summary = "Delete a user by ID", description = "Delete a user by ID", responses = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        if (!dao.deleteById(id)) {
            throw new UserNotFoundException();
        }
        return new ResponseEntity<>(HttpStatusCode.valueOf(204));
    }
}
