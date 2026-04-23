package com.github.renanlmv.ms.restaurante.repositories;

import com.github.renanlmv.ms.restaurante.entities.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // metodo para listar todas as reservas pelo id do restaurante
    List<Reserva> findByRestauranteId (Long restauranteId);

    // metodo personalizado para encontrar uma reserva pelo seu id e o id do restaurante
    Optional<Reserva> findByIdAndRestauranteId(Long reservaId, Long restauranteId);
}
