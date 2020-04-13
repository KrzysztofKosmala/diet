package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.Role;
import com.kosmala.springbootapp.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Optional<Role> findByName(RoleName roleName);
}
