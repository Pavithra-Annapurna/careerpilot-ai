package com.careerpilot.config;
import com.careerpilot.entity.Job;import com.careerpilot.repository.JobRepository;import org.springframework.boot.CommandLineRunner;import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Configuration;
@Configuration public class DataSeeder {
 @Bean CommandLineRunner seed(JobRepository jobs){return args->{if(jobs.count()==0){
 jobs.save(new Job("Java Full Stack Developer","DemoTech","Build REST APIs, secure services, relational schemas and React interfaces. Write tests and collaborate with developers.","Java, Spring Boot, REST APIs, SQL, React, Git, JUnit"));
 jobs.save(new Job("Backend Software Engineer","Northstar Labs","Develop Java services, optimize SQL, implement authentication, monitoring and automated tests.","Java, Spring Boot, MySQL, REST APIs, Spring Security, Docker, JUnit"));
 jobs.save(new Job("Junior AI Application Developer","BrightPath AI","Integrate language-model APIs, validate structured responses, build document processing and evaluate results.","Java, REST APIs, Python, Machine Learning, SQL, Git, Prompt Engineering"));
 jobs.save(new Job("Frontend Developer","PixelWorks","Build responsive React interfaces, connect APIs, improve accessibility and test components.","React, JavaScript, HTML, CSS, REST APIs, Git, TypeScript"));
 }};}
}
