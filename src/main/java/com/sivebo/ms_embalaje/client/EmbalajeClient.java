package com.sivebo.ms_embalaje.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class EmbalajeClient {

    private static final Logger log = LoggerFactory.getLogger(EmbalajeClient.class);
    private final WebClient webClient;

    public EmbalajeClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8084").build();
    }

    public Boolean verificarCajaAbierta(Long idSucursal) {
        log.info("Verificando caja abierta para sucursal id: {}", idSucursal);
        try {
            return webClient.get()
                    .uri("/api/cajas/sucursal/{idSucursal}", idSucursal)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(r -> r.contains("ABIERTA"))
                    .block();
        } catch (Exception e) {
            log.error("Error al consultar ms-finanzas: {}", e.getMessage());
            return false;
        }
    }
}