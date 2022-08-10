package artplancom.test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Animal Already Exists")
public class AnimalAlreadyExistsException extends Exception{
}
