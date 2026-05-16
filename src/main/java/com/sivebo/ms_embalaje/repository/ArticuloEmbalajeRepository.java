package com.sivebo.ms_embalaje.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sivebo.ms_embalaje.model.entity.ArticuloEmbalaje;

@Repository
public interface ArticuloEmbalajeRepository extends JpaRepository<ArticuloEmbalaje, Long> {
    List<ArticuloEmbalaje> findByCategoria_IdCat(Long idCat);
}