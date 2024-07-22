package com.example.project_hackaton.repository;

import com.example.project_hackaton.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByName(String name);
    Optional<Event> findById(Long id);
    Optional<Event> findByCreatorId(Long creatorId);
}
