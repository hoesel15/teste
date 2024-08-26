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

import com.app.entity.Product;
import com.app.service.ProductService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody @Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        try {
            String message = productService.save(product);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para salvar o produto "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveAll")
    public ResponseEntity<String> saveAll(@RequestBody @Valid List<Product> products, BindingResult result) {
        if (result.hasErrors()) {
            String errorMsg = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(errorMsg, HttpStatus.BAD_REQUEST);
        }
        try {
            productService.saveAll(products);
            return new ResponseEntity<>("Produtos salvos", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para salvar os produtos"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid Product product, BindingResult result) {
        if (result.hasErrors()) {
            String error = result.getAllErrors().get(0).getDefaultMessage();
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        try {
            String message = productService.update(product, id);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para atualizar o produto"+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            String message = productService.delete(id);
            return new ResponseEntity<>(message, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para deletar o produto" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findAll")
    public ResponseEntity<?> findAll() {
        try {
            List<Product> products = productService.findAll();
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para listar os produtos" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Product product = productService.findById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um erro para listar o produto por ID" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findByNameContaining")
    public ResponseEntity<?> findByNameContaining(@RequestParam String name) {
        try {
            List<Product> products = productService.findByNameContaining(name);
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            return new ResponseEntity<>("Ocorreu um para listar o produto com valor solicitado " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findByPriceGreaterThan") //Mapeia solicitações GET para /product/findByPriceGreaterThan. Recebe um parâmetro de consulta price e retorna produtos com preço maior que o valor fornecido. Se nenhum preço for fornecido, o valor padrão é 0.
    public ResponseEntity<List<Product>> findByPriceGreaterThan(@RequestParam(required = false, defaultValue = "0") Double price) {
        try {
            List<Product> list = productService.findByPriceGreaterThan(price);
            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/findProductsInRange") //apeia solicitações GET para /product/findProductsInRange. Recebe parâmetros de consulta minPrice e maxPrice e retorna produtos cujo preço está dentro do intervalo fornecido. Se nenhum valor for fornecido, o valor padrão é 0.
    public ResponseEntity<List<Product>> findProductsInRange(@RequestParam(required = false, defaultValue = "0") Double minPrice, @RequestParam(required = false, defaultValue = "0") Double maxPrice) {
        try {
            List<Product> list = productService.findProductsInRange(minPrice, maxPrice);
            return new ResponseEntity<>(list, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
