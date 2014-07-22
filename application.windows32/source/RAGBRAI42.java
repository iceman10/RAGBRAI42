import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class RAGBRAI42 extends PApplet {


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



public void setup(){
  
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
  
  totalDistMI = totalDistKM*.621371f;
  // print(totalDistMI);
}

public void draw(){
  background(0xff474747);  

  //Show the plot area as a white box
  /*
  fill(255);
  rectMode(CORNERS); 
  noStroke(); 
  rect(25,25,width-25,height-25);
  
  */
  
  
  drawDay(day);
       
}

public void drawDay(int day){
  // Elevation Code
  
  if (day == 8){
    for (int row =0; row < rowCount; row++){
      fill(0xff0000FF);
      text("Overview", width/2-75, 50); //Replace "magic number"
 
      float elevation = dataTable.getFloat(row,3);
      float totalD = dataTable.getFloat(row,6);
      float x = map(totalD, 0,totalDistKM,padXL,width-padXR);
      float y = map(elevation,eleMin,eleMax,height-padYBtm,0+padYTop); // )(0,0) is top left corner thus y is "inverted"
      if (dataTable.getFloat(row,0) == 1){
        fill(0xff0000FF);
      } else if (dataTable.getFloat(row,0) == 2){
        fill(0xff0080FF);
      } else if (dataTable.getFloat(row,0) == 3){
        fill(0xff00FFFF);
      } else if (dataTable.getFloat(row,0) == 4){
        fill(0xff00FF00);
      } else if (dataTable.getFloat(row,0) == 5){
        fill(0xffFFFF00);
      } else if (dataTable.getFloat(row,0) == 6){
        fill(0xffFF8000);
      } else if (dataTable.getFloat(row,0) == 7){
        fill(0xffFF0000);
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
            fill(0xff0000FF);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 2){
            fill(0xff0080FF);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 3){
            fill(0xff00FFFF);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 4){
            fill(0xff00FF00);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 5){
            fill(0xffFFFF00);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 6){
            fill(0xffFF8000);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          } else if (dataTable.getFloat(row,0) == 7){
            fill(0xffFF0000);
            text("Day " + day, width/2-75, 50); //Replace "magic number"
            printStats();
          }
          noStroke(); 
          ellipse(x,y,pointSize,pointSize); 
      }  
    } 
  }
}

public void keyPressed(){
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

public void mouseClicked(){
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

public void printStats(){
  textSize(24);
  text("Distance: "+kmToMi(dayDistKM)+" mi",75,75);
  text("Max height: "+ metersToFt(dayMax)+" ft", 400,100); 
}

public float kmToMi(float km){
  return km*0.621371f; //i.e 1 mi = 0.621371 km 
}

public float metersToFt(float meters){
  return meters*3.28084f; //i.e. 1 meter = 3.28084 ft
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


  public int getRowCount() {
    return rowCount;
  }
  
  
  // find a row by its name, returns -1 if no row found
  public int getRowIndex(String name) {
    for (int i = 0; i < rowCount; i++) {
      if (data[i][0].equals(name)) {
        return i;
      }
    }
    println("No row named '" + name + "' was found");
    return -1;
  }
  
  
  public String getRowName(int row) {
    return getString(row, 0);
  }


  public String getString(int rowIndex, int column) {
    return data[rowIndex][column];
  }

  
  public String getString(String rowName, int column) {
    return getString(getRowIndex(rowName), column);
  }

  
  public int getInt(String rowName, int column) {
    return parseInt(getString(rowName, column));
  }

  
  public int getInt(int rowIndex, int column) {
    return parseInt(getString(rowIndex, column));
  }

  
  public float getFloat(String rowName, int column) {
    return parseFloat(getString(rowName, column));
  }

  
  public float getFloat(int rowIndex, int column) {
    return parseFloat(getString(rowIndex, column));
  }
  
  
  public void setRowName(int row, String what) {
    data[row][0] = what;
  }


  public void setString(int rowIndex, int column, String what) {
    data[rowIndex][column] = what;
  }

  
  public void setString(String rowName, int column, String what) {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = what;
  }

  
  public void setInt(int rowIndex, int column, int what) {
    data[rowIndex][column] = str(what);
  }

  
  public void setInt(String rowName, int column, int what) {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = str(what);
  }

  
  public void setFloat(int rowIndex, int column, float what) {
    data[rowIndex][column] = str(what);
  }


  public void setFloat(String rowName, int column, float what) {
    int rowIndex = getRowIndex(rowName);
    data[rowIndex][column] = str(what);
  }
  
  
  // Write this table as a TSV file
  public void write(PrintWriter writer) {
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
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "RAGBRAI42" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
