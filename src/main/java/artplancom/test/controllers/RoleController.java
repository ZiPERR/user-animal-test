package artplancom.test.controllers;

import artplancom.test.exceptions.RoleAlreadyExistsException;
import artplancom.test.models.Role;
import artplancom.test.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/api/roles")
    private ResponseEntity<Role> addRoleToDatabase(@RequestBody Role role) throws
            RoleAlreadyExistsException {
        if (roleService.findByName(role.getName()) != null) {
            throw new RoleAlreadyExistsException();
        }
        roleService.save(role);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }
}
