package com.sivebo.ms_embalaje.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sivebo.ms_embalaje.dto.request.StockSucursalRequest;
import com.sivebo.ms_embalaje.dto.response.StockSucursalResponse;
import com.sivebo.ms_embalaje.exception.RecursoNoEncontradoException;
import com.sivebo.ms_embalaje.exception.ReglaNegocioException;
import com.sivebo.ms_embalaje.model.entity.ArticuloEmbalaje;
import com.sivebo.ms_embalaje.model.entity.StockSucursal;
import com.sivebo.ms_embalaje.repository.ArticuloEmbalajeRepository;
import com.sivebo.ms_embalaje.repository.StockSucursalRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockSucursalService{

    private final StockSucursalRepository repository;
    private final ArticuloEmbalajeRepository articuloRepository;


    public StockSucursalResponse crear(StockSucursalRequest request) {
        log.info("Creando stock para artículo: {} en sucursal: {}", request.getNombreArt(), request.getNombreSucursal());
        ArticuloEmbalaje articulo = articuloRepository.findByNombre(request.getNombreArt())
                .orElseThrow(() -> new RecursoNoEncontradoException("Artículo no encontrado"));
        StockSucursal stock = new StockSucursal();
        stock.setNombreArt(articulo.getNombre());
        stock.setNombreSucursal(request.getNombreSucursal());
        stock.setCantidadDisponible(request.getCantidadDisponible());
        return toResponse(repository.save(stock));
    }


    public StockSucursalResponse obtenerPorId(Long id) {
        log.info("Buscando stock id: {}", id);
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Stock no encontrado con id: " + id)));
    }


    public List<StockSucursalResponse> listarPorSucursal(String nombreSucursal) {
        log.info("Listando stock de sucursal: {}", nombreSucursal);
        return repository.findByNombreSucursal(nombreSucursal).stream().map(this::toResponse).collect(Collectors.toList());
    }


    public StockSucursalResponse actualizarCantidad(String nombreArt, String nombreSucursal, Integer cantidad) {
        log.info("Actualizando stock artículo: {} sucursal: {}", nombreArt, nombreSucursal);
        StockSucursal stock = repository.findByNombreArtAndNombreSucursal(nombreArt, nombreSucursal)
                .orElseThrow(() -> new RecursoNoEncontradoException("Stock no encontrado"));
        stock.setCantidadDisponible(cantidad);
        return toResponse(repository.save(stock));
    }


    public StockSucursalResponse descontarStock(String nombreArt, String nombreSucursal, Integer cantidadVendida) {
        log.info("Descontando {} unidades del artículo: {} en sucursal: {}", cantidadVendida, nombreArt, nombreSucursal);
        StockSucursal stock = repository.findByNombreArtAndNombreSucursal(nombreArt, nombreSucursal)
                .orElseThrow(() -> new RecursoNoEncontradoException("Stock no encontrado para este artículo y sucursal"));

        if (stock.getCantidadDisponible() < cantidadVendida) {
            throw new ReglaNegocioException("Stock insuficiente. Disponible: " + stock.getCantidadDisponible()
                    + ", solicitado: " + cantidadVendida);
        }

        stock.setCantidadDisponible(stock.getCantidadDisponible() - cantidadVendida);
        return toResponse(repository.save(stock));
    }


    public Boolean verificarStock(String nombreArt, String nombreSucursal, Integer cantidadRequerida) {
        log.info("Verificando stock artículo: {} sucursal: {}", nombreArt, nombreSucursal);
        return repository.findByNombreArtAndNombreSucursal(nombreArt, nombreSucursal)
                .map(s -> s.getCantidadDisponible() >= cantidadRequerida)
                .orElse(false);
    }

    private StockSucursalResponse toResponse(StockSucursal s) {
        StockSucursalResponse r = new StockSucursalResponse();
        r.setIdStock(s.getIdStock());
        r.setNombreArt(s.getNombreArt());
        r.setNombreSucursal(s.getNombreSucursal());
        r.setCantidadDisponible(s.getCantidadDisponible());
        return r;
    }
}