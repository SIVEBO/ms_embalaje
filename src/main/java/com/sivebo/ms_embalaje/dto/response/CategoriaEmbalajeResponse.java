package com.sivebo.ms_embalaje.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaEmbalajeResponse {
    private Long idCat;
    private String nombreCategoria;
}