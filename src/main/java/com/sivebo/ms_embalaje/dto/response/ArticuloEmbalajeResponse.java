package com.sivebo.ms_embalaje.dto.response;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloEmbalajeResponse {
    private Long idArt;
    private String nombreCategoria;
    private String nombre;
    private String descripcion;
    private BigDecimal precioVta;
    private Boolean activo;
}