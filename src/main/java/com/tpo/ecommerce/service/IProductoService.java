package com.tpo.ecommerce.service;

import com.tpo.ecommerce.dto.ProductoDTO;
import com.tpo.ecommerce.enums.Categorias;
import com.tpo.ecommerce.enums.Color;
import com.tpo.ecommerce.enums.Estado;
import com.tpo.ecommerce.enums.Marca;
import com.tpo.ecommerce.enums.Talle;

import java.util.List;

// ProductoService -> todo lo de productos
public interface IProductoService {
    ProductoDTO crear(ProductoDTO dto);

    List<ProductoDTO> getProductos(Long id, String titulo, String descripcion, Double precio, Categorias categoria,
                                   Marca marca, Talle talle, Color color, Estado estado);

    ProductoDTO actualizar(Long id, String titulo, String descripcion, Double precio, Categorias categoria,
                           Marca marca, Talle talle, Color color, Estado estado, String imagenUrl);

    void deleteProducto(Long id);
}
