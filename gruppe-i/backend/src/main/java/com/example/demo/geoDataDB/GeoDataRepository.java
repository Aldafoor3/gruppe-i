package com.example.demo.geoDataDB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeoDataRepository extends JpaRepository<GeoData, Long> {

    @Query("SELECT id, name FROM GeoData")
    List<Object[]> allNamesAndIds();

    @Query("SELECT name FROM GeoData")
    List<String> allNames();

}