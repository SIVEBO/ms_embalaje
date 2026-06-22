package com.sivebo.ms_embalaje.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sivebo.ms_embalaje.model.entity.CategoriaEmbalaje;

public interface CategoriaEmbalajeRepository extends JpaRepository<CategoriaEmbalaje, Long> {
}