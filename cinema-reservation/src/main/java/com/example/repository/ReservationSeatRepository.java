package com.example.repository;

import com.example.model.ReservationSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    List<ReservationSeat> findByReservationId(Long reservationId);
    List<ReservationSeat> findByScreeningId(Long screeningId);
    boolean existsByScreeningIdAndSeatId(Long screeningId, Long seatId);

    void deleteByReservationId(Long reservationId);
}
