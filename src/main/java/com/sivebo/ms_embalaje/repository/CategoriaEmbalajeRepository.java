package com.sivebo.ms_embalaje.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivebo.ms_embalaje.model.entity.CategoriaEmbalaje;

@Repository
public interface CategoriaEmbalajeRepository extends JpaRepository<CategoriaEmbalaje, Long> {
}