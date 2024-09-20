package kassandrafalsitta.u2w3d5.services;

import kassandrafalsitta.u2w3d5.entities.Event;
import kassandrafalsitta.u2w3d5.enums.StateEvent;
import kassandrafalsitta.u2w3d5.exceptions.BadRequestException;
import kassandrafalsitta.u2w3d5.exceptions.NotFoundException;
import kassandrafalsitta.u2w3d5.payloads.EventDTO;
import kassandrafalsitta.u2w3d5.payloads.EventRespUserDT0;
import kassandrafalsitta.u2w3d5.payloads.StateEventDTO;
import kassandrafalsitta.u2w3d5.repositories.EventsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.UUID;

@Service
public class EventsService {
    @Autowired
    private EventsRepository eventsRepository;

    public Page<Event> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventsRepository.findAll(pageable);
    }

    public Event saveEvent(EventDTO body) {
        LocalDate dateEvent = null;
        try {
            dateEvent = LocalDate.parse(body.dateEvent());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + body.dateEvent() + " inserire nel seguente formato: AAAA/MM/GG");
        }
        Optional<Event> dateEventAndPlace = eventsRepository.findByDateEventAndPlace(dateEvent, body.place());
        if (dateEventAndPlace.isPresent()) {
            throw new BadRequestException("La data " + body.dateEvent() + " e il luogo " + body.place() + " sono già in uso!");
        }

        Event employee = new Event(body.title(), body.description(), dateEvent, body.place(), Integer.parseInt(body.numberOfPlaces()));

        return this.eventsRepository.save(employee);
    }

    public Event findById(UUID travelId) {
        return this.eventsRepository.findById(travelId).orElseThrow(() -> new NotFoundException(travelId));
    }

    public Event findByIdAndUpdate(UUID reservationId, EventDTO updatedEvent) {
        Event found = findById(reservationId);
        LocalDate date = null;
        try {
            date = LocalDate.parse(updatedEvent.dateEvent());
        } catch (DateTimeParseException e) {
            throw new BadRequestException("Il formato della data non è valido: " + updatedEvent.dateEvent() + " inserire nel seguente formato: AAAA/MM/GG");
        }
        found.setDateEvent(date);
        found.setTitle(updatedEvent.title());
        found.setPlace(updatedEvent.place());
        found.setDescription(updatedEvent.description());
        found.setNumberOfPlaces(Integer.parseInt(updatedEvent.numberOfPlaces()));
        return this.eventsRepository.save(found);
    }

    public void findByIdAndDelete(UUID travelId) {
        this.eventsRepository.delete(this.findById(travelId));
    }

    public Event findByIdAndUpdateState(UUID eventId, StateEventDTO updatedStateEvent) {
        Event found = findById(eventId);
        StateEvent stateEvent = null;
        try {
            stateEvent = StateEvent.valueOf(updatedStateEvent.stateEvent().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Stato dell'evento non valido: " + updatedStateEvent.stateEvent());
        }
        found.setStateEvent(stateEvent);
        return this.eventsRepository.save(found);
    }
}
