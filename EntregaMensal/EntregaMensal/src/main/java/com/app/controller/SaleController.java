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

import com.app.entity.Sale;
import com.app.service.SaleService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("sale")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody @Valid Sale sale, BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        try {
            String message = saleService.save(sale);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para salvar venda " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveAll")
    public ResponseEntity<String> saveAll(@RequestBody @Valid List<Sale> sales, BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        try {
            saleService.saveAll(sales);
            return new ResponseEntity<>("Vendas salvas", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para salvar as vendas" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid Sale sale, BindingResult result) {
        if (result.hasErrors()) {
            String error = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        try {
            String message = saleService.update(sale, id);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para atualizar a venda" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            String message = saleService.delete(id);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para deletar a venda" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        try {
            List<Sale> sales = saleService.findAll();
            return new ResponseEntity<>(sales, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para listar as vendas" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findByAddress")
    public ResponseEntity<?> findByNameContaining(@RequestParam String address) {
        try {
            List<Sale> sales = saleService.findByAddress(address);
            return new ResponseEntity<>(sales, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para listar  vendas com o endereço solicitado" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Sale sale = saleService.findById(id);
            return new ResponseEntity<>(sale, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para listar a venda com o ID solicitado " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //1. Cliente que mais gastou em compras
    //2. Produtos mais vendidos
    //3. Vendas realizadas por um funcionário específico
    //4. Clientes que realizaram compras acima de um determinado valor
    //5. Produtos vendidos em um determinado período
}
