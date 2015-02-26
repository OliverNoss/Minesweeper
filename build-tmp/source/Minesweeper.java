import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {

// Oliver Noss CompSci Block 4 


public final static int NUM_ROWS = 20;
public final static int NUM_COLS = 20;
public final static int NUM_BOMBS = 20;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs = new ArrayList <MSButton>(); //ArrayList of just the minesweeper buttons that are mined

public void setup ()
{
    size(400, 420);
    textAlign(CENTER,CENTER);
    
    // make the manager
    Interactive.make( this );
    
    buttons = new MSButton[NUM_ROWS][NUM_COLS];
    for (int i = 0; i < NUM_ROWS; i++) {
        for (int j = 0; j < NUM_COLS; j++) {
            buttons[i][j] = new MSButton(i,j);
        }
    }
    
    //declare and initialize buttons
    // for (int b = 0; b < 20; b++) {


        setBombs();
// }
}
public void setBombs()
{
    for (int i = 0; i < NUM_BOMBS; i++) {


        int row = (int)(Math.random()*20);
        int col = (int)(Math.random()*20);

        while (bombs.contains(buttons[row][col])){
           row = (int)(Math.random()*20);
           col = (int)(Math.random()*20);
       }
       bombs.add(buttons[row][col]);

   }
    //your code
}

public void draw ()
{
    
    /*if (keyPressed && key==' ')
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++){
                buttons[i][j].mousePressed();
            }
        }*/
        if(isWon()){
            displayWinningMessage();
        // System.out.println(1234567);
        // background( 0 );
    }
}
    public boolean isWon()
    {
        int count = 0;
    // for (MSButton temp : bombs)
    //     if (temp.isMarked())
    //         count++;
     for (int i = 0; i < NUM_ROWS; i++) {
        for (int j = 0; j < NUM_COLS; j++) {
           if (!bombs.contains(buttons[i][j]) && buttons[i][j].isClicked())
            count++;
        }
    }
    return 400-NUM_BOMBS==count;
}
public void displayLosingMessage()
{
    for (int i = 0; i < NUM_ROWS; i++) {
        for (int j = 0; j < NUM_COLS; j++) {
            if (bombs.contains(buttons[i][j]) && !buttons[i][j].isClicked())
                buttons[i][j].mousePressed();
        }
    }
    textSize(20);
    fill(255,0,0);
    text("You Lose.", 200, 408);
    // System.out.println("Lose1");
    noLoop();
      // text("You Lose.", 200,200);
      // System.out.println("Lose2"); 
}
public void displayWinningMessage()
{
     textSize(20);
    fill(0,100,0);
    text("You Win.", 200, 408);
          // System.out.println("Win1");

    noLoop();
        // text("You Win.", 200,200);
        //       System.out.println("Win2");
    }

public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 400/NUM_COLS;
        height = 400/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    
    public void mousePressed () 
    {
        if (mouseButton == LEFT && !marked)
            clicked = true;
        if (mouseButton == RIGHT && !clicked)
            marked =! marked;
        else if (bombs.contains(this)) {
            displayLosingMessage();
        }// }
        else if (countBombs(r,c)>0)
            label = nf(countBombs(r,c),1);
        else   
        {
            if (isValid(r+1,c+1))
                if (!buttons[r+1][c+1].isClicked()) 
                buttons[r+1][c+1].mousePressed();
            if (isValid(r-1,c-1))
                if (!buttons[r-1][c-1].isClicked()) 
                buttons[r-1][c-1].mousePressed();
            if (isValid(r+1,c-1))
                if (!buttons[r+1][c-1].isClicked()) 
                buttons[r+1][c-1].mousePressed();
            if (isValid(r-1,c+1))
                if (!buttons[r-1][c+1].isClicked()) 
                buttons[r-1][c+1].mousePressed();
            if (isValid(r,c+1))
                if (!buttons[r][c+1].isClicked()) 
                buttons[r][c+1].mousePressed();
            if (isValid(r+1,c))
                if (!buttons[r+1][c].isClicked()) 
                buttons[r+1][c].mousePressed();
            if (isValid(r,c-1))
                if (!buttons[r][c-1].isClicked()) 
                buttons[r][c-1].mousePressed();
            if (isValid(r-1,c))
                if (!buttons[r-1][c].isClicked()) 
                buttons[r-1][c].mousePressed();
        }
    }
    

    public void draw () 
    {    
        if (marked)
            fill(0);
        else if( clicked && bombs.contains(this) ) 
            fill(255,0,0);
        else if(clicked)
            fill( 200 );
        else 
            fill( 100 );

        rect(x, y, width, height);
        fill(0);
        textSize(12);
        text(label,x+width/2,y+height/2);
    }
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    public boolean isValid(int r, int c) 
    {
       return r>=0&&r<NUM_ROWS&&c>=0&&c<NUM_COLS;
   }
   public int countBombs(int row, int col)
   {   int numBombs = 0;
    // if (isValid(row, col)) {           
        if (isValid(row+1,col+1))
            if (bombs.contains(buttons[row+1][col+1])) {numBombs++;}
        if (isValid(row-1,col-1))
            if (bombs.contains(buttons[row-1][col-1])) {numBombs++;}
        if (isValid(row+1,col-1))
            if (bombs.contains(buttons[row+1][col-1])) {numBombs++;}
        if (isValid(row-1,col+1))
            if (bombs.contains(buttons[row-1][col+1])) {numBombs++;}
        if (isValid(row,col+1))
            if (bombs.contains(buttons[row][col+1])) {numBombs++;}
        if (isValid(row+1,col))
            if (bombs.contains(buttons[row+1][col])) {numBombs++;}
        if (isValid(row,col-1))
            if (bombs.contains(buttons[row][col-1])) {numBombs++;}
        if (isValid(row-1,col))
            if (bombs.contains(buttons[row-1][col])) {numBombs++;}

    // }
    return numBombs;
}
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
