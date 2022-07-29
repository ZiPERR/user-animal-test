package artplancom.test.controllers;


import artplancom.test.misc.ExcelFileService;
import artplancom.test.models.Animal;
import artplancom.test.repositories.AnimalsRepository;
import artplancom.test.repositories.UsersRepository;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class MiscController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AnimalsRepository animalsRepository;

    @Autowired
    private ExcelFileService excelFileService;

    @GetMapping(value = "/api/usernameAvailability/{username}/isUsernameAvailable", produces = {"application/json"})
    private ResponseEntity getUsernameAvailability(@PathVariable String username) {

        if (usersRepository.findByUsername(username).isPresent()) {
            return ResponseEntity
                    .ok("{\n\"status\": 200,\n\"message\": \"This username has been already taken\"\n}");
        }
        return ResponseEntity.ok("{\n\"status\": 200,\n\"message\": \"This username is available\"\n}");
    }

    @GetMapping("/api/excel/export")
    private void exportToExcel(HttpServletResponse response) throws IOException {
        List<Animal> animals = animalsRepository.findAll();
        ByteArrayInputStream byteArrayInputStream = excelFileService.export(animals);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=animal_info.xlsx");
        IOUtils.copy(byteArrayInputStream, response.getOutputStream());
    }

}
