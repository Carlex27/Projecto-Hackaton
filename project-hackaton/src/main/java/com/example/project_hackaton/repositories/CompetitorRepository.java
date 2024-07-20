package com.example.project_hackaton.repositories;

import com.example.project_hackaton.entities.Competitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompetitorRepository extends JpaRepository<Competitor, Long> {
    List<Competitor> findAllByEventId(Long eventId);
    List<Competitor> findAllByUserId(Long userId);
    Optional<Competitor> findByUserIdAndEventId(Long userId, Long eventId);
}
