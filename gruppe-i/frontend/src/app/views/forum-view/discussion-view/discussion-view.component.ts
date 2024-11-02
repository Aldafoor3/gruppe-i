import {Component, OnInit} from '@angular/core';
import {CroppedUser} from "../../../Models/CroppedUser";
import {Comment} from "../../../Models/Comment";
import {ActivatedRoute} from "@angular/router";
import {ForumServiceService} from "../forum-service.service";
import {LoginServiceService} from "../../../services/login-service.service";

@Component({
  selector: 'app-discussion-view',
  templateUrl: './discussion-view.component.html',
  styleUrls: ['./discussion-view.component.css']
})
export class DiscussionViewComponent  implements OnInit{
  id:bigint;
  title:string = "";
  category:string = "";
  text:string = "";
  creator:CroppedUser|undefined = undefined;
  datum:Date|undefined;
  commentList:Comment[] = [];
  isUser:boolean = true;
  likes: number = 0;

  constructor(private route: ActivatedRoute, private forumService:ForumServiceService, private loginService:LoginServiceService) {
    this.id = BigInt(this.route.snapshot.paramMap.get('id')!!);
  }

  ngOnInit(): void {
    if (this.loginService.isLoggedIn()) {
      this.loginService.isUser().subscribe(response => {
        this.isUser = response;
      });
    }
    this.updateDiscussion(); // Call updateDiscussion() here
  }

  public deleteDiscussion(commentId:number){
    if(localStorage.getItem('SID') == null || localStorage.getItem('SID') == ""){
      alert("you are not logged in.");
      return;
    }
    this.forumService.deleteComment(commentId).subscribe((response) => {
      if (response === 'Comment deleted.') {
        alert('Comment deleted.');
        this.updateDiscussion();
      } else {
        alert('Failed to delete comment.');
      }
    });

    this.updateDiscussion();
  }


  commentContent:string = "";
  public createNewComment() {
    if (this.commentContent == "") {
      alert("textfield is empty.")
      return;
    }

    this.forumService.createNewComment(this.id, this.commentContent).subscribe(response => {
      alert(response.body);
      console.log(response.body);
      this.commentContent = "";
    })
    this.updateDiscussion();
  }

  public async updateDiscussion(){
    await new Promise(f => setTimeout(f, 1000));
    this.forumService.getDiscussion(this.id).subscribe(response =>{
      console.log("----------DATA----------")
      console.log(response)
      console.log("------------------------")
      this.title = response.croppedTopic.title;
      this.category = response.croppedTopic.category;
      this.creator = response.croppedTopic.creator;
      this.text = response.croppedTopic.text;
      this.datum = response.croppedTopic.datum;
      this.commentList = response.commentList;
      this.likes = response.croppedTopic.likes;
      console.log(this.commentList.length);
      this.sortComments();
    });
  }

  public sortComments(){
    let sortedCommentList:Comment[] = this.commentList;
    sortedCommentList.sort((a,b) =>a.id-b.id)
    this.commentList = sortedCommentList;
  }

  public likeDiscussion() {
    if (localStorage.getItem('SID') == null || localStorage.getItem('SID') == '') {
      alert('You are not logged in.');
      return;
    }
    this.forumService.likeDiscussion(this.id).subscribe((response) => {
      if (response != undefined) {
        this.likes = response;
      }
    });
  }

  public favoriteDiscussion() {
    if (localStorage.getItem('SID') == null || localStorage.getItem('SID') == '') {
      alert('You are not logged in.');
      return;
    }
    this.forumService.favoriteDiscussion(this.id).subscribe((response) => {
      // TODO: Handle response
    });
  }


    protected readonly undefined = undefined;
}
