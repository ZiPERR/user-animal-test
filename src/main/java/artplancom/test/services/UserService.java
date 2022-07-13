package artplancom.test.services;

import artplancom.test.models.Animal;
import artplancom.test.models.User;
import artplancom.test.repositories.UsersRepository;
import artplancom.test.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {


    @Autowired
    private final UsersRepository usersRepository;


    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }


    public User findUserByUsername(String username) {
        return usersRepository.findByUsername(username).get();
    }

    public boolean createUser(User user) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        usersRepository.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (usersRepository.findById(userId).isPresent()) {
            usersRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public void addAnimal(User user, Animal animal) {
        user.getAnimals().add(animal);
        animal.setUser(user);
        usersRepository.save(user);
    }

    public void deleteAnimal(User user, Animal animal) {
        user.getAnimals().remove(animal);
        animal.setUser(null);
        usersRepository.save(user);
    }


    public List<User> findAll() {
        return usersRepository.findAll();
    }

    public User findById(Long userId) {
        return usersRepository.findById(userId).orElse(null);
    }

}
