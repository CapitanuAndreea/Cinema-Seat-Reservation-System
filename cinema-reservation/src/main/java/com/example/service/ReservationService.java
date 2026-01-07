package com.example.service;

import com.example.exceptions.BadRequestException;
import com.example.exceptions.NotFoundException;
import com.example.model.*;
import com.example.model.dto.CreateReservationRequest;
import com.example.model.dto.ReservationResponse;
import com.example.model.dto.ReservedSeatResponse;
import com.example.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReservationService {

    private final CustomerRepository customerRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    public ReservationService(CustomerRepository customerRepository,
                              ScreeningRepository screeningRepository,
                              SeatRepository seatRepository,
                              ReservationRepository reservationRepository,
                              ReservationSeatRepository reservationSeatRepository) {
        this.customerRepository = customerRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.reservationRepository = reservationRepository;
        this.reservationSeatRepository = reservationSeatRepository;
    }

    public ReservationResponse findById(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with id=" + reservationId));
        return toResponse(reservation);
    }

    public List<ReservedSeatResponse> getSeats(Long reservationId) {
        // Ensure reservation exists
        if (!reservationRepository.existsById(reservationId)) {
            throw new NotFoundException("Reservation not found with id=" + reservationId);
        }

        return reservationSeatRepository.findByReservationId(reservationId).stream()
                .map(rs -> new ReservedSeatResponse(
                        rs.getSeat().getId(),
                        rs.getSeat().getRowNumber(),
                        rs.getSeat().getSeatNumber()
                ))
                .toList();
    }

    @Transactional
    public ReservationResponse create(CreateReservationRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new NotFoundException("Customer not found with id=" + request.customerId()));

        Screening screening = screeningRepository.findById(request.screeningId())
                .orElseThrow(() -> new NotFoundException("Screening not found with id=" + request.screeningId()));

        // Create reservation first
        Reservation reservation = reservationRepository.save(new Reservation(customer, screening));

        // Reserve seats
        for (Long seatId : request.seatIds()) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new NotFoundException("Seat not found with id=" + seatId));

            // seat must belong to same hall as screening
            if (!seat.getHall().getId().equals(screening.getHall().getId())) {
                throw new BadRequestException("Seat " + seatId + " does not belong to the screening hall");
            }

            // no double booking
            if (reservationSeatRepository.existsByScreeningIdAndSeatId(screening.getId(), seatId)) {
                throw new BadRequestException("Seat " + seatId + " is already reserved for this screening");
            }

            reservationSeatRepository.save(new ReservationSeat(reservation, screening, seat));
        }

        return toResponse(reservation);
    }

    @Transactional
    public void cancel(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("Reservation not found with id=" + reservationId));

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BadRequestException("Reservation is already cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // Free seats
        reservationSeatRepository.deleteByReservationId(reservationId);
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getCustomer().getId(),
                reservation.getScreening().getId(),
                reservation.getStatus(),
                reservation.getCreatedAt()
        );
    }
}
