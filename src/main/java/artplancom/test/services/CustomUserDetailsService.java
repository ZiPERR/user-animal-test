package artplancom.test.services;

import artplancom.test.models.User;
import artplancom.test.repositories.UsersRepository;
import artplancom.test.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = usersRepository.findByUsername(username);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found.");
        }
        return new CustomUserDetails(user.get());
    }
}
