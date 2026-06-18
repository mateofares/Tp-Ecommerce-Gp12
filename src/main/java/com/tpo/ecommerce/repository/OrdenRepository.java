package com.tpo.ecommerce.repository;

import com.tpo.ecommerce.entity.Orden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByCompradorIdOrderByFechaDesc(Long compradorId);
    List<Orden> findDistinctByItemsProductoUsuarioIdOrderByFechaDesc(Long vendedorId);
    boolean existsByIdAndCompradorId(Long id, Long compradorId);

    @Query("""
            select count(io) > 0
            from ItemOrden io
            where io.orden.id = :ordenId
              and io.orden.comprador.id = :compradorId
              and io.producto.id = :productoId
            """)
    boolean existsCompraDeProductoEnOrden(@Param("ordenId") Long ordenId,
                                          @Param("compradorId") Long compradorId,
                                          @Param("productoId") Long productoId);

}
