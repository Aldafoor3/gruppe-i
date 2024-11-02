import {AfterViewInit, Component, OnInit} from '@angular/core';
import {FriendListService} from "./friend-list.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-friend-list',
  templateUrl: './friend-list.component.html',
  styleUrls: ['./friend-list.component.scss']
})
export class FriendListComponent implements OnInit, AfterViewInit{

  openFriendRequests: any[] = [];
  friendList: any[] = [];
  userList: any[] = [];

  friendListOpen: boolean = false;

  windowNumber:number = 1;

  //SCSS
  activeDropdown: string = "null";


  constructor(private friendListService:FriendListService, private router:Router) {
    if(!this.isLoggedIn()) {
      alert("you are not logged in.");
      return;
    }
    friendListService.getLists(localStorage.getItem("SID")!).subscribe((data: any) => {
      this.openFriendRequests = data.friendRequestList;
      this.friendList = data.friendList;
      this.userList = data.userList;
      console.log(this.userList);
      })
  }



  public changeWindow(id:number){
    this.windowNumber = id;
  }

  ngOnInit() {

  }

  public navigateToProfil(id:number){
    this.router.navigateByUrl('/', { skipLocationChange: false }).then(()=>
      this.router.navigate(['/profil/'+id],{replaceUrl:true}));
  }

  ngAfterViewInit() {

    const smallFriendlistdialog = document.getElementById("openFriendlistDialog") as HTMLDialogElement
    const FriendlistDialog = document.getElementById("closeFriendlistDialog") as HTMLDialogElement
    const friendlistDialogCloseButton = document.getElementById("friendlistDialogCloseButton");

    smallFriendlistdialog?.addEventListener('click', () => {
      smallFriendlistdialog.close();
      this.friendListOpen = !this.friendListOpen;
      FriendlistDialog.showModal();
    })

    friendlistDialogCloseButton?.addEventListener('click', () => {
      console.log("close")
      FriendlistDialog.close();
      this.friendListOpen = !this.friendListOpen;
      smallFriendlistdialog.showModal();
    })
  }

  sendFriendRequest(friendRequestUserId:number){
    if(!this.isLoggedIn()) {
      alert("you are not logged in.");
      return;
    }
    this.friendListService.sendFriendRequest(localStorage.getItem("SID")!,friendRequestUserId).subscribe( httpResponse=>{
      alert(httpResponse.body);
    });
    this.upDateAllLists();
  }
  public acceptFriendRequest(friendRequestUserId:number){
    if(!this.isLoggedIn()) {
      alert("you are not logged in.");
      return;
    }
    this.friendListService.acceptFriendRequest(localStorage.getItem("SID")!,friendRequestUserId).subscribe( httpResponse=>{
      alert(httpResponse.body);
    });
  }

  public declineFriendRequest(friendRequestUserId:number){
    if(!this.isLoggedIn()) {
      alert("you are not logged in.");
      return;
    }
    this.friendListService.declineFriendRequest(localStorage.getItem("SID")!,friendRequestUserId).subscribe( httpResponse=>{
      alert(httpResponse.body);
    });
  }

  upDateAllLists(){
    if(!this.isLoggedIn()) {
      alert("you are not logged in.");
      return;
    }
    this.friendListService.getLists(localStorage.getItem("SID")!).subscribe((data: any) => {
      this.openFriendRequests = data.friendRequestList;
      this.friendList = data.friendList;
      this.userList = data.userList;
      console.log(this.userList);
    })
  }

  isLoggedIn(){
    return !(localStorage.getItem("SID") == null || localStorage.getItem("SID") == "");

  }
    //TODO: Methode, welche uberpfüft, ob man angemeldet ist. Falls nicht, route zur Anmeldung

  toggleDropdown(identifier: String) {

}

  protected readonly console = console;
}
