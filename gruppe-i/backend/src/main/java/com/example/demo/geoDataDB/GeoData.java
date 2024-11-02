package com.example.demo.geoDataDB;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class GeoData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @Setter
    private String name;

    @Column(name = "filename")
    @Setter
    private String filename;
}
