package com.app.entity;

import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity //Marca a classe como uma entidade JPA, o que significa que ela será mapeada para uma tabela no banco de dados.
@AllArgsConstructor //Gera um construtor com todos os argumentos necessários.
@NoArgsConstructor //Gera um construtor sem argumentos, necessário para frameworks que requerem um construtor padrão (por exemplo, JPA).
@Getter //Geram os métodos getters e setters automaticamente para todos os campos da classe, simplificando a manipulação dos atributos da entidade.
@Setter
public class Client {

    @Id //Marca o campo id como a chave primária da entidade.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Define a estratégia de geração para o campo id, que será gerado automaticamente pelo banco de dados (geralmente um auto incremento).
    private Long id;

    @NotBlank(message = "O nome não pode ser nulo.") //Valida que o campo name não pode ser nulo ou vazio e deve conter pelo menos duas palavras.
    @Pattern(regexp = "^(\\S+\\s+\\S+.*)$", message = "O nome deve conter pelo menos duas palavras.")
    private String name;

    @NotNull(message = "O CPF não deve ser nulo.")
    @CPF(message = "CPF Inválido. O formato deve ser 123.456.789.09")
    private String cpf;

    @NotNull(message = "A idade não pode ser nula.")
    @Min(value = 0, message = "A idade não deve ser negativa.")
    private Integer age;

    @NotNull(message = "O telefone não deve ser nulo.")
    @Pattern(regexp = "\\(\\d{2}\\) \\d{4,5}-\\d{4}", message = "Número de telefone inválido. O formato deve ser (XX) XXXX-XXXX ou (XX) XXXXX-XXXX.")
    private String telephone;

    @OneToMany(mappedBy = "client", cascade = CascadeType.PERSIST) //Define um relacionamento de um-para-muitos entre Client e Sale.
    @JsonIgnoreProperties("client") //Instrui o Jackson a ignorar a propriedade client quando serializar/deserializar Sale para evitar loops infinitos no JSON.
    private List<Sale> sales;
}
