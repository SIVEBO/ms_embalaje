package com.sivebo.ms_embalaje.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.Data;

@Component
public class EmbalajeClient {

    private static final Logger log = LoggerFactory.getLogger(EmbalajeClient.class);
    private final WebClient webClient;

    public EmbalajeClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://ms-finanzas").build();
    }

    public Boolean verificarCajaAbierta(Long idSucursal) {
        log.info("Verificando caja abierta para sucursal id: {}", idSucursal);
        try {
            CajaSucursalDTO caja = webClient.get()
                    .uri("/api/v1/cajas/sucursal/{idSucursal}", idSucursal)
                    .retrieve()
                    .bodyToMono(CajaSucursalDTO.class)
                    .block();
            return caja != null && "ABIERTA".equals(caja.getEstadoActual());
        } catch (Exception e) {
            log.error("Error al consultar ms-finanzas: {}", e.getMessage());
            return false;
        }
    }

    @Data
    private static class CajaSucursalDTO {
        private Long idCaja;
        private Long idSucursal;
        private String estadoActual;
    }
}