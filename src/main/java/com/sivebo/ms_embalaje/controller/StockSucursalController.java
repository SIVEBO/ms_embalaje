package com.sivebo.ms_embalaje.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sivebo.ms_embalaje.dto.request.StockSucursalRequest;
import com.sivebo.ms_embalaje.dto.response.StockSucursalResponse;
import com.sivebo.ms_embalaje.service.StockSucursalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stock")
@RequiredArgsConstructor
public class StockSucursalController {

    private final StockSucursalService service;

    @PostMapping
    public ResponseEntity<StockSucursalResponse> crear(@Valid @RequestBody StockSucursalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockSucursalResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @GetMapping("/sucursal/{idSucursal}")
    public ResponseEntity<List<StockSucursalResponse>> listarPorSucursal(@PathVariable Long idSucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(idSucursal));
    }

    @PatchMapping("/actualizar")
    public ResponseEntity<StockSucursalResponse> actualizarCantidad(
            @RequestParam Long idArt,
            @RequestParam Long idSucursal,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(service.actualizarCantidad(idArt, idSucursal, cantidad));
    }

    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarStock(
            @RequestParam Long idArt,
            @RequestParam Long idSucursal,
            @RequestParam Integer cantidadRequerida) {
        return ResponseEntity.ok(service.verificarStock(idArt, idSucursal, cantidadRequerida));
    }
}