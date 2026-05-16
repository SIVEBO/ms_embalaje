package com.sivebo.ms_embalaje.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoriaEmbalajeRequest {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombreCategoria;
}