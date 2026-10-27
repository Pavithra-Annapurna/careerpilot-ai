package com.careerpilot.controller;
import com.careerpilot.service.AnalysisService; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/v1/analyses") public class AnalysisController {
 private final AnalysisService service;public AnalysisController(AnalysisService s){service=s;}
 @PostMapping public AnalysisService.Dto create(@RequestBody AnalysisService.Request r){return service.create(r);}
 @GetMapping public List<AnalysisService.Dto> list(){return service.list();}
 @GetMapping("/{id}") public AnalysisService.Dto get(@PathVariable Long id){return service.get(id);}
}
