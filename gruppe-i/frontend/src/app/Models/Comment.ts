import {CroppedUser} from "./CroppedUser";

export class Comment{
  id:number;
  content:string;
  commenter:CroppedUser;
  datum: Date|undefined;

  constructor(id:number, content:string, commenter:CroppedUser) {
    this.id = id;
    this.content = content;
    this.commenter = commenter;
  }
}
