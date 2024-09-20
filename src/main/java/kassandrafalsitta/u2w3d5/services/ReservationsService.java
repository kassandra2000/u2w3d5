package kassandrafalsitta.u2w3d5.services;

import kassandrafalsitta.u2w3d5.entities.Event;
import kassandrafalsitta.u2w3d5.entities.Reservation;
import kassandrafalsitta.u2w3d5.entities.User;
import kassandrafalsitta.u2w3d5.exceptions.BadRequestException;
import kassandrafalsitta.u2w3d5.exceptions.NotFoundException;
import kassandrafalsitta.u2w3d5.payloads.ReservationDTO;
import kassandrafalsitta.u2w3d5.repositories.ReservationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationsService {
    @Autowired
    private ReservationsRepository reservationsRepository;
    @Autowired
    private UsersService usersService;
    @Autowired
    private EventsService eventsService;

    public Page<Reservation> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.reservationsRepository.findAll(pageable);
    }

    public Reservation saveReservation(ReservationDTO body) {
        UUID userID = null;
        try {
            userID = UUID.fromString(body.userID());
        } catch (NumberFormatException e) {
            throw new BadRequestException("L'UUID dell' utente non è corretto");
        }
        UUID eventID = null;
        try {
            eventID = UUID.fromString(body.eventID());
        } catch (NumberFormatException e) {
            throw new BadRequestException("L'UUID dell' evento non è corretto");
        }
        Event event = eventsService.findById(eventID);
        User user = usersService.findById(userID);
        Optional<Reservation> existingReservation = this.reservationsRepository.findByUserIdAndEvent_DateEvent(userID, event.getDateEvent());
        System.out.println(existingReservation);
        this.reservationsRepository.findByUserIdAndEvent_DateEvent(userID,event.getDateEvent()).ifPresent(
                reservation -> {
                    throw new BadRequestException("La data " +event.getDateEvent() + " è già in uso per l'utente " + user.getId());
                }
        );


        Reservation reservation = new Reservation( event, user);
        return this.reservationsRepository.save(reservation);
    }

    public Reservation findById(UUID reservationId) {
        return this.reservationsRepository.findById(reservationId).orElseThrow(() -> new NotFoundException(reservationId));
    }

    public Reservation findByIdAndUpdate(UUID reservationId, ReservationDTO updatedReservation) {
        UUID userID = null;
        try {
            userID = UUID.fromString(updatedReservation.userID());
        } catch (NumberFormatException e) {
            throw new BadRequestException("L'UUID dell' utente non è corretto");
        }
        UUID eventID = null;
        try {
            eventID = UUID.fromString(updatedReservation.eventID());
        } catch (NumberFormatException e) {
            throw new BadRequestException("L'UUID dell' evento non è corretto");
        }
        Reservation found = findById(reservationId);

        Event event = eventsService.findById(eventID);
        User user = usersService.findById(userID);
        found.setEvent(event);
        found.setUser(user);
        return this.reservationsRepository.save(found);
    }

    public void findByIdAndDelete(UUID reservationId) {
        this.reservationsRepository.delete(this.findById(reservationId));
    }

    public List<Reservation> findByUser(User user) {
        return this.reservationsRepository.findByUser(user);
    }

}
