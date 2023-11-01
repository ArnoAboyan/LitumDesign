package com.litumdesign.LitumDesign.repository;

import com.litumdesign.LitumDesign.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

//    Optional<UserEntity> findByUserNickName(String nickName);
//
//
//    boolean existsByUserNickName(String nickName);

    UserEntity findDistinctByEmail (String email);
}
