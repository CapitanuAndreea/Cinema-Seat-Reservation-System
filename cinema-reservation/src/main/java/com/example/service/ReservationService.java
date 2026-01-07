package com.example.service;

import com.example.model.*;
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

    @Transactional
    public Reservation createReservation(Long customerId, Long screeningId, List<Long> seatIds) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id=" + customerId));

        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("Screening not found with id=" + screeningId));

        // Create reservation first
        Reservation reservation = reservationRepository.save(new Reservation(customer, screening));

        // For each seat validate and reserve
        for (Long seatId : seatIds) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found with id=" + seatId));

            // Seat must belong to the same hall as the screening
            if (!seat.getHall().getId().equals(screening.getHall().getId())) {
                throw new RuntimeException("Seat " + seatId + " does not belong to screening hall");
            }

            // Prevent double booking
            if (reservationSeatRepository.existsByScreeningIdAndSeatId(screeningId, seatId)) {
                throw new RuntimeException("Seat " + seatId + " is already reserved for this screening");
            }

            reservationSeatRepository.save(new ReservationSeat(reservation, screening, seat));
        }

        return reservation;
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id=" + reservationId));

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        // Free seats by deleting join rows
        reservationSeatRepository.deleteByReservationId(reservationId);
    }
}
