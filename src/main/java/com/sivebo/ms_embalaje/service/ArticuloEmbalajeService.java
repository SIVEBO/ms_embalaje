package com.sivebo.ms_embalaje.service;

import java.util.List;

import com.sivebo.ms_embalaje.dto.request.ArticuloEmbalajeRequest;
import com.sivebo.ms_embalaje.dto.response.ArticuloEmbalajeResponse;

public interface ArticuloEmbalajeService {
    ArticuloEmbalajeResponse crear(ArticuloEmbalajeRequest request);
    ArticuloEmbalajeResponse obtenerPorId(Long id);
    List<ArticuloEmbalajeResponse> listarTodos();
    List<ArticuloEmbalajeResponse> listarPorCategoria(Long idCat);
    ArticuloEmbalajeResponse actualizar(Long id, ArticuloEmbalajeRequest request);
    ArticuloEmbalajeResponse desactivar(Long id);
}