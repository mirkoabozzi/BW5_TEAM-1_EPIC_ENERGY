package BW5_TEAM_1.EPIC.ENERGY.SERVICES.controllers;


import BW5_TEAM_1.EPIC.ENERGY.SERVICES.dto.UserDTO;
import BW5_TEAM_1.EPIC.ENERGY.SERVICES.entities.User;
import BW5_TEAM_1.EPIC.ENERGY.SERVICES.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    // GET per utenti
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<User> users = usersService.getAllEmployee(page, size, sortBy);
        return ResponseEntity.ok(users);
    }

    // GET utente per ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        User user = usersService.findById(id);
        return ResponseEntity.ok(user);
    }

    // POST nuovo utente
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO userDTO) {
        User newUser = usersService.saveUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    // PUT aggiorna l'utente
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody UserDTO userDTO) {
        User existingUser = usersService.findById(id);

        // Aggiorna i campi del utente
        existingUser.setUsername(userDTO.username());
        existingUser.setEmail(userDTO.email());
        existingUser.setPassword(usersService.bcrypt.encode(userDTO.password()));
        existingUser.setName(userDTO.name());
        existingUser.setSurname(userDTO.surname());
        existingUser.setAvatar("https://ui-avatars.com/api/?name=" + userDTO.name() + "+" + userDTO.surname());

        // Salva l'utente aggiornato
        User updatedUser = usersService.saveUser(userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE elimina utente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        User userToDelete = usersService.findById(id);
        usersService.userRepository.delete(userToDelete);
        return ResponseEntity.noContent().build();
    }
}
