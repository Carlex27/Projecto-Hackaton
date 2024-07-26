package com.example.project_hackaton.service;

import com.example.project_hackaton.entity.TeamMember;
import com.example.project_hackaton.entity.Teams;
import com.example.project_hackaton.entity.User;
import com.example.project_hackaton.repository.TeamMemberRepository;
import com.example.project_hackaton.service.interfaces.ITeamService;
import com.example.project_hackaton.service.interfaces.IUserSearchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TeamMemberService {
    private final TeamMemberRepository teamMemberRepository;
    private final IUserSearchService userSearchService;
    private final ITeamService teamService;
    //CRUD

    //Create
    public TeamMember createTeamMember(Long idTeam, Long idUser, String rol, boolean isLeader){
        User user = userSearchService.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Teams team = teamService.getTeamById(idTeam)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        TeamMember teamMember = TeamMember.builder()
                .team(team)
                .user(user)
                .rol(rol)
                .isLeader(isLeader)
                .build();
        return teamMemberRepository.save(teamMember);
    }

    //Read
    public Optional<TeamMember> getTeamMemberById(Long id){
        return teamMemberRepository.findById(id);
    }
    public Optional<TeamMember> getTeamMemberByUserIdAndTeamId(Long idUser, Long idTeam){
        return teamMemberRepository.findByUserIdAndTeamId(idUser, idTeam);
    }

    //Update
    public TeamMember updateTeamMember(Long id, Long idTeam, Long idUser, String rol, boolean isLeader){
        TeamMember teamMember = getTeamMemberById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team member not found"));
        User user = userSearchService.findById(idUser)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Teams team = teamService.getTeamById(idTeam)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        teamMember.setTeam(team);
        teamMember.setUser(user);
        teamMember.setRol(rol);
        teamMember.setLeader(isLeader);
        return teamMemberRepository.save(teamMember);
    }

    //Delete
    public void deleteTeamMember(Long id){
        teamMemberRepository.deleteById(id);
    }
    public void deleteTeamMemberByUserIdAndTeamId(Long idUser, Long idTeam){
        TeamMember teamMember = getTeamMemberByUserIdAndTeamId(idUser, idTeam)
                .orElseThrow(() -> new EntityNotFoundException("Team member not found"));
        teamMemberRepository.delete(teamMember);
    }


}
