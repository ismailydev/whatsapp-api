package com.example.whatsapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String phone;

    @JsonIgnore
    private String code;

    @Size(max = 139)
    private String about;

    private String firstName;
    private String lastName;

    @OneToOne
    private Media profilePic;

    // @OneToMany
    // private List<Conversation> conversations;

}
