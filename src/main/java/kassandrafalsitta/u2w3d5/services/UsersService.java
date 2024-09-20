package kassandrafalsitta.u2w3d5.services;

import kassandrafalsitta.u2w3d5.entities.User;
import kassandrafalsitta.u2w3d5.exceptions.BadRequestException;
import kassandrafalsitta.u2w3d5.exceptions.NotFoundException;
import kassandrafalsitta.u2w3d5.payloads.UserDTO;
import kassandrafalsitta.u2w3d5.repositories.UsersRepository;
import kassandrafalsitta.u2w3d5.tools.MailgunSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private MailgunSender mailgunSender;


    public Page<User> findAll(int page, int size, String sortBy) {
        if (page > 100) page = 100;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.usersRepository.findAll(pageable);
    }

    public User saveUser(UserDTO body) {
        this.usersRepository.findByEmail(body.email()).ifPresent(
                employee -> {
                    throw new BadRequestException("L'email " + body.email() + " è già in uso!");
                }
        );
        User employee = new User(body.username(), body.name(), body.surname(), body.email(),bcrypt.encode(body.password()));
        User savedUser = this.usersRepository.save(employee);

        // 4. Invio email conferma registrazione
        mailgunSender.sendRegistrationEmail(savedUser);
        return savedUser ;
    }

    public User findById(UUID employeeId) {
        return this.usersRepository.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId));
    }

    public User findByIdAndUpdate(UUID employeeId, UserDTO updatedUser) {
        User found = findById(employeeId);
        found.setUsername(updatedUser.username());
        found.setName(updatedUser.name());
        found.setSurname(updatedUser.surname());
        found.setEmail(updatedUser.email());
        found.setPassword(updatedUser.password());
        return this.usersRepository.save(found);
    }

    public void findByIdAndDelete(UUID employeeId) {
        this.usersRepository.delete(this.findById(employeeId));
    }


    public User findByEmail(String email) {
        return usersRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con l'email " + email + " non è stato trovato!"));
    }
}
