package com.careerpilot.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="users", uniqueConstraints=@UniqueConstraint(columnNames="email"))
public class AppUser {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=100) private String name;
 @Column(nullable=false,unique=true,length=190) private String email;
 @Column(nullable=false) private String passwordHash;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected AppUser(){}
 public AppUser(String n,String e,String p){name=n;email=e.toLowerCase();passwordHash=p;}
 public Long getId(){return id;} public String getName(){return name;} public String getEmail(){return email;}
 public String getPasswordHash(){return passwordHash;} public Instant getCreatedAt(){return createdAt;}
}
