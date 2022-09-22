package kit.test;

import artplancom.test.models.Role;
import artplancom.test.repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class Application {

	@Autowired
	private static RolesRepository rolesRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
