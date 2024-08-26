package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContaining(String name); //Encontra produtos cujo nome contém a substring
    List<Product> findByPriceGreaterThan(Double price); //Encontra produtos com um preço maior que o valor fornecido. Ideal para filtragem por preços acima de um certo valor.

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    public List<Product> findProductsInRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice); //Usa JPQL para encontrar produtos cujo preço está entre os valores mínimo e máximo fornecidos
}
