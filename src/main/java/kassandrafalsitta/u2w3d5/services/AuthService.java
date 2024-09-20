package kassandrafalsitta.u2w3d5.services;

import kassandrafalsitta.u2w3d5.entities.User;
import kassandrafalsitta.u2w3d5.exceptions.UnauthorizedException;
import kassandrafalsitta.u2w3d5.payloads.UserLoginDTO;
import kassandrafalsitta.u2w3d5.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UsersService usersService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public String checkCredentialsAndGenerateToken(UserLoginDTO body) {

        User found = this.usersService.findByEmail(body.email());
        if (bcrypt.matches(body.password(), found.getPassword())) {
            return jwtTools.createToken(found);
        } else {
            throw new UnauthorizedException("Credenziali errate!");
        }


    }
}
