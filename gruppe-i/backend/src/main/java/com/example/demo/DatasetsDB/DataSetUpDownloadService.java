package com.example.demo.DatasetsDB;

import com.example.demo.login.Sessions;
import com.example.demo.user.UserRepository;
import com.example.demo.user.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opencsv.CSVReader;
import jakarta.servlet.ServletContext;
import jdk.jfr.StackTrace;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.*;
import org.w3c.dom.*;

import javax.xml.parsers.*;

import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DataSetUpDownloadService {

    @Autowired
    private DataSetInfoRepository dataInfoRepository;

    private ServletContext sevCont;



    public boolean setNewDataset(MultipartFile file) {

        String filename = file.getOriginalFilename();

        try {
            // Get the absolute path of the assets folder
            // Für Docker TODO: DOCKER
            //String assetsFolderPath = "/app/assets/";
            String assetsFolderPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/main/java/com/example/demo/DatasetsDB/assets/";

            // Create a Path object for the asset folder with the file name and extension
            Path assetFilePath = Paths.get(assetsFolderPath, filename);


            // Create a new DataSetInfo object and save it to the database
            DataSetInfo dataSetInfo = new DataSetInfo();

            String[] nameAndFormat = filename.split("\\.");
            dataSetInfo.setName(nameAndFormat[0]);
            dataSetInfo.setFilename(nameAndFormat[0]+ ".json");

            if (nameAndFormat[1].toUpperCase().equals("CSV")) {
                CSVReader reader = new CSVReader(new BufferedReader(new InputStreamReader(file.getInputStream())));
                List<String[]> rows = reader.readAll();
                reader.close();

                int arrColumns = rows.get(0).length;

                // Initialize data array
                String[][] data = new String[rows.size()][arrColumns];

                // Parse values into the data array
                for (int i = 0; i < rows.size(); i++) {
                    String[] cp = new String[rows.size()];
                    cp = rows.get(i);
                    data[i] = cp;
                }
/*
                System.out.println("####################");
                for (int i = 0; i < data.length; i++) {
                    for (int j = 0; j <data[i].length; j++) {
                        System.out.print(data[i][j] + ", ");
                    }
                    System.out.println(" !Length: " + data.length);
                }
                System.out.println("####################");
*/
                if (!string2DArrayToJson(data, assetsFolderPath + dataSetInfo.getFilename())) {
                    return false;
                }

            } else if (nameAndFormat[1].toUpperCase().equals("XML")){

                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file.getInputStream());
                Element root = doc.getDocumentElement();
                NodeList nodes = root.getChildNodes();


                // Zähle die Anzahl der gefüllten Zellen in jeder Zeile
                int[] columnCounts = new int[nodes.getLength()];
                for (int i = 1; i < nodes.getLength(); ++i) {
                    int cellCounter = 0;

                    if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        for (int j = 1; j < nodes.item(i).getChildNodes().getLength(); j++) {
                            if (nodes.item(i).getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE) {
                                cellCounter++;
                            }
                        }
                    }

                    columnCounts[i] = cellCounter;
                }

                // Ermittle die maximale Anzahl von gefüllten Zellen in einer Zeile
                int maxColumnCount = 0;
                for (int count : columnCounts) {
                    if (count > maxColumnCount) {
                        maxColumnCount = count;
                    }
                }

                // Erstelle ein neues Array mit der richtigen Größe und kopiere die Daten
                String[][] newData = new String[nodes.getLength()+1][maxColumnCount];
                int newDataIndex = 1; // Index für das neue Array newData

                int cellCounter = 0;
                for(int i = 1; i < nodes.item(1).getChildNodes().getLength(); i++) {

                    if (nodes.item(1).getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
                        newData[0][cellCounter] = nodes.item(1).getChildNodes().item(i).getNodeName();
                        cellCounter++;
                    }
                }

                for (int i = 0; i < nodes.getLength(); ++i) {
                    cellCounter = 0;

                    if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        for (int j = 1; j < nodes.item(i).getChildNodes().getLength(); j++) {
                            if (nodes.item(i).getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE) {
                                newData[newDataIndex][cellCounter] = nodes.item(i).getChildNodes().item(j).getTextContent();
                                cellCounter++;
                            }
                        }

                        if (cellCounter > 0) {
                            newDataIndex++;
                        }
                    }
                }

                // Erstelle ein neues Array mit der richtigen Größe, um Nullzeilen zu entfernen
                String[][] filteredData = new String[newDataIndex][maxColumnCount];

                // Kopiere Daten aus newData in filteredData
                for (int i = 0; i < newDataIndex; i++) {
                    for (int j = 0; j < maxColumnCount; j++) {
                        filteredData[i][j] = newData[i][j];
                    }
                }


                for (int i = 0; i < filteredData.length; i++) {
                    for (int j = 0; j < filteredData[i].length; j++) {
                        filteredData[i][j] = filteredData[i][j].replaceAll("\\r|\\n", "");
                    }
                }

                // Verwende das Array filteredData für weitere Verarbeitung oder Ausgabe



                for(int i = 0; i < filteredData.length; i++) {
                    System.out.print("Index: " + i + "| ");
                    for(int j = 0; j < filteredData[i].length; j++) {
                        System.out.print(filteredData[i][j] + "; ");
                    }
                    System.out.println();
                }

                if (!string2DArrayToJson(filteredData, assetsFolderPath + dataSetInfo.getFilename())) {
                    return false;
                }


            } else {
                return false;
            }

            //TEMPORARY LIST LIMITER
            if (!(dataInfoRepository.allNames().contains(dataSetInfo.getName()))) {
                dataInfoRepository.save(dataSetInfo);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




    public List<Object[]> getDatasetList() {
        return dataInfoRepository.findAllNamesAndIds();
    }

    //testing
    public List <DataSetInfo> getDataSetListTest(){
        return dataInfoRepository.findAll();
    }




    public String getDataset(long Id) {
        try {
            DataSetInfo dataSetInfo = dataInfoRepository.getReferenceById(Id);

            String fileName = dataSetInfo.getFilename();

            //TODO: DOCKER
            String assetsFolderPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/main/java/com/example/demo/DatasetsDB/assets/";
            //String assetsFolderPath = "/app/assets/";
            String filePath = assetsFolderPath + fileName;

            FileSystemResource jsonFileResource = new FileSystemResource(filePath);
            byte[] fileContent = FileCopyUtils.copyToByteArray(jsonFileResource.getInputStream());

            String jsonData = new String(fileContent, "UTF-8"); // Assuming UTF-8 encoding

            //Kontrolle ob file gefunden wird
            System.out.println("########################\nID: " + Id + "\nFilename: " + fileName + "\nFilepath: " + filePath + "\n########################");
            System.out.println(jsonData);
            System.out.println("-#-#-#-#-#- GET DATASET END -#-#-#-#-#-");

            return jsonData;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }






    public boolean updateDataset(Long id, Object jsonObject) {
        //TODO: Replace updated Dataset
        try {
            //TODO: DOCKER
            String assetsFolderPath = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/main/java/com/example/demo/DatasetsDB/assets/";
            //String assetsFolderPath = "/app/assets/";

            DataSetInfo overwrite = dataInfoRepository.getReferenceById(id);

            String[] rows = jsonObject.toString().split("},");
            String[][] table = new String[rows.length][];

            for (int i = 0; i < rows.length; i++) {
                rows[i] = rows[i].replaceAll("[\\[\\]{}\\s]", "");
                table[i] = rows[i].toString().split(",");

            }



            BufferedWriter writer = new BufferedWriter(new FileWriter(assetsFolderPath + overwrite.getFilename()));
            System.out.println("Datei erstellt");

            writer.write("[");
            writer.newLine();
            for (int i = 0; i < table.length; i++) {
                writer.write("  {");
                writer.newLine();
                for (int j = 0; j < table[0].length; j++) {
                    String[] value = table[i][j].split("=");
                    writer.write("      \"" + value[0] + "\": " + "\"" + value[1] + "\"");
                    if (!(j == table[0].length - 1)) {
                        writer.write(",");
                    }
                    writer.newLine();
                }
                writer.write("  }");
                if (!(i == table.length - 1)) {
                    writer.write(",");
                }
                writer.newLine();
            }
            writer.write("]");
            writer.close();
            System.out.println("Writer Closed");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
  }



    public void downloadDataset() {
        //TODO: Search and send back dataset
    }


    UserRepository userRepository;

    public boolean setAsNewFavourite(Sessions session, Long fileId){
        Optional<Users> user = userRepository.findById(session.getAccID());
        if(user.isPresent()){
            user.get().setFavouriteDatasetId(fileId);
            userRepository.save(user.get());
            System.out.println("New favourite File set on User:" +user.get().getEmail());
            return true;
        }
        return false;
    }

    public boolean string2DArrayToJson(String[][] data, String pathAndName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathAndName));
            System.out.println("Datei erstellt");

            writer.write("[");
            writer.newLine();
            for (int i = 1; i < data.length; i++) {
                writer.write("  {");
                writer.newLine();
                for (int j = 0; j < data[0].length; j++) {
                    writer.write("      \"" + data[0][j] + "\": " + "\"" + data[i][j] + "\"");
                    if (!(j == data[0].length-1)) {
                        writer.write(",");
                    }
                    writer.newLine();
                }
                writer.write("  }");
                if (!(i == data.length-1)) {
                    writer.write(",");
                }
                writer.newLine();
            }
            writer.write("]");
            writer.close();
            System.out.println("Writer Closed");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}