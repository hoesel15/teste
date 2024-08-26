package com.app.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entity.Employee;
import com.app.entity.Product;
import com.app.entity.Sale;
import com.app.repository.ClientRepository;
import com.app.repository.EmployeeRepository;
import com.app.repository.ProductRepository;
import com.app.repository.SaleRepository;

import ch.qos.logback.core.net.server.Client;
import jakarta.persistence.EntityNotFoundException;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ValidationService validationService;

	private Client client;

    SaleService(ClientRepository clientRepository, EmployeeRepository employeeRepository, ProductRepository productRepository) {
    }

    private double totalCalculator(List<Product> products) {
        double totalValue = 0;
        for (Product p : products) {
            Product product = this.productService.findById(p.getId());
            totalValue += product.getPrice();
        }
		return totalValue;
       
    }


    public String save(Sale sale) {
        if (sale == null) {
            throw new IllegalArgumentException("A venda não pode ser nula!");
        }

        com.app.entity.Client client = (com.app.entity.Client) validationService.validateClientById(sale.getClient().getId());
        sale.setClient(client);

        Employee employee = validationService.validateEmployeeById(sale.getEmployee().getId());
        sale.setEmployee(employee);

        List<Product> products = sale.getProducts().stream()
                .map(product -> validationService.validateProductById(product.getId()))
                .collect(Collectors.toList());
        sale.setProducts(products);

        double totalValue = totalCalculator(products);
        sale.setTotalValue(totalValue);

        saleRepository.save(sale);
        return "Venda realizada com sucesso" + "Valor total R$ " + totalValue;
    }

    public void saveAll(List<Sale> sales) {
        if (sales == null || sales.isEmpty()) {
            throw new IllegalArgumentException("A lista de vendas não pode ser nula nem negativa");
        }

        for (Sale sale : sales) {
            if (sale == null) {
                throw new IllegalArgumentException("Venda não pode ser nula na lista");
            }

            setClient(validationService.validateClientById(sale.getClient().getId()));
            

            Employee employee = validationService.validateEmployeeById(sale.getEmployee().getId());
            sale.setEmployee(employee);

            List<Product> products = sale.getProducts().stream()
                    .map(product -> validationService.validateProductById(product.getId()))
                    .collect(Collectors.toList());
            sale.setProducts(products);

            double totalValue = totalCalculator(products);
            sale.setTotalValue(totalValue);
        }

        saleRepository.saveAll(sales);
    }

    public String update(Sale sale, Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser um valor negativo");
        }
        if (sale == null) {
            throw new IllegalArgumentException("A venda não pode ser nula");
        }

        Employee employee = validationService.validateEmployeeById(sale.getEmployee().getId());
        sale.setEmployee(employee);

        List<Product> products = sale.getProducts().stream()
                .map(product -> validationService.validateProductById(product.getId()))
                .collect(Collectors.toList());
        sale.setProducts(products);

        if (saleRepository.existsById(id)) {
            sale.setId(id);
            double totalValue = totalCalculator(products);
            sale.setTotalValue(totalValue);

            saleRepository.save(sale);
            return "Venda com o ID " + sale.getId() + " atualizada com sucesso!" + "Valor total R$ " + totalValue;
        } else {
            throw new EntityNotFoundException("Venda com o ID solicitado não encontrada!");
        }
    }

    public String delete(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser um valor negativo");
        }
        if (saleRepository.existsById(id)) {
            saleRepository.deleteById(id);
            return "Venda com ID solicitado foi deletada com sucesso";
        } else {
            throw new EntityNotFoundException("Venda com o ID solicitado não encontrada");
        }
    }

    public Sale findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou negativo!");
        }
        return saleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Venda com o ID solicitado não encontrado!"));
    }

    public List<Sale> findAll(){
        List<Sale> sales = saleRepository.findAll();
        if (sales.isEmpty()) {
            throw new EntityNotFoundException("Nenhuma venda foi encontrada");
        }
        return sales;
    }


    public List<Sale> findByAddress(String address){
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("O addres é obrigatório e não pode estar vazio");
        }
        if (address.matches("\\d+")) {
            throw new IllegalArgumentException("O addres não pode ser um número");
        }
        List<Sale> sales = saleRepository.findByAddress(address);
        if (sales.isEmpty()){
            throw new EntityNotFoundException("Nenhuma venda encontrada com o endereço solicitado ");
        }
        return sales;
    }

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}

