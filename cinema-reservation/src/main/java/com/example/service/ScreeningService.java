package com.example.service;

import com.example.exceptions.BadRequestException;
import com.example.exceptions.NotFoundException;
import com.example.model.Hall;
import com.example.model.Movie;
import com.example.model.Screening;
import com.example.model.dto.CreateScreeningRequest;
import com.example.model.dto.ScreeningResponse;
import com.example.repository.HallRepository;
import com.example.repository.MovieRepository;
import com.example.repository.ScreeningRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.model.ReservationSeat;
import com.example.model.Seat;
import com.example.model.dto.SeatAvailabilityResponse;
import com.example.repository.ReservationSeatRepository;
import com.example.repository.SeatRepository;

import java.util.List;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    public ScreeningService(ScreeningRepository screeningRepository,
                            MovieRepository movieRepository,
                            HallRepository hallRepository,
                            SeatRepository seatRepository,
                            ReservationSeatRepository reservationSeatRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.hallRepository = hallRepository;
        this.seatRepository = seatRepository;
        this.reservationSeatRepository = reservationSeatRepository;
    }

    public List<ScreeningResponse> findAll() {
        return screeningRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public ScreeningResponse findById(Long id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Screening not found with id=" + id));
        return toResponse(screening);
    }

    public List<ScreeningResponse> findByMovie(Long movieId) {
        return screeningRepository.findByMovieId(movieId).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ScreeningResponse> findByHall(Long hallId) {
        return screeningRepository.findByHallId(hallId).stream()
                .map(this::toResponse)
                .toList();
    }

    public java.util.List<SeatAvailabilityResponse> getSeatMap(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new NotFoundException("Screening not found with id=" + screeningId));

        Long hallId = screening.getHall().getId();

        java.util.List<Seat> allSeats = seatRepository.findByHallId(hallId);

        Set<Long> reservedSeatIds = reservationSeatRepository.findByScreeningId(screeningId).stream()
                .map(rs -> rs.getSeat().getId())
                .collect(Collectors.toSet());

        return allSeats.stream()
                .sorted(Comparator.comparing(Seat::getRowNumber).thenComparing(Seat::getSeatNumber))
                .map(seat -> new SeatAvailabilityResponse(
                        seat.getId(),
                        seat.getRowNumber(),
                        seat.getSeatNumber(),
                        reservedSeatIds.contains(seat.getId())
                ))
                .toList();
    }

    @Transactional
    public ScreeningResponse addScreening(CreateScreeningRequest request) {
        if (request.price().signum() < 0) {
            throw new BadRequestException("Price must be >= 0");
        }

        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new NotFoundException("Movie not found with id=" + request.movieId()));

        Hall hall = hallRepository.findById(request.hallId())
                .orElseThrow(() -> new NotFoundException("Hall not found with id=" + request.hallId()));

        Screening screening = new Screening(movie, hall, request.startTime(), request.price());

        try {
            Screening saved = screeningRepository.save(screening);
            return toResponse(saved);
        } catch (Exception ex) {
            throw new BadRequestException("A screening already exists in this hall at the same startTime");
        }
    }

    private ScreeningResponse toResponse(Screening screening) {
        return new ScreeningResponse(
                screening.getId(),
                screening.getMovie().getId(),
                screening.getMovie().getTitle(),
                screening.getMovie().getDurationMinutes(),
                screening.getHall().getId(),
                screening.getHall().getName(),
                screening.getHall().getCinema().getId(),
                screening.getStartTime(),
                screening.getPrice()
        );
    }
}
