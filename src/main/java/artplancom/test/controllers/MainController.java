package artplancom.test.controllers;

import artplancom.test.models.Animal;
import artplancom.test.models.User;
import artplancom.test.repositories.AnimalsRepository;
import artplancom.test.repositories.UsersRepository;
import artplancom.test.security.CustomUserDetails;
import artplancom.test.services.AnimalService;
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
public class MainController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AnimalsRepository animalsRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private AnimalService animalService;

    @GetMapping(value = "/api/usernameAvailability/{username}/isUsernameAvailable", produces = {"application/json"})
    private ResponseEntity getUsernameAvailability(@PathVariable String username) {

        if (usersRepository.findByUsername(username).isPresent()) {
            return ResponseEntity
                    .ok("{\n\"status\": 200,\n\"message\": \"This username has been already taken\"\n}");
        }
        return ResponseEntity.ok()
                .body("{\n\"status\": 200,\n\"message\": \"This username is available\"\n}");
    }

    // GET ALL USERS
    @GetMapping(value = "/api/user/getAllUsers", produces = {"application/json"})
    private ResponseEntity getAllUsers() {
        return ResponseEntity.ok().body(usersRepository.findAll());
    }


    // GET ALL ANIMALS
    @GetMapping(value = "/api/animal/getAllAnimals")
    private ResponseEntity getAllAnimals() {
        return ResponseEntity.ok().body(animalsRepository.findAll());
    }

    // GET ONE USER BY USER ID
    @GetMapping(value = "/api/user/{userId}/get", produces = {"application/json"})
    private ResponseEntity getOneUser(@PathVariable Long userId) {
        if (usersRepository.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404,\n\"message\": \"This user doesn't exist\"\n}");
        }
        return ResponseEntity
                .ok(usersRepository.findById(userId));
    }

    // GET ONE ANIMAL BY ANIMAL ID
    @GetMapping(value = "/api/animal/{animalId}/get", produces = {"application/json"})
    private ResponseEntity getOneAnimal(@PathVariable Long animalId) {
        if (animalsRepository.findById(animalId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404,\n\"message\": \"This animal doesn't exist\"\n}");
        }
        return ResponseEntity
                .ok(animalsRepository.findById(animalId));
    }

    // GET USER'S ANIMALS
    @GetMapping(value = "/api/user/{userId}/animal/getUserAnimals", produces = {"application/json"})
    private ResponseEntity getUserAnimal(@PathVariable Long userId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object currentUser = authentication.getName();

        if (usersRepository.findById(userId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404,\n\"message\": \"This user doesn't exist\"\n}");
        }

        Set<Animal> animals = usersRepository.findById(userId).get().getAnimals();

        return ResponseEntity.ok(animals);
    }

    // ADD ANIMAL TO USER'S LIST
    @GetMapping(value = "/api/user/{userId}/animal/{animalId}/add", produces = {"application/json"})
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

    // CREATE USER
    @PostMapping(value = "/api/registration_process", produces = "application/json")
    private ResponseEntity registrationProcess(@Valid @RequestBody User user) {

        if (usersRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("{\n\"status\": 409, \n\"message\": \"This username already exists!\"\n}");
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        usersRepository.save(user);
        return ResponseEntity
                .ok("{\n\"status\": 200, \n\"message\": \"You have been signed up successfully!\"\n}");
    }

    // CREATE GLOBAL ANIMAL
    @PostMapping(value = "/api/animal/create_process", produces = "application/json")
    private ResponseEntity createAnimalProcess(@Valid @RequestBody Animal animal) {

        if (animalsRepository.findByNickname(animal.getNickname()).isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("{\n\"status\": 409, \n\"message\": \"This animal has been created already\"\n}");
        }
        animalsRepository.save(animal);

        return ResponseEntity
                .ok("{\n\"status\": 200,\n\"message\": \"Animal "
                        + animal.getNickname() + " have been created successfully\"\n}");
    }

    // DELETE ANIMAL FROM GLOBAL LIST
    @DeleteMapping(value = "/api/animal/{animalId}/delete", produces = "application/json")
    private ResponseEntity deleteAnimal(@PathVariable Long animalId) {

        if (animalsRepository.findById(animalId).isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404, \n\"message\": \"This animal doesn't exist\"\n}");
        }

        Animal animal = animalsRepository.findById(animalId).get();

        animalsRepository.delete(animal);

        return ResponseEntity
                .ok("{\n\"status\": 200, \n\"message\": \"Animal " + animal.getNickname() + " have been deleted successfully\"\n}");
    }

    // DELETE ANIMAL FROM USER'S LIST
    @DeleteMapping(value = "/api/user/{userId}/animal/{animalId}/delete", produces = {"application/json"})
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

    // EDIT GLOBAL ANIMAL
    @PostMapping(value = "/api/animal/{animalId}/edit", produces = "application/json")
    private ResponseEntity editGlobalAnimal(@PathVariable Long animalId, @RequestBody Animal newAnimal) {

        if (animalsRepository.findById(animalId).isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404, \n\"message\": \"This animal doesn't exist\"\n}");
        }

        Animal oldAnimal = animalService.findById(animalId);
        oldAnimal.setSpecies(newAnimal.getSpecies());
        oldAnimal.setBirthDate(newAnimal.getBirthDate());
        oldAnimal.setSex(newAnimal.getSex());
        oldAnimal.setNickname(newAnimal.getNickname());
        animalsRepository.save(oldAnimal);
        return ResponseEntity.ok("{\n\"status\": 200, \n\"message\": \""
                + newAnimal.getNickname() + "'s information have been edited successfully\"\n}");
    }
}
