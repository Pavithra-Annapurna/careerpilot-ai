package com.careerpilot.service;
import com.careerpilot.entity.Job; import com.careerpilot.repository.JobRepository; import org.springframework.stereotype.Service; import org.springframework.http.HttpStatus; import org.springframework.web.server.ResponseStatusException; import java.util.*;
@Service public class JobService {
 private final JobRepository repo; public JobService(JobRepository r){repo=r;}
 public record JobDto(Long id,String title,String company,String description,String requiredSkills){}
 public List<JobDto> list(){return repo.findAll().stream().map(this::dto).toList();}
 public Job get(Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Job not found"));}
 public JobDto create(String title,String company,String desc,String skills){if(List.of(title,company,desc,skills).stream().anyMatch(x->x==null||x.isBlank()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"All fields are required");return dto(repo.save(new Job(title.trim(),company.trim(),desc.trim(),skills.trim())));}
 private JobDto dto(Job j){return new JobDto(j.getId(),j.getTitle(),j.getCompany(),j.getDescription(),j.getRequiredSkills());}
}
