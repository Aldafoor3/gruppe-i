export class DataSet {
  id: number;
  name: string;
  fileName: string;
  isFavorite: boolean;
  isXML: boolean;
  isCSV: boolean;

  constructor(
    id: number,
    name: string,
    fileName: string,
    isFavorite: boolean,
    isXML: boolean,
    isCSV: boolean
  ) {
    this.id = id;
    this.name = name;
    this.isFavorite = isFavorite;
    this.isXML = isXML;
    this.isCSV = isCSV;
    this.fileName = fileName;
  }
}
