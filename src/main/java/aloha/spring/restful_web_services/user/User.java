package aloha.spring.restful_web_services.user;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

@Schema(description = "User entity")
public class User {

    @Schema(description = "User ID", example = "123")
    private Integer id;

    @JsonProperty(value = "user_name")
    @Schema(description = "User name", example = "John")
    @NotBlank(message = "User name can't be blank")
    private String name;

    @JsonProperty("birth_date")
    @Schema(description = "User birth date", example = "2000-01-01")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    public User() {
    }

    public User(Integer id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", birthDate=" + birthDate + "]";
    }

}
