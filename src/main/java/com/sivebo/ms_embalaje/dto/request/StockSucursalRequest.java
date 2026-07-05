package com.sivebo.ms_embalaje.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockSucursalRequest {

    @NotBlank(message = "El artículo es obligatorio")
    private String nombreArt;

    @NotBlank(message = "La sucursal es obligatoria")
    private String nombreSucursal;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 0, message = "La cantidad no puede ser negativa")
    private Integer cantidadDisponible;
}