package com.sivebo.ms_embalaje.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sivebo.ms_embalaje.dto.request.CategoriaEmbalajeRequest;
import com.sivebo.ms_embalaje.dto.response.CategoriaEmbalajeResponse;
import com.sivebo.ms_embalaje.service.CategoriaEmbalajeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Categorías de Embalaje", description = "Gestión de categorías para artículos de embalaje")
@RestController
@RequestMapping("api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaEmbalajeController {

    private final CategoriaEmbalajeService service;

    @Operation(summary = "Crear categoría", description = "RF-26: registra una nueva categoría de embalaje")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Categoría creada"),
        @ApiResponse(responseCode = "409", description = "Nombre de categoría duplicado")
    })
    @PostMapping
    public ResponseEntity<CategoriaEmbalajeResponse> crear(@Valid @RequestBody CategoriaEmbalajeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @Operation(summary = "Listar todas las categorías")
    @ApiResponse(responseCode = "200", description = "Lista de categorías")
    @GetMapping
    public ResponseEntity<List<CategoriaEmbalajeResponse>> listar() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @Operation(summary = "Obtener categoría por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaEmbalajeResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Actualizar categoría")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categoría actualizada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaEmbalajeResponse> actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaEmbalajeRequest request) {
        return ResponseEntity.ok(service.actualizar(id, request));
    }

    @Operation(summary = "Eliminar categoría")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categoría eliminada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}