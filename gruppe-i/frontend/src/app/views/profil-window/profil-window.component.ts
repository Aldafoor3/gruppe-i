import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { DataSet } from "../../Models/DataSet";
import { DataSetService } from "../../services/data-set.service";
import { ProfilWindowService } from "./profil-window.service";
import { HttpResponse } from '@angular/common/http';
import * as d3 from "d3";
import {ActivatedRoute, Router} from "@angular/router";
import {Arc, arc, DefaultArcObject, PieArcDatum, select, style, ValueFn} from "d3";
import {transformRotate} from "@turf/turf";

@Component({
  selector: 'app-profil-window',
  templateUrl: './profil-window.component.html',
  styleUrls: ['./profil-window.component.scss']
})
export class ProfilWindowComponent implements OnInit,AfterViewInit {
  datenArray: Array<DataSet> = [];

  id: bigint;
  firstname: string = "";
  lastname:string = "";
  birthdate: string= "";
  email: string = "";
  picture: string | any;
  isPrivate: boolean = true;
  privacySettingFriendList:boolean = true;
  isMyProfile: boolean = false;

  newPassword: string | undefined
  newPasswordRepeat: string | undefined

  friendList: any[] = [];

  favoriteData: string[] = [];
  dataSets: String[][] | any;

  settingWindowDivs: string = "div1"



  constructor(
    private profileService: ProfilWindowService,
    private dataSetService: DataSetService,
    private route: ActivatedRoute,
    private routerOutlet: Router
  ) {
    this.id = BigInt(this.route.snapshot.paramMap.get('id')!!)
  }

  //TODO: Friendlist fetchen, sofern nicht Privat

  ngOnInit(): void {
    //this.getData();
    this.profileService.getProfile(this.id, localStorage.getItem("SID")!).subscribe(
      (response: any) => {
        this.firstname = response.firstname;
        this.lastname = response.lastname;
        this.birthdate = response.birthday;
        this.email = response.email;
        this.isPrivate = response.privacySetting;
        this.privacySettingFriendList = response.friendlistPrivacySetting;

        this.nyanCounter = response.nyanCounter;

        this.color1 = response.profileBackgroundColor;

        this.dataSets = response.datasetsSettings;
        this.favoriteData = response.favoriteData;

        for(let i = 0; i < this.dataSets.length; i++) {
          if (this.dataSets[i][0] != null) {
            console.log("###### NEW DATASET ######")
            if(this.dataSets[i][4] == "tree") this.createTreeChart(i);
            if(this.dataSets[i][4] == "bar")this.createBarChart(i);
          }
        }

        console.log(this.firstname + this.lastname + this.birthdate);
        //this.isMyProfile = response.myProfileBool;
        this.getProfilPicture();
      },
      (error: any) => {
        alert("This profile is private")
        this.routerOutlet.navigate(['/geoData']);
        console.error(error);
        // Fehlerbehandlung hier durchführen
      }
    );
    if(localStorage.getItem("SID") == null || localStorage.getItem("SID") ==""){
      alert("you are not logged in.")
      this.routerOutlet.navigate(['/geoData']);
    }


    //TODO: falls bad request --> reroute nacho geoData oder so
    this.profileService.getProfilePrivacySetting(this.id, localStorage.getItem("SID")!).subscribe(response =>{
      this.isMyProfile = response.isMyProfile;
      this.isPrivate =response.privacySetting;
      this.privacySettingFriendList  = response.privacySettingFriendList;
      console.log("isMyProfile " +response.isMyProfile);
      console.log("privacySetting " +response.privacySetting);
      console.log("privacySettingFriendList "+response.privacySettingFriendList);
      this.getProfilData();
      this.getFriendListFromUser();

    });


    console.log(this.friendList)
    this.getProfilPicture();

  }

  @ViewChild('openDialogButton') openDialogButton!: ElementRef<HTMLElement>;

  ngAfterViewInit() {
    const changeSettingsDialog = document.getElementById("changeSettingsDialog") as HTMLDialogElement;
    const openChangeSettingDialog = document.getElementById("privacySwitch")
    const closeChangeSettingDialog = document.getElementById("closeWithoutSave");


    openChangeSettingDialog?.addEventListener('click', () => {
      if(this.isMyProfile) {
        changeSettingsDialog.showModal();
      }
    });

    closeChangeSettingDialog?.addEventListener('click', () => {
      changeSettingsDialog.close();
    });

  }



  getFriendListFromUser(){
    console.log("###################");
    if(this.privacySettingFriendList || this.isMyProfile) {
      this.profileService.getFriendListFromProfile(this.id, localStorage.getItem("SID")!).subscribe(response => {
        this.friendList = response.friendList;
        console.log(this.friendList);
        console.log(response)
      })
    }
  }

  public getProfilData(){
    if(this.isMyProfile || !this.isPrivate) {
      console.log("FETCHING PROFILE")
      this.profileService.getProfile(this.id, localStorage.getItem("SID")!).subscribe(
        (response: any) => {
          this.firstname = response.firstname;
          this.lastname = response.lastname;
          this.birthdate = response.birthday;
          this.email = response.email;
          this.isPrivate = response.privacySetting;
          console.log(this.firstname + this.lastname + this.birthdate);
          //this.isMyProfile = response.myProfileBool;
        },
        (error: any) => {
          console.error(error);
          // Fehlerbehandlung hier durchführen
        });
    }
  }

  public getProfilPicture(){
    if(this.id==null || this.id<=0) return;
    this.profileService.getProfilPictureObservable(Number(this.id)).subscribe(response =>{
      this.picture = "data:image/jpeg;base64, " + this.arrayBufferToBase64(response);
    })
  }




  getData(): void {
    this.dataSetService.getDataSetList().subscribe((data) => {
      this.datenArray = data;
    });
  }

  public arrayBufferToBase64(buffer:ArrayBuffer) {
    let binary = '';
    let bytes = new Uint8Array(buffer);
    let len = bytes.byteLength;
    for (let i = 0; i < len; i++) {
      binary += String.fromCharCode(bytes[i]);
    }
    return window.btoa(binary);
  }

  routeToProfile(accId:number){
    this.routerOutlet.navigateByUrl('/', { skipLocationChange: false }).then(()=>
      this.routerOutlet.navigate(['/profil/'+accId],{replaceUrl:true}));
  }


  public changePrivacy(){
    this.isPrivate = ! this.isPrivate;
    console.log("changing Privacy to: " + this.isPrivate)
    this.profileService.changePrivacySetting(this.id, localStorage.getItem("SID")!, this.isPrivate).subscribe(response => {
      console.log(response)
      //privacy Settings nochmal fetchen
      //TODO: Anfrage reagiert ab hier nicht mehr - funktioniert aber trotzdem angemessen
      this.profileService.getProfilePrivacySetting(this.id, localStorage.getItem("SID")!).subscribe(response =>{
        this.isMyProfile = response.isMyProfile;
        this.isPrivate =response.privacySetting;
        this.privacySettingFriendList  = response.privacySettingFriendList;
      });

    })
  }

  changeFriendlistPrivacy() {
    this.privacySettingFriendList = !this.privacySettingFriendList;
    console.log("changing Privacy to: " + this.privacySettingFriendList)
    this.profileService.changeFriendlistPrivacySetting(this.id, localStorage.getItem("SID")!, this.privacySettingFriendList).subscribe(response => {
      this.profileService.getProfilePrivacySetting(this.id, localStorage.getItem("SID")!).subscribe(response =>{
        this.isMyProfile = response.isMyProfile;
        this.isPrivate =response.privacySetting;
        this.privacySettingFriendList  = response.privacySettingFriendList;
      });
    });
  }

  // ------------------------------------- EINFACH NEIN -------------------------------------

  private createBarChart(id: number) {
    let data = this.dataSets[id];
    console.log(data);
    //let finalData = JSON.parse(data[0]).hasOwnProperty('row') ? data[0]['row'] : data;
    let finalData = JSON.parse(data[0])

    console.log(finalData)

    const nativeElement = document.getElementById('grid-item-' + data[3]);
    console.log("nativeElement")
    console.log(nativeElement)

    if (!nativeElement) {
      console.error('Element not found with ID:', 'grid-item-' + data[3]);
      return;
    }


    const width = nativeElement.clientWidth;
    const height = nativeElement.clientHeight;
    console.log("width: "+ width)
    console.log("heigth: "+ height)

    let key = -1;
    let value= -1;

    const keyvalues = finalData.map((obj: any) => Object.keys(obj));
    const someData = keyvalues[0];
    console.log("####################keyvalues########################")
    console.log(someData)
    console.log(data[1]);
    console.log(data[2])

    for(let i =0; i < someData.length; i++){
      if(someData[i]===data[1]){ key = i;}
      if(someData[i]===data[2]){ value = i;}
    }
    console.log("KEY AND VALUE")
    console.log(key);
    console.log(value);

    console.log(nativeElement.id);

    const svg = d3
      .select(nativeElement)
      .append('svg')
      .attr('width', width)
      .attr('height', height);

    let filteredData = finalData.map((obj: any) => Object.values(obj));

    //TODO: IN ABHÄNGIGKEIT VON BACKEND SPLICEN

    filteredData = filteredData.splice(0,data[5]); //Anzahl an Objects, die angezeigt werden sollen

    console.log("####FILTERED DATA####");
    console.log(filteredData);

    const xScale = d3
      .scaleBand()
      .domain(filteredData.map((d: any) => d[key]))
      .range([0, width])
      .padding(0.1);

    const maxValue = d3.max(filteredData, (d: any) => Number(d[value])) || 0;
    const yScale = d3.scaleLinear().domain([0, maxValue]).range([height, 0]);


    svg
      .selectAll('rect')
      .data(filteredData)
      .enter()
      .append('rect')
      .attr('x', (d: any) => xScale(d[key])!)
      .attr('y', (d: any) => yScale(Number(d[value])))
      .attr('width', xScale.bandwidth())
      .attr('height', (d: any) => height - yScale(Number(d[value])) )
      .attr('fill', 'blue');

    svg
      .selectAll('text')
      .data(filteredData)
      .enter()
      .append('text')
      .text((d: any) => `${d[key]} (${d[value]})`)
      .attr('x', (d: any) => xScale(d[key])! + xScale.bandwidth() / 2)
      .attr('y', (d: any) => yScale(Number(d[value])) +20)
      //.select((d: any) => d.attr("transform", "rotate(90)"))
      .style('fill', 'black');

  }

  // https://d3-graph-gallery.com/graph/pie_annotation.html

  private createTreeChart(id: number){
    console.log("CREATE PIE CHART START")
    let dataInfo = this.dataSets[id];//Alle Informationen des Favoriten
    let data = JSON.parse(dataInfo[0]) //Der Datensatz selbst

    data = data.splice(0,dataInfo[5]); //anzahl an Objects, die angezeigt werden sollen

    const nativeElement = document.getElementById('grid-item-' + dataInfo[3]); //Referenz zur HTML
    console.log(data[3])
    console.log(nativeElement)

    if (!nativeElement) {
      console.error('Element not found with ID:', 'grid-item-' + dataInfo[3]);
      return;
    }

    //TODO: Höhen und Breiten potentiell ändern

    const width = nativeElement.clientWidth ; //Breite
    const height = nativeElement.clientHeight ; //Höhe

    let margin = 50;
    const radius = Math.min(width,height) /2 -margin
    let colors: d3.ScaleOrdinal<string, unknown, never> | ((arg0: any) => any);

    const key:string = dataInfo[1];
    const value:string = dataInfo[2];

    let svg = d3.select(nativeElement)
      .append("svg")
      .attr("width", width)
      .attr("height", height)
      .append("g")
      .attr(
        "transform",
        "translate(" + width / 2 + "," + height / 2 + ")"
      );

    console.log("ValueMappingTest")
    data.map((d: { [x: string]: any; }) => console.log(d[value]));

    colors = d3.scaleOrdinal()
      .domain(data.map((d: { [x: string]: any; }) => d[value]))
      .range(d3.schemeSet2);
    const pie = d3.pie<any>().value((d:{ [x: string]: any; }) => d[value]);

    //Pie Data für SVG's
    var data_ready = pie(data);
    console.log("DATA_READY")
    console.log(data_ready);

    //PIE CHART PARTS
    const arcFn = d3
      .arc<d3.PieArcDatum<any>>()
      .innerRadius(0)
      .outerRadius(radius);
    //SVG für Pie-Pieces
    svg
      .selectAll('pieces')
      .data(data_ready)
      .enter()
      .append('path')
      .attr('d', arcFn)
      .attr('fill', (d:any, i:any) => colors(i))
      .attr("stroke", "black")
      .style("stroke-width", "1px")

    console.log("KeyMappingTest")
    data.map((d: { [x: string]: any; }) => console.log(d[key]));
    //SVG für Namen der Pie-Pieces
    svg
      .selectAll('pieces')
      .data(data_ready)
      .enter()
      .append('text')
      .text( (d:{ [x: string]: any; }) => d['data'][key])
      .attr('transform', (d:any) => "translate(" + arcFn.centroid(d) + ")")
      .style("text-anchor", "middle")
      .style("font-size", 15)
  }

  switchDiv(windowDiv: string) {
    this.settingWindowDivs = windowDiv;
  }

  submitForm() {

    if (!this.checkInputData() || !this.isMyProfile) {
      location.reload()
      return;
    }

    const changedProfileDataRequest = new FormData();
    changedProfileDataRequest.append("sessionid", localStorage.getItem("SID")!);
    changedProfileDataRequest.append("profileID", this.id.toString())
    changedProfileDataRequest.append("firstname", this.firstname);
    changedProfileDataRequest.append("lastname", this.lastname);
    changedProfileDataRequest.append("email", this.email);
    changedProfileDataRequest.append("birthday", this.birthdate);

    if(this.newPassword == undefined || this.newPasswordRepeat == undefined) {
      this.newPassword = "DONOTSETPASSWORD";
      this.newPasswordRepeat = "DONOTSETPASSWORD";
    }

    changedProfileDataRequest.append("newPassword", this.newPassword);
    changedProfileDataRequest.append("newPasswordRepeat", this.newPasswordRepeat);

    this.profileService.changeProfileData(changedProfileDataRequest).subscribe(response => {
      location.reload()
    });
  }

  checkInputData():boolean{
    let bool: boolean = true;

    if( !(this.email.indexOf("@")>=0) || !(this.email.indexOf(".")>=0) ){
      alert("email nicht Korrekt angegeben.");
      bool = false;
    }
    if(this.newPassword != undefined && this.newPasswordRepeat != undefined) {
      if(this.newPassword.toString() != this.newPasswordRepeat.toString()) {
        alert("passwörter stimmen nicht überein.");
        bool = false;
      }
    }

    return bool;
  }

  toDataset(id: string) {
    this.routerOutlet.navigateByUrl('/', { skipLocationChange: false }).then(()=>
      this.routerOutlet.navigate(['/testData/' + id],{replaceUrl:true}));
  }


  color1: string = "#23887f";
  colorpickervisible:boolean = false

  updateColor(event:string) {
    if(localStorage.getItem("SID") ==""||undefined){
      alert("not logged in.")
      return;
    }
    console.log(event); // this is your selected color
    console.log(this.color1)
    this.profileService.setBackgroundColor(localStorage.getItem("SID")!,event).subscribe(response =>{
      console.log(response)
    })
    this.colorpickervisible = false;
  }

  setColorPickerVisible() {
    this.colorpickervisible = !this.colorpickervisible;
  }

  nyanCounter: number = 0;
  nyanCatVisible: boolean = false;

  doNyan() {
    this.nyanCatVisible = true;
    this.profileService.giveNyan(this.id).subscribe(response => {
      console.log(response)
    });
    setTimeout(() => {
      this.nyanCatVisible = false;
    }, 2500);
  }
}
