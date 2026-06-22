package com.sivebo.ms_embalaje.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sivebo.ms_embalaje.model.entity.ArticuloEmbalaje;

public interface ArticuloEmbalajeRepository extends JpaRepository<ArticuloEmbalaje, Long> {
    List<ArticuloEmbalaje> findByCategoria_IdCat(Long idCat);
}