import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import { DemoDataSetService } from "./demo-data-set.service";
import {Router} from "@angular/router";

import { DataSetInfo} from "../../../Models/dataSetInfo";

@Component({
  selector: 'app-demo-data-set',
  templateUrl: './demo-data-set.component.html',
  styleUrls: ['./demo-data-set.component.scss']
})
export class DemoDataSetComponent implements OnInit{

  dataSetList: DataSetInfo[]|any;

  uploadWindowOpen: boolean = false;
  sortState: number = 0;

  constructor(private demoDBService: DemoDataSetService, private router: Router) { }


  ngOnInit():void {
    this.updateDataSetList();
    this.updateDataSetListTest();
  }

  ngAfterViewInit() {
    const uploadDataDialog = document.getElementById("uploadDatasetDialog") as HTMLDialogElement;
    const openUploadButton = document.getElementById("openUploadButton");
    const uploadDatasetButton = document.getElementById("uploadDatasetButton");
    const closeDialogButton = document.getElementById("closeUploadDialogButton");


    openUploadButton?.addEventListener('click', () => { if(!this.uploadWindowOpen) {uploadDataDialog.showModal(); this.uploadWindowOpen = true}});

    uploadDatasetButton?.addEventListener('click', () => {
      //TODO: Upload hinzufügen
      this.onSubmit()
      uploadDataDialog.close();
      this.uploadWindowOpen = false;
    });

    closeDialogButton?.addEventListener('click', () => {this.uploadWindowOpen = false; uploadDataDialog.close();})

    // ----- Sorting Table -----
    const sortByName = document.getElementById("sortByName");
    const sortByID = document.getElementById("sortByID");
    const wordSearch = document.getElementById("textSearchInput")

    sortByName?.addEventListener('click', () => {
      this.sortState = 1;
      this.dataSetList.sort((a: DataSetInfo, b: DataSetInfo) => a.name.localeCompare(b.name, undefined, { sensitivity: 'base' }));
    });

    sortByID?.addEventListener('click', () => {
      if (this.sortState != 2) {
        this.sortState = 2;
        this.dataSetList.sort((a: DataSetInfo, b: DataSetInfo) => {
          if (a.id < b.id) {
            return -1;
          } else if (a.id > b.id) {
            return 1;
          } else {
            return 0;
          }
        });
      } else {
        this.sortState = 0;
      }
    });

    wordSearch?.addEventListener('click', () => {

    });


  }


  @ViewChild('fileInput') fileInput: ElementRef | undefined;

  onFileSelect(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    const file = (inputElement.files as FileList)[0];
    console.log(this.dataSetList);
    //this.demoDBService.uploadDataSet(file);
  }

  onSubmit() {
    if (this.fileInput && this.fileInput.nativeElement.files.length > 0) {
      const file = this.fileInput.nativeElement.files[0];
      this.demoDBService.uploadDataSet(file);
      this.updateDataSetList();
    }
  }

  public updateDataSetList() {
    this.demoDBService.getDatasetList()
      .subscribe(list => {
        this.dataSetList = list;
        console.log(this.dataSetList)
      });
    this.updateDataSetListSearched();
  }

  searchText: string = "";
  dataSetListDisplayed: DataSetInfo[]|any;
  public updateDataSetListSearched(){
    let tempDataList: DataSetInfo[] = [];
    for(let i= 0; i<this.dataSetList.length;i++){
      const info = this.dataSetList[i];
      const infoName:string = info[1];
      if(infoName.indexOf(this.searchText)>=0){
        tempDataList[tempDataList.length] = info;
      }
      this.dataSetListDisplayed = tempDataList;
    }
  }

  dataSetListTest: DataSetInfo[]|any;
  public updateDataSetListTest(){
    this.demoDBService.getDatasetListTest()
      .subscribe(list =>{
        this.dataSetListTest = list;
        console.log(this.dataSetListTest);
      })
  }

  getDataset(id: bigint) {
    this.router.navigate(["/testData",id]);
  }

  updateDataset() {
  }
}
