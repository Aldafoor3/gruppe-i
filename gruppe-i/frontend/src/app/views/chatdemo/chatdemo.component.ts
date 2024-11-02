import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {User} from "../../Models/User";
import {RegistrationServiceService} from "../../services/registration-service.service";
import {ChatserviceService} from "../../services/chatservice.service";
import {Chat} from "../../Models/Chat";
import {interval, Subscription} from "rxjs";
import {Message_Instance} from "../../Models/Message_Instance";
import {nearestPointOnLine} from "@turf/turf";
import {Router} from "@angular/router";

@Component({
  selector: 'app-chatdemo',
  templateUrl: './chatdemo.component.html',
  styleUrls: ['./chatdemo.component.scss']
})
export class ChatdemoComponent implements AfterViewInit, OnDestroy, OnInit{
  chatlist: Object[]|any;
  chatMessages: Message_Instance[] = [];
  message: any;


  constructor(private routerOutlet:Router,private registrationService: RegistrationServiceService, private chatService: ChatserviceService) {
    this.registrationService.getUserList().subscribe(userList =>{
      this.userList = userList;
    });
  }

  ngAfterViewInit(): void {

    //Create new chat Dialog
    const newChatDialog = document.getElementById("newChatroomDialog") as HTMLDialogElement;
    const openNewChatDialog = document.getElementById("newChat")
    const closeNewChatDialog = document.getElementById("newChatroomDialogClose")
    const createNewChatDialog = document.getElementById("newChatroomDialogCreate")

    openNewChatDialog?.addEventListener('click' ,() => {newChatDialog.showModal()});
    closeNewChatDialog?.addEventListener('click' ,() => {newChatDialog.close()});
    createNewChatDialog?.addEventListener('click', () =>{this.createChatRoom()})
  }


  getChat(chatElement: bigint) {

  }

  onSubmit() {

  }

  //USER LIST:

  userList:User[] = [];



  editMessageBoolean:boolean = false;
  messageIdToEdit:number = -1;
  public setEditMessageBoolean(messageId:number){
    this.editMessageBoolean = !this.editMessageBoolean;
    this.messageIdToEdit = messageId;
  }

  ngOnInit(){

    if(localStorage.getItem("SID") == null || localStorage.getItem("SID") ==""){
      alert("you are not logged in.")
      this.routerOutlet.navigate(['/geoData']);
    }

    this.registrationService.getUserList().subscribe(userList =>{
      this.userList = userList;
    });
    this.fetchChatList();
    //TODO: Intervall anpassen
    //TODO: Intervall nach verlassen des Chatrooms beenden
    setInterval(()=>{
      if(!this.editMessageBoolean){this.fetchChat();}
    }, 1000)
  }

  ngOnDestroy() {

  }


  chatName: string = "";
  participantsIds: number[] = [];
  createChatRoom(){
    if(this.chatName == "" || this.chatName == null) {
      alert("ChatName is missing");
      return;
    }
    let answer =this.chatService.createChat(this.chatName,this.participantsIds);
    if(answer != undefined) answer.subscribe( response =>{
      console.log("createChatroomBody:"+response.body);
      alert(response.body);
    });
    this.chatName = "";
    this.participantsIds = [];
    this.fetchChatList();

  }

  //ChatRooms des users fetchen
  public fetchChatList(){
    this.chatService.getChatList().subscribe(chatList =>{
      this.chatList = chatList;
    })
  }

  editChatRoomParticipants(id: number) {
    for (let i = 0; i < this.participantsIds.length; i++) {
      if(id == this.participantsIds[i]) {
        this.participantsIds.splice(i, 1);
        console.log(this.participantsIds);
        return;
      }
    }
    this.participantsIds[this.participantsIds.length] = id;
    console.log(this.participantsIds);
  }

  //CHAT LIST:
  chatList: Chat[] = [];
  public openChatRoom(ID:number) {
    this.chatRoomId = ID;
    this.fetchChat();
    //TODO: CHATROOM ÖFFNEN --> Get Chat history, then establish Websocket Connection
  }

  chatRoomId:number = -1;

  //CHAT (nachrichten senden, alter messages empfangen etc.)
  message_to_send:string ="";
  public sendMsgToChatRoom(){
    if(this.chatRoomId == -1) {
      return;
    }
    this.chatService.sendMsg(BigInt(this.chatRoomId), this.message_to_send)?.subscribe(response =>{
      console.log(response);
    });
    this.message_to_send = "";
  }


  //fetch die Messages eines Chatrooms
  public fetchChat(){
    if(this.chatRoomId==-1) {return;}

    this.chatService.getChatRoomMessages(BigInt(this.chatRoomId)).subscribe( response =>{
      if(response==null){
        this.chatMessages = [];
      } else{
        this.chatMessages = response;
      }
    });

  }

  editedMessage!:string;
  public editMessage(messageId:number){
    this.chatService.editMessage(messageId,this.editedMessage).subscribe(response => {
      console.log(response);
    });
    this.editMessageBoolean = false;
    this.editedMessage = "";
  }

  public deleteMessage(messageId:number){
    this.chatService.deleteMSG(messageId).subscribe(response => console.log(response));
  }

  protected readonly localStorage = localStorage;
}
