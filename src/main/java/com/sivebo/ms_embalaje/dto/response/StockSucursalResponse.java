package com.sivebo.ms_embalaje.dto.response;

import lombok.Data;

@Data
public class StockSucursalResponse {
    private Long idStock;
    private Long idArt;
    private String nombreArticulo;
    private Long idSucursal;
    private Integer cantidadDisponible;
}