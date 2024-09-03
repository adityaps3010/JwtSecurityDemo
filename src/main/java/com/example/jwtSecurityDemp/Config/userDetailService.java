package com.example.jwtSecurityDemp.Config;

import com.example.jwtSecurityDemp.Models.UserModel;
import com.example.jwtSecurityDemp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class userDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel users=  userRepository.findByUsername(username);
        System.out.println(username);


        if(users==null){
            System.out.println("  user not found");
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> authorities = users.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());;


        return new User(users.getUsername(), users.getPassword(),authorities);
    }
}
