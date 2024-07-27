package com.example.project_hackaton.controller;

import com.example.project_hackaton.entity.Event;
import com.example.project_hackaton.entity.User;
import com.example.project_hackaton.service.EventService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventController {
    private final EventService eventService;


    //CRUD
    //CREATE
    @PreAuthorize("hasAuthority('SCOPE_WRITE_TEAM')")
    @PostMapping("/create")
    public void createEvent(
            @AuthenticationPrincipal User user,
            @RequestBody Event event
            ){
        Long id = user.getId();
        eventService.createEvent(event, id);
    }
    //READ

    //UPDATE

    //DELETE


}
