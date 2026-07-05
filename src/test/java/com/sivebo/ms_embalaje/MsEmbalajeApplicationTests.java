package com.sivebo.ms_embalaje;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivebo.ms_embalaje.dto.request.ArticuloEmbalajeRequest;
import com.sivebo.ms_embalaje.dto.response.ArticuloEmbalajeResponse;
import com.sivebo.ms_embalaje.dto.response.StockSucursalResponse;
import com.sivebo.ms_embalaje.exception.RecursoNoEncontradoException;
import com.sivebo.ms_embalaje.exception.ReglaNegocioException;
import com.sivebo.ms_embalaje.model.entity.ArticuloEmbalaje;
import com.sivebo.ms_embalaje.model.entity.CategoriaEmbalaje;
import com.sivebo.ms_embalaje.model.entity.StockSucursal;
import com.sivebo.ms_embalaje.repository.ArticuloEmbalajeRepository;
import com.sivebo.ms_embalaje.repository.CategoriaEmbalajeRepository;
import com.sivebo.ms_embalaje.repository.StockSucursalRepository;
import com.sivebo.ms_embalaje.service.ArticuloEmbalajeService;
import com.sivebo.ms_embalaje.service.StockSucursalService;

@ExtendWith(MockitoExtension.class)
class MsEmbalajeApplicationTests {

    @Nested
    class ArticuloEmbalajeTests {

        @Mock
        private ArticuloEmbalajeRepository repository;
        @Mock
        private CategoriaEmbalajeRepository categoriaRepository;
        @InjectMocks
        private ArticuloEmbalajeService service;

        private CategoriaEmbalaje categoria;
        private ArticuloEmbalaje articulo;

        @BeforeEach
        void setUp() {
            categoria = new CategoriaEmbalaje();
            categoria.setIdCat(1L);
            categoria.setNombreCategoria("Cajas");

            articulo = new ArticuloEmbalaje();
            articulo.setIdArt(1L);
            articulo.setNombreCategoria("Cajas");
            articulo.setNombre("Caja Grande");
            articulo.setDescripcion("Caja de carton grande");
            articulo.setPrecioVta(new BigDecimal("2500"));
            articulo.setActivo(true);
        }

        @Test
        void crear_categoriaExiste_creaConActivoTrue() {
            ArticuloEmbalajeRequest request = new ArticuloEmbalajeRequest();
            request.setNombreCategoria("Cajas");
            request.setNombre("Caja Grande");
            request.setDescripcion("Caja de carton grande");
            request.setPrecioVta(new BigDecimal("2500"));

            when(categoriaRepository.findByNombreCategoria("Cajas")).thenReturn(Optional.of(categoria));
            when(repository.save(any(ArticuloEmbalaje.class))).thenReturn(articulo);

            ArticuloEmbalajeResponse response = service.crear(request);

            assertNotNull(response);
            assertTrue(response.getActivo());
            assertEquals("Caja Grande", response.getNombre());
        }

        @Test
        void crear_categoriaNoExiste_lanzaExcepcion() {
            ArticuloEmbalajeRequest request = new ArticuloEmbalajeRequest();
            request.setNombreCategoria("Inexistente");
            request.setNombre("Test");
            request.setPrecioVta(new BigDecimal("100"));

            when(categoriaRepository.findByNombreCategoria("Inexistente")).thenReturn(Optional.empty());

            assertThrows(RecursoNoEncontradoException.class, () -> service.crear(request));
        }

        @Test
        void desactivar_articuloExiste_cambiaActivoAFalse() {
            when(repository.findById(1L)).thenReturn(Optional.of(articulo));

            ArticuloEmbalaje desactivado = new ArticuloEmbalaje();
            desactivado.setIdArt(1L);
            desactivado.setNombreCategoria("Cajas");
            desactivado.setNombre("Caja Grande");
            desactivado.setPrecioVta(new BigDecimal("2500"));
            desactivado.setActivo(false);

            when(repository.save(any(ArticuloEmbalaje.class))).thenReturn(desactivado);

            ArticuloEmbalajeResponse response = service.desactivar(1L);

            assertFalse(response.getActivo());
        }

        @Test
        void desactivar_articuloNoExiste_lanzaExcepcion() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RecursoNoEncontradoException.class, () -> service.desactivar(99L));
        }

        @Test
        void obtenerPorId_noExiste_lanzaExcepcion() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));
        }
    }

    @Nested
    class StockSucursalTests {

        @Mock
        private StockSucursalRepository repository;
        @Mock
        private ArticuloEmbalajeRepository articuloRepository;
        @InjectMocks
        private StockSucursalService service;

        private StockSucursal stock;
        private ArticuloEmbalaje articulo;

        @BeforeEach
        void setUp() {
            articulo = new ArticuloEmbalaje();
            articulo.setIdArt(1L);
            articulo.setNombreCategoria("Cajas");
            articulo.setNombre("Caja Grande");
            articulo.setPrecioVta(new BigDecimal("2500"));
            articulo.setActivo(true);

            stock = new StockSucursal();
            stock.setIdStock(1L);
            stock.setNombreArt("Caja Grande");
            stock.setNombreSucursal("Sucursal Centro");
            stock.setCantidadDisponible(20);
        }

        @Test
        void descontarStock_stockSuficiente_descuentaCorrectamente() {
            when(repository.findByNombreArtAndNombreSucursal("Caja Grande", "Sucursal Centro")).thenReturn(Optional.of(stock));

            StockSucursal actualizado = new StockSucursal();
            actualizado.setIdStock(1L);
            actualizado.setNombreArt("Caja Grande");
            actualizado.setNombreSucursal("Sucursal Centro");
            actualizado.setCantidadDisponible(15);

            when(repository.save(any(StockSucursal.class))).thenReturn(actualizado);

            StockSucursalResponse response = service.descontarStock("Caja Grande", "Sucursal Centro", 5);

            assertEquals(15, response.getCantidadDisponible());
        }

        @Test
        void descontarStock_stockInsuficiente_lanzaExcepcion() {
            when(repository.findByNombreArtAndNombreSucursal("Caja Grande", "Sucursal Centro")).thenReturn(Optional.of(stock));

            assertThrows(ReglaNegocioException.class,
                    () -> service.descontarStock("Caja Grande", "Sucursal Centro", 50));
        }

        @Test
        void descontarStock_stockNoExiste_lanzaExcepcion() {
            when(repository.findByNombreArtAndNombreSucursal("Inexistente", "Sucursal Centro")).thenReturn(Optional.empty());

            assertThrows(RecursoNoEncontradoException.class,
                    () -> service.descontarStock("Inexistente", "Sucursal Centro", 1));
        }

        @Test
        void verificarStock_suficiente_retornaTrue() {
            when(repository.findByNombreArtAndNombreSucursal("Caja Grande", "Sucursal Centro")).thenReturn(Optional.of(stock));

            assertTrue(service.verificarStock("Caja Grande", "Sucursal Centro", 10));
        }

        @Test
        void verificarStock_insuficiente_retornaFalse() {
            when(repository.findByNombreArtAndNombreSucursal("Caja Grande", "Sucursal Centro")).thenReturn(Optional.of(stock));

            assertFalse(service.verificarStock("Caja Grande", "Sucursal Centro", 50));
        }

        @Test
        void verificarStock_noExiste_retornaFalse() {
            when(repository.findByNombreArtAndNombreSucursal("Inexistente", "Sucursal Centro")).thenReturn(Optional.empty());

            assertFalse(service.verificarStock("Inexistente", "Sucursal Centro", 1));
        }
    }
}
