package com.tpo.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrito")
public class Carrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Usuario comprador;
    private List<ItemCarrito> items = new ArrayList<>();

    public void agregarItem(ItemCarrito item) {
        items.add(item);
        item.setCarrito(this);
    }
    
    public void eliminarItem(ItemCarrito item) {
        items.remove(item);
        item.setCarrito(null);
    }
    
}