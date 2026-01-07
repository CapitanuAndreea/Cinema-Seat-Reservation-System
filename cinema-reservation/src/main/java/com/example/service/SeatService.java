package com.example.service;

import com.example.exceptions.BadRequestException;
import com.example.exceptions.NotFoundException;
import com.example.model.Hall;
import com.example.model.Seat;
import com.example.model.dto.CreateSeatRequest;
import com.example.model.dto.SeatResponse;
import com.example.repository.HallRepository;
import com.example.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;

    public SeatService(SeatRepository seatRepository, HallRepository hallRepository) {
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
    }

    public List<SeatResponse> findAll() {
        return seatRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<SeatResponse> findByHall(Long hallId) {
        return seatRepository.findByHallId(hallId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SeatResponse addSeat(CreateSeatRequest request) {
        Hall hall = hallRepository.findById(request.hallId())
                .orElseThrow(() -> new NotFoundException("Hall not found with id=" + request.hallId()));

        if (request.rowNumber() <= 0 || request.seatNumber() <= 0) {
            throw new BadRequestException("rowNumber and seatNumber must be >= 1");
        }

        Seat seat = new Seat(request.rowNumber(), request.seatNumber(), hall);

        try {
            Seat saved = seatRepository.save(seat);
            return toResponse(saved);
        } catch (Exception ex) {
            throw new BadRequestException("Seat already exists in this hall for row=" +
                    request.rowNumber() + ", seat=" + request.seatNumber());
        }
    }

    private SeatResponse toResponse(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getRowNumber(),
                seat.getSeatNumber(),
                seat.getHall().getId()
        );
    }
}
