import {Injectable, isDevMode} from "@angular/core";
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {DataSetInfo} from "../../../Models/dataSetInfo";
import {LoginServiceService} from "../../../../services/login-service.service";
import {formatDate} from "@angular/common";

@Injectable({
  providedIn: "root"
})
export class DemoDataSetService {
  private baseURI: string = "http://localhost:8080/testData/dataSets";

  constructor(private http: HttpClient, private loginservice:LoginServiceService) {}

  public uploadDataSet(file: File): Promise<any> {
    const formData = new FormData();
    formData.append("file", file, file.name);

    const headers = new HttpHeaders();
    headers.append("Content-Type", "multipart/form-data");
    headers.append("Accept", "application/json");

    return this.http.post(`${this.baseURI}/uploadFile`, formData, {headers: headers}).toPromise();
  }

  public getDatasetList() {
    return this.http.get<DataSetInfo[]>(this.baseURI + "/listDatasets");
  }

  public getDatasetListTest() {
    return this.http.get<DataSetInfo[]>(this.baseURI+"/listDatasetsTest");
}

  public getDataset(fileId: bigint) {
    return this.http.get<Object>(this.baseURI+ `/getDataset/${fileId}`);
  }

  public updateDataset(fileId: string, jsonString: Object) {
      console.log("Send Change");
      if (!this.loginservice.isLoggedIn()){
        alert("You can't do that, your are not logged in!");
        return null;
      }
      return this.http.post<string>(`${this.baseURI}/update/?fileId=${fileId}&sessionId=${localStorage.getItem('SID')!.toString()}`, jsonString);
  }

  public setNewFavourite(fileId:number){
    if (!this.loginservice.isLoggedIn()){
      alert("You can't do that, your are not logged in!");
      return null;
    }

    let formData: FormData = new FormData();
    formData.append('sessionId', localStorage.getItem('SID')!);
    return this.http.post<string>(this.baseURI + "/setAsFovourite/"+fileId, formData);
  }

  isFavoriteDataset(fileId:number){
    let sessionId = localStorage.getItem('SID');
    //if(localStorage.getItem('SID') == null || localStorage.getItem('SID') == "") ; TODO:
    return this.http.get<boolean>("http://localhost:8080/testData/dataSets/isFavourite/"+fileId+"/"+sessionId);
  }

  sendDTPrequest(request: FormData) {
    console.log("sendDTPrequest");
    return this.http.post<string>(this.baseURI + "/sendDTPrequest", request);
  }

}


