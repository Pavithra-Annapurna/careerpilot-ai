package com.careerpilot.controller;
import com.careerpilot.entity.AppUser; import com.careerpilot.repository.UserRepository; import com.careerpilot.security.JwtService;
import jakarta.validation.Valid; import jakarta.validation.constraints.*; import org.springframework.http.HttpStatus; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.web.bind.annotation.*; import org.springframework.web.server.ResponseStatusException;
@RestController @RequestMapping("/api/v1/auth") public class AuthController {
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthController(UserRepository u,PasswordEncoder e,JwtService j){users=u;encoder=e;jwt=j;}
 public record Register(@NotBlank @Size(max=100) String name,@Email @NotBlank String email,@NotBlank @Size(min=8,max=72) String password){}
 public record Login(@Email @NotBlank String email,@NotBlank String password){}
 public record AuthResponse(String token,String name,String email){}
 @PostMapping("/register") public AuthResponse register(@Valid @RequestBody Register r){String email=r.email().trim().toLowerCase();if(users.existsByEmail(email))throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");AppUser u=users.save(new AppUser(r.name().trim(),email,encoder.encode(r.password())));return new AuthResponse(jwt.generate(email),u.getName(),email);}
 @PostMapping("/login") public AuthResponse login(@Valid @RequestBody Login r){AppUser u=users.findByEmail(r.email().trim().toLowerCase()).filter(x->encoder.matches(r.password(),x.getPasswordHash())).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid email or password"));return new AuthResponse(jwt.generate(u.getEmail()),u.getName(),u.getEmail());}
}
