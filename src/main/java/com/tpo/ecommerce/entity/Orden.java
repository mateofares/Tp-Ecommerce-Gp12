package com.tpo.ecommerce.entity;

import com.tpo.ecommerce.dto.ItemOrdenDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orden")
public class Orden {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //private Usuario comprador;
   // private List<ItemOrdenDTO> items = new ArrayList<>();
    private LocalDateTime fecha;
    private String estado;
    private double total;
    

}