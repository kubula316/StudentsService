package com.jakub.students.controller;


import com.jakub.students.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        System.out.println("DEBUG");
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authHeader) {
        System.out.println("DEBUG");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);
        boolean isValid = authenticationService.isTokenValid(token);
        return ResponseEntity.ok(isValid);
    }
}
