package artplancom.test.controllers;

import artplancom.test.models.Animal;
import artplancom.test.repositories.AnimalsRepository;
import artplancom.test.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class AnimalController {

    @Autowired
    private AnimalsRepository animalsRepository;

    @Autowired
    private AnimalService animalService;

    @GetMapping(value = "/api/animals")
    private ResponseEntity getAllAnimals() {
        return ResponseEntity.ok().body(animalsRepository.findAll());
    }

    @GetMapping(value = "/api/animals/{animalId}", produces = {"application/json"})
    private ResponseEntity getOneAnimal(@PathVariable Long animalId) {
        if (animalsRepository.findById(animalId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\n\"status\": 404,\n\"message\": \"This animal doesn't exist\"\n}");
        }
        return ResponseEntity
                .ok(animalsRepository.findById(animalId));
    }

    @PostMapping(value = "/api/animals", produces = "application/json")
    private ResponseEntity createAnimal(@Valid @RequestBody Animal animal) {

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

    @DeleteMapping(value = "/api/animals/{animalId}", produces = "application/json")
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

    @PutMapping(value = "/api/animals/{animalId}", produces = "application/json")
    private ResponseEntity editAnimal(@PathVariable Long animalId, @RequestBody Animal newAnimal) {

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
