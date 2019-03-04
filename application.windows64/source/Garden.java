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

public class Garden extends PApplet {

Plot currentPlot;
Plot[][] plots = new Plot[5][5];
Button[] leafButtons = new Button[3];
Button[] flowerButtons = new Button[3];
Button saveButton;
Button clearButton;
int filesSaved;
int plotX;
int plotY;
int currentFlower;
int currentLeaf;
PGraphics pgPlant;
PGraphics[] pgLeaf = new PGraphics[5];
PGraphics[] pgFlower = new PGraphics[5];
int greenLeafColor, redLeafColor, yellowLeafColor;
int whiteFlowerColor, blueFlowerColor, pinkFlowerColor;  
int currentLeafColor, currentFlowerColor;

public void setup() {
  
  background(0xff2E4053);
  currentFlower = 0;
  currentLeaf = 0;
  plotX = 0;
  plotY = 0;
  filesSaved = 0;
  initSelector();
  for (int i = 0; i < 5; i++) {
    for (int j = 0; j < 5; j++) {
      plots[i][j] = new Plot(i * 100, j * 100);
      plots[i][j].drawPlot();
    }
  }
  plots[plotX][plotY].editing = true;
  pgPlant = createGraphics(500, 500);
  for (int i = 0; i < 5; i++) {
    pgLeaf[i] = createGraphics(500, 500);
    pgFlower[i] = createGraphics(500, 500);
  }
}

public void initSelector() {
  textSize(20);
  text("Leaf Color", 10, 530);
  text("Flower Color", 10, 600);
  greenLeafColor = 0xff0E6655;
  redLeafColor = 0xffFF5733;
  yellowLeafColor = 0xffFFC300;
  leafButtons[0] = new Button(10, 540, 50, 30, greenLeafColor, null);
  leafButtons[0].drawButton();
  leafButtons[0].selected = true;
  leafButtons[1] = new Button(80, 540, 50, 30, redLeafColor, null);
  leafButtons[1].drawButton();
  leafButtons[2] = new Button(150, 540, 50, 30, yellowLeafColor, null);
  leafButtons[2].drawButton();
  whiteFlowerColor = 0xffFFFFFF;
  blueFlowerColor = 0xffAED6F1;
  pinkFlowerColor = 0xffF5B7B1;
  flowerButtons[0] = new Button(10, 610, 50, 30, whiteFlowerColor, null);
  flowerButtons[0].drawButton();
  flowerButtons[0].selected = true;
  flowerButtons[1] = new Button(80, 610, 50, 30, blueFlowerColor, null);
  flowerButtons[1].drawButton();
  flowerButtons[2] = new Button(150, 610, 50, 30, pinkFlowerColor, null);
  flowerButtons[2].drawButton();
  currentLeafColor = leafButtons[currentLeaf].buttonColor;
  currentFlowerColor = flowerButtons[currentFlower].buttonColor;
  saveButton = new Button(260, 520, 200, 50, 0xffBCED91, "Save");
  clearButton = new Button(260, 590, 200, 50, 0xffBCED91, "Clear");
}

public void draw() {
   updateButtons();
   for (int i = 0; i < 5; i++) {
     for (int j = 0; j < 5; j++) {
         plots[i][j].display();
         if (plots[i][j].plant != null) {
           plots[i][j].plant.grow();
         }
     }
   }
   for (int i = 0; i < 3; i++) {
     leafButtons[i].display();
     flowerButtons[i].display();
   }
   saveButton.display();
   clearButton.display();
   image(pgPlant, 0, 0);
   for (int i = 0; i < 5; i++) {
     image(pgLeaf[i], 0, 0);
     image(pgFlower[i], 0, 0);
   }
}

public void updateButtons() {
  for (int i = 0; i < 3; i++) {
    if (leafButtons[i].overButton()) {
      leafButtons[i].mouseOver = true;
    } else {
      leafButtons[i].mouseOver = false;
    }
    if (flowerButtons[i].overButton()){
      flowerButtons[i].mouseOver = true;
    } else {
      flowerButtons[i].mouseOver = false;
    }
  }
  if (saveButton.overButton()) {
    saveButton.mouseOver = true;
  } else {
    saveButton.mouseOver = false;
  }
  if (clearButton.overButton()) {
    clearButton.mouseOver = true;
  } else {
    clearButton.mouseOver = false;
  }
}

public void keyPressed() {
  if (key == CODED) {
    if (keyCode == RIGHT && plotX < 4) {
        plots[plotX][plotY].editing = false;
        plotX += 1;
        plots[plotX][plotY].editing = true;
    } else if (keyCode == LEFT && plotX > 0) {
        plots[plotX][plotY].editing = false;
        plotX -= 1;
        plots[plotX][plotY].editing = true;
    } else if (keyCode == UP && plotY > 0) {
        plots[plotX][plotY].editing = false;
        plotY -= 1;
        plots[plotX][plotY].editing = true;
    } else if (keyCode == DOWN && plotY < 4) {
        plots[plotX][plotY].editing = false;
        plotY += 1;
        plots[plotX][plotY].editing = true;
    }
  } else if (key == ENTER) {
        plots[plotX][plotY].plant = new Plant((plotX * 100) + 45, (plotY * 100) + 50, currentLeafColor, currentFlowerColor, plotY);
  }
}

public void mousePressed() {
  for (int i = 0; i < 3; i++) {
    if (leafButtons[i].mouseOver) {
      leafButtons[currentLeaf].selected = false;
      currentLeaf = i;
      leafButtons[currentLeaf].selected = true;
      currentLeafColor = leafButtons[currentLeaf].buttonColor;
    }
    if (flowerButtons[i].mouseOver) {
      flowerButtons[currentFlower].selected = false;
      currentFlower = i;
      flowerButtons[currentFlower].selected = true;
      currentFlowerColor = flowerButtons[currentFlower].buttonColor;
    }
  }
  if (saveButton.mouseOver) {
    saveButton.selected = true;
    save("garden" + filesSaved +".png");
    saveButton.selected = false;
  }
  if (clearButton.mouseOver) {
    clearButton.selected = true;
    pgPlant.beginDraw();
    pgPlant.clear();
    pgPlant.endDraw();
    for (int i = 0; i < 5; i++) {
      pgLeaf[i].beginDraw();
      pgLeaf[i].clear();
      pgLeaf[i].endDraw();
      pgFlower[i].beginDraw();
      pgFlower[i].clear();
      pgFlower[i].endDraw();
    }
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        plots[i][j].plant = null;
      }
    } 
    clearButton.selected = false;
  }
}
//Plot
class Plot {
  float x;
  float y;
  Plant plant;
  boolean editing;
  int timer;
  
  Plot(float x, float y) {
    this.x = x;
    this.y = y;
    plant = null;
    editing = false;
  }
  
  public void display() {
    if (editing) {
      stroke(0xffFFFFFF);
      fill(0xffBCED91);
      rect(x, y - 1, 99, 98);
    } else {
      stroke(0xffBCED91);
      fill(0xffBCED91);
      rect(x, y-1, 100, 100);
    }
  }
  
  public void drawPlot() {
    stroke(0xffBCED91);
    fill(0xffBCED91);
    rect(x, y, 100, 100);
  }
}

//Plant
class Plant {
  int leafColor;
  int flowerColor;
  boolean growing;
  int timer;
  float bushSize;
  float x;
  float y;
  int layer;
  
  Plant(float x, float y, int leafColor, int flowerColor, int layer) {
    this.leafColor = leafColor;
    this.flowerColor = flowerColor;
    this.x = x;
    this.y = y;
    bushSize = 1;
    timer = 0;
    this.layer = layer;
    growing = true;
    initPlant(x, y);
  }
  
  public void initPlant(float x, float y) {
    pgPlant.beginDraw();
    pgPlant.stroke(0xffA04000);
    pgPlant.fill(0xffA04000);
    pgPlant.rect(x, y, 10, 20);
    pgPlant.endDraw();
  }
  
  public void grow() {
    pgLeaf[layer].beginDraw();
    pgLeaf[layer].stroke(leafColor);
    pgLeaf[layer].fill(leafColor);
    pgLeaf[layer].ellipse(x + 5, y - bushSize/2, bushSize, bushSize);
    pgLeaf[layer].endDraw();
    bushSize += .05f;
    timer++;
    if (timer % 100 == 0) {
      pgFlower[layer].beginDraw();
      pgFlower[layer].stroke(flowerColor);
      pgFlower[layer].fill(flowerColor);
      pgFlower[layer].ellipse(x + 5 + random(- bushSize / 2, bushSize / 2) * cos(PI/6), y - bushSize/2 + random(-bushSize / 2, bushSize/2) * sin(PI/6), 5, 5);
      pgFlower[layer].endDraw();
    }
  }
}

class Button {
  boolean selected;
  boolean mouseOver;
  int buttonColor;
  String text;
  float x;
  float y;
  float w;
  float h;
  Button(float x, float y, float w, float h, int buttonColor, String text) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
    this.text = text;
    selected = false;
    mouseOver = false;
    this.buttonColor = buttonColor;
  }
  
  public void display() {
    if (selected) {
      stroke(0xffFFFFFF);
      fill(buttonColor);
      rect(x, y, w, h);
      if (text != null) {
        fill(0xffFFFFFF);
        text(text, x + 75, y + 30);
      }
    } else {
      stroke(buttonColor);
      fill(buttonColor);
      rect(x, y, w, h);
      if (text != null) {
        fill(0xffFFFFFF);
        text(text, x + 75, y + 30);
      }
    }
  }
  
  public void drawButton() {
    stroke(buttonColor);
    fill(buttonColor);
    rect(x, y, w, h);
  }
  
  public boolean overButton()  {
  if (mouseX >= x && mouseX <= x+w && 
      mouseY >= y && mouseY <= y+h) {
    return true;
  } else {
    return false;
  }
}
}
  public void settings() {  size(500, 650); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Garden" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
