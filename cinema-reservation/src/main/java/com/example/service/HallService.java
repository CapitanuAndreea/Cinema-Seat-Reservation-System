package com.example.service;

import com.example.exceptions.NotFoundException;
import com.example.model.Cinema;
import com.example.model.Hall;
import com.example.model.dto.CreateHallRequest;
import com.example.model.dto.HallResponse;
import com.example.repository.CinemaRepository;
import com.example.repository.HallRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class HallService {

    private final HallRepository hallRepository;
    private final CinemaRepository cinemaRepository;

    public HallService(HallRepository hallRepository, CinemaRepository cinemaRepository) {
        this.hallRepository = hallRepository;
        this.cinemaRepository = cinemaRepository;
    }

    public List<HallResponse> findAll() {
        return hallRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public List<HallResponse> findByCinema(Long cinemaId) {
        return hallRepository.findByCinemaId(cinemaId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public HallResponse addHall(CreateHallRequest request) {
        Cinema cinema = cinemaRepository.findById(request.cinemaId())
                .orElseThrow(() -> new NotFoundException("Cinema not found with id=" + request.cinemaId()));

        Hall hall = new Hall(request.name(), cinema);
        Hall saved = hallRepository.save(hall);
        return toResponse(saved);
    }

    private HallResponse toResponse(Hall hall) {
        return new HallResponse(hall.getId(), hall.getName(), hall.getCinema().getId());
    }
}
