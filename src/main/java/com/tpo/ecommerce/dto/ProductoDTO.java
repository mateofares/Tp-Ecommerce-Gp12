package com.tpo.ecommerce.dto;

import com.tpo.ecommerce.enums.Categorias;
import com.tpo.ecommerce.enums.Color;
import com.tpo.ecommerce.enums.Estado;
import com.tpo.ecommerce.enums.Marca;
import com.tpo.ecommerce.enums.Talle;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductoDTO {
    private Long id;
    private Long usuarioId;
    private String titulo;
    private String descripcion;
    private Double precio;
    private Categorias categoria;
    private Marca marca;
    private Talle talle;
    private Color color;
    private Estado estado;
    private String imagenUrl;
}
