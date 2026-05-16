package com.sivebo.ms_embalaje.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sivebo.ms_embalaje.dto.request.CategoriaEmbalajeRequest;
import com.sivebo.ms_embalaje.dto.response.CategoriaEmbalajeResponse;
import com.sivebo.ms_embalaje.model.entity.CategoriaEmbalaje;
import com.sivebo.ms_embalaje.repository.CategoriaEmbalajeRepository;
import com.sivebo.ms_embalaje.service.CategoriaEmbalajeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaEmbalajeServiceImpl implements CategoriaEmbalajeService {

    private static final Logger log = LoggerFactory.getLogger(CategoriaEmbalajeServiceImpl.class);
    private final CategoriaEmbalajeRepository repository;

    @Override
    public CategoriaEmbalajeResponse crear(CategoriaEmbalajeRequest request) {
        log.info("Creando categoría: {}", request.getNombreCategoria());
        CategoriaEmbalaje categoria = new CategoriaEmbalaje();
        categoria.setNombreCategoria(request.getNombreCategoria());
        return toResponse(repository.save(categoria));
    }

    @Override
    public CategoriaEmbalajeResponse obtenerPorId(Long id) {
        log.info("Buscando categoría id: {}", id);
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id)));
    }

    @Override
    public List<CategoriaEmbalajeResponse> listarTodas() {
        log.info("Listando todas las categorías");
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public CategoriaEmbalajeResponse actualizar(Long id, CategoriaEmbalajeRequest request) {
        log.info("Actualizando categoría id: {}", id);
        CategoriaEmbalaje categoria = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + id));
        categoria.setNombreCategoria(request.getNombreCategoria());
        return toResponse(repository.save(categoria));
    }

    @Override
    public void eliminar(Long id) {
        log.info("Eliminando categoría id: {}", id);
        repository.deleteById(id);
    }

    private CategoriaEmbalajeResponse toResponse(CategoriaEmbalaje c) {
        CategoriaEmbalajeResponse r = new CategoriaEmbalajeResponse();
        r.setIdCat(c.getIdCat());
        r.setNombreCategoria(c.getNombreCategoria());
        return r;
    }
}