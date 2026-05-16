package com.sivebo.ms_embalaje.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_sucursal")
public class StockSucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stock")
    private Long idStock;

    @ManyToOne
    @JoinColumn(name = "id_art", nullable = false)
    private ArticuloEmbalaje articulo;

    @Column(name = "id_sucursal", nullable = false)
    private Long idSucursal;

    @Column(name = "cantidad_disponible", nullable = false)
    private Integer cantidadDisponible;
}