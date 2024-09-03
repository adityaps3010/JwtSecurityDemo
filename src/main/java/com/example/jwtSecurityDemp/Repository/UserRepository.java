package com.example.jwtSecurityDemp.Repository;

import com.example.jwtSecurityDemp.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<UserModel,Integer> {

    UserModel findByUsername(String username);
}
