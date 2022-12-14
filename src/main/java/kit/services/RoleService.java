package kit.services;

import kit.models.Role;
import kit.models.User;
import kit.repositories.RolesRepository;
import kit.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RoleService {

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UsersRepository usersRepository;

    public List<Role> findAll() {
        return rolesRepository.findAll();
    }

    public Role findByName(String name) {
        return rolesRepository.findByName(name);
    }

    public Optional<Role> findById(Long id) {
        return rolesRepository.findById(id);
    }

    public void delete(Long id) {
        rolesRepository.deleteById(id);
    }

    public void save(Role role) {
        rolesRepository.save(role);
    }

    public void assignUserRole(Long userId, Long roleId) {
        User user = usersRepository.findById(userId).orElse(null);
        Role role = rolesRepository.findById(roleId).orElse(null);

        Set<Role> userRoles = user.getRoles();
        userRoles.add(role);
        user.setRoles(userRoles);

        usersRepository.save(user);
    }

    public void unassignUserRole(Long userId, Long roleId) {
        User user = usersRepository.findById(userId).orElse(null);
        Set<Role> userRoles = user.getRoles();

        userRoles.removeIf(x -> x.getId() == roleId);
        usersRepository.save(user);
    }

    public Set<Role> getUserRoles(User user) {
        return user.getRoles();
    }

    public Set<Role> getUserNotRoles(User user) {
        return rolesRepository.getUserNotRoles(user.getUserId());
    }
}
