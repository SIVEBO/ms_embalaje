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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Stock por Sucursal", description = "control de stock de embalaje por sucursal")
@RestController
@RequestMapping("api/v1/stock")
@RequiredArgsConstructor
public class StockSucursalController {

    private final StockSucursalService service;

    @Operation(summary = "Crear registro de stock", description = "Inicializa el stock de un artículo en una sucursal")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Stock creado"),
        @ApiResponse(responseCode = "404", description = "Artículo no encontrado")
    })
    @PostMapping
    public ResponseEntity<StockSucursalResponse> crear(@Valid @RequestBody StockSucursalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @Operation(summary = "Obtener stock por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock encontrado"),
        @ApiResponse(responseCode = "404", description = "Registro de stock no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StockSucursalResponse> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Listar stock de una sucursal", description = "muestra disponibilidad de embalaje por sucursal")
    @ApiResponse(responseCode = "200", description = "Lista de stock por sucursal")
    @GetMapping("/sucursal/{nombreSucursal}")
    public ResponseEntity<List<StockSucursalResponse>> listarPorSucursal(@PathVariable String nombreSucursal) {
        return ResponseEntity.ok(service.listarPorSucursal(nombreSucursal));
    }

    @Operation(summary = "Actualizar cantidad en stock")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock actualizado"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @PatchMapping("/actualizar")
    public ResponseEntity<StockSucursalResponse> actualizarCantidad(
            @RequestParam String nombreArt,
            @RequestParam String nombreSucursal,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(service.actualizarCantidad(nombreArt, nombreSucursal, cantidad));
    }

    @Operation(summary = "Descontar stock por venta", description = "descuenta unidades vendidas del stock de sucursal")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Stock descontado"),
        @ApiResponse(responseCode = "400", description = "stock insuficiente")
    })
    @PatchMapping("/descontar")
    public ResponseEntity<StockSucursalResponse> descontarStock(
            @RequestParam String nombreArt,
            @RequestParam String nombreSucursal,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(service.descontarStock(nombreArt, nombreSucursal, cantidad));
    }

    @Operation(summary = "Verificar disponibilidad de stock", description = "verifica si hay stock suficiente antes de vender")
    @ApiResponse(responseCode = "200", description = "Booleano indicando si hay stock suficiente")
    @GetMapping("/verificar")
    public ResponseEntity<Boolean> verificarStock(
            @RequestParam String nombreArt,
            @RequestParam String nombreSucursal,
            @RequestParam Integer cantidadRequerida) {
        return ResponseEntity.ok(service.verificarStock(nombreArt, nombreSucursal, cantidadRequerida));
    }
}