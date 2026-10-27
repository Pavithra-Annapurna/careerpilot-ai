package com.careerpilot.controller;
import com.careerpilot.service.ResumeService; import org.springframework.http.MediaType; import org.springframework.web.bind.annotation.*; import org.springframework.web.multipart.MultipartFile; import java.util.List;
@RestController @RequestMapping("/api/v1/resumes") public class ResumeController {
 private final ResumeService service; public ResumeController(ResumeService s){service=s;}
 @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE) public ResumeService.ResumeDto upload(@RequestPart("file") MultipartFile file){return service.upload(file);}
 @GetMapping public List<ResumeService.ResumeDto> list(){return service.list();}
}
