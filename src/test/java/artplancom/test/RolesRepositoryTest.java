package artplancom.test;

import artplancom.test.models.Role;
import artplancom.test.repositories.RolesRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RolesRepositoryTest {

    @Autowired
    RolesRepository rolesRepository;

    @Test
    public void testCreateRoles(){
        Role user = new Role("USER");
        Role admin = new Role("ADMIN");
        Role moderator = new Role("MODERATOR");
        List<Role> listRoles = rolesRepository.findAll();
        rolesRepository.saveAll(List.of(user, admin, moderator));
        assertThat(listRoles.size()).isEqualTo(3);
    }
}
