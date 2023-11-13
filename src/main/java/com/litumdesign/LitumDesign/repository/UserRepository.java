package com.litumdesign.LitumDesign.repository;

import com.litumdesign.LitumDesign.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, String> {

//    Optional<UserEntity> findByUserNickName(String nickName);
//
//
//    boolean existsByUserNickName(String nickName);

    UserEntity findDistinctByEmail (String email);

      Boolean existsByName (String name);

      UserEntity findByName (String name);
}
