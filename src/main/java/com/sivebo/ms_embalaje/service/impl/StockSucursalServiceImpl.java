package com.sivebo.ms_embalaje.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sivebo.ms_embalaje.dto.request.StockSucursalRequest;
import com.sivebo.ms_embalaje.dto.response.StockSucursalResponse;
import com.sivebo.ms_embalaje.exception.RecursoNoEncontradoException;
import com.sivebo.ms_embalaje.exception.ReglaNegocioException;
import com.sivebo.ms_embalaje.model.entity.ArticuloEmbalaje;
import com.sivebo.ms_embalaje.model.entity.StockSucursal;
import com.sivebo.ms_embalaje.repository.ArticuloEmbalajeRepository;
import com.sivebo.ms_embalaje.repository.StockSucursalRepository;
import com.sivebo.ms_embalaje.service.StockSucursalService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockSucursalServiceImpl implements StockSucursalService {

    private static final Logger log = LoggerFactory.getLogger(StockSucursalServiceImpl.class);
    private final StockSucursalRepository repository;
    private final ArticuloEmbalajeRepository articuloRepository;

    @Override
    public StockSucursalResponse crear(StockSucursalRequest request) {
        log.info("Creando stock para artículo id: {} en sucursal id: {}", request.getIdArt(), request.getIdSucursal());
        ArticuloEmbalaje articulo = articuloRepository.findById(request.getIdArt())
                .orElseThrow(() -> new RecursoNoEncontradoException("Artículo no encontrado"));
        StockSucursal stock = new StockSucursal();
        stock.setArticulo(articulo);
        stock.setIdSucursal(request.getIdSucursal());
        stock.setCantidadDisponible(request.getCantidadDisponible());
        return toResponse(repository.save(stock));
    }

    @Override
    public StockSucursalResponse obtenerPorId(Long id) {
        log.info("Buscando stock id: {}", id);
        return toResponse(repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Stock no encontrado con id: " + id)));
    }

    @Override
    public List<StockSucursalResponse> listarPorSucursal(Long idSucursal) {
        log.info("Listando stock de sucursal id: {}", idSucursal);
        return repository.findByIdSucursal(idSucursal).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public StockSucursalResponse actualizarCantidad(Long idArt, Long idSucursal, Integer cantidad) {
        log.info("Actualizando stock artículo id: {} sucursal id: {}", idArt, idSucursal);
        StockSucursal stock = repository.findByArticulo_IdArtAndIdSucursal(idArt, idSucursal)
                .orElseThrow(() -> new RecursoNoEncontradoException("Stock no encontrado"));
        stock.setCantidadDisponible(cantidad);
        return toResponse(repository.save(stock));
    }

    @Override
    public StockSucursalResponse descontarStock(Long idArt, Long idSucursal, Integer cantidadVendida) {
        log.info("Descontando {} unidades del artículo id: {} en sucursal id: {}", cantidadVendida, idArt, idSucursal);
        StockSucursal stock = repository.findByArticulo_IdArtAndIdSucursal(idArt, idSucursal)
                .orElseThrow(() -> new RecursoNoEncontradoException("Stock no encontrado para este artículo y sucursal"));

        if (stock.getCantidadDisponible() < cantidadVendida) {
            throw new ReglaNegocioException("Stock insuficiente. Disponible: " + stock.getCantidadDisponible()
                    + ", solicitado: " + cantidadVendida);
        }

        stock.setCantidadDisponible(stock.getCantidadDisponible() - cantidadVendida);
        return toResponse(repository.save(stock));
    }

    @Override
    public Boolean verificarStock(Long idArt, Long idSucursal, Integer cantidadRequerida) {
        log.info("Verificando stock artículo id: {} sucursal id: {}", idArt, idSucursal);
        return repository.findByArticulo_IdArtAndIdSucursal(idArt, idSucursal)
                .map(s -> s.getCantidadDisponible() >= cantidadRequerida)
                .orElse(false);
    }

    private StockSucursalResponse toResponse(StockSucursal s) {
        StockSucursalResponse r = new StockSucursalResponse();
        r.setIdStock(s.getIdStock());
        r.setIdArt(s.getArticulo().getIdArt());
        r.setNombreArticulo(s.getArticulo().getNombre());
        r.setIdSucursal(s.getIdSucursal());
        r.setCantidadDisponible(s.getCantidadDisponible());
        return r;
    }
}