package com.example.demo.profile;

import com.example.demo.user.Users;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DatasetSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "userProfileDatasetID")
    private Long userProfileDatasetID;

    @Column(name = "datasetid")
    private Long datasetId;

    @Column(name = "rw")
    private String rw;

    @Column(name = "col")
    private String col;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "rw_crop")
    private Long rw_crop;


    @Column(name = "graphType")
    private String graphType;

    public DatasetSettings(Long userProfileDatasetID, Long datasetID, String rw, String col, String graphType, Long rowLength) {
        this.userProfileDatasetID = userProfileDatasetID;
        this.datasetId = datasetID;
        this.rw = rw;
        this.col = col;
        this.graphType = graphType;
        this.rw_crop = rowLength;
    }

    public DatasetSettings() {

    }

}
