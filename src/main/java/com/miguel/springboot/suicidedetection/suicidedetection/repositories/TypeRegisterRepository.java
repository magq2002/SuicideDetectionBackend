package com.miguel.springboot.suicidedetection.suicidedetection.repositories;

import com.miguel.springboot.suicidedetection.suicidedetection.common.entities.TypeRegister;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TypeRegisterRepository extends JpaRepository<TypeRegister, Long> {

}
