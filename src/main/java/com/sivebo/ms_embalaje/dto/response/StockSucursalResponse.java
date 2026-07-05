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
    private String nombreArt;
    private String nombreSucursal;
    private Integer cantidadDisponible;
    private LocalDateTime updatedAt;
}