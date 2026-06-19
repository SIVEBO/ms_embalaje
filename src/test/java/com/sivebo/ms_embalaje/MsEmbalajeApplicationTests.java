package com.sivebo.ms_embalaje;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import com.sivebo.ms_embalaje.service.impl.ArticuloEmbalajeServiceImpl;
import com.sivebo.ms_embalaje.service.impl.StockSucursalServiceImpl;

@ExtendWith(MockitoExtension.class)
class MsEmbalajeApplicationTests {

    @Nested
    class ArticuloEmbalajeTests {

        @Mock
        private ArticuloEmbalajeRepository repository;
        @Mock
        private CategoriaEmbalajeRepository categoriaRepository;
        @InjectMocks
        private ArticuloEmbalajeServiceImpl service;

        private CategoriaEmbalaje categoria;
        private ArticuloEmbalaje articulo;

        @BeforeEach
        void setUp() {
            categoria = new CategoriaEmbalaje();
            categoria.setIdCat(1L);
            categoria.setNombreCategoria("Cajas");

            articulo = new ArticuloEmbalaje();
            articulo.setIdArt(1L);
            articulo.setCategoria(categoria);
            articulo.setNombre("Caja Grande");
            articulo.setDescripcion("Caja de carton grande");
            articulo.setPrecioVta(new BigDecimal("2500"));
            articulo.setActivo(true);
        }

        @Test
        void crear_categoriaExiste_creaConActivoTrue() {
            ArticuloEmbalajeRequest request = new ArticuloEmbalajeRequest();
            request.setIdCat(1L);
            request.setNombre("Caja Grande");
            request.setDescripcion("Caja de carton grande");
            request.setPrecioVta(new BigDecimal("2500"));

            when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
            when(repository.save(any(ArticuloEmbalaje.class))).thenReturn(articulo);

            ArticuloEmbalajeResponse response = service.crear(request);

            assertNotNull(response);
            assertTrue(response.getActivo());
            assertEquals("Caja Grande", response.getNombre());
        }

        @Test
        void crear_categoriaNoExiste_lanzaExcepcion() {
            ArticuloEmbalajeRequest request = new ArticuloEmbalajeRequest();
            request.setIdCat(99L);
            request.setNombre("Test");
            request.setPrecioVta(new BigDecimal("100"));

            when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RecursoNoEncontradoException.class, () -> service.crear(request));
        }

        @Test
        void desactivar_articuloExiste_cambiaActivoAFalse() {
            when(repository.findById(1L)).thenReturn(Optional.of(articulo));

            ArticuloEmbalaje desactivado = new ArticuloEmbalaje();
            desactivado.setIdArt(1L);
            desactivado.setCategoria(categoria);
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
        private StockSucursalServiceImpl service;

        private StockSucursal stock;
        private ArticuloEmbalaje articulo;

        @BeforeEach
        void setUp() {
            CategoriaEmbalaje cat = new CategoriaEmbalaje();
            cat.setIdCat(1L);
            cat.setNombreCategoria("Cajas");

            articulo = new ArticuloEmbalaje();
            articulo.setIdArt(1L);
            articulo.setCategoria(cat);
            articulo.setNombre("Caja Grande");
            articulo.setPrecioVta(new BigDecimal("2500"));
            articulo.setActivo(true);

            stock = new StockSucursal();
            stock.setIdStock(1L);
            stock.setArticulo(articulo);
            stock.setIdSucursal(5L);
            stock.setCantidadDisponible(20);
        }

        @Test
        void descontarStock_stockSuficiente_descuentaCorrectamente() {
            when(repository.findByArticulo_IdArtAndIdSucursal(1L, 5L)).thenReturn(Optional.of(stock));

            StockSucursal actualizado = new StockSucursal();
            actualizado.setIdStock(1L);
            actualizado.setArticulo(articulo);
            actualizado.setIdSucursal(5L);
            actualizado.setCantidadDisponible(15);

            when(repository.save(any(StockSucursal.class))).thenReturn(actualizado);

            StockSucursalResponse response = service.descontarStock(1L, 5L, 5);

            assertEquals(15, response.getCantidadDisponible());
        }

        @Test
        void descontarStock_stockInsuficiente_lanzaExcepcion() {
            when(repository.findByArticulo_IdArtAndIdSucursal(1L, 5L)).thenReturn(Optional.of(stock));

            assertThrows(ReglaNegocioException.class,
                    () -> service.descontarStock(1L, 5L, 50));
        }

        @Test
        void descontarStock_stockNoExiste_lanzaExcepcion() {
            when(repository.findByArticulo_IdArtAndIdSucursal(99L, 5L)).thenReturn(Optional.empty());

            assertThrows(RecursoNoEncontradoException.class,
                    () -> service.descontarStock(99L, 5L, 1));
        }

        @Test
        void verificarStock_suficiente_retornaTrue() {
            when(repository.findByArticulo_IdArtAndIdSucursal(1L, 5L)).thenReturn(Optional.of(stock));

            assertTrue(service.verificarStock(1L, 5L, 10));
        }

        @Test
        void verificarStock_insuficiente_retornaFalse() {
            when(repository.findByArticulo_IdArtAndIdSucursal(1L, 5L)).thenReturn(Optional.of(stock));

            assertFalse(service.verificarStock(1L, 5L, 50));
        }

        @Test
        void verificarStock_noExiste_retornaFalse() {
            when(repository.findByArticulo_IdArtAndIdSucursal(99L, 5L)).thenReturn(Optional.empty());

            assertFalse(service.verificarStock(99L, 5L, 1));
        }
    }
}
