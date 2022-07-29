package artplancom.test.controllers;

import artplancom.test.models.Animal;
import artplancom.test.models.User;
import artplancom.test.repositories.AnimalsRepository;
import artplancom.test.repositories.RolesRepository;
import artplancom.test.repositories.UsersRepository;
import artplancom.test.security.CustomUserDetails;
import artplancom.test.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
public class UserController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AnimalsRepository animalsRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping(value = "/api/admin", produces = {"application/json"})
    private ResponseEntity isAdmin(){
        return ResponseEntity.ok("{\n\"status\": 200,\n\"message\": \"You're admin\"");
    }

    @GetMapping(value = "/api/users", produces = {"application/json"})
    private ResponseEntity getAllUsers() {
        return ResponseEntity.ok().body(usersRepository.findAll());
    }

    @GetMapping(value = "/api/users/{userId}", produces = {"application/json"})
    private ResponseEntity getOneUser(@PathVariable Long userId) {
        if (usersRepository.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404,\n\"message\": \"This user doesn't exist\"\n}");
        }
        return ResponseEntity
                .ok(usersRepository.findById(userId));
    }

    @GetMapping(value = "/api/users/{userId}/animals", produces = {"application/json"})
    private ResponseEntity getUserAnimal(@PathVariable Long userId) {

        if (usersRepository.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404,\n\"message\": \"This user doesn't exist\"\n}");
        }

        Set<Animal> animals = usersRepository.findById(userId).get().getAnimals();

        return ResponseEntity.ok(animals);
    }

    @GetMapping(value = "/api/users/{userId}/animals/{animalId}", produces = {"application/json"})
    private ResponseEntity addAnimalToUser(@PathVariable Long userId, @PathVariable Long animalId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        User user = usersRepository.findById(userId).get();
        Animal animal = animalsRepository.findById(animalId).get();

        if (animalsRepository.findById(animalId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404,\n\"message\": \"This animal doesn't exist\"\n}");
        }

        if (usersRepository.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404, \n\"message\": \"This user doesn't exist\"\n}");
        }

        if (usersRepository.findById(userId).isPresent()
                && !principal.getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\n\"status\": 401, \n\"message\": \"You don't have access to this user's list\"\n}");
        }

        if (usersRepository.findById(userId).get().getAnimals().contains(animalsRepository.findById(animalId).get())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{\n\"status\": 409, \n\"message\": \"This user already have this animal\"\n}");
        }


        userService.addAnimal(user, animal);
        return ResponseEntity
                .ok("{\n\"status\": 200, \n\"message\": \"Animal " + animal.getNickname() + " have been added to "
                        + principal.getUsername() + " successfully\"\n}");
    }

    @DeleteMapping(value = "/api/users/{userId}/animals/{animalId}", produces = {"application/json"})
    private ResponseEntity deleteAnimalFromUser(@PathVariable Long userId, @PathVariable Long animalId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        User user = usersRepository.findById(userId).get();

        if (usersRepository.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404, \n\"message\": \"This user doesn't exist\"\n}");
        }

        if (usersRepository.findById(userId).isPresent()
                && !principal.getUsername().equals(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\n\"status\": 401, \"message\": \"You don't have access to this user's list\"}");
        }


        if (animalsRepository.findById(animalId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404, \"message\": \"This animal doesn't exist\"}");
        }

        Animal animal = animalsRepository.findById(animalId).get();
        userService.deleteAnimal(user, animal);
        return ResponseEntity
                .ok("{\n\"status\": 200, \n\"message\": \"Animal " + animal.getNickname() + " have been deleted from user "
                        + principal.getUsername() + " successfully\"\n}");
    }

    @PostMapping(value = "/register", produces = "application/json")
    private ResponseEntity register(@Valid @RequestBody User user) {

        if (usersRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{\n\"status\": 409, \n\"message\": \"This username already exists!\"\n}");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.addRole(rolesRepository.findByName("USER"));
        usersRepository.save(user);
        return ResponseEntity
                .ok("{\n\"status\": 200, \n\"message\": \"You have been signed up successfully!\"\n}");
    }

    @DeleteMapping(value="/api/users/{userId}", produces = "application/json")
    private ResponseEntity deleteUser(@PathVariable Long userId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = usersRepository.findById(userId).get();

        if(!authentication.getAuthorities().contains("ADMIN")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\n\"status\": 401, \n\"message\": \"You don't have access\"\n}");
        }

        if (usersRepository.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404, \n\"message\": \"This user doesn't exist\"\n}");
        }
        usersRepository.delete(user);
        return ResponseEntity.ok("{\n\"status\": 200, \n\"message\": \"User have been deleted successfully!\"");
    }



}
