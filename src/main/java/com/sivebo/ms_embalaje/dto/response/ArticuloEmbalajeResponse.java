package com.sivebo.ms_embalaje.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ArticuloEmbalajeResponse {
    private Long idArt;
    private Long idCat;
    private String nombreCategoria;
    private String nombre;
    private String descripcion;
    private BigDecimal precioVta;
    private Boolean activo;
}