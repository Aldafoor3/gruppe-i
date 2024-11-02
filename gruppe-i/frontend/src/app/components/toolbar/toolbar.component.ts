import { Component, OnInit} from '@angular/core';
import {LoginServiceService} from "../../services/login-service.service";
import {Router} from "@angular/router";
import {ProfilWindowService} from "../../views/profil-window/profil-window.service";

@Component({
  selector: 'app-toolbar',
  templateUrl: './toolbar.component.html',
  styleUrls: ['./toolbar.component.scss']
})
export class ToolbarComponent implements OnInit{

  loggedIn:boolean = false;
  isUser:boolean = true;
  constructor(private loginservice:LoginServiceService, private router:Router, private profileService:ProfilWindowService) {
  }
  ngOnInit():void{
    this.updateToolbarSetting()
  }

  updateToolbarSetting(){
    this.loggedIn=this.loginservice.isLoggedIn();
    if(this.loggedIn)this.loginservice.isUser().subscribe(response=>{
      this.isUser = response;
    });
  }

  public logout(){
    this.loginservice.logout().subscribe(response =>{
        alert(response.body);
      },
      error=>{
        //TODO: ALLE Responses werden als "error" erkannt...
      });
    this.loggedIn=this.loginservice.isLoggedIn();
    this.router.navigate(['/geoData'],{replaceUrl:true})
  }

  public login(){
    this.router.navigate(['/login'],{replaceUrl:true})
    this.loggedIn=this.loginservice.isLoggedIn(); //hier evt unnötig
  }

  getProfileBySessionId(){
    this.profileService.getAccIdBySessionId().subscribe(response =>{
      console.log(response);
      this.routeToProfile(response!);
    });
  }

  routeToProfile(accId:number){
    this.router.navigateByUrl('/', { skipLocationChange: false }).then(()=>
      this.router.navigate(['/profil/'+accId],{replaceUrl:true}));
  }



}
