import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import javax.swing.JFileChooser; 
import java.io.BufferedWriter; 
import java.io.FileWriter; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_140416d extends PApplet {





float [] p1x = new float[0]; // hold the mouse pressed marks
float [] p1y = new float[0];
float [] p2x = new float[0]; // hold the mouse pressed marks
float [] p2y = new float[0];
int count=0;  //to count all rectangles
PImage img;
PrintWriter output;
FileWriter fout;
int rect_x1; // catch the start dragging point x
int rect_y1; // catch the start dragging point y
int rect_x2; // record  moving mouseX
int rect_y2; // record moving mouseY


boolean press, release, drag, drawRect;

public void setup() {
//  output = createWriter("positions.txt");  //createWrite cannot append text
  File f = new File(dataPath("AOIs.txt"));
  if(!f.exists()){
    createFile(f);
  }
  try {
    output = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));
  }catch (IOException e){
      e.printStackTrace();
  }
  smooth();
  size(1024, 768);
  stroke(255);
  fill(255, 255, 255, 10);
  JFileChooser chooser = new JFileChooser();
  chooser.setFileFilter(chooser.getAcceptAllFileFilter());
  int returnVal = chooser.showOpenDialog(null);
  if (returnVal == JFileChooser.APPROVE_OPTION) {
    String filename = chooser.getSelectedFile().getName();
    String filePath = chooser.getSelectedFile().getAbsolutePath();
    println("<<<" + filename + ">>> AOIs");
    output.print('\n'+filename+'\t');
    img = loadImage(filePath);
  }
}

public void draw() {
  background(50); 
  image(img, 0, 0);
  Rect();
}

public void keyPressed() {
  if (key==ESC) {
    key=0;
    println("FILE CLOSED");
    output.close();
    exit();
  }
}

public void Rect() {

  float sizex = rect_x2 - rect_x1;
  float sizey = rect_y2 - rect_y1; 
  for (int i=0; i<count; i++) {
    beginShape();
    vertex(p1x[i], p1y[i]);
    vertex(p2x[i], p1y[i]);
    vertex(p2x[i], p2y[i]);
    vertex(p1x[i], p2y[i]);
    endShape(CLOSE);
  }
  if (mousePressed && mouseButton == LEFT) {
    rect(rect_x1, rect_y1, sizex, sizey);
  }
}

public void mousePressed() {
  p1x= append(p1x, mouseX);
  p1y= append(p1y, mouseY);
  rect_x1 = mouseX;
  rect_y1 = mouseY;
  mouseDragged(); // Reset vars
}

public void mouseReleased() {
  p2x= append(p2x, mouseX);
  p2y= append(p2y, mouseY);
  rect_x2 = mouseX;
  rect_y2 = mouseY;
  System.out.format("%d, %d, %d, %d \n", rect_x1, rect_y1, rect_x2, rect_y2);
  output.print(count + ":[" + rect_x1 + "," + rect_y1 + "," + rect_x2 + "," + rect_y2 + "]\t");
  count++;
}

public void mouseDragged() {
  rect_x2 = mouseX;
  rect_y2 = mouseY;
}


public void createFile(File f){
  File parentDir = f.getParentFile();
  try{
    parentDir.mkdirs(); 
    f.createNewFile();
  }catch(Exception e){
    e.printStackTrace();
  }
}    
  

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_140416d" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
