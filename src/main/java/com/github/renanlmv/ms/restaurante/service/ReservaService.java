package com.github.renanlmv.ms.restaurante.service;

import com.github.renanlmv.ms.restaurante.dto.ReservaDTO;
import com.github.renanlmv.ms.restaurante.entities.Reserva;
import com.github.renanlmv.ms.restaurante.entities.Restaurante;
import com.github.renanlmv.ms.restaurante.exceptions.DatabaseException;
import com.github.renanlmv.ms.restaurante.exceptions.ResourceNotFoundException;
import com.github.renanlmv.ms.restaurante.repositories.ReservaRepository;
import com.github.renanlmv.ms.restaurante.repositories.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservaService {

    @Autowired
    ReservaRepository reservaRepository;

    @Autowired
    RestauranteRepository restauranteRepository;

    @Transactional(readOnly = true)
    public List<ReservaDTO> findAllReservasByRestauranteId(Long restauranteId) {

        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(
                () -> new ResourceNotFoundException("Restaurante não encontrado. ID: " + restauranteId)
        );

        //return restaurante.getReservas().stream().map(ReservaDTO::new).toList();
        //return reservaRepository.findByRestauranteId(restauranteId).stream().map(ReservaDTO::new).toList();

        // Validar se a lista estiver vazia
        List<Reserva> reservas = reservaRepository.findByRestauranteId(restaurante.getId());

        if (reservas.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma reserva cadastrada para o Restaurante ID: " + restauranteId);
        }

        return reservas.stream().map(ReservaDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public ReservaDTO findReservaById(Long restauranteId, Long reservaId) {

        Reserva reserva = reservaRepository.findByIdAndRestauranteId(reservaId, restauranteId).orElseThrow(
                () -> new ResourceNotFoundException("Não foi encontrada Reserva ID: " + reservaId +
                        " para Restaurante ID: " + restauranteId)
        );

        return new ReservaDTO(reserva);
    }

    @Transactional
    public ReservaDTO saveReserva(Long restauranteId, ReservaDTO reservaDTO) {

        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(
                () -> new ResourceNotFoundException("Restaurante não encontrado. ID: " + restauranteId)
        );

        try {
            Reserva reserva = new Reserva();
            mapDtoToReserva(reserva, reservaDTO, restaurante);
            reserva = reservaRepository.save(reserva);
            return new ReservaDTO(reserva);
        } catch (DataAccessException e) {
            throw new DatabaseException("Não foi possível salvar a reserva.");
        }

    }

    public void mapDtoToReserva(Reserva reserva, ReservaDTO reservaDTO, Restaurante restaurante) {

        reserva.setDataReserva(reservaDTO.getDataReserva());
        reserva.setNomeCliente(reservaDTO.getNomeCliente());
        reserva.setQtdePessoas(reservaDTO.getQtdePessoas());
        reserva.setRestaurante(restaurante);
    }

    @Transactional
    public ReservaDTO updateReserva(Long restauranteId, Long reservaId, ReservaDTO reservaDTO) {

        Restaurante restaurante = restauranteRepository.findById(restauranteId).orElseThrow(
                () -> new ResourceNotFoundException("Restaurante não encontrado. ID: " + restauranteId)
        );

        Reserva reserva = reservaRepository.findByIdAndRestauranteId(reservaId, restauranteId).orElseThrow(
                () -> new ResourceNotFoundException("Não foi encontrada Reserva ID: " + reservaId +
                        " para Restaurante ID: " + restauranteId)
        );

        try {
            mapDtoToReserva(reserva, reservaDTO, restaurante);
            reserva = reservaRepository.save(reserva);
            return  new ReservaDTO(reserva);
        } catch (DataAccessException e) {
            throw new DatabaseException("Não foi possível atualizar a reserva. ID " + reservaId);
        }

    }

    @Transactional
    public void deleteReserva(Long restauranteId, Long reservaId) {

        restauranteRepository.findById(restauranteId).orElseThrow(
                () -> new ResourceNotFoundException("Restaurante não encontrado. ID: " + restauranteId)
        );

        Reserva reserva = reservaRepository.findByIdAndRestauranteId(restauranteId, reservaId).orElseThrow(
                () -> new ResourceNotFoundException("Não foi encontrada Reserva ID: " + reservaId +
                        " para Restaurante ID: " + restauranteId)
        );

        try {
            reservaRepository.delete(reserva);
        } catch (DataAccessException e) {
            throw new DatabaseException("Não foi possível excluir a reserva. ID: " + reservaId);
        }
    }
}
