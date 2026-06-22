package com.sivebo.ms_embalaje.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockSucursalResponse {
    private Long idStock;
    private Long idArt;
    private String nombreArticulo;
    private Long idSucursal;
    private Integer cantidadDisponible;
    private LocalDateTime updatedAt;
}