package com.sivebo.ms_embalaje.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sivebo.ms_embalaje.dto.request.CategoriaEmbalajeRequest;
import com.sivebo.ms_embalaje.dto.response.CategoriaEmbalajeResponse;
import com.sivebo.ms_embalaje.exception.RecursoNoEncontradoException;
import com.sivebo.ms_embalaje.exception.ReglaNegocioException;
import com.sivebo.ms_embalaje.model.entity.CategoriaEmbalaje;
import com.sivebo.ms_embalaje.repository.CategoriaEmbalajeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriaEmbalajeService{

    private final CategoriaEmbalajeRepository repository;

    
    public CategoriaEmbalajeResponse crear(CategoriaEmbalajeRequest request) {
        log.info("Creando categoría: {}", request.getNombreCategoria());
        if (repository.existsByNombreCategoria(request.getNombreCategoria())) {
            throw new ReglaNegocioException(
                    "Ya existe una categoría con el nombre: " + request.getNombreCategoria());
        }
        CategoriaEmbalaje categoria = new CategoriaEmbalaje();
        categoria.setNombreCategoria(request.getNombreCategoria());
        return toResponse(repository.save(categoria));
    }

    
    public CategoriaEmbalajeResponse obtenerPorId(Long id) {
        log.info("Buscando categoría id: {}", id);
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con id: " + id)));
    }


    public List<CategoriaEmbalajeResponse> listarTodas() {
        log.info("Listando todas las categorías");
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }


    public CategoriaEmbalajeResponse actualizar(Long id, CategoriaEmbalajeRequest request) {
        log.info("Actualizando categoría id: {}", id);
        CategoriaEmbalaje categoria = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada con id: " + id));
        categoria.setNombreCategoria(request.getNombreCategoria());
        return toResponse(repository.save(categoria));
    }

    
    public void eliminar(Long id) {
        log.info("Eliminando categoría id: {}", id);
        if (!repository.existsById(id)) {
            throw new RecursoNoEncontradoException("Categoría no encontrada con id: " + id);
        }
        repository.deleteById(id);
    }

    private CategoriaEmbalajeResponse toResponse(CategoriaEmbalaje c) {
        CategoriaEmbalajeResponse r = new CategoriaEmbalajeResponse();
        r.setIdCat(c.getIdCat());
        r.setNombreCategoria(c.getNombreCategoria());
        return r;
    }
}