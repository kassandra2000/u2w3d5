package kassandrafalsitta.u2w3d5.controllers;

import kassandrafalsitta.u2w3d5.entities.Reservation;
import kassandrafalsitta.u2w3d5.entities.User;
import kassandrafalsitta.u2w3d5.exceptions.NotFoundException;
import kassandrafalsitta.u2w3d5.payloads.ReservationDTO;
import kassandrafalsitta.u2w3d5.payloads.UserDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private ReservationsService reservationsService;


    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "id") String sortBy) {
        return this.usersService.findAll(page, size, sortBy);
    }

    @GetMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(@PathVariable UUID employeeId) {
        return usersService.findById(employeeId);
    }

    @PutMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public User findUserByIdAndUpdate(@PathVariable UUID employeeId, @RequestBody @Validated UserDTO body) {
        return usersService.findByIdAndUpdate(employeeId, body);
    }

    @DeleteMapping("/{employeeId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findUserByIdAndDelete(@PathVariable UUID employeeId) {
        usersService.findByIdAndDelete(employeeId);
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
        Reservation employeeReservation = reservationList.stream().filter(reservation -> reservation.getId().equals(reservationId)).findFirst()
                .orElseThrow(() -> new NotFoundException(reservationId));
        return reservationsService.findById(employeeReservation.getId());
    }


    @PutMapping("/me/reservation/{reservationId}")
    public Reservation updateProfileReservation(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID reservationId, @RequestBody ReservationDTO body) {
        List<Reservation> reservationList = this.reservationsService.findByUser(currentAuthenticatedUser);
        Reservation employeeReservation = reservationList.stream().filter(reservation -> reservation.getId().equals(reservationId)).findFirst()
                .orElseThrow(() -> new NotFoundException(reservationId));
        return this.reservationsService.findByIdAndUpdate(employeeReservation.getId(), body);
    }

    @DeleteMapping("/me/reservation/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfileReservation(@AuthenticationPrincipal User currentAuthenticatedUser, @PathVariable UUID reservationId) {
        List<Reservation> reservationList = this.reservationsService.findByUser(currentAuthenticatedUser);
        Reservation employeeReservation = reservationList.stream().filter(reservation -> reservation.getId().equals(reservationId)).findFirst()
                .orElseThrow(() -> new NotFoundException(reservationId));
        this.reservationsService.findByIdAndDelete(employeeReservation.getId());
    }
}
