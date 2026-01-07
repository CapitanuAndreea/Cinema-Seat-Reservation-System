package com.example.repository;

import com.example.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByMovieId(Long movieId);
    List<Screening> findByHallId(Long hallId);
    List<Screening> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);
}
