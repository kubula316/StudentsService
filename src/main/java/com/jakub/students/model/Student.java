package com.jakub.students.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.DefaultValue;
import lombok.Getter;
import lombok.Setter;

@Entity
@SequenceGenerator(name = "seqIdGen", initialValue = 20000, allocationSize = 1)
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqIdGen")
    private Long id;

    @NotBlank
    private String firstName;
    @NotBlank
    @Size(min = 3)
    private String lastName;
    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status;
    public enum Status{
        ACTIVE,
        INACTIVE
    }
}
