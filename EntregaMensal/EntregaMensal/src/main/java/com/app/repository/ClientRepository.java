package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ch.qos.logback.core.net.server.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByNameContaining(String name); //findByNameContaining(String name): Encontra clientes cujo nome contém o valor fornecido
    List<Client> findByCpf(String cpf); //Encontra clientes cujo cpf contém o valor fornecido

    //JPQL | %.% significa "qualquer coisa antes e depois do valor".
    @Query("SELECT c FROM Client c WHERE c.name LIKE %:name% AND c.age = :age")
    List<Client> findByNameAndAge(@Param("name") String name, @Param("age") Integer age); //Usa JPQL para encontrar clientes que correspondem ao nome e à idade fornecidos
}
