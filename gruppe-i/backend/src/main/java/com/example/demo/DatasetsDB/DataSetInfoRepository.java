package com.example.demo.DatasetsDB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataSetInfoRepository extends JpaRepository<DataSetInfo, Long> {

    @Query("SELECT id, name FROM DataSetInfo ")
    List<Object[]> findAllNamesAndIds();

    @Query("SELECT name FROM DataSetInfo")
    List<String> allNames();
}