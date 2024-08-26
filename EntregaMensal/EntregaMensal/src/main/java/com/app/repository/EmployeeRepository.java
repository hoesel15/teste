package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByNameContaining(String name); //Encontra funcionários cujo nome contém a substring fornecida. Isso permite buscas flexíveis no nome do funcionário
    List<Employee> findByRegistration(String registration); //Encontra funcionários com o registro exato fornecido. Isso é útil para buscas precisas por um identificador único.

    //JPQL
    @Query("SELECT DISTINCT e FROM Employee e JOIN e.sales s")
    List<Employee> findEmployeesWithSales(); //Utiliza JPQL para encontrar funcionários que têm pelo menos uma venda associada. A cláusula JOIN é usada para combinar a entidade Employee com a entidade Sale, e DISTINCT garante que cada funcionário seja listado apenas uma vez, mesmo que tenha várias vendas.

}
