import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {formatDate} from "@angular/common";

@Injectable({
  providedIn: 'root'
})

//INFO: Komponenten, welche den Service aufrufen, sollten vorher abchecken, ob der User eingeloggt ist.

export class ForumServiceService {

  baseURI:string = "http://localhost:8080/topics";

  constructor(private http:HttpClient) { }

  public deleteTopic(topicId:number){
    let formData = new FormData();
    formData.append("sessionId", localStorage.getItem("SID")!);
    return this.http.post<string>(this.baseURI+"/"+topicId+"/deleteTopic", formData);
  }

  public deleteComment(commentId:number){
    let formData = new FormData();
    formData.append("sessionId", localStorage.getItem("SID")!);
    return this.http.post<string>(this.baseURI+"/"+commentId+"/deleteComment", formData);
  }

  public createNewTopic(topic:string, category:string, text:string){

    let formData:FormData = new FormData();
    formData.append("topic",topic);
    formData.append("category", category);
    formData.append("text",text);
    formData.append("sessionId",localStorage.getItem("SID")!);

    return this.http.post<HttpResponse<string>>(this.baseURI+"/create", formData);
  }

  public createNewComment(topicId:bigint, content:string){

    let formData = new FormData();
    formData.append("content", content);
    formData.append("sessionId", localStorage.getItem("SID")!);

    return this.http.post<HttpResponse<string>>(this.baseURI+"/"+topicId+"/createComment", formData);

  }


  public getAllTopics(){
    return this.http.get<any>(this.baseURI+"/getAllTopics");
  }

  public getDiscussion(discussionId:bigint):Observable<any>{
    return this.http.get<any>(this.baseURI+"/"+discussionId);
  }

  public likeDiscussion(discussionId: bigint) {
    let formData = new FormData();
    // formData.append('sessionId', localStorage.getItem('SID')!);
    // return this.http.post<HttpResponse<boolean>>(
    //   `${this.baseURI}/${discussionId}/like`,
    //   formData
    // );
    formData.append('sessionId', localStorage.getItem('SID')!);
    return this.http.post<any>(this.baseURI + '/' + discussionId + '/like', formData);
  }

  public favoriteDiscussion(discussionId: bigint) {
    let formData = new FormData();
    formData.append('sessionId', localStorage.getItem('SID')!);
    return this.http.post<HttpResponse<any>>(
      `${this.baseURI}/${discussionId}/favorite`,
      formData
    );
  }




}
