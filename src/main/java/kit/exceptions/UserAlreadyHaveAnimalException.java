package kit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User Already Have This Animal")
public class UserAlreadyHaveAnimalException extends Exception{
}
