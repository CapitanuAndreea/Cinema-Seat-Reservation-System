package com.example.service;

import com.example.exceptions.NotFoundException;
import com.example.model.Movie;
import com.example.model.dto.CreateMovieRequest;
import com.example.model.dto.MovieResponse;
import com.example.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieResponse> findAll() {
        return movieRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public MovieResponse findById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found with id=" + id));
        return toResponse(movie);
    }

    @Transactional
    public MovieResponse add(CreateMovieRequest request) {
        Movie movie = new Movie(request.title(), request.durationMinutes(), request.rating(), request.description());
        Movie saved = movieRepository.save(movie);
        return toResponse(saved);
    }

    @Transactional
    public MovieResponse update(Long id, CreateMovieRequest request) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Movie not found with id=" + id));

        movie.setTitle(request.title());
        movie.setDurationMinutes(request.durationMinutes());
        movie.setRating(request.rating());
        movie.setDescription(request.description());

        Movie saved = movieRepository.save(movie);
        return toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new NotFoundException("Movie not found with id=" + id);
        }
        movieRepository.deleteById(id);
    }

    private MovieResponse toResponse(Movie m) {
        return new MovieResponse(m.getId(), m.getTitle(), m.getDurationMinutes(), m.getRating(), m.getDescription());
    }
}
