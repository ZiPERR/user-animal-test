package artplancom.test.controllers;


import artplancom.test.exceptions.UserAlreadyExistsException;
import artplancom.test.misc.ExcelFileService;
import artplancom.test.models.Animal;
import artplancom.test.models.User;
import artplancom.test.services.AnimalService;
import artplancom.test.services.CustomUserDetailsService;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class MiscController {

    @Autowired
    private CustomUserDetailsService userService;
    @Autowired
    private AnimalService animalService;

    @Autowired
    private ExcelFileService excelFileService;

    @GetMapping(value = "/api/usernameAvailability/{username}/isUsernameAvailable", produces = {"application/json"})
    private ResponseEntity<User> getUsernameAvailability(@PathVariable String username) throws
            UserAlreadyExistsException {

        User user = userService.findByUsername(username).get();

        if (userService.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/api/excel/export")
    @ResponseStatus(HttpStatus.OK)
    private void exportToExcel(HttpServletResponse response) throws
            IOException {
        List<Animal> animals = animalService.findAll();
        ByteArrayInputStream byteArrayInputStream = excelFileService.export(animals);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=animal_info.xlsx");
        IOUtils.copy(byteArrayInputStream, response.getOutputStream());
    }

}
