package com.example.techmentor.bean.dto;

import java.io.Serializable;
import lombok.*;

/** DTO de token: datos que viajan entre la API y el cliente. */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;
    private String tipo;
    private Long idUsuario;
    private String username;
    private String rol;
}
