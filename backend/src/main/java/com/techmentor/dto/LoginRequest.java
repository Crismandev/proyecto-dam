package com.techmentor.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "El usuario o correo es obligatorio")
    @JsonAlias({"correo", "email"})
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @JsonAlias({"password"})
    private String contrasena;
}
