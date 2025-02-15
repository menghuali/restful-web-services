package aloha.spring.restful_web_services.helloworld;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Hello World API", description = "Hello World application for RESTful API")
@RestController
public class HelloWorldController {

    private MessageSource messageSource;

    public HelloWorldController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Hidden // Hide this API from documents◊
    @Operation(summary = "Hello world", description = "Returns Hello World")
    // @RequestMapping(method = RequestMethod.GET, path = "/hello-world")
    @GetMapping(path = "/hello-world")
    public String helloWorld() {
        return "Hello World";
    }

    @Parameter(name = "name", description = "A name", required = true, example = "Mike")
    @Operation(summary = "Hello world with username", description = "Returns Hello World <username>")
    @GetMapping(path = "/hello-world-bean/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable("name") String name) {
        return new HelloWorldBean("Hello World, " + name + "!");
    }

    @Operation(summary = "Hello World i18n", description = "Internationalized Hello World")
    @GetMapping("/hello-world-i18n")
    public String helloWorldI18n() {
        Locale locale = LocaleContextHolder.getLocale();
        String message = messageSource.getMessage("good.morning", null, "Morning", locale);
        return message;
    }

}
