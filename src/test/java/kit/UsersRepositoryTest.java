package kit;

import kit.models.Role;
import kit.models.User;
import kit.repositories.RolesRepository;
import kit.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UsersRepositoryTest {

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testCreateUser() {
        User user = new User();

        Role adminRole = rolesRepository.findByName("ADMIN");
        Set<Role> rolesSet = new HashSet<>();
        rolesSet.add(adminRole);
        user.setUsername("admin");
        user.setPassword("$2a$04$tPJqEnJBw8DX1JUez6RJt.warV9gPaP1FJUBPgZUzipP18etF1PGq");
        user.setRoles(rolesSet);
        User savedUser = usersRepository.save(user);
        User existUser = testEntityManager.find(User.class, savedUser.getUserId());
        assertEquals(existUser.getUsername(), savedUser.getUsername());
    }
}