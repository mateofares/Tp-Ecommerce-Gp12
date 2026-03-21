package com.tpo.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden")
public class Orden {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Usuario comprador;
    private List<ItemOrden> items = new ArrayList<>();
    private LocalDateTime fecha;
    private String estado;
    private double total;
    
    public void agregarItem(ItemOrden item) {
        items.add(item);
        item.setOrden(this);
    }
    
}