package kassandrafalsitta.u2w3d5.controllers;

import kassandrafalsitta.u2w3d5.exceptions.BadRequestException;
import kassandrafalsitta.u2w3d5.payloads.UserDTO;
import kassandrafalsitta.u2w3d5.payloads.UserLoginDTO;
import kassandrafalsitta.u2w3d5.payloads.UserLoginRespDTO;
import kassandrafalsitta.u2w3d5.payloads.UserRespDTO;
import kassandrafalsitta.u2w3d5.services.AuthService;
import kassandrafalsitta.u2w3d5.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UsersService UsersService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public UserLoginRespDTO login(@RequestBody UserLoginDTO payload) {
        return new UserLoginRespDTO(this.authService.checkCredentialsAndGenerateToken(payload));
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRespDTO createUser(@RequestBody  @Validated UserDTO body, BindingResult validationResult) {
        if(validationResult.hasErrors())  {
            String messages = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            return new UserRespDTO(this.UsersService.saveUser(body).getId());
        }
    }
}
