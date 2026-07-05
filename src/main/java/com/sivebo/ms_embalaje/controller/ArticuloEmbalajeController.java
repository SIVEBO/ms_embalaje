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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Artículos de Embalaje", description = "Gestión del catálogo de artículos de embalaje")
@RestController
@RequestMapping("api/v1/articulos")
@RequiredArgsConstructor
public class ArticuloEmbalajeController {

    private final ArticuloEmbalajeService service;

    @Operation(summary = "Crear artículo de embalaje", description = "registra un nuevo artículo con categoría y precio")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Artículo creado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PostMapping
    public ResponseEntity<ArticuloEmbalajeResponse> crear(@Valid @RequestBody ArticuloEmbalajeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @Operation(summary = "Listar todos los artículos")
    @ApiResponse(responseCode = "200", description = "Lista de artículos")
    @GetMapping
    public ResponseEntity<List<ArticuloEmbalajeResponse>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Obtener artículo por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artículo encontrado"),
        @ApiResponse(responseCode = "404", description = "Artículo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArticuloEmbalajeResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Listar artículos por categoría")
    @ApiResponse(responseCode = "200", description = "Lista filtrada por categoría")
    @GetMapping("/categoria/{nombreCategoria}")
    public ResponseEntity<List<ArticuloEmbalajeResponse>> listarPorCategoria(@PathVariable String nombreCategoria) {
        return ResponseEntity.ok(service.listarPorCategoria(nombreCategoria));
    }

    @Operation(summary = "Actualizar artículo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artículo actualizado"),
        @ApiResponse(responseCode = "404", description = "Artículo o categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ArticuloEmbalajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody ArticuloEmbalajeRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Desactivar artículo", description = "Marca el artículo como inactivo sin eliminarlo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Artículo desactivado"),
        @ApiResponse(responseCode = "404", description = "Artículo no encontrado")
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<ArticuloEmbalajeResponse> desactivar(@PathVariable Long id) {
        return ResponseEntity.ok(service.desactivar(id));
    }
}