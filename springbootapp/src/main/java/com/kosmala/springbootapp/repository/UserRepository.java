package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{


    Optional<User> findByUsernameOrEmail(String usernameOrEmail, String usernameOrEmail1);
}
