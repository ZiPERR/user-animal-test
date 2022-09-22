package kit.controllers;

import kit.exceptions.AnimalAlreadyExistsException;
import kit.exceptions.AnimalNotFoundException;
import kit.models.Animal;
import kit.services.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AnimalController {

    @Autowired
    private AnimalService animalService;

    @GetMapping(value = "/api/animals")
    private ResponseEntity<List<Animal>> getAllAnimals() {
        return ResponseEntity.status(HttpStatus.OK).body(animalService.findAll());
    }

    @GetMapping(value = "/api/animals/{animalId}", produces = {"application/json"})
    private ResponseEntity<Animal> getOneAnimal(@PathVariable Long animalId) throws AnimalNotFoundException {
        if (animalService.findById(animalId).isEmpty()) {
            throw new AnimalNotFoundException();
        }
        return ResponseEntity.ok(animalService.findById(animalId).get());
    }

    @PostMapping(value = "/api/animals", produces = "application/json")
    private ResponseEntity<Animal> createAnimal(@Valid @RequestBody Animal animal) throws AnimalAlreadyExistsException {

        if (animalService.findByNickname(animal.getNickname()).isPresent()) {
            throw new AnimalAlreadyExistsException();
        }

        animalService.saveAnimal(animal);

        return ResponseEntity.status(HttpStatus.CREATED).body(animal);
    }

    @DeleteMapping(value = "/api/animals/{animalId}", produces = "application/json")
    private ResponseEntity<Animal> deleteAnimal(@PathVariable Long animalId) throws AnimalNotFoundException {

        if (animalService.findById(animalId).isEmpty()) {
            throw new AnimalNotFoundException();
        }

        Animal animal = animalService.findById(animalId).get();

        animalService.deleteAnimal(animal);

        return ResponseEntity.status(HttpStatus.OK).body(animal);
    }

    @PutMapping(value = "/api/animals/{animalId}", produces = "application/json")
    private ResponseEntity<Animal> editAnimal(@PathVariable Long animalId, @RequestBody Animal newAnimal) throws AnimalNotFoundException {

        if (animalService.findById(animalId).isEmpty()) {
            throw new AnimalNotFoundException();
        }

        Animal oldAnimal = animalService.findById(animalId).get();
        oldAnimal.setSpecies(newAnimal.getSpecies());
        oldAnimal.setBirthDate(newAnimal.getBirthDate());
        oldAnimal.setSex(newAnimal.getSex());
        oldAnimal.setNickname(newAnimal.getNickname());
        animalService.saveAnimal(oldAnimal);
        return ResponseEntity.status(HttpStatus.OK).body(oldAnimal);
    }
}
