import {Component, OnInit, AfterViewInit, ViewChild, ElementRef} from '@angular/core';
import * as L from 'leaflet';
import { GeoJsonObject } from 'geojson';
import {GeoJSON, LatLng} from "leaflet";
import {GeoDataMapService} from "./geo-data-map.service";
import {GeoDataSet} from "../../Models/GeoDataSet";
import proj4 from "proj4";


@Component({
  selector: 'app-geo-data-map',
  templateUrl: './geo-data-map.component.html',
  styleUrls: ['./geo-data-map.component.scss']
})
export class GeoDataMapComponent implements OnInit, AfterViewInit {
  jsonObject!: GeoJsonObject;
  public objectList: Object[] = []

  geoDataSetList: GeoDataSet[]|any;


  constructor(private geoDataMapService: GeoDataMapService) { }

  ngOnInit(): void {

    this.geoDataMapService.getGeoDatasets().subscribe(
      (response: Object[]) => {
        this.objectList = response;
      },
      (error) => {
        console.log(error);
      }
    );
    this.updateGeoDatasetList();
  }

  ngAfterViewInit(): void {

    // Map inizialisieren
    const map = L.map('map').setView([51.10, 10.27], 6);

    L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    }).addTo(map);


    //Buttons and other EventListener
    //Upload Listener
    const uploadDialog = document.getElementById("uploadDialog") as HTMLDialogElement;
    const openUploadDialog = document.getElementById("openUploadDialog");
    const closeUploadDialog = document.getElementById("closeUploadDialog");

    openUploadDialog?.addEventListener('click', () => {
        uploadDialog.showModal();
    });

    closeUploadDialog?.addEventListener('click', () => {
        uploadDialog.close();
    });


    //Define Dataset Listener
    const defineDatasets  = document.getElementById("defineDatasets") as HTMLDialogElement;
    const openDefineDatasets = document.getElementById("defineDatasetsButton");
    const closeDefineDatasets = document.getElementById("closeDatasetDialog");

    openDefineDatasets?.addEventListener('click', () => {
      defineDatasets.showModal();
      console.log(this.geoDataMapService.getGeoDatasets())
    });

    closeDefineDatasets?.addEventListener('click', () => {
      defineDatasets.close()

      // Zurücksetzen der Karte
      map.eachLayer((layer) => {
          map.removeLayer(layer);
      });

      L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
      }).addTo(map);


      // Hinzufügen der geoDaten

      let listLength: number  = this.geoDataSetList.length;

      for (let i = 0; i < listLength; i++) {

        if (this.geoDataSetList[i].isShown) {
          var geoJsonFile = JSON.parse(this.geoDataSetList[i].file.toString());


          // Tauschen der UTM Koordinaten in Dezimalgrad
          geoJsonFile.features.forEach((feature: any) => {
            const utmCoords = feature.geometry.coordinates;

            // UTM-Zone und Hemisphäre
            const utmZone = 32; //UTM-Zone 32
            const utmHemisphere = 'N'; //Nördliche Hemisphäre (N)

            // Konvertieren der UTM-Koordinaten in geografische Koordinaten
            const wgs84Coords = proj4('+proj=utm +zone=' + utmZone + utmHemisphere + ' +ellps=WGS84 +datum=WGS84 +units=m +no_defs', 'EPSG:4326', utmCoords);
            feature.geometry.coordinates = wgs84Coords;
          });

          // Farbcode aus der Liste
          const listColor = this.geoDataSetList[i].color;

          L.geoJSON(geoJsonFile, {
            pointToLayer: function (feature, latlng) {
              // Definieren der Markeroptionen
              const markerOptions = {
                fillColor: listColor,
                color: "#000000",
                weight: 1,
                radius: 6,
                fillOpacity: 0.8
              };

              // Erstellen des Markers
              return L.circleMarker(latlng, markerOptions);
            },
            onEachFeature: function (feature, layer) {
              // Erstellen des Popups und hinzufügen der Eigenschaften
              const popupContent = "<p>" + JSON.stringify(feature.properties) + "</p>";
              layer.bindPopup(popupContent);
            }
          }).addTo(map);
        }

      }

    });
  }

  // Ist der File Upload
  @ViewChild('fileInput') fileInput: ElementRef | undefined;
  onSubmit() {
    if (this.fileInput && this.fileInput.nativeElement.files.length > 0) {
      console.log("onSubmit wird ausgeführt")
      const file = this.fileInput.nativeElement.files[0];
      this.geoDataMapService.uploadGeoData(file, "FILLER");
      this.updateGeoDatasetList();
    }
  }


  public updateGeoDatasetList() {
    this.geoDataMapService.getGeoDatasets().subscribe((list: Object[]) => {
      this.geoDataSetList = list.map((obj: any) => {
        const geoDataSet: GeoDataSet = {
          id: obj[0],
          name: obj[1],
          file: obj[2],
          isShown: false,
          color: 'FFFFFF'
        };
        return geoDataSet;
      });
    });

    /*
    console.log("Updated DatasetList");
    console.log(this.geoDataSetList[0].id)
    console.log(this.geoDataSetList[0].name)
    console.log(JSON.stringify(this.geoDataSetList[0].file))
    console.log(this.geoDataSetList[0].file)
    console.log(this.geoDataSetList[0].isShown)
    console.log(this.geoDataSetList[0].color)
    */
  }
}
