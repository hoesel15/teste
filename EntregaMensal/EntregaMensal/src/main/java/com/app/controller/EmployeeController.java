package com.app.controller;

import java.util.List; //Incluem classes e pacotes necessários para operações HTTP, validação, e manipulação de erros.

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

import com.app.entity.Employee;
import com.app.service.EmployeeService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController // Define a classe como um controlador onde os métodos retornam diretamente dados (JSON ou XML) em vez de uma visão.
@RequestMapping("employee") // Define a URL base para todas as solicitações neste controlador, prefixando todas as URLs com "employee".
public class EmployeeController {

    @Autowired  //Injeta a dependência EmployeeService, que é uma classe responsável pela lógica de negócios para Employee.
    private EmployeeService employeeService;

    @PostMapping("/save") // Mapeia solicitações POST para /employee/save. Recebe um objeto Employee no corpo da solicitação, o valida e, se não houver erros, tenta salvar o funcionário. Se a validação falhar ou ocorrer um erro, retorna uma resposta apropriada com um código de status HTTP.
    public ResponseEntity<String> save(@RequestBody @Valid Employee employee, BindingResult result) { 
        if (result.hasErrors()) {
            String errorMsg = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        try {
            String message = employeeService.save(employee);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao salvar funcionario"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveAll")
    public ResponseEntity<String> saveAll(@RequestBody @Valid List<Employee> employees, BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        try {
            employeeService.saveAll(employees);
            return new ResponseEntity<>("Funcionários salvos", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Erro ao salvar funcionairos"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        try {
            String message = employeeService.update(employee, id);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (EntityNotFoundException e) { //Lançada quando o recurso solicitado não é encontrado (por exemplo, um funcionário com um ID específico não existe).
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) { //Lançada quando um argumento inválido é fornecido (por exemplo, dados de entrada que não atendem aos requisitos esperados).
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) { //Captura qualquer outra exceção inesperada e retorna uma mensagem genérica de erro.
            return new ResponseEntity<>("Ocorreu um erro para atualizar "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            String message = employeeService.delete(id);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para deletar"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        try {
            List<Employee> employees = employeeService.findAll();
            return new ResponseEntity<>(employees, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para listar todos funcionarios"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Employee employee = employeeService.findById(id);
            return new ResponseEntity<>(employee, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para listar o funcionario por ID"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findByNameContaining")
    public ResponseEntity<?> findByNameContaining(@RequestParam String name) {
        try {
            List<Employee> employees = employeeService.findByNameContaining(name);
            return new ResponseEntity<>(employees, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para listar funcionarios com o valor solicitado"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findByRegistration")
    public ResponseEntity<?> findByRegistration(@RequestParam String registration) {
        try {
            List<Employee> employees = employeeService.findByRegistration(registration);
            return new ResponseEntity<>(employees, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para listar os registros de funcionarios"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findEmployeesWithSales")
    public ResponseEntity<?> findEmployeesWithSales() {
        try {
            List<Employee> employees = employeeService.findEmployeesWithSales();
            return new ResponseEntity<>(employees, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para listar funcionarios com vendas associadas"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
