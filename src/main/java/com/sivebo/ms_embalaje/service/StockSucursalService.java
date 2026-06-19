package com.sivebo.ms_embalaje.service;

import java.util.List;

import com.sivebo.ms_embalaje.dto.request.StockSucursalRequest;
import com.sivebo.ms_embalaje.dto.response.StockSucursalResponse;

public interface StockSucursalService {
    StockSucursalResponse crear(StockSucursalRequest request);
    StockSucursalResponse obtenerPorId(Long id);
    List<StockSucursalResponse> listarPorSucursal(Long idSucursal);
    StockSucursalResponse actualizarCantidad(Long idArt, Long idSucursal, Integer cantidad);
    StockSucursalResponse descontarStock(Long idArt, Long idSucursal, Integer cantidadVendida);
    Boolean verificarStock(Long idArt, Long idSucursal, Integer cantidadRequerida);
}