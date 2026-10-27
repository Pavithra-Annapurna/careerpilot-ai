package com.careerpilot.controller;
import com.careerpilot.service.JobService; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/v1/jobs") public class JobController {
 private final JobService jobs; public JobController(JobService j){jobs=j;}
 public record CreateJob(String title,String company,String description,String requiredSkills){}
 @GetMapping public List<JobService.JobDto> list(){return jobs.list();}
 @PostMapping public JobService.JobDto create(@RequestBody CreateJob r){return jobs.create(r.title(),r.company(),r.description(),r.requiredSkills());}
}
