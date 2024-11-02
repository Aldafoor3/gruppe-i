import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DemoDataSetService } from '../demo-data-set.service';
import { JsonEditorComponent, JsonEditorOptions } from 'ang-jsoneditor';
import * as d3 from 'd3';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import {toNumbers} from "@angular/compiler-cli/src/version_helpers";

@Component({
  selector: 'app-single-data-view',
  templateUrl: './single-data-view.component.html',
  styleUrls: ['./single-data-view.component.scss'],
})
export class SingleDataViewComponent implements OnInit {
  @ViewChild('content', { static: false }) content!: ElementRef;

  id: bigint;
  data: String | undefined;
  jsonData: any;
  jsonItemKeys: string[] = [];
  jsonItemValues: [] = [];

  changeWindowOpen: boolean = false;
  isFavoriteDataset!: boolean;

  itemKeys: string[] = [];
  itemValues: [] = [];
  selectedKey: string = '';
  selectedValue: string = '';
  treeMapData: any;

  radioValue: string = "bar"
  public profileDatasetVariable: number = 1;

  fileName: string = '';

  editorOptions: JsonEditorOptions;
  @ViewChild(JsonEditorComponent, { static: true })
  editor!: JsonEditorComponent;

  constructor(
    private route: ActivatedRoute,
    private dataSetService: DemoDataSetService
  ) {
    this.id = BigInt(this.route.snapshot.paramMap.get('id')!!);
    this.getData();
    this.getName();
    this.editorOptions = new JsonEditorOptions();
    this.editorOptions.modes = ['code', 'text', 'tree', 'view'];
  }

  ngOnInit(): void {
    this.checkFavourite();
  }
  ngAfterViewInit() {
    const changeDataDialog = document.getElementById(
      'changeDataWindow'
    ) as HTMLDialogElement;
    const openDialogButton = document.getElementById('openChangeData');
    const closeDialogButton = document.getElementById('closeChangeData');
    const sendChangeButton = document.getElementById('sendChange');

    openDialogButton?.addEventListener('click', () => {
      if (!this.changeWindowOpen) {
        changeDataDialog.showModal();
        this.changeWindowOpen = true;
      }
    });
    closeDialogButton?.addEventListener('click', () => {
      if (this.changeWindowOpen) {
        changeDataDialog.close();
        this.changeWindowOpen = false;
      }
    });
    sendChangeButton?.addEventListener('click', () => {
      this.updateDataset();

      changeDataDialog.close();
      this.changeWindowOpen = false;
    });


    const datasetProfileDialog = document.getElementById("dataToProfileDialog") as HTMLDialogElement;
    const openDTPButton = document.getElementById("addDataToProfile");
    const closeDTPButton = document.getElementById("closeWithoutSaveDTP");
    const saveAndCloseDTP = document.getElementById("saveAndCloseDTP");



    openDTPButton?.addEventListener('click', () => {
      datasetProfileDialog.showModal();
    });

    saveAndCloseDTP?.addEventListener('click', () => {

    });

    closeDTPButton?.addEventListener('click', () => {
      datasetProfileDialog.close();
    })


  }

  getName() {
    this.dataSetService.getDatasetList().subscribe({
      next: (data) => {
        data.forEach((element: any) => {
          if (Number(this.id) === element[0]) {
            this.fileName = element[1];
          }
        });
      },
      error: () => console.log('Error'),
    });
  }

  getData() {
    this.dataSetService.getDataset(this.id).subscribe({
      next: (data) => {
        console.log('#####', data);
        this.jsonData = data;
        if (this.jsonData.length > 200) {
          this.jsonData = this.jsonData.slice(0, 200);
        }

        this.jsonItemKeys = Object.keys(this.jsonData[0]);
        this.jsonItemValues = this.jsonData.map((obj: any) =>
          Object.values(obj)
        );

        this.updateGraphData(this.jsonData);
      },
      error: (error) => {
        console.error('Error fetching JSON data', error);
      },
    });
  }

  updateDataset() {
    this.jsonData = this.editor.get();
    this.updateGraphData(this.jsonData);

    let response = this.dataSetService.updateDataset(
      this.id.toString(),
      this.jsonData
    );
    if (response != null) {
      response.subscribe(
        (response) => {
          // Hier können Sie die Antwort verarbeiten
          alert(response);
          console.log(response);
        },
        (error) => {
          //TODO: ALLE Responses werden als "error" erkannt...

          // Hier können Sie Fehlerbehandlung durchführen
          console.error(error);
        }
      );
    }
  }

  updateGraphData(data: any) {
    let finalData = data[0].hasOwnProperty('row') ? data[0]['row'] : data;
    console.log("###############################treeMapData#######################");
    console.log(finalData);
    let dataChildren = finalData;

    if (dataChildren.length > 100) {
      dataChildren = dataChildren.slice(0, 100);
    }
    this.itemKeys = Object.keys(finalData[0]);
    this.selectedKey = this.itemKeys[0];
    this.selectedValue = this.itemKeys[0];

    this.itemValues = finalData.map((obj: any) => Object.values(obj));

    this.treeMapData = {
      name: 'Root',
      children: dataChildren,
    };
  }

  onFavoriteClick() {
    this.dataSetService
      .setNewFavourite(Number(this.id))
      ?.subscribe((response) => {
        console.log(response);
        alert(response);
      });

    this.checkFavourite();
  }

  checkFavourite() {
    this.dataSetService
      .isFavoriteDataset(Number(this.id))
      .subscribe((response) => {
        this.isFavoriteDataset = response;
      });
  }

  onSubmit() {
    this.createBarChart(this.treeMapData);
    this.createTreemap(this.treeMapData);
  }

  private createBarChart(treeMapData: any) {
    const nativeElement = document.getElementById('barchart')!;
    const width = nativeElement.clientWidth;
    const height = nativeElement.clientHeight;

    const svg = d3
      .select(nativeElement)
      .append('svg')
      .attr('width', width)
      .attr('height', height);

    const filteredData = treeMapData.children;
    console.log("###############################treeMapData#######################");
    console.log(treeMapData);
    console.log("###############################FILTERDDATA#######################");
    console.log(filteredData);

    let keys = Object.keys(filteredData[0]);

    const xScale = d3
      .scaleBand()
      .domain(filteredData.map((d: any) => d[this.selectedKey]))
      .range([0, width])
      .padding(0.1);

    const maxValue =
      d3.max(filteredData, (d: any) => Number(d[this.selectedValue])) || 0;
    const yScale = d3.scaleLinear().domain([0, maxValue]).range([height, 0]);

    svg
      .selectAll('rect')
      .data(filteredData)
      .enter()
      .append('rect')
      .attr('x', (d: any) => xScale(d[this.selectedKey])!)
      .attr('y', (d: any) => yScale(Number(d[this.selectedValue])))
      .attr('width', xScale.bandwidth())
      .attr(
        'height',
        (d: any) => height - yScale(Number(d[this.selectedValue]))
      )
      .attr('fill', 'steelblue');

    svg
      .selectAll('text')
      .data(filteredData)
      .enter()
      .append('text')
      .text((d: any) => `${d[this.selectedKey]} (${d[this.selectedValue]})`)
      .attr(
        'x',
        (d: any) => xScale(d[this.selectedKey])! + xScale.bandwidth() / 2
      )
      .attr('y', (d: any) => yScale(Number(d[this.selectedValue])) - 5)
      .attr('text-anchor', 'middle')
      .style('fill', 'black');
  }

  createTreemap(treeMapData: any) {
    const nativeElement = document.getElementById('treemap')!;
    const width = nativeElement.clientWidth;
    const height = nativeElement.clientHeight;

    const treemapLayout = d3
      .treemap()
      .size([width, height])
      .padding(1)
      .round(true);

    console.log("##############TREEMAPDATA FOR TREEMAP###########")
    console.log(treeMapData);

    const root = d3
      .hierarchy(treeMapData)
      .sum((d: any) => d[this.selectedValue]) // Update this line to use the correct key for the value property
      .sort((a, b) => (b.value || 0) - (a.value || 0)); // Update this line to use the correct key for the value property

    treemapLayout(root);

    const svg = d3
      .select(nativeElement)
      .append('svg')
      .attr('width', width)
      .attr('height', height);

    const nodes = svg
      .selectAll('rect')
      .data(root.leaves())
      .enter()
      .append('g')
      .attr('transform', (d: any) => `translate(${d.x0}, ${d.y0})`);

    nodes
      .append('rect')
      .attr('width', (d: any) => d.x1 - d.x0)
      .attr('height', (d: any) => d.y1 - d.y0)
      .style('fill', 'steelblue');

    nodes
      .append('text')
      .attr('dx', 4)
      .attr('dy', 14)
      .text((d: any) => d.data[this.selectedKey]) // Update this line to use the correct key for the name property
      .style('fill', 'black');
  }

  SavePDF(): void {
    const quality = 1; // Higher the better but larger file
    html2canvas(this.content.nativeElement, { scale: quality }).then(
      (canvas) => {
        const pdf = new jsPDF('p', 'mm', 'a4');
        pdf.addImage(canvas.toDataURL('image/png'), 'PNG', 0, 0, 211, 298);
        pdf.save('test.pdf');
      }
    );
  }

  generatePDF(): void {
    const content = this.content.nativeElement;
    html2canvas(content).then((canvas: any) => {
      const imgWidth = 208;
      const pageHeight = 295;
      const imgHeight = (canvas.height * imgWidth) / canvas.width;
      let heightLeft = imgHeight;
      let position = 0;
      heightLeft -= pageHeight;
      const doc = new jsPDF('p', 'mm');

      doc.addImage(canvas, 'PNG', 0, position, imgWidth, imgHeight, '', 'FAST');
      while (heightLeft >= 0) {
        position = heightLeft - imgHeight;
        doc.addPage();
        doc.addImage(
          canvas,
          'PNG',
          0,
          position,
          imgWidth,
          imgHeight,
          '',
          'FAST'
        );
        heightLeft -= pageHeight;
      }

      doc.addImage(canvas, 'PNG', 0, position, imgWidth, imgHeight, '', 'FAST');
      doc.setFontSize(10);
      doc.text('Exportdatum: ' + new Date().toISOString(), 10, pageHeight - 10);
      doc.save(this.fileName + '.pdf');
    });
  }


  saveDatasetToProfile() {

  }


  speichereWert(buttonWert: number) {
    this.profileDatasetVariable = buttonWert;
    console.log(this.profileDatasetVariable)
  }

  saveRadioValue(radioValue: string) {
    this.radioValue = radioValue;
    console.log(this.radioValue)
  }

  rowLengt: number = this.jsonItemValues.length


  setRowAndColumn() {
    const DTPRequest = new FormData();
    DTPRequest.append("datasetID", this.id.toString());
    DTPRequest.append("sessionID", localStorage.getItem("SID")!);
    DTPRequest.append("datasetIDinProfile", this.profileDatasetVariable.toString());
    DTPRequest.append("rowname", this.selectedKey);
    DTPRequest.append("colname", this.selectedValue);
    DTPRequest.append("graphType", this.radioValue);

    DTPRequest.append("rowLength", this.rowLengt.toString())

    console.log("rowname: " + this.selectedKey + "\ncolname: " + this.selectedValue);
    console.log("Created Formdata");
    this.dataSetService.sendDTPrequest(DTPRequest).subscribe(response =>{
      console.log(response)
    });
  }
}
