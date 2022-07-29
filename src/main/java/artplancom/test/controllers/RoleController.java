package artplancom.test.controllers;

import artplancom.test.models.Role;
import artplancom.test.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RoleController {

    @Autowired
    private RolesRepository rolesRepository;

    @PostMapping("/api/roles")
    private ResponseEntity addRoleToDatabase(@RequestBody Role role){
        if(rolesRepository.findByName(role.getName()) != null){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("{\n\"status\": 409, \n\"message\": \"This role already exists\"\n}");
        }
        rolesRepository.save(role);
        return ResponseEntity.ok("{\n\"status\": 200, \n\"message\": \"Role have been added successfully\" \n");
    }
}
