package com.example.jwtSecurityDemp.Controller;
import com.example.jwtSecurityDemp.Models.UserDTO;
import com.example.jwtSecurityDemp.Models.UserModel;
import com.example.jwtSecurityDemp.Models.credentials;
import com.example.jwtSecurityDemp.Repository.UserRepository;
import com.example.jwtSecurityDemp.filters.JwtGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import com.example.jwtSecurityDemp.Models.LoginResponseDTO;

import javax.management.relation.Role;
import java.util.HashSet;
import java.util.Set;

@RestController
public class userController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtGenerator jwtGenerator;
    Logger log = LoggerFactory.getLogger(userController.class);


    @PostMapping("/loginuser")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody credentials cred) {
        log.info("inside the login method "+cred);

        Authentication auth = UsernamePasswordAuthenticationToken.unauthenticated(cred.getUsername(),cred.getPassword());
        Authentication authenticationResponse = authenticationManager.authenticate(auth);

        System.out.println(authenticationResponse);

        SecurityContextHolder.getContext().setAuthentication(authenticationResponse);

        String jwt=jwtGenerator.generatorjwt();


        System.out.println(SecurityContextHolder.getContext().getAuthentication());





        return ResponseEntity.status(HttpStatus.OK).header("Authorization",jwt).body(new LoginResponseDTO(HttpStatus.OK.getReasonPhrase(), jwt));
    }

    @PostMapping("/addadmin")
    public ResponseEntity<String> adduser(@RequestBody UserDTO userdata) {
        Set<String> roles= new HashSet<>();
        roles.add("ROLE_ADMIN");
        UserModel u = new UserModel();
        u.setUsername(userdata.getUsername());
        u.setPassword(userdata.getPassword());
        u.setEmail(userdata.getEmail());
        u.setRoles(roles);
        log.info("inside the adduser method "+u);
        userRepository.save(u);
        return ResponseEntity.ok().body("new admin added");
    }

    @PostMapping("/adduser")
    public ResponseEntity<String> addadmin(@RequestBody UserDTO userdata) {
        Set<String> roles= new HashSet<>();
        roles.add("ROLE_USER");
        UserModel u = new UserModel();
        u.setUsername(userdata.getUsername());
        u.setPassword(userdata.getPassword());
        u.setEmail(userdata.getEmail());
        u.setRoles(roles);
        log.info("inside the adduser method "+u);
        userRepository.save(u);
        return ResponseEntity.ok().body("new user added");
    }

}
