package com.ra.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @Column(unique = true)
    private String userName;
    @Column(unique = true)
    private String password;
    @Column(columnDefinition = "Boolean default true")
    private Boolean status = true;
    private Boolean gender;
    private String phone;
    private int age;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Orders> orders;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
private Set<Role> roles;
}
