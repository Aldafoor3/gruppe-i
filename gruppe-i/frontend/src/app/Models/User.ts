export class User{
  id!:number;
  firstname!:string;
  lastname!:string;
  email!:string;
  password!:string;
  birthday!:string;
  profilePictureFile: File|any;
  chatList!: number[];


  //testingpurposes
  profilePicture!: string;
}
