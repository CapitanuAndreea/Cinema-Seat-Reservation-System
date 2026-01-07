package com.example.service;

import com.example.exceptions.NotFoundException;
import com.example.model.Cinema;
import com.example.model.dto.CinemaResponse;
import com.example.model.dto.CreateCinemaRequest;
import com.example.repository.CinemaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    public CinemaService(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    public List<CinemaResponse> findAll() {
        return cinemaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CinemaResponse findById(Long id) {
        Cinema cinema = cinemaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cinema not found with id=" + id));
        return toResponse(cinema);
    }

    @Transactional
    public CinemaResponse add(CreateCinemaRequest request) {
        Cinema cinema = new Cinema(request.name(), request.city());
        Cinema saved = cinemaRepository.save(cinema);
        return toResponse(saved);
    }

    private CinemaResponse toResponse(Cinema c) {
        return new CinemaResponse(c.getId(), c.getName(), c.getCity());
    }
}
