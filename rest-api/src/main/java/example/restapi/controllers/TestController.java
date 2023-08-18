package example.restapi.controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uni-api")
@SecurityRequirement(name = "bearerAuth")
@Hidden
public class TestController {

    @GetMapping("/Hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello World");
    }

}
