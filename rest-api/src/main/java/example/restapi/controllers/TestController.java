package example.restapi.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uni-api")
public class TestController {

    @GetMapping("/Hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello World");
    }

}
