package com.jakub.students.security;

import com.jakub.students.controller.AuthenticationRequest;
import com.jakub.students.controller.AuthenticationResponse;
import com.jakub.students.controller.RegisterRequest;
import com.jakub.students.model.EnrolledCourse;
import com.jakub.students.model.Role;
import com.jakub.students.model.Student;
import com.jakub.students.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = Student.builder()
                .email(request.getEmail())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .status(Student.Status.ACTIVE)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enrolledCourses(new ArrayList<EnrolledCourse>())
                .profileImageUrl("https://coursesapp.blob.core.windows.net/student-profile-image-container/BlankProfile.png")
                .build();
        studentRepository.save(user);
        var jwtToken = jwtService.generateToken(user, user.getId());
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = studentRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user, user.getId());
        return AuthenticationResponse.builder()
                .token(jwtToken).build();
    }

    public boolean isTokenValid(String token) {
        return !jwtService.isTokenExpired(token);
    }
}
