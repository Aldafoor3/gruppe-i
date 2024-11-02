package com.example.demo.DatasetsDB;

import com.example.demo.login.LoginService;
import com.example.demo.login.Sessions;
import com.example.demo.profile.ProfileService;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/testData/dataSets")
@CrossOrigin(origins = "http://localhost:4200")
public class DataSetUpDownloadController {

    @Autowired
    private DataSetUpDownloadService dataSetService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ProfileService profileService;

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (dataSetService.setNewDataset(file)) {
            return ResponseEntity.ok("Datei erfolgreich hochgeladen");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fehler beim Hochladen der Datei");
        }
    }

    @GetMapping("/listDatasets")
    private ResponseEntity<List<Object[]>> getDatasetList() {
        return new ResponseEntity<>(dataSetService.getDatasetList(), HttpStatus.OK);
    }

    @GetMapping("/dataSets/listDatasetsTest")
    private ResponseEntity<List<DataSetInfo>> getDatasetListTest() {
        return new ResponseEntity<>(dataSetService.getDataSetListTest(), HttpStatus.OK);
    }


    @GetMapping("/getDataset/{fileID}")
    public ResponseEntity<Object> getDataset(@PathVariable("fileID") int fileId) {
        try {
            String jsonData = dataSetService.getDataset(fileId);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(value = "/update/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateDataset(@RequestParam("fileId") String fileId,
                                                @RequestBody Object jsonString,
                                                @RequestParam("sessionId") String sessionId) {


        System.out.println("################");
        System.out.println(sessionId);
        System.out.println("################");
        System.out.println("Received fileId: " + fileId);
        System.out.println("Received jsonString: " + jsonString);
        System.out.println("################");
        if(!loginService.hasAdminRights(sessionId)){
            //Falls die Anfrage nicht von einem Admin stammt, wird die Anfrage verworfen
            return new ResponseEntity<String>("You are not authorized to Edit Files.",HttpStatus.UNAUTHORIZED);
        }
        // Rückmeldung an den Client
        if (dataSetService.updateDataset(Long.parseLong(fileId), jsonString)) {
            return ResponseEntity.ok("Dataset updated successfully");
        } else {
            return new ResponseEntity<String>("Dataset update is unsuccessful",HttpStatus.I_AM_A_TEAPOT) ;
        }


    }

    @PostMapping(value = "/setAsFovourite/{fileId}")
    public ResponseEntity<String> setAsFovourite(@PathVariable("fileId") Long fileId, @RequestParam String sessionId){
        Sessions session = this.loginService.getSession(sessionId);

        if(session == null) return new ResponseEntity<String>("Session does not exist, nowhere to set as favourite", HttpStatus.EXPECTATION_FAILED);
        if(session.isUser() == false) return new ResponseEntity<String>("You should be a User, not an Admin",  HttpStatus.EXPECTATION_FAILED);

        if (this.dataSetService.setAsNewFavourite(session,fileId)) return new ResponseEntity<String>("new Favourite Set.",HttpStatus.OK);

        return new ResponseEntity<String>("couldnt sett new favourite",HttpStatus.I_AM_A_TEAPOT);
    }


    @GetMapping(value = "/isFavourite/{fileId}/{sessionId}")
    public ResponseEntity<Boolean> isFavourite(@PathVariable Long fileId, @PathVariable String sessionId){
        Sessions session = this.loginService.getSession(sessionId);

        if(session == null) return new ResponseEntity<Boolean>(false, HttpStatus.EXPECTATION_FAILED);
        System.out.println("SESSION EXISTS");
        if(!session.isUser()) return new ResponseEntity<Boolean>(false,  HttpStatus.EXPECTATION_FAILED);
        System.out.println("SESSION AND IS USER ");
        if(this.loginService.getUserBySessionID(sessionId).getFavouriteDatasetId()!=fileId){
            System.out.println("NOT FAVOURITE DATASET");
            return new ResponseEntity<Boolean>(false,  HttpStatus.OK);
            }
        System.out.println("#############FAVOURITE DATASET###############");
        return new ResponseEntity<Boolean>(true,  HttpStatus.OK);
    }

    @PostMapping(value = "/sendDTPrequest")
    public ResponseEntity<String> newDTPrequest(@RequestParam Long datasetID,
                                                @RequestParam String sessionID,
                                                @RequestParam Long datasetIDinProfile,
                                                @RequestParam String rowname,
                                                @RequestParam String colname,
                                                @RequestParam String graphType,
                                                @RequestParam Long rowLength) {
        System.out.println(String.format("Start DTP process,\n rowname: %s\n columname: %s", rowname, colname));

        profileService.addDTP(datasetID, sessionID, datasetIDinProfile, rowname, colname, graphType, rowLength);

        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

}
