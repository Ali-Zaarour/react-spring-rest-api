package example.restapi.exception;

import example.restapi.exception.config.ExceptionMessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserDisabledException extends DisabledException {
    public UserDisabledException() {
        super(ExceptionMessageConstant.USER_APP_DISABLED_CONTACT_MANAGER);
    }
    public UserDisabledException(String msg) {
        super(msg);
    }
}
