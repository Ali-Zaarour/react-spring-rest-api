package example.restapi.exception;

import example.restapi.exception.config.ExceptionMessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SQLBatchOperationException extends RuntimeException{
    public SQLBatchOperationException() {
        super(ExceptionMessageConstant.SQL_BATCH_OPERATION);
    }
    public SQLBatchOperationException(String msg) {
        super(msg);
    }
}
