package com.careerpilot.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="jobs")
public class Job {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false) private String title;
 @Column(nullable=false) private String company;
 @Column(nullable=false,length=5000) private String description;
 @Column(nullable=false,length=2000) private String requiredSkills;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected Job(){}
 public Job(String t,String c,String d,String s){title=t;company=c;description=d;requiredSkills=s;}
 public Long getId(){return id;} public String getTitle(){return title;} public String getCompany(){return company;}
 public String getDescription(){return description;} public String getRequiredSkills(){return requiredSkills;}
 public Instant getCreatedAt(){return createdAt;}
}
