package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.Employee;
import com.app.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public String save(Employee employee) {
        employeeRepository.save(employee);
        return "Funcionário salvo com sucesso!";
    }

    public void saveAll(List<Employee> employees) {
        employeeRepository.saveAll(employees);
    }

    public String update(Employee employee, Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo nem negativo");
        }
        if (employeeRepository.existsById(id)) {
            employee.setId(id);
            employeeRepository.save(employee);
            return "Funcionário atualizado com sucesso!";
        } else {
            throw new EntityNotFoundException("Funcionário com ID solicitado não encontrado");
        }
    }

    public String delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo nem negativo");
        }
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
            return "Funcionário com o ID solicitado foi deletado";
        } else {
            throw new EntityNotFoundException("Funcionário com ID solicitado não encontrado!");
        }
    }

    public Employee findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo nem negativo");
        }
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário com ID solicitado não encontrado!"));
    }

    public List<Employee> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw new EntityNotFoundException("Nenhum funcionário foi encontrado");
        }
        return employees;
    }

    public List<Employee> findByNameContaining(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O name não pode ser nulo nem vazio");
        }
        if (name.matches("\\d+")) {
            throw new IllegalArgumentException("O name não pode ser um número");
        }
        List<Employee> employees = employeeRepository.findByNameContaining(name);
        if (employees.isEmpty()) {
            throw new EntityNotFoundException("Nenhum funcionário encontrado com o nome ou letra solicitado");
        }
        return employees;
    }

    public List<Employee> findByRegistration(String registration) {
        if (registration == null || registration.trim().isEmpty()) {
            throw new IllegalArgumentException("O registration não pode ser nulo ou vazio");
        }
        List<Employee> employees = employeeRepository.findByRegistration(registration);
        if (employees.isEmpty()) {
            throw new EntityNotFoundException("Nenhum funcionário encontrado com o registro solicitado");
        }
        return employees;
    }

    public List<Employee> findEmployeesWithSales() {
        List<Employee> employees = employeeRepository.findEmployeesWithSales();
        if (employees.isEmpty()) {
            throw new EntityNotFoundException("Nenhum funcionário encontrado com vendas");
        }
        return employees;
    }
}
