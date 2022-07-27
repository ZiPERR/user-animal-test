package artplancom.test.services;

import artplancom.test.models.Animal;
import artplancom.test.repositories.AnimalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimalService {
    @Autowired
    private AnimalsRepository animalsRepository;

    public Animal findById(Long animalId) {
        return animalsRepository.findById(animalId).orElse(null);
    }

    public List<Animal> listAll() {
        return animalsRepository.findAll(Sort.by("nickname").ascending());
    }
}
