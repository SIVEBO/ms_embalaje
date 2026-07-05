package com.sivebo.ms_embalaje.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivebo.ms_embalaje.dto.request.CategoriaEmbalajeRequest;
import com.sivebo.ms_embalaje.dto.response.CategoriaEmbalajeResponse;
import com.sivebo.ms_embalaje.exception.RecursoNoEncontradoException;
import com.sivebo.ms_embalaje.model.entity.CategoriaEmbalaje;
import com.sivebo.ms_embalaje.repository.CategoriaEmbalajeRepository;

@ExtendWith(MockitoExtension.class)
class CategoriaEmbalajeServiceTest {

    @Mock
    private CategoriaEmbalajeRepository repository;

    @InjectMocks
    private CategoriaEmbalajeService service;

    private CategoriaEmbalaje categoria;

    @BeforeEach
    void setUp() {
        categoria = new CategoriaEmbalaje(1L, "Cajas");
    }

    private CategoriaEmbalajeRequest buildRequest(String nombre) {
        CategoriaEmbalajeRequest req = new CategoriaEmbalajeRequest();
        req.setNombreCategoria(nombre);
        return req;
    }

    @Test
    void crear_retornaDTO() {
        when(repository.save(any(CategoriaEmbalaje.class))).thenReturn(categoria);

        CategoriaEmbalajeResponse result = service.crear(buildRequest("Cajas"));

        assertNotNull(result);
        assertEquals(1L, result.getIdCat());
        assertEquals("Cajas", result.getNombreCategoria());
        verify(repository).save(any(CategoriaEmbalaje.class));
    }

    @Test
    void obtenerPorId_encontrado_retornaDTO() {
        when(repository.findById(1L)).thenReturn(Optional.of(categoria));

        CategoriaEmbalajeResponse result = service.obtenerPorId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdCat());
        assertEquals("Cajas", result.getNombreCategoria());
    }

    @Test
    void obtenerPorId_noEncontrado_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void listarTodas_retornaListaMapeada() {
        when(repository.findAll()).thenReturn(List.of(categoria));

        List<CategoriaEmbalajeResponse> result = service.listarTodas();

        assertEquals(1, result.size());
        assertEquals("Cajas", result.get(0).getNombreCategoria());
    }

    @Test
    void actualizar_encontrado_retornaActualizado() {
        CategoriaEmbalaje actualizada = new CategoriaEmbalaje(1L, "Bolsas");
        when(repository.findById(1L)).thenReturn(Optional.of(categoria));
        when(repository.save(any(CategoriaEmbalaje.class))).thenReturn(actualizada);

        CategoriaEmbalajeResponse result = service.actualizar(1L, buildRequest("Bolsas"));

        assertEquals("Bolsas", result.getNombreCategoria());
        verify(repository).save(any(CategoriaEmbalaje.class));
    }

    @Test
    void actualizar_noEncontrado_lanzaExcepcion() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> service.actualizar(99L, buildRequest("Bolsas")));
    }

    @Test
    void eliminar_existe_invocaDeleteById() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void eliminar_noExiste_lanzaExcepcion() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class, () -> service.eliminar(99L));
        verify(repository, never()).deleteById(any());
    }
}
