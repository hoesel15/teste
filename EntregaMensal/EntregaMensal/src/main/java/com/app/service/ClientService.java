package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.repository.ClientRepository;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public String save(Client client) {
        clientRepository.save(client);
        return "Cliente " + client;
    }

    public void saveAll(List<Client> clients){
        clientRepository.saveAll(clients);
    }

    public String update(Client client, Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo nem negativo");
        }
        if (clientRepository.existsById(id)) {
            clientRepository.save(client);
            return "Cliente " +" Atualizado";
        } else {
            throw new EntityNotFoundException("Cliente com ID não encontrado");
        }
    }

    public String delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo nem negativo");
        }
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return "Cliente com ID solicitado deletado com sucesso!";
        } else {
            throw new EntityNotFoundException("Cliente com ID solicidato não encontrado!");
        }
    }

    public List<Client> findAll(){
        List<Client> clients = clientRepository.findAll();
        if (clients.isEmpty()) {
            throw  new EntityNotFoundException("Nenhum cliente foi encontrado");
        }
        return clients;
    }

    public Client findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo nem negativo");
        }
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID solicitado não encontrado!"));
    }

    public List<Client> findByNameContaining(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O name não pode estar vazio!");
        }
        if (name.matches("\\d+")) { // 'matches' verifica se a string corresponde à expressão solicitada
            throw new IllegalArgumentException("O name não pode ser um número");
        }
        List<Client> clients = clientRepository.findByNameContaining(name);
        if (clients.isEmpty()) {
            throw new EntityNotFoundException("Nenhum cliente encontrado com a letra ou palvra solicitada: ");
        }
        return clients;
    }

    public List<Client> findByCpf(String cpf){
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("O cpf é obrigatório e não pode estar vazio!");
        }
        //validando o cpf no request
        String cpfRegex = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$";
        if (!cpf.matches(cpfRegex)) {
            throw new IllegalArgumentException("O formado do CPF está incorreto");
        }
        List<Client> client = clientRepository.findByCpf(cpf);
        if (client.isEmpty()) {
            throw new EntityNotFoundException("Nenhum cliente encontrado com o CPF solicitado");
        }
        return client;
    }

    public List<Client> findByNameAndAge(String name, Integer age) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("O name é obrigatório e não pode estar nulo ou vazio");
        }
        if (age!= null && age <= 0) {
            throw new IllegalArgumentException("A idade não pode ser negativa");
        }
        List<Client> clients = clientRepository.findByNameAndAge(name, age);
        if (clients.isEmpty()) {
            throw new EntityNotFoundException("Nenhum cliente encontrado com o nome " + name + (age != null ? " e idade " + age : "") + ".");
        }
        return clients;
    }

}
