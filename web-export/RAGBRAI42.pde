
Table dataTable;
int rowCount;
float eleMin = MAX_FLOAT;
float eleMax = MIN_FLOAT;
float padXR =25; 
float padXL =75; 
float padYTop =200; 
float padYBtm = 50; 
int pointSize =2; 
float totalDistKM; 
float totalDistMI; 
int height = 600;
int width = (height *4)/3;
int day = 8; //Day 8 is the overview
float dayMax = MIN_FLOAT;
float dayMin = MAX_FLOAT;
float dayDistKM =0; 
float startDistKM = 0; 
float finalDistKM =0; 
PFont titleFont; 



void setup(){
  
  size(width,height);
  dataTable = new Table("data2.txt");
  rowCount = dataTable.getRowCount();
  titleFont = createFont("SansSerif",40);
  textFont(titleFont); 
  
  
  for (int row = 0; row < rowCount; row++){
    float value = dataTable.getFloat(row,3);
      if (value > eleMax){
        eleMax = value;
      }
      if (value < eleMin){
        eleMin = value;
      }
      if (row == rowCount-1) {
        totalDistKM = dataTable.getFloat(row,6);
      }
  }
  
  totalDistMI = totalDistKM*.621371;
  // print(totalDistMI);
}

void draw(){
  background(#474747);  

  //Show the plot area as a white box
  /*
  fill(255);
  rectMode(CORNERS); 
  noStroke(); 
  rect(25,25,width-25,height-25);
  
  */
  
  
  drawDay(day);
       
}

void drawDay(int day){
  // Elevation Code
  
  if (day == 8){
    for (int row =0; row < rowCount; row++){
      fill(#0000FF);
      text("RAGBRAI 42 Overview", width/2-75, 50); //Replace "magic number"
 
      float elevation = dataTable.getFloat(row,3);
      float totalD = dataTable.getFloat(row,6);
      float x = map(totalD, 0,totalDistKM,padXL,width-padXR);
      float y = map(elevation,eleMin,eleMax,height-padYBtm,0+padYTop); // )(0,0) is top left corner thus y is "inverted"
      if (dataTable.getFloat(row,0) == 1){
        fill(#0000FF);
      } else if (dataTable.getFloat(row,0) == 2){
        fill(#0080FF);
      } else if (dataTable.getFloat(row,0) == 3){
        fill(#00FFFF);
      } else if (dataTable.getFloat(row,0) == 4){
        fill(#00FF00);
      } else if (dataTable.getFloat(row,0) == 5){
        fill(#FFFF00);
      } else if (dataTable.getFloat(row,0) == 6){
        fill(#FF8000);
      } else if (dataTable.getFloat(row,0) == 7){
        fill(#FF0000);
      }
      noStroke(); 
      ellipse(x,y,pointSize,pointSize);
    }  
  } else {
      for (int row =0; row < rowCount; row++){
        
        //float elapsedDist = dataTable.getFloat(row,6);
        
        if (dataTable.getFloat(row,0) == day){
          
          
          float elevation = dataTable.getFloat(row,3);
          float currentDist = dataTable.getFloat(row,6);
          float x = map(currentDist, startDistKM,finalDistKM,padXL,width-padXR);
          float y = map(elevation,dayMin,dayMax,height-padYBtm,0+padYTop); // (0,0) is top left corner thus y is "inverted"
          if (dataTable.getFloat(row,0) == 1){
            fill(#0000FF);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 2){
            fill(#0080FF);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 3){
            fill(#00FFFF);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 4){
            fill(#00FF00);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 5){
            fill(#FFFF00);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 6){
            fill(#FF8000);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 7){
            fill(#FF0000);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          }
          noStroke(); 
          ellipse(x,y,pointSize,pointSize); 
      }  
    } 
  }
}

void keyPressed(){
  if (key == ' '){
    day = day+1; //Day 8 is the overview
    startDistKM = 1000; 
    finalDistKM =0;
    dayMax= MIN_FLOAT; 
    dayMin = MAX_FLOAT;
    if (day == 9){
      day =1;
    }
    //Loop that recalculates max and min elevation, and start/end dist each time spacebar is pressed. 
    for (int row =0; row < rowCount; row++){
      if (dataTable.getFloat(row,0) == day){
      float value = dataTable.getFloat(row,3);
      if (value > dayMax){
        dayMax = value;
        
      }
      if (value < dayMin){
        dayMin = value;
      }
      float value2 = dataTable.getFloat(row,6);
      if ( value2 < startDistKM){
        startDistKM = value2;
      } 
      
      if ( value2 > finalDistKM){
        finalDistKM = value2;
      }
      dayDistKM = finalDistKM-startDistKM;
      }
    }
  }
}

void mouseClicked(){
    day = day+1; //Day 8 is the overview
    startDistKM = 1000; 
    finalDistKM =0;
    dayMax= MIN_FLOAT; 
    dayMin = MAX_FLOAT;
    if (day == 9){
      day =1;
    }
    //Loop that recalculates max and min elevation, and start/end dist each time spacebar is pressed. 
    for (int row =0; row < rowCount; row++){
      if (dataTable.getFloat(row,0) == day){
      float value = dataTable.getFloat(row,3);
      if (value > dayMax){
        dayMax = value;
        
      }
      if (value < dayMin){
        dayMin = value;
      }
      float value2 = dataTable.getFloat(row,6);
      if ( value2 < startDistKM){
        startDistKM = value2;
      } 
      
      if ( value2 > finalDistKM){
        finalDistKM = value2;
      }
      dayDistKM = finalDistKM-startDistKM;
      }
    }
}

void printStats(){
  textSize(24);
  text("Distance: "+kmToMi(dayDistKM)+" mi",75,75);
  text("Max height: "+ metersToFt(dayMax)+" ft", 400,100); 
}

float kmToMi(float km){
  return km*0.621371; //i.e 1 mi = 0.621371 km 
}

float metersToFt(float meters){
  return meters*3.28084; //i.e. 1 meter = 3.28084 ft
}

class Table {
  String[][] data;
  int rowCount;
  
  
  Table() {
    data = new String[10][10];
  }

  
  Table(String filename) {
    String[] rows = loadStrings(filename);
    data = new String[rows.length][];
    
    for (int i = 0; i < rows.length; i++) {
      if (trim(rows[i]).length() == 0) {
        continue; // skip empty rows
      }
      if (rows[i].startsWith("#")) {
        continue;  // skip comment lines
      }
      
      // split the row on the tabs
      String[] pieces = split(rows[i], TAB);
      // copy to the table array
      data[rowCount] = pieces;
      rowCount++;
      
      // this could be done in one fell swoop via:
      //data[rowCount++] = split(rows[i], TAB);
    }
    // resize the 'data' array as necessary
    data = (String[][]) subset(data, 0, rowCount);
  }


  int getRowCount() {
    return rowCount;
  }
  
  
  // find a row by its name, returns -1 if no row found
  int getRowIndex(String name) {
    for (int i = 0; i < rowCount; i++) {
      if (data[i][0].equals(name)) {
        return i;
      }
    }
    println("No row named '" + name + "' was found");
    return -1;
  }
  
  
  String getRowName(int row) {
    return getString(row, 0);
  }


  String getString(int rowIndex, int column) {
    return data[rowIndex][column];
  }

  
  String getString(String rowName, int column) {
    return getString(getRowIndex(rowName), column);
  }

  
  int getInt(String rowName, int column) {
    return parseInt(getString(rowName, column));
  }

  
  int getInt(int rowIndex, int column) {
    return parseInt(getString(rowIndex, column));
  }

  
  float getFloat(String rowName, int column) {
    return parseFloat(getString(rowName, column));
  }

  
  float getFloat(int rowIndex, int column) {
    return parseFloat(getString(rowIndex, column));
  }
  
  
  void setRowName(int row, String what) {
    data[row][0] = what;
  }


  void setString(int rowIndex, int column, String what) {
    data[rowIndex][column] = what;
  }

  
  void setString(String rowName, int column, String what) {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = what;
  }

  
  void setInt(int rowIndex, int column, int what) {
    data[rowIndex][column] = str(what);
  }

  
  void setInt(String rowName, int column, int what) {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = str(what);
  }

  
  void setFloat(int rowIndex, int column, float what) {
    data[rowIndex][column] = str(what);
  }


  void setFloat(String rowName, int column, float what) {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = str(what);
  }
  
  
  // Write this table as a TSV file
  void write(PrintWriter writer) {
    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < data[i].length; j++) {
        if (j != 0) {
          writer.print(TAB);
        }
        if (data[i][j] != null) {
          writer.print(data[i][j]);
        }
      }
      writer.println();
    }
    writer.flush();
  }
}

