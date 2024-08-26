package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByAddress(String address); //    // Encontra vendas baseadas no endereço
    List<Sale> findByTotalValueGreaterThan(Double totalValue);
    List<Sale> findByClientNameContaining(String clientName);

}
