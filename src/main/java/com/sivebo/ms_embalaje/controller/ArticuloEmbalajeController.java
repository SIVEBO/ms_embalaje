package com.sivebo.ms_embalaje.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivebo.ms_embalaje.dto.request.ArticuloEmbalajeRequest;
import com.sivebo.ms_embalaje.dto.response.ArticuloEmbalajeResponse;
import com.sivebo.ms_embalaje.service.ArticuloEmbalajeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/articulos")
@RequiredArgsConstructor
public class ArticuloEmbalajeController {

    private final ArticuloEmbalajeService service;

    @PostMapping
    public ResponseEntity<ArticuloEmbalajeResponse> crear(@Valid @RequestBody ArticuloEmbalajeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<ArticuloEmbalajeResponse>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloEmbalajeResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/categoria/{idCat}")
    public ResponseEntity<List<ArticuloEmbalajeResponse>> listarPorCategoria(@PathVariable Long idCat) {
        return ResponseEntity.ok(service.listarPorCategoria(idCat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloEmbalajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ArticuloEmbalajeRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ArticuloEmbalajeResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(service.desactivar(id));
    }
}