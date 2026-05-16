package com.sivebo.ms_embalaje.service;

import java.util.List;

import com.sivebo.ms_embalaje.dto.request.CategoriaEmbalajeRequest;
import com.sivebo.ms_embalaje.dto.response.CategoriaEmbalajeResponse;

public interface CategoriaEmbalajeService {
    CategoriaEmbalajeResponse crear(CategoriaEmbalajeRequest request);
    CategoriaEmbalajeResponse obtenerPorId(Long id);
    List<CategoriaEmbalajeResponse> listarTodas();
    CategoriaEmbalajeResponse actualizar(Long id, CategoriaEmbalajeRequest request);
    void eliminar(Long id);
}