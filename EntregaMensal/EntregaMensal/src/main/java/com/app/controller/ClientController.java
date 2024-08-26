package com.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.service.ClientService;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController //Marca a classe como um controlador onde os métodos retornam dados diretamente (em vez de exibir uma visão). É uma combinação das anotações @Controller e @ResponseBody.
@RequestMapping("/client") //Define a URL base para todos os endpoints dentro deste controlador, neste caso, "client".
public class ClientController {

    @Autowired // Injeta a dependência ClientService automaticamente no controlador. O ClientService deve ser uma classe de serviço que contém a lógica de negócios relacionada a Client.
    private ClientService clientService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody @Valid Client client, BindingResult result) { //Usado para capturar e verificar erros de validação após o @Valid ser aplicado no objeto Client.
        if (result.hasErrors()) {
            String errorMsg = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        try {
            String message = clientService.save(client);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro para salvar cliente" + e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR); //400 Bad Request: Para erros de validação ou argumentos inválidos.
          
        }
    }  //Se houver erros de validação, retorna uma resposta com o erro e status 400 (BAD_REQUEST). Se não houver erros, tenta salvar o cliente usando o clientService e retorna uma mensagem de sucesso ou um erro inesperado.



    @PostMapping("/saveAll")
    public ResponseEntity<String> saveAll(@RequestBody @Valid List<Client> clients, BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        try {
            clientService.saveAll(clients);
            return new ResponseEntity<>("Clientes salvos", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro para salvar os clientes"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //Aceita uma lista de objetos Client e valida. Se houver erros, retorna um erro de validação. Caso contrário, tenta salvar todos os clientes e retorna uma mensagem de sucesso ou um erro inesperado.

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid Client client, BindingResult result) {
        if (result.hasErrors()) {
            String error = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        try {
            String message = clientService.update(client, id);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro para atualizar cliente"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//Espera um ID de cliente e um objeto Client no corpo da solicitação. Se houver erros de validação, retorna uma mensagem de erro. Se não houver erros, tenta atualizar o cliente com o ID especificado. Retorna uma mensagem de sucesso, um erro de cliente não encontrado, ou um erro inesperado.
    
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            String message = clientService.delete(id);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro para deletar cliente" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //Mapeia solicitações DELETE para /client/delete/{id}. Tenta deletar o cliente com o ID especificado. Retorna uma mensagem de sucesso ou um erro de cliente não encontrado, argumento inválido ou erro inesperado.


    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() { // Permite que o controlador retorne diferentes tipos de respostas, como listas de funcionários ou mensagens de erro.
        try {
            List<Client> clients = clientService.findAll();
            return new ResponseEntity<>(clients, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro para listar cliente" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //Mapeia solicitações GET para /client/findAll. Retorna uma lista de todos os clientes. Se não houver clientes, ou ocorrer um erro, retorna uma mensagem de erro.

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Client client = this.clientService.findById(id);
            return new ResponseEntity<>(client, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro para listar cliente por ID" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //Mapeia solicitações GET para /client/findById/{id}. Retorna o cliente com o ID especificado. Se o cliente não for encontrado ou ocorrer um erro, retorna uma mensagem de erro.

    @GetMapping("/findByNameContaining")
    public ResponseEntity<?> findByNameContaining(@RequestParam String name) {
        try {
            List<Client> clients = clientService.findByNameContaining(name);
            return new ResponseEntity<>(clients, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro para listar cliente pelo valor solicitado" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } //Mapeia solicitações GET para /client/findByNameContaining. Aceita um parâmetro de consulta name e retorna uma lista de clientes cujo nome contém o valor fornecido. Se não encontrar clientes ou ocorrer um erro, retorna uma mensagem de erro.

    @GetMapping("/findByCpf")
    public ResponseEntity<?> findByCpf( @RequestParam String cpf) {
        try {
            List<Client> clients = this.clientService.findByCpf(cpf);
            return new ResponseEntity<>(clients, HttpStatus.OK);

        } catch(EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro para listar cliente pelo CPF"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }// Mapeia solicitações GET para /client/findByCpf. Aceita um parâmetro de consulta cpf e retorna uma lista de clientes com o CPF fornecido. Retorna uma mensagem de erro se não encontrar clientes ou se ocorrer um erro.

    @GetMapping("/findByNameAndAge")
    public ResponseEntity<?> findByNameAndAge(@RequestParam String name, @RequestParam Integer age) {
        try {
            List<Client> clients = clientService.findByNameAndAge(name, age);
            return new ResponseEntity<>(clients, HttpStatus.OK);

        } catch(EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro para listar clientes com nome e idade solicitado"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

} //Mapeia solicitações GET para /client/findByNameAndAge. Aceita parâmetros de consulta name e age e retorna uma lista de clientes que correspondem a ambos os critérios. Retorna uma mensagem de erro se não encontrar clientes ou se ocorrer um erro.
