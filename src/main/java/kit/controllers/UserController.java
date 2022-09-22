package kit.controllers;

import kit.exceptions.*;
import kit.models.Animal;
import kit.models.User;
import kit.security.CustomUserDetails;
import kit.services.AnimalService;
import kit.services.CustomUserDetailsService;
import kit.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping(value = "/api/admin", produces = {"application/json"})
    private void isAdmin() {
        ResponseEntity.status(HttpStatus.OK);
    }

    @GetMapping(value = "/api/users", produces = {"application/json"})
    private ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping(value = "/api/users/{userId}", produces = {"application/json"})
    private ResponseEntity<User> getOneUser(@PathVariable Long userId) throws
            UserNotFoundException {
        if (userService.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }
        return ResponseEntity.ok(userService.findById(userId).get());
    }

    @GetMapping(value = "/api/users/{userId}/animals", produces = {"application/json"})
    private ResponseEntity<Set<Animal>> getUserAnimal(@PathVariable Long userId) throws
            UserNotFoundException {

        if (userService.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }

        Set<Animal> animals = userService.findById(userId).get().getAnimals();

        return ResponseEntity.ok(animals);
    }

    @GetMapping(value = "/api/users/{userId}/animals/{animalId}", produces = {"application/json"})
    private ResponseEntity<User> addAnimalToUser(@PathVariable Long userId, @PathVariable Long animalId) throws
            UserNotFoundException, UserAlreadyHaveAnimalException,
            AnimalNotFoundException, NoAccessException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = userService.findById(userId).get();
        Animal animal = animalService.findById(animalId).get();

        if (animalService.findById(animalId).isEmpty()) {
            throw new AnimalNotFoundException();
        }

        if (userService.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }

        if (userService.findById(userId).isPresent() && !principal.getUsername().equals(user.getUsername())) {
            throw new NoAccessException();
        }

        if (userService.findById(userId).get().getAnimals().contains(animalService.findById(animalId).get())) {
            throw new UserAlreadyHaveAnimalException();
        }
        return ResponseEntity.ok().body(userService.addAnimalToUser(user, animal));
    }

    @DeleteMapping(value = "/api/users/{userId}/animals/{animalId}", produces = {"application/json"})
    private ResponseEntity<User> deleteAnimalFromUser(@PathVariable Long userId, @PathVariable Long animalId) throws
            UserNotFoundException, NoAccessException,
            AnimalNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        User user = userService.findById(userId).get();

        if (userService.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }

        if (userService.findById(userId).isPresent() && !principal.getUsername().equals(user.getUsername())) {
            throw new NoAccessException();
        }


        if (animalService.findById(animalId).isEmpty()) {
            throw new AnimalNotFoundException();
        }

        Animal animal = animalService.findById(animalId).get();
        userService.deleteAnimalFromUser(user, animal);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping(value = "/register", produces = "application/json")
    private ResponseEntity<User> register(@Valid @RequestBody User user) throws
            UserAlreadyExistsException {

        if (userService.findByUsername(user.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.addRole(roleService.findByName("USER"));
        userService.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping(value = "/api/users/{userId}", produces = "application/json")
    private ResponseEntity<User> deleteUser(@PathVariable Long userId) throws
            NoAccessException, UserNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findById(userId).get();

        if (!authentication.getAuthorities().contains("ADMIN")) {
            throw new NoAccessException();
        }

        if (userService.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }
        userService.delete(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }


}
