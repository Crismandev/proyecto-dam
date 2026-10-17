package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

/** DTO de login: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
