import { Component, OnInit } from '@angular/core';
import {CSVService} from '../../testService/CSVService';
import {TestCSV} from '../../Models/testCSV';
import { DomSanitizer } from '@angular/platform-browser';

import { HttpClient } from "@angular/common/http";
import {RegistrationServiceService} from "../../../services/registration-service.service"
import {Admin} from "../../../Models/Admin"
import {User} from "../../../Models/User"



@Component({
  selector: 'app-test-window',
  templateUrl: './test-window.component.html',
  styleUrls: ['./test-window.component.css']
})
export class TestWindowComponent implements OnInit{
  csvList: TestCSV[] |any;
  constructor(private sanitizer: DomSanitizer,private http:HttpClient,private csvService: CSVService, private registrationService: RegistrationServiceService) {
  }
  public updateCSVLIst(){
    this.csvService.getCSVlist().subscribe(list => this.csvList = list);

  }

  ngOnInit():void{

    this.updateCSVLIst();
    this.updateAdminList();
    this.updateUserList();

  }


  adminList: Admin[] = [];
  userList: User[] = [];



  public updateAdminList(){
    this.registrationService.getAdminList()
      .subscribe(list => this.adminList = list);
  }

  public updateUserList(){
    this.registrationService.getUserList()
      .subscribe(list =>this.userList=list);

    this.getProfilePics();

  }

  public getProfilePics(){
    for (let i = 0; i < this.userList.length; i++) {
        this.getProfilPictureObservable(this.userList[i].id)
        .subscribe((data:ArrayBuffer) =>{
          this.userList[i].profilePicture = "data:image/jpeg;base64, "+   this.arrayBufferToBase64(data);


        });
    }
  }


  public getProfilPictureObservable(userID:number){
    return this.http.get('http://localhost:8080/registration/users/'+userID.toString()+'/profile-picture', {responseType:"arraybuffer"})
  }

  //copied from "https://gist.github.com/Deliaz/e89e9a014fea1ec47657d1aac3baa83c"
  public arrayBufferToBase64(buffer:ArrayBuffer) {
    let binary = '';
    let bytes = new Uint8Array(buffer);
    let len = bytes.byteLength;
    for (let i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i]);
    }
    return window.btoa(binary);
  }

  f:string ="";
  updatedString:string="";
  formatByteCast(){
     const byteCast = "(byte)";
    for (let i = 0;i<this.f.length;i++){
      if (i<this.f.length-1&& this.f.charAt(i) == "0" && this.f.charAt(i+1) == "x") this.updatedString += byteCast;
      this.updatedString += this.f.charAt(i);
    }
  }


}
