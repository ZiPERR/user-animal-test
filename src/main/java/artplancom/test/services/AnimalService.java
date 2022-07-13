package artplancom.test.services;

import artplancom.test.models.Animal;
import artplancom.test.models.User;
import artplancom.test.repositories.AnimalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnimalService {
    @Autowired
    private AnimalsRepository animalsRepository;

    public Animal findById(Long animalId) {
        return animalsRepository.findById(animalId).orElse(null);
    }
}
