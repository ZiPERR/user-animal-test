package kit.repositories;

import kit.models.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnimalsRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findByNickname(String nickname);

}
