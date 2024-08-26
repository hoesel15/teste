package com.app.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    @NotNull
    @Positive
    private double totalValue;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties("sales")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonIgnoreProperties("sales")
    private Employee employee;

    @ManyToMany
    @JoinTable(
            name = "sale_product",
            joinColumns = @JoinColumn(name = "sale_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    ) //Verifique se o nome da tabela de junção e as colunas são adequadas para suas necessidades.
    @JsonIgnoreProperties("sales")
    private List<Product> products;

}
