package com.example.project_hackaton.repositories;

import com.example.project_hackaton.entities.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>{
    Optional<TeamMember> findByUserIdAndTeamId(Long userId, Long teamId);
}
