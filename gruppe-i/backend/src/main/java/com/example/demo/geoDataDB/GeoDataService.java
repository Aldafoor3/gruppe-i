package com.example.demo.geoDataDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeoDataService {

    @Autowired
    private GeoDataRepository geoDataRepository;

    // TODO: Docker
    //private String assetsFolderPath = "/app/geoDataDB/assets/";

    // Für Programmierung
    private String assetsFolderPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/main/java/com/example/demo/geoDataDB/assets/";

    public boolean saveNewGeoData(MultipartFile file){
        String filename = file.getOriginalFilename();
        System.out.println(filename);

        try {
            GeoData newGeoData = new GeoData();
            newGeoData.setFilename(filename);
//          TODO: Hinzufügen namens String

            // Temp name Setter
            String[] nameSetter = filename.split("\\.");
            newGeoData.setName(nameSetter[0]);

            File filePath = new File(assetsFolderPath + newGeoData.getFilename());
            FileCopyUtils.copy(file.getBytes(), filePath);
            System.out.println("Datei erstellt");

            if (!(geoDataRepository.allNames().contains(newGeoData.getName()))) {
                geoDataRepository.save(newGeoData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Object[]> getDatasets() throws FileNotFoundException {
        List<Object[]> entitys = geoDataRepository.allNamesAndIds();
        List<Object[]> resizedEntitys = new ArrayList<>();

        for (Object[] entity : entitys) {
            Object[] resizedEntity = new Object[entity.length + 1];
            System.arraycopy(entity, 0, resizedEntity, 0, entity.length);


            try {

                /*File getFile = new File(assetsFolderPath + geoDataRepository.findById((Long) entity[0]).get().getFilename());
                FileReader filereader = new FileReader(getFile);
                resizedEntity[entity.length] = filereader;
                */
                FileSystemResource geoJsonFileRessource = new FileSystemResource(assetsFolderPath + geoDataRepository.findById((Long) entity[0]).get().getFilename());
                byte[] fileContent = FileCopyUtils.copyToByteArray(geoJsonFileRessource.getInputStream());
                String geoJsonData = new String(fileContent, "UTF-8");
                System.out.println(geoJsonData);
                resizedEntity[entity.length] = geoJsonData;
            } catch (Exception e) {
                e.printStackTrace();
            }

            resizedEntitys.add(resizedEntity);
        }

        return resizedEntitys;
    }

    interface GeodataList {

    }
}
