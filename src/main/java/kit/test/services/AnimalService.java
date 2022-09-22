package artplancom.test.services;

import artplancom.test.models.Animal;
import artplancom.test.repositories.AnimalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {
    @Autowired
    private AnimalsRepository animalsRepository;

    public Optional<Animal> findById(Long animalId) {
        return animalsRepository.findById(animalId);
    }

    public List<Animal> findAll() {
        return animalsRepository.findAll();
    }

    public Animal saveAnimal(Animal animal){
        return animalsRepository.save(animal);
    }

    public void deleteAnimal(Animal animal){
        animalsRepository.delete(animal);
    }

    public Optional<Animal> findByNickname(String nickname) { return animalsRepository.findByNickname(nickname); }

    public List<Animal> listAll() {
        return animalsRepository.findAll(Sort.by("nickname").ascending());
    }
}
