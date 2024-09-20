package kassandrafalsitta.u2w3d5.controllers;

import kassandrafalsitta.u2w3d5.entities.Event;
import kassandrafalsitta.u2w3d5.exceptions.BadRequestException;
import kassandrafalsitta.u2w3d5.payloads.EventDTO;
import kassandrafalsitta.u2w3d5.payloads.EventRespDTO;
import kassandrafalsitta.u2w3d5.services.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventsService eventsService;

    @GetMapping
    public Page<Event> getAllEvents(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "id") String sortBy) {
        return this.eventsService.findAll(page, size, sortBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyAuthority('EVENT_ORGANIZER','ADMIN')")
    public EventRespDTO createEvent(@RequestBody @Validated EventDTO body, BindingResult validationResult) {
        if(validationResult.hasErrors())  {
            String messages = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            return new EventRespDTO(this.eventsService.saveEvent(body).getId());
        }
    }

    @GetMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('EVENT_ORGANIZER','ADMIN')")
    public Event getEventById(@PathVariable UUID eventId) {
        return eventsService.findById(eventId);
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAnyAuthority('EVENT_ORGANIZER','ADMIN')")
    public Event findEventByIdAndUpdate(@PathVariable UUID eventId, @RequestBody @Validated EventDTO body) {
        return eventsService.findByIdAndUpdate(eventId, body);
    }

    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('EVENT_ORGANIZER','ADMIN')")
    public void findEventByIdAndDelete(@PathVariable UUID eventId) {
        eventsService.findByIdAndDelete(eventId);
    }



}
