package com.example.demo.geoDataDB;

import com.example.demo.login.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping(value = "/geoData")
@CrossOrigin(origins = "http://localhost:4200")
public class GeoDataController {

    @Autowired
    private GeoDataService geoDataService;

    //TODO: Namen mitgeben
    @PostMapping(value = "/uploadFile", produces = "application/geo+json")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        // Andere Codezeilen

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        if (geoDataService.saveNewGeoData(file)) {
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body("{\"message\": \"angekommen\"}");
        } else {
            return ResponseEntity.badRequest()
                    .headers(responseHeaders)
                    .body("{\"message\": \"Failure\"}");
        }
    }

    @GetMapping(value ="/getGeoDatasets")
    public ResponseEntity<List<Object[]>> getGeoDatasets() throws FileNotFoundException {
        return new ResponseEntity<>(geoDataService.getDatasets(), HttpStatus.OK);
    }


}
