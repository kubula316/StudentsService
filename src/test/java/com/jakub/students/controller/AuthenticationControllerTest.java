package com.jakub.students.controller;

import com.jakub.students.controller.AuthenticationController;
import com.jakub.students.controller.AuthenticationRequest;
import com.jakub.students.controller.AuthenticationResponse;
import com.jakub.students.controller.RefreshRequest;
import com.jakub.students.controller.RegisterRequest;
import com.jakub.students.security.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("securepassword")
                .build();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("jwt-token")
                .refreshToken("refresh-token")
                .build();

        when(authenticationService.register(registerRequest)).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = authenticationController.register(registerRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("jwt-token", result.getBody().getToken());
        assertEquals("refresh-token", result.getBody().getRefreshToken());
        verify(authenticationService, times(1)).register(registerRequest);
    }


    @Test
    void testAuthenticate() {
        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .email("john.doe@example.com")
                .password("securepassword")
                .build();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("jwt-token")
                .refreshToken("refresh-token")
                .build();

        when(authenticationService.authenticate(authRequest)).thenReturn(response);

        ResponseEntity<AuthenticationResponse> result = authenticationController.authenticate(authRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("jwt-token", result.getBody().getToken());
        assertEquals("refresh-token", result.getBody().getRefreshToken());
        verify(authenticationService, times(1)).authenticate(authRequest);
    }

    @Test
    void testRefreshToken() {
        RefreshRequest refreshRequest = RefreshRequest.builder()
                .refreshToken("refresh-token")
                .build();

        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("new-jwt-token")
                .refreshToken("new-refresh-token")
                .build();

        when(authenticationService.refreshToken("refresh-token")).thenReturn(ResponseEntity.ok(response));

        ResponseEntity<AuthenticationResponse> result = authenticationController.refreshToken(refreshRequest);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("new-jwt-token", result.getBody().getToken());
        assertEquals("new-refresh-token", result.getBody().getRefreshToken());
        verify(authenticationService, times(1)).refreshToken("refresh-token");
    }

    @Test
    void testValidateToken_ValidToken() {
        String token = "Bearer valid-token";

        when(authenticationService.isTokenValid("valid-token")).thenReturn(true);

        ResponseEntity<Boolean> result = authenticationController.validateToken(token);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.getBody());
        verify(authenticationService, times(1)).isTokenValid("valid-token");
    }

    @Test
    void testValidateToken_InvalidHeader() {
        String invalidHeader = "InvalidHeader";

        ResponseEntity<Boolean> result = authenticationController.validateToken(invalidHeader);

        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertFalse(result.getBody());
        verify(authenticationService, never()).isTokenValid(any());
    }

    @Test
    void testValidateToken_MissingBearerPrefix() {
        String tokenWithoutBearer = "invalid-token";

        ResponseEntity<Boolean> result = authenticationController.validateToken(tokenWithoutBearer);

        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertFalse(result.getBody());
        verify(authenticationService, never()).isTokenValid(any());
    }
}
