package com.miguel.springboot.suicidedetection.suicidedetection.common.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.geo.Point;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "types_registers")
public class TypeRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
}
