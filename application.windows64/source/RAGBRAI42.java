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

//Created by Isaac Beck
//Visit www.isaacbeck.com for more information 

/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
 
Table dataTable;
int rowCount;
float eleMin = MAX_FLOAT;
float eleMax = MIN_FLOAT;
float padXR =50; 
float padXL =75; 
float padYTop =200; 
float padYBtm = 50; 
int pointSize =4; 
float totalDistKM; 
float totalMClimb =0; 
boolean dispTxt = false;
int day = 8; //Day 8 is the overview
float dayMax = MIN_FLOAT;
float dayMin = MAX_FLOAT;
float dayDistKM =0; 
float startDistKM = 0; 
float finalDistKM =0; 
PFont titleFont; 
PFont bodyFont; 
float dayMClimb = 0; 
float gradeMin = MAX_FLOAT;
float gradeMax = MIN_FLOAT;
float dayGradeMin = MAX_FLOAT;
float dayGradeMax = MIN_FLOAT;
float closestDist;
String closestText;
float closestTextX; 
float closestTextY;
long lastTime = 0; 


public void setup(){
  //int height = displayHeight;
  //int width = displayWidth;
  size(960,720);
  //size(displayWidth, displayHeight);
  if (frame != null) {
    frame.setResizable(true);
  }
  dataTable = new Table("data3.txt");
  rowCount = dataTable.getRowCount();
  titleFont = loadFont("Cambria-Bold-48.vlw");
  bodyFont = loadFont("Corbel-Bold-24.vlw");
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
    float value2 = dataTable.getFloat(row,8);
      if (value2 > gradeMax){
        gradeMax = value2;
      }
      if (value2 < gradeMin){
        gradeMin = value2;
      }
      float value3 = dataTable.getFloat(row,3);
      if (row < rowCount-1){
        if (value3 < dataTable.getFloat(row+1,3)){
          totalMClimb = totalMClimb +(dataTable.getFloat(row+1,3)-value3);
        }
      }
  }
  
  //totalDistMI = totalDistKM*.621371;
  //print(gradeMax);
  //print(gradeMin);
  smooth();
  lastTime = millis();
}

public void draw(){
  //background(#FFFFFF);
  background(0xff474747);
  closestDist = MAX_FLOAT;
  
  
  noFill();
  strokeWeight(1.5f);
  drawDay(day);
  drawXLabels();
  drawYLabels();
  drawAxisLines();
  //Display rollover text on each graph
  //closestText,closTextX and closestTextY are set in the drawDataHighlight method
  if(closestDist != MAX_FLOAT){
   fill(0xffF8FFC6);
   rect(closestTextX-45,closestTextY-20,90,50);
   strokeWeight(1.5f);
   fill(0);
   textSize(18);
   textAlign(CENTER);
   text(closestText, closestTextX, closestTextY);
   
  } 
  drawYText();
}
//Melissa is the best bug tester ever

public void drawDay(int day){
  
  if (day == 8){
    for (int row =0; row < rowCount; row++){
      fill(0xff373EFF);
      textFont(titleFont); 
      textAlign(CENTER,TOP);
      text("RAGBRAI 2014 Overview", width/2,10); 
      printDayStats();
      //drawDataHighlight(row);
 
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
    beginShape();
      for (int row =0; row < rowCount; row++){
        
        //float elapsedDist = dataTable.getFloat(row,6);
        drawDataHighlight(row);
        
        if (dataTable.getFloat(row,0) == day){
          
          
          float elevation = dataTable.getFloat(row,3);
          float currentDist = dataTable.getFloat(row,6);
          float x = map(currentDist, startDistKM,finalDistKM,padXL,width-padXR);
          float y = map(elevation,dayMin,dayMax,height-padYBtm,0+padYTop); // (0,0) is top left corner thus y is "inverted"
          if (dataTable.getFloat(row,0) == 1){
            printDayStats();
            stroke(0xff0000FF);
            printTitle(row);
          } else if (dataTable.getFloat(row,0) == 2){
            printDayStats();
            stroke(0xff0080FF);
            printTitle(row);
          } else if (dataTable.getFloat(row,0) == 3){
            printDayStats();
            stroke(0xff00FFFF);
            printTitle(row);
          } else if (dataTable.getFloat(row,0) == 4){
            printDayStats();
            stroke(0xff00FF00);
            printTitle(row);
          } else if (dataTable.getFloat(row,0) == 5){
            printDayStats();
            stroke(0xffFFFF00);
            printTitle(row);
          } else if (dataTable.getFloat(row,0) == 6){
            printDayStats();
            stroke(0xffFF8000);
            printTitle(row);
          } else if (dataTable.getFloat(row,0) == 7){
            printDayStats();
            stroke(0xffFF0000);
            printTitle(row);
          }
          //noStroke(); 
          curveVertex(x,y); 
      }
    } 
    endShape();
  }
}

public void keyPressed(){
  if (key == ' '){
    day = day+1; //Day 8 is the overview
    resetDailyStats();
    if (day == 9){
      day =1;
    }
    //Recalculate max and min elevation, and start/end dist each time spacebar is pressed. 
    updateDailyStats();
  }
  delay(100); 
}

public void mouseClicked(){
  if (millis()-lastTime>1000){ //added 5 second delay betewwn mouse clicks 
    day = day+1; //Day 8 is the overview
    resetDailyStats();
    if (day == 9){
      day =1;
    }
    //Recalculate max and min elevation, and start/end dist each time the mouse is clicked. 
    updateDailyStats();
    lastTime = millis();
  }
}

public void printDayStats(){
  
 fill(0);
  
  if (day ==8) {
    textFont(bodyFont);
    smooth();
    textAlign(LEFT); 
    text("Total Distance: "+nf(kmToMi(totalDistKM),0,2)+" mi",75,75);
    text("Total Feet of Climb: "+nf(metersToFt(totalMClimb),0,2)+" ft",75,100);
    text("Each Color Represents a Day",425,75);
    text("Left Click to See Individual Days",425,100);
    
  } else{
    textFont(bodyFont);
    smooth();
    textAlign(LEFT); 
    text("Distance: "+nf(kmToMi(dayDistKM),0,2)+" mi",75,75);
    text("Max height: "+ nf(metersToFt(dayMax),0,2)+" ft", 75,100);
    text("Total feet of climb: " + nf(metersToFt(dayMClimb),0,2)+" ft", 75, 125);
    text("Max sustained grade: " + nf(dayGradeMax,0,2)+"%", 425, 75);
    text("Min sustained grade: " + nf(dayGradeMin,0,2)+"%", 425, 100);
    text("Mouse Over Graph to see Additional Information",425,125);
  }
  noFill();
}

public float kmToMi(float km){
  return km*0.621371f; //i.e 1 mi = 0.621371 km 
}

public float metersToFt(float meters){
  return meters*3.28084f; //i.e. 1 meter = 3.28084 ft
}

public void printTitle(int row){
  
  if (dataTable.getFloat(row,0) == 1){
            fill(0xff0000FF);
          } else if (dataTable.getFloat(row,0) == 2){
            fill(0xff0080FF);
          } else if (dataTable.getFloat(row,0) == 3){
            fill(0xff00FFFF);
          } else if (dataTable.getFloat(row,0) == 4){
            printDayStats();
            fill(0xff00FF00);
          } else if (dataTable.getFloat(row,0) == 5){
            printDayStats();
            fill(0xffFFFF00);
          } else if (dataTable.getFloat(row,0) == 6){
            printDayStats();
            fill(0xffFF8000);
          } else if (dataTable.getFloat(row,0) == 7){
            printDayStats();
            fill(0xffFF0000);
          }
  textFont(titleFont); 
  textAlign(CENTER,TOP);
  text("Day " + day, width/2,10); 
  noFill();
}

public void drawXLabels(){
  fill(0);
  textFont(bodyFont);
  textSize(20);
  textAlign(CENTER, TOP);
  
  //Use thin, gray lines to draw the grid. 
  //stroke(255);
  //strokeWeight(1);

  if (day == 8){
    int interval = round(kmToMi(totalDistKM)/5);//5 x axis labels 
    int xCoord =0; 
    for ( int i =0; i < 6; i++){
      float x = map(xCoord,0,kmToMi(totalDistKM),padXL,width-padXR);
      text(xCoord,x,height-40);
      xCoord = xCoord+interval;
      //line(xCoord,height-padYTop,x,height-padYTop);
      text("Distance in Miles", width/2, height-25);
    }
  } else {  
    int interval = round(kmToMi(dayDistKM)/5);//5 x axis labels 
    int xCoord =0;
    for ( int i =0; i < 6; i++){
      float x = map(xCoord,0,kmToMi(dayDistKM),padXL,width-padXR);
      text(xCoord,x,height-40);
      xCoord = xCoord+interval;
      //line(xCoord,height-padYTop,x,height-padYTop);
      text("Distance in Miles", width/2, height-25);
    }
  }
}

public void drawAxisLines(){
  stroke(0);
  strokeWeight(2);
  line(padXL-5,height-padYBtm+5,padXL-5,padYTop);
  line(padXL-5,height-padYBtm+5,width-padXR+15,height-padYBtm+5);
}  

public void drawYLabels() {
  fill(0);
  textFont(bodyFont);
  textSize(20);
  textAlign(CENTER,BASELINE);
  
  //Use thin, gray lines to draw the grid. 
  //stroke(255);
  //strokeWeight(1);

  if (day == 8){
    int interval = round(metersToFt(eleMax-eleMin)/3);  //Gives 4 Y axis markers
    int yCoord = (int)metersToFt(eleMin);
    for ( int i =0; i < 4; i++){
      float y = map(yCoord,metersToFt(eleMin),metersToFt(eleMax),height-padYBtm,padYTop);
      text(yCoord,padXL-25,y+5);
      yCoord = yCoord+interval;
      
    }
  } else {  
    int interval = round(metersToFt(dayMax-dayMin)/3); //Gives 4 Y axis markers 
    int yCoord =(int) metersToFt(dayMin);
    for ( int i =0; i < 4; i++){
      float y = map(yCoord,metersToFt(dayMin),metersToFt(dayMax),height-padYBtm,padYTop);
      text(yCoord,padXL-25,y+5);
      yCoord = yCoord+interval;
    }
  }
} 

public void resetDailyStats(){
  //Reset daily values
    startDistKM = 1000; 
    finalDistKM =0;
    dayMax= MIN_FLOAT; 
    dayMin = MAX_FLOAT;
    dayMClimb = 0;
    dayGradeMin = MAX_FLOAT;
    dayGradeMax = MIN_FLOAT;
}

public void updateDailyStats(){
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
      
      //Calculate total climb
      float value3 = dataTable.getFloat(row,3);
      if (row < rowCount-1){
        if (value3 < dataTable.getFloat(row+1,3)){
          dayMClimb = dayMClimb +(dataTable.getFloat(row+1,3)-value3);
        }
      }
      //Calculate daily max/min grade
      float value4 = dataTable.getFloat(row,8);
      if (value4 > dayGradeMax){
        dayGradeMax = value4;
      }
      if (value4 < dayGradeMin){
        dayGradeMin = value4;
      }
    }
  }
}

public void drawYText(){
  fill(0);
  textFont(bodyFont);   
  textSize(20);  
  translate(25,(height+padYTop-25)/2);  
  rotate(3*PI/2);               
  textAlign(CENTER);            
  text("Elevation in Feet",0,0);
  
}

public void drawDataHighlight(int row){
  float ele = dataTable.getFloat(row,3);
  float slope = dataTable.getFloat(row,8);
  float distance = dataTable.getFloat(row,6);
  float x = map(distance, startDistKM,finalDistKM,padXL,width-padXR);
  float y = map(ele,dayMin,dayMax,height-padYBtm,0+padYTop);
  float d = dist(mouseX,mouseY,x,y);
  if ((d < 15) && (d < closestDist)){
    point(x,y);
    closestDist = d; 
    closestTextX = x-50; 
    closestTextY = y-50; 
    closestText = nf(metersToFt(ele),0,2)+"ft\n("+nf(slope,1,2)+"%)";
  }
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
