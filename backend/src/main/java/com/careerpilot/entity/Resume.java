package com.careerpilot.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="resumes")
public class Resume {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private AppUser user;
 @Column(nullable=false) private String fileName;
 @Lob @Column(nullable=false,columnDefinition="LONGTEXT") private String extractedText;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected Resume(){}
 public Resume(AppUser u,String f,String t){user=u;fileName=f;extractedText=t;}
 public Long getId(){return id;} public AppUser getUser(){return user;} public String getFileName(){return fileName;}
 public String getExtractedText(){return extractedText;} public Instant getCreatedAt(){return createdAt;}
}
