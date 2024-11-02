import {Component, OnInit} from '@angular/core';
import {ForumServiceService} from "./forum-service.service";
import {Router} from "@angular/router";
import {LoginServiceService} from "../../services/login-service.service";

@Component({
  selector: 'app-forum-view',
  templateUrl: './forum-view.component.html',
  styleUrls: ['./forum-view.component.css']
})
export class ForumViewComponent implements OnInit{
  discussionTopics:any[] = [];

  isUser:boolean = true;

  constructor(private routerOutlet:Router,private forumService:ForumServiceService, private router: Router, private loginService:LoginServiceService) {
  }
  ngOnInit(): void {

    if(localStorage.getItem("SID") == null || localStorage.getItem("SID") ==""){
      alert("you are not logged in.")
      this.routerOutlet.navigate(['/geoData']);
    }

    this.getAllDiscussionTopics();
    if(this.loginService.isLoggedIn())this.loginService.isUser().subscribe(response=>{
      this.isUser = response;
    });
  }

  public deleteDiscussion(topicId:number){
    if(localStorage.getItem('SID') == null || localStorage.getItem('SID') == ""){
      alert("you are not logged in.");
      return;
    }
    this.forumService.deleteTopic(topicId).subscribe(response =>{
      console.log(response);
      //TODO: responsive antwort
    });

    this.getAllDiscussionTopics();
  }


  newTopic: string="";
  newCategory:string="";
  newText:string="";
  public createNewTopic() {
    if (!this.isLoggedIn()) {
      alert("you are not logged in");
      return;
    }

    console.log(this.newTopic)
    console.log(this.newText)
    console.log(this.newCategory)

    this.forumService.createNewTopic(this.newTopic, this.newCategory, this.newText).subscribe(response => {
      alert(response.body);
    });
    this.newTopic="";
    this.newCategory="";
    this.newText="";

    this.getAllDiscussionTopics();

  }

  public async getAllDiscussionTopics(){
    if(!this.isLoggedIn()){
      alert("you are not logged in");
      return;
    }
    await new Promise(f => setTimeout(f, 1000));
    this.forumService.getAllTopics().subscribe(response =>{
      this.discussionTopics = response;
    })
  }

  public isLoggedIn() :boolean{
    return !(localStorage.getItem("SID") == null || localStorage.getItem("SID") == "");
  }


  goToDiscussion(id: bigint) {
    this.router.navigate(["/forum",id]);
  }

}
