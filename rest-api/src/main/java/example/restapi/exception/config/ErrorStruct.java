package example.restapi.exception.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ErrorStruct {

    @JsonProperty("errorCode")
    private int code;
    @JsonProperty("httpStatus")
    private HttpStatus  httpStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @JsonProperty("localDateTime")
    private LocalDateTime localDateTime;
    @JsonProperty("errorMessage")
    private String message;

    public ErrorStruct(HttpStatus httpStatus, String error){
        this.httpStatus = httpStatus;
        this.message = error;
        this.localDateTime = LocalDateTime.now();
        this.code = httpStatus.value();
    }

    public static ErrorStructBuilder withDefaultLocalDateTime(HttpStatus httpStatus, String key){
        return ErrorStruct.builder()
                .httpStatus(httpStatus)
                .localDateTime(LocalDateTime.now())
                .message(key)
                .code(httpStatus.value());
    }
}
