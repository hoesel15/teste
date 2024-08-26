package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.Employee;
import com.app.entity.Product;
import com.app.repository.ClientRepository;
import com.app.repository.EmployeeRepository;
import com.app.repository.ProductRepository;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ValidationService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProductRepository productRepository;

    public Client validateClientById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID do cliente não pode ser nulo ou vazio");
        }
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID solicitado não encontrado"));
    }

    public Employee validateEmployeeById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID do funcionário não pode ser negativo");
        }
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário com ID solicitado não encontrado!"));
    }

    public Product validateProductById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID do produto não pode ser negativo");
        }
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto com ID solicitado não foi encontrado!"));
    }
}
