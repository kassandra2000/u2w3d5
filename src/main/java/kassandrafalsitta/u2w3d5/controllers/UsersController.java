package kassandrafalsitta.u2w3d5.controllers;

import kassandrafalsitta.u2w3d5.entities.Event;
import kassandrafalsitta.u2w3d5.entities.Reservation;
import kassandrafalsitta.u2w3d5.entities.User;
import kassandrafalsitta.u2w3d5.exceptions.NotFoundException;
import kassandrafalsitta.u2w3d5.payloads.*;
import kassandrafalsitta.u2w3d5.services.EventsService;
import kassandrafalsitta.u2w3d5.services.ReservationsService;
import kassandrafalsitta.u2w3d5.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private ReservationsService reservationsService;
    @Autowired
    private EventsService eventsService;


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy) {
        return this.usersService.findAll(page, size, sortBy);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(@PathVariable UUID userId) {
        return usersService.findById(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findUserByIdAndUpdate(@PathVariable UUID userId, @RequestBody @Validated UserDTO body) {
        return usersService.findByIdAndUpdate(userId, body);
    }
    @PatchMapping("/{userId}")
    public User findByIdAndUpdateRole(@PathVariable UUID userId, @RequestBody @Validated UserRoleDTO body) {
        return usersService.findByIdAndUpdateRole(userId, body);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findUserByIdAndDelete(@PathVariable UUID userId) {
        usersService.findByIdAndDelete(userId);
    }


    // me
    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return currentAuthenticatedUser;
    }

    @PutMapping("/me")
    public User updateProfile(@AuthenticationPrincipal User currentAuthenticatedUser, @RequestBody UserDTO body) {
        return this.usersService.findByIdAndUpdate(currentAuthenticatedUser.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User currentAuthenticatedUser) {
        this.usersService.findByIdAndDelete(currentAuthenticatedUser.getId());
    }


    //  me/reservation
    @GetMapping("/me/reservation")
    public List<Reservation> getProfileReservation(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return this.reservationsService.findByUser(currentAuthenticatedUser);
    }

    @GetMapping("/me/reservation/{reservationId}")
    public Reservation getProfileReservationById(@AuthenticationPrincipal User currentAuthenticatedUser,@PathVariable UUID reservationId) {
        List<Reservation> reservationList = this.reservationsService.findByUser(currentAuthenticatedUser);
        Reservation userReservation = reservationList.stream().filter(reservation -> reservation.getId().equals(reservationId)).findFirst()
                .orElseThrow(() -> new NotFoundException(reservationId));
        return reservationsService.findById(userReservation.getId());
    }


    @PutMapping("/me/reservation/{reservationId}")
    public Reservation updateProfileReservation(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID reservationId, @RequestBody ReservationDTO body) {
        List<Reservation> reservationList = this.reservationsService.findByUser(currentAuthenticatedUser);
        Reservation userReservation = reservationList.stream().filter(reservation -> reservation.getId().equals(reservationId)).findFirst()
                .orElseThrow(() -> new NotFoundException(reservationId));
        return this.reservationsService.findByIdAndUpdate(userReservation.getId(), body);
    }

    @DeleteMapping("/me/reservation/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfileReservation(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID reservationId) {
        List<Reservation> reservationList = this.reservationsService.findByUser(currentAuthenticatedUser);
        Reservation userReservation = reservationList.stream().filter(reservation -> reservation.getId().equals(reservationId)).findFirst()
                .orElseThrow(() -> new NotFoundException(reservationId));
        this.reservationsService.findByIdAndDelete(userReservation.getId());
    }

    // me/event
    @GetMapping("/me/event")
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public List<Event> getProfileEvent(@AuthenticationPrincipal User currentAuthenticatedUser) {
        return this.eventsService.findByUser(currentAuthenticatedUser);
    }

    @GetMapping("/me/event/{eventId}")
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public Event getProfileEventById(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID eventId) {
        List<Event> eventList = this.eventsService.findByUser(currentAuthenticatedUser);
        Event userEvent = eventList.stream().filter(event -> event.getId().equals(eventId)).findFirst()
                .orElseThrow(() -> new NotFoundException(eventId));
        return eventsService.findById(userEvent.getId());
    }


    @PutMapping("/me/event/{eventId}")
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public Event updateProfileEvent(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID eventId, @RequestBody EventDTO body) {
        List<Event> eventList = this.eventsService.findByUser(currentAuthenticatedUser);
        Event userEvent = eventList.stream().filter(event -> event.getId().equals(eventId)).findFirst()
                .orElseThrow(() -> new NotFoundException(eventId));
        return this.eventsService.findByIdAndUpdate(userEvent.getId(), body);
    }

    @DeleteMapping("/me/event/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public void deleteProfileEvent(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID eventId) {
        List<Event> eventList = this.eventsService.findByUser(currentAuthenticatedUser);
        Event userEvent = eventList.stream().filter(event -> event.getId().equals(eventId)).findFirst()
                .orElseThrow(() -> new NotFoundException(eventId));
        this.eventsService.findByIdAndDelete(userEvent.getId());
    }

    @PatchMapping("/me/event/{eventId}")
    @PreAuthorize("hasAuthority('EVENT_ORGANIZER')")
    public Event findEventByIdAndUpdateState(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID eventId, @RequestBody @Validated StateEventDTO body) {
        List<Event> eventList = this.eventsService.findByUser(currentAuthenticatedUser);
        Event userEvent = eventList.stream().filter(event -> event.getId().equals(eventId)).findFirst()
                .orElseThrow(() -> new NotFoundException(eventId));
        return eventsService.findByIdAndUpdateState(userEvent.getId(), body);
    }
}
