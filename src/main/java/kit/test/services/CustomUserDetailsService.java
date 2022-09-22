package artplancom.test.services;

import artplancom.test.models.Animal;
import artplancom.test.models.User;
import artplancom.test.repositories.RolesRepository;
import artplancom.test.repositories.UsersRepository;
import artplancom.test.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = usersRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found.");
        }
        return new CustomUserDetails(user.get());
    }

    @Autowired
    private RolesRepository rolesRepository;

    public CustomUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> findAll() {
        return usersRepository.findAll();
    }

    public Optional<User> findById(Long userId) {
        return usersRepository.findById(userId);
    }

    public Optional<User> findByUsername(String username) {
        return usersRepository.findByUsername(username);
    }

    public User save(User user){
        return usersRepository.save(user);
    }

    public void delete(User user) {
        usersRepository.delete(user);
    }

    public User addAnimalToUser(User user, Animal animal) {
        user.getAnimals().add(animal);
        animal.setUser(user);
        return usersRepository.save(user);
    }

    public void deleteAnimalFromUser(User user, Animal animal) {
        user.getAnimals().remove(animal);
        animal.setUser(null);
        usersRepository.save(user);
    }
}
