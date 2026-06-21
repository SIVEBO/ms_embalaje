package com.sivebo.ms_embalaje.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivebo.ms_embalaje.dto.request.ArticuloEmbalajeRequest;
import com.sivebo.ms_embalaje.dto.response.ArticuloEmbalajeResponse;
import com.sivebo.ms_embalaje.exception.RecursoNoEncontradoException;
import com.sivebo.ms_embalaje.model.entity.ArticuloEmbalaje;
import com.sivebo.ms_embalaje.model.entity.CategoriaEmbalaje;
import com.sivebo.ms_embalaje.repository.ArticuloEmbalajeRepository;
import com.sivebo.ms_embalaje.repository.CategoriaEmbalajeRepository;
import com.sivebo.ms_embalaje.service.impl.ArticuloEmbalajeServiceImpl;

@ExtendWith(MockitoExtension.class)
class ArticuloEmbalajeServiceTest {

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
        categoria = new CategoriaEmbalaje(1L, "Cajas");
        articulo = new ArticuloEmbalaje(1L, categoria, "Caja Grande", "Descripción", new BigDecimal("9990"), true);
    }

    private ArticuloEmbalajeRequest buildRequest(Long idCat, String nombre, BigDecimal precio) {
        ArticuloEmbalajeRequest req = new ArticuloEmbalajeRequest();
        req.setIdCat(idCat);
        req.setNombre(nombre);
        req.setPrecioVta(precio);
        return req;
    }

    @Test
    void crear_categoriaExiste_retornaDTO() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(repository.save(any(ArticuloEmbalaje.class))).thenReturn(articulo);

        ArticuloEmbalajeResponse result = service.crear(buildRequest(1L, "Caja Grande", new BigDecimal("9990")));

        assertNotNull(result);
        assertEquals("Caja Grande", result.getNombre());
        assertTrue(result.getActivo());
        verify(repository).save(any(ArticuloEmbalaje.class));
    }

    @Test
    void crear_categoriaNoExiste_lanzaExcepcion() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.crear(buildRequest(99L, "Caja", new BigDecimal("100"))));
        verify(repository, never()).save(any());
    }

    @Test
    void obtenerPorId_encontrado_retornaDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(articulo));

        ArticuloEmbalajeResponse result = service.obtenerPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdArt());
        assertEquals(1L, result.getIdCat());
    }

    @Test
    void obtenerPorId_noEncontrado_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void listarTodos_retornaListaMapeada() {
        when(repository.findAll()).thenReturn(List.of(articulo));

        List<ArticuloEmbalajeResponse> result = service.listarTodos();

        assertEquals(1, result.size());
        assertEquals("Caja Grande", result.get(0).getNombre());
    }

    @Test
    void listarPorCategoria_retornaListaFiltrada() {
        when(repository.findByCategoria_IdCat(1L)).thenReturn(List.of(articulo));

        List<ArticuloEmbalajeResponse> result = service.listarPorCategoria(1L);

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getIdCat());
    }

    @Test
    void actualizar_encontrado_retornaActualizado() {
        ArticuloEmbalaje actualizado = new ArticuloEmbalaje(
                1L, categoria, "Caja Actualizada", null, new BigDecimal("12000"), true);

        when(repository.findById(1L)).thenReturn(Optional.of(articulo));
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));
        when(repository.save(any(ArticuloEmbalaje.class))).thenReturn(actualizado);

        ArticuloEmbalajeResponse result = service.actualizar(1L,
                buildRequest(1L, "Caja Actualizada", new BigDecimal("12000")));

        assertEquals("Caja Actualizada", result.getNombre());
        verify(repository).save(any(ArticuloEmbalaje.class));
    }

    @Test
    void actualizar_noEncontrado_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.actualizar(99L, buildRequest(1L, "Caja", new BigDecimal("100"))));
    }

    @Test
    void desactivar_encontrado_poneActivoFalse() {
        ArticuloEmbalaje inactivo = new ArticuloEmbalaje(
                1L, categoria, "Caja Grande", "Descripción", new BigDecimal("9990"), false);

        when(repository.findById(1L)).thenReturn(Optional.of(articulo));
        when(repository.save(any(ArticuloEmbalaje.class))).thenReturn(inactivo);

        ArticuloEmbalajeResponse result = service.desactivar(1L);

        assertFalse(result.getActivo());
        verify(repository).save(any(ArticuloEmbalaje.class));
    }

    @Test
    void desactivar_noEncontrado_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.desactivar(99L));
    }
}
