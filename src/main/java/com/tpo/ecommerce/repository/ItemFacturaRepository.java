package com.tpo.ecommerce.repository;

import com.tpo.ecommerce.entity.ItemFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemFacturaRepository extends JpaRepository<ItemFactura, Long> {

    List<ItemFactura> findByFacturaId(Long facturaId);
}
