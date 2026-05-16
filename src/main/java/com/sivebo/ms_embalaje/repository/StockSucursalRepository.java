package com.sivebo.ms_embalaje.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivebo.ms_embalaje.model.entity.StockSucursal;

@Repository
public interface StockSucursalRepository extends JpaRepository<StockSucursal, Long> {
    List<StockSucursal> findByIdSucursal(Long idSucursal);
    Optional<StockSucursal> findByArticulo_IdArtAndIdSucursal(Long idArt, Long idSucursal);
}