package kit.repositories;

import kit.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RolesRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);

    @Query(
            value = "SELECT * FROM roles WHERE id NOT IN (SELECT role_id FROM users_roles WHERE user_id = ?)",
            nativeQuery = true)
    Set<Role> getUserNotRoles(Long userId);
}
