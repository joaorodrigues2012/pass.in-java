package dev.com.passin.controllers;

import dev.com.passin.dto.attendee.AttendeeIdDTO;
import dev.com.passin.dto.attendee.AttendeeRequestDTO;
import dev.com.passin.dto.attendee.AttendeesListResponseDTO;
import dev.com.passin.dto.event.EventIdDTO;
import dev.com.passin.dto.event.EventRequestDTO;
import dev.com.passin.dto.event.EventResponseDTO;
import dev.com.passin.services.AttendeeService;
import dev.com.passin.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AttendeeService attendeeService;

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable String eventId) {
        EventResponseDTO event = this.eventService.getEventDetail(eventId);
        return ResponseEntity.ok(event);
    }

    @PostMapping
    public ResponseEntity<EventIdDTO> createEvent(@RequestBody EventRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        EventIdDTO eventIdDTO = this.eventService.createEvent(body);
        var uri = uriComponentsBuilder.path("/events/{id}").buildAndExpand(eventIdDTO.eventId()).toUri();
        return ResponseEntity.created(uri).body(eventIdDTO);
    }

    @PostMapping("/{eventId}/attendees")
    public ResponseEntity<AttendeeIdDTO> registerParticipant(@PathVariable String eventId, @RequestBody AttendeeRequestDTO body, UriComponentsBuilder uriComponentsBuilder) {
        AttendeeIdDTO attendeeIdDTO = this.eventService.registerAttendeeOnEvent(eventId, body);
        var uri = uriComponentsBuilder.path("/attendees/{attendeeId}/badge").buildAndExpand(attendeeIdDTO.attendeeId()).toUri();
        return ResponseEntity.created(uri).body(attendeeIdDTO);
    }

    @GetMapping("/attendees/{eventId}")
    public ResponseEntity<AttendeesListResponseDTO> getEventAttendees(@PathVariable String eventId) {
        AttendeesListResponseDTO attendeesListResponseDTO = this.attendeeService.getEventsAttendee(eventId);
        return ResponseEntity.ok(attendeesListResponseDTO);
    }
}
