package com.sivebo.ms_embalaje.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sivebo.ms_embalaje.model.entity.CategoriaEmbalaje;

public interface CategoriaEmbalajeRepository extends JpaRepository<CategoriaEmbalaje, Long> {
    boolean existsByNombreCategoria(String nombreCategoria);
    Optional<CategoriaEmbalaje> findByNombreCategoria(String nombreCategoria);
}