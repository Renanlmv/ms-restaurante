package com.github.renanlmv.ms.restaurante.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_reserva")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_reserva", nullable = false)
    private LocalDate dataReserva;

    @Column(name = "nome_cliente", nullable = false, length = 100)
    private String nomeCliente;

    @Column(name = "qtd_pessoas", nullable = false)
    private Integer qtdePessoas;

    // relacionamento
    // tabela tb_reserva recebe a pk(id) da tb_restaurante como fk
    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reserva_restaurante"))
    private Restaurante restaurante;
}
