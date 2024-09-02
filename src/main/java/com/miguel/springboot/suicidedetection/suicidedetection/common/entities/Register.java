package com.miguel.springboot.suicidedetection.suicidedetection.common.entities;
import org.springframework.data.geo.Point;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "registers")
public class Register {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @Column(columnDefinition = "POINT")
    private Point ubicacion;
    private String ipAddress;
    @ManyToOne
    @JoinColumn(name = "type_register_id", nullable = false)
    private TypeRegister typeRegister;
}
