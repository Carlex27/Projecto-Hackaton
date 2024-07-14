package com.example.project_hackaton.repositories;

import com.example.project_hackaton.entities.Teams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamsRepository extends JpaRepository<Teams, Long> {
    Optional<Teams> findByName(String name);
    List<Teams> findAllByEventId(Long eventId);
    Optional<Teams> findByNameAndEventId(String name, Long eventId);
}
