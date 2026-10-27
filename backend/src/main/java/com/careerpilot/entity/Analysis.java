package com.careerpilot.entity;
import jakarta.persistence.*;
import java.time.Instant;
@Entity @Table(name="analyses")
public class Analysis {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private AppUser user;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private Resume resume;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private Job job;
 @Column(nullable=false) private int matchScore;
 @Column(nullable=false,length=4000) private String matchedSkills;
 @Column(nullable=false,length=4000) private String missingSkills;
 @Column(nullable=false,length=8000) private String recommendations;
 @Column(nullable=false) private Instant createdAt=Instant.now();
 protected Analysis(){}
 public Analysis(AppUser u,Resume r,Job j,int s,String m,String x,String rec){user=u;resume=r;job=j;matchScore=s;matchedSkills=m;missingSkills=x;recommendations=rec;}
 public Long getId(){return id;} public AppUser getUser(){return user;} public Resume getResume(){return resume;} public Job getJob(){return job;}
 public int getMatchScore(){return matchScore;} public String getMatchedSkills(){return matchedSkills;} public String getMissingSkills(){return missingSkills;}
 public String getRecommendations(){return recommendations;} public Instant getCreatedAt(){return createdAt;}
}
