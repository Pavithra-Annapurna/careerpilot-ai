package com.careerpilot.service;
import com.careerpilot.entity.AppUser; import com.careerpilot.repository.UserRepository; import org.springframework.security.core.context.SecurityContextHolder; import org.springframework.stereotype.Service; import org.springframework.http.HttpStatus; import org.springframework.web.server.ResponseStatusException;
@Service public class CurrentUserService {
 private final UserRepository users; public CurrentUserService(UserRepository u){users=u;}
 public AppUser require(){Object p=SecurityContextHolder.getContext().getAuthentication().getPrincipal();if(!(p instanceof String email))throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);return users.findByEmail(email).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED));}
}
