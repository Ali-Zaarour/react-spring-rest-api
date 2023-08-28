package example.restapi.exception.config;

import example.restapi.exception.SQLBatchOperationException;
import example.restapi.exception.UserDisabledException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = UserDisabledException.class)
    public ResponseEntity<?> handleUserDisabledException(UserDisabledException exception){
        log.error(exception.getMessage(),exception);
        var errorStruct = ErrorStruct.withDefaultLocalDateTime(HttpStatus.FORBIDDEN,exception.getMessage()).build();
        return new ResponseEntity<>(errorStruct,errorStruct.getHttpStatus());
    }

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityException( DataIntegrityViolationException exception){
        log.error(exception.getMessage(),exception);
        var errorStruct = ErrorStruct.withDefaultLocalDateTime(HttpStatus.CONFLICT,exception.getMessage()).build();
        return new ResponseEntity<>(errorStruct,errorStruct.getHttpStatus());
    }

    @ExceptionHandler(value = SQLBatchOperationException.class)
    public ResponseEntity<?> handleSQLBatchOperationException( SQLBatchOperationException exception){
        log.error(exception.getMessage(),exception);
        var errorStruct = ErrorStruct.withDefaultLocalDateTime(HttpStatus.CONFLICT,exception.getMessage()).build();
        return new ResponseEntity<>(errorStruct,errorStruct.getHttpStatus());
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException( UsernameNotFoundException exception){
        log.error(exception.getMessage(),exception);
        var errorStruct = ErrorStruct.withDefaultLocalDateTime(HttpStatus.NOT_FOUND,exception.getMessage()).build();
        return new ResponseEntity<>(errorStruct,errorStruct.getHttpStatus());
    }



}
