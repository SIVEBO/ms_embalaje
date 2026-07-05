package com.sivebo.ms_embalaje.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sivebo.ms_embalaje.dto.request.ArticuloEmbalajeRequest;
import com.sivebo.ms_embalaje.dto.response.ArticuloEmbalajeResponse;
import com.sivebo.ms_embalaje.exception.RecursoNoEncontradoException;
import com.sivebo.ms_embalaje.model.entity.ArticuloEmbalaje;
import com.sivebo.ms_embalaje.repository.ArticuloEmbalajeRepository;
import com.sivebo.ms_embalaje.repository.CategoriaEmbalajeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticuloEmbalajeService{

    private final ArticuloEmbalajeRepository repository;
    private final CategoriaEmbalajeRepository categoriaRepository;


    public ArticuloEmbalajeResponse crear(ArticuloEmbalajeRequest request) {
        log.info("Creando artículo: {}", request.getNombre());
        categoriaRepository.findByNombreCategoria(request.getNombreCategoria())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada"));
        ArticuloEmbalaje articulo = new ArticuloEmbalaje();
        articulo.setNombreCategoria(request.getNombreCategoria());
        articulo.setNombre(request.getNombre());
        articulo.setDescripcion(request.getDescripcion());
        articulo.setPrecioVta(request.getPrecioVta());
        articulo.setActivo(true);
        return toResponse(repository.save(articulo));
    }


    public ArticuloEmbalajeResponse obtenerPorId(Long id) {
        log.info("Buscando artículo id: {}", id);
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Artículo no encontrado con id: " + id)));
    }


    public List<ArticuloEmbalajeResponse> listarTodos() {
        log.info("Listando todos los artículos");
        return repository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }


    public List<ArticuloEmbalajeResponse> listarPorCategoria(String nombreCategoria) {
        log.info("Listando artículos de categoría: {}", nombreCategoria);
        return repository.findByNombreCategoria(nombreCategoria).stream().map(this::toResponse).collect(Collectors.toList());
    }


    public ArticuloEmbalajeResponse actualizar(Long id, ArticuloEmbalajeRequest request) {
        log.info("Actualizando artículo id: {}", id);
        ArticuloEmbalaje articulo = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Artículo no encontrado con id: " + id));
        categoriaRepository.findByNombreCategoria(request.getNombreCategoria())
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada"));
        articulo.setNombreCategoria(request.getNombreCategoria());
        articulo.setNombre(request.getNombre());
        articulo.setDescripcion(request.getDescripcion());
        articulo.setPrecioVta(request.getPrecioVta());
        return toResponse(repository.save(articulo));
    }


    public ArticuloEmbalajeResponse desactivar(Long id) {
        log.info("Desactivando artículo id: {}", id);
        ArticuloEmbalaje articulo = repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Artículo no encontrado con id: " + id));
        articulo.setActivo(false);
        return toResponse(repository.save(articulo));
    }

    private ArticuloEmbalajeResponse toResponse(ArticuloEmbalaje a) {
        ArticuloEmbalajeResponse r = new ArticuloEmbalajeResponse();
        r.setIdArt(a.getIdArt());
        r.setNombreCategoria(a.getNombreCategoria());
        r.setNombre(a.getNombre());
        r.setDescripcion(a.getDescripcion());
        r.setPrecioVta(a.getPrecioVta());
        r.setActivo(a.getActivo());
        return r;
    }
}