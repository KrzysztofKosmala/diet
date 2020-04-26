package com.kosmala.springbootapp.repository;

import com.kosmala.springbootapp.entity.DetailedUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailedUserInfoRepository extends JpaRepository<DetailedUserInfo, Long>
{

}
