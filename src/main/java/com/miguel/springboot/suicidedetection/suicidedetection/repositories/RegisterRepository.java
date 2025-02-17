package com.miguel.springboot.suicidedetection.suicidedetection.repositories;

import com.miguel.springboot.suicidedetection.suicidedetection.common.entities.Register;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RegisterRepository extends JpaRepository<Register, Long> {

}
