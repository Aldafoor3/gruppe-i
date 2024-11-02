import { Component } from '@angular/core';
import {LoginServiceService} from "../../services/login-service.service";
import {Router} from '@angular/router';
@Component({
  selector: 'app-login-window',
  templateUrl: './login-window.component.html',
  styleUrls: ['./login-window.component.scss']
})
export class LoginWindowComponent {

  isUser:boolean = true;

  email!:string;
  password!:string;
  twoFactorCode:number;

  validateLogin: boolean = false;

  toggleTwoFactorWindow:boolean;


  emailSave!:string;
  passwordSave!:string;
  isUserSave!:boolean;

  constructor(private loginService:LoginServiceService, private router: Router) {
    this.email = "";
    this.password = "";
    this.twoFactorCode = 0;
    this.toggleTwoFactorWindow = false;
  }

  toggleLogin(){
    if(!this.checkInputData()) return;
    this.login()
  }

  toggleTwoFactor(){
    console.log('twofactor entered:'+this.twoFactorCode);

    if(this.isUserSave) {
      this.loginService.twoFactorCheckUser(this.emailSave, this.passwordSave, this.twoFactorCode).subscribe(response => {
        console.log(response.body);
        console.log(response.body.sessionId);
        const sessionID = response.body.sessionId;
        localStorage.setItem('SID', sessionID); //speicherung der SessionID
        console.log('sessionID:' + localStorage.getItem('SID'));
        if (localStorage.getItem('SID')!=null&&localStorage.getItem('SID') != "") {
          this.router.navigate(['/geoData'], {replaceUrl: true});
        } else {
          alert("Entered Code: "+this.twoFactorCode+" is false.");
        }
      });
    } else {
      this.loginService.twoFactorCheckAdmin(this.emailSave, this.passwordSave, this.twoFactorCode).subscribe(response => {
        console.log(response.body);
        console.log(response.body.sessionId);
        const sessionID = response.body.sessionId;
        localStorage.setItem('SID', sessionID); //speicherung der SessionID
        console.log('sessionID:' + localStorage.getItem('SID'));
        if (localStorage.getItem('SID') != ""){
          this.router.navigate(['/geoData'], {replaceUrl: true})
        }  else {
          alert("Entered Code: "+this.twoFactorCode+" is false.");
        }
      });
    }
    //TODO: Benutzen der SessionID
  }

  login(){
    if (this.isUser) {
      this.loginService.loginUser(this.email,this.password).subscribe((answer) =>
      {
        switch (answer.status.toString()) {
          case '202': {
            console.log("Login Valid: login-window.component.ts");
            this.emailSave = this.email; //Sicherung der Eingaben, sodasss
            this.passwordSave = this.password; // die 2-Faktor-Authentifizierung konsistent funktioniert
            this.isUserSave = this.isUser;
            this.toggleTwoFactorWindow = true;
            break;
          }
          case '204': {
            alert("Login Invalid.")
            this.toggleTwoFactorWindow = false
            console.log("Login Invalid: login-window.component.ts");
            return;
          }
          default: {
            alert("something unexpected happened");
            this.toggleTwoFactorWindow = false
            console.log("HTTP Status Code not Defined");
            break
          }
        }
        console.log(answer.type);
        console.log(answer.url);
        console.log(answer.headers);
        console.log(answer.status);
        console.log(answer.statusText);
      });

    } else {
      this.loginService.loginAdmin(this.email,this.password).subscribe((answer) =>
      {
        switch (answer.status.toString()) {
          case '202': {
            console.log("Login Valid");
            this.emailSave = this.email; //Sicherung der Eingaben, sodasss
            this.passwordSave = this.password; // die 2-Faktor-Authentifizierung konsistent funktioniert
            this.isUserSave = this.isUser;
            this.toggleTwoFactorWindow = true;
            break;
          }
          case '204': {
            alert("Login Invalid.")
            this.toggleTwoFactorWindow = false
            console.log("Login Invalid");
            return;
          }
          default: {
            alert("something unexpected happened");
            this.toggleTwoFactorWindow = false
            console.log("HTTP Status Code not Defined");
            break
          }
        }
        console.log(answer.type);
        console.log(answer.url);
        console.log(answer.headers);
        console.log(answer.status);
        console.log(answer.statusText);
      });

    }
  }

  setAdminLogin(){
    this.isUser=false;
  }
  setUserLogin(){
    this.isUser = true;
  }

  checkInputData():boolean{
    let bool: boolean = true;
    if( !(this.email.indexOf("@") >= 0) || !(this.email.indexOf(".") >= 0) ){
      alert("email nicht Korrekt angegeben.");
      bool = false;
    }
    if(!bool) this.validateLogin = false;
    return bool;
  }

}
