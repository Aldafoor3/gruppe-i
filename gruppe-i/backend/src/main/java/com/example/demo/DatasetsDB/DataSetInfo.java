package com.example.demo.DatasetsDB;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

@Entity
@Getter
public class DataSetInfo {

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

    @Column(name = "format")
    @Setter
    @Enumerated(EnumType.STRING)
    private fileType format;

}

enum fileType{
    XML, CSV
}