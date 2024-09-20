package kassandrafalsitta.u2w3d5.controllers;

import kassandrafalsitta.u2w3d5.entities.Reservation;
import kassandrafalsitta.u2w3d5.exceptions.BadRequestException;
import kassandrafalsitta.u2w3d5.payloads.ReservationDTO;
import kassandrafalsitta.u2w3d5.payloads.ReservationRespDTO;
import kassandrafalsitta.u2w3d5.services.ReservationsService;
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
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationsService reservationsService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Reservation> getAllReservations(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "id") String sortBy) {
        return this.reservationsService.findAll(page, size, sortBy);
    }



    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationRespDTO createReservation(@RequestBody @Validated ReservationDTO body, BindingResult validationResult) {
        if(validationResult.hasErrors())  {
            String messages = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            return new ReservationRespDTO(this.reservationsService.saveReservation(body).getId());
        }
    }

    @GetMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Reservation getReservationById(@PathVariable UUID reservationId) {
        return reservationsService.findById(reservationId);
    }

    @PutMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Reservation findReservationByIdAndUpdate(@PathVariable UUID reservationId, @RequestBody @Validated ReservationDTO body) {
        return reservationsService.findByIdAndUpdate(reservationId, body);
    }

    @DeleteMapping("/{reservationId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findReservationByIdAndDelete(@PathVariable UUID reservationId) {
        reservationsService.findByIdAndDelete(reservationId);
    }
}
