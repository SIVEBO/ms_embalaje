package com.sivebo.ms_embalaje.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sivebo.ms_embalaje.model.entity.StockSucursal;

public interface StockSucursalRepository extends JpaRepository<StockSucursal, Long> {
    List<StockSucursal> findByIdSucursal(Long idSucursal);
    Optional<StockSucursal> findByArticulo_IdArtAndIdSucursal(Long idArt, Long idSucursal);
}