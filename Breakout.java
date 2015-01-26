//Breakout.java
//Yasin Avci
//A simple version of the game Breakout. It has a menu, the ability to choose different balls, background music,
//3 levels, ability to use lasers, a cheat to raise score, a penalty if the cheat option is abused, lives, and scoring.
//The point of the game is to hit all the bricks. The ball bounces off objects, but is also affected by the
//movement of the paddle.

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Breakout extends JFrame implements ActionListener, MouseListener, MouseMotionListener{
	javax.swing.Timer myTimer;
	GamePanel game;
	BreakoutMenu bm;
	String place = ""; //filename of ball
	int oldX; //old x coordinate of paddle
	int newX; //new x coordinate of paddle

	int direction = -1; //direction of board
	int dist = 0; //how much the ball moves
	int angle = (int)(Math.random()*50+30); //the angle the ball moves at
	int pointx = 450; //points for line animation coming from paddle
	int pointy = 490;
	int lives = 3; //# of lives
	int goneBricks = 0; ////counter for the number of bricks hit
	int currLevel = 1; //level user is on
	boolean win = false; //flag to tell if won
	boolean lose = false; //flag to tell if lost
	int score = 0; //total score of user

 	int boxx = 450; //paddle coordinates
	int boxy = 490;
	double ballx = boxx+25; //ball coordinates
	double bally = 470;

	Rectangle[] brick;
	int brickx = 95;//brick coordinates
 	int bricky = 90;
	 //level brick placement
 	String [] levelInfo = {"0010000101010010100000000010000100000000011100111", "0010000101010010100100100001001001110011100000000", "010101010000000001010101010001010000000000000000000", "001111110010000100100001001000010011001100010010000000000", "0010000100100001001111110000110000001100000011000"};
 	int brickNum = 0; //number of bricks in the level

 	Rectangle ball = new Rectangle((int)ballx, (int)bally, 20, 20); //area of ball
 	Rectangle box = new Rectangle(boxx, boxy, 70, 20); //area of paddle

	public Breakout(){
		super("BreakOut of The Unknown Jail");
		setLayout(null);
		bm = new BreakoutMenu(this);

		addMouseListener(this);
		addMouseMotionListener(this);
		setSize(1200,640);
		game = new GamePanel();
		game.setSize(1200,640);
		game.setLocation(0,0);
		add(game);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myTimer = new javax.swing.Timer(10, this); // trigger every 10 ms
		myTimer.start();
	}

	public void actionPerformed(ActionEvent evt){
		Object source = evt.getSource();
		game.moveBall();
		game.updatePaddleCoor();
		place = bm.getnewBall(); //the file name for the ball that is chosen from the menu

  		for (int i = 0; i < brick.length; i++) {
    		if (brick[i] != null) {
     			if (brick[i].intersects(ball)) { //checks to see if ball hit the brick
     				if (ball.x>=brick[i].x-7&&ball.x<=(brick[i].x+brick[i].width-7)){
     					angle = 360 - angle; //will bounce off top/bottom
     				}
     				else{
     					angle = 180 - angle; //will bounce off sides
     				}
      				brick[i] = null;
      				if (lose==false){
      					score += 10; //10 points for each brick broken
      				}
      				goneBricks +=1;
      				if (goneBricks == brickNum){ //if all are hit
      					if (currLevel == levelInfo.length){ //if last level
      						if (lose == false){ //not already lost
      							win = true;
      						}
      						break;
      					}
      					else{
      						currLevel += 1; //moves on to next level

      						bricky = 90;		//resetting everything back
      						brickx = 90;
      						brickNum = 0;
      						game.setUpBricks();
      						goneBricks = 0;

							ball.x = box.x+25;
							ball.y = 470;
							dist = 0;
      					}
      				}
     			}
    		}
   		}
//		System.out.println(angle);
		while (angle<0){			//angle is lowered/increased so the if statement directly below can work for all cases
			angle = angle+360;
	//		System.out.println("HIHGHERED");
		}
		while (angle>360){
			angle = angle-360;
	//		System.out.println("LOWERED");
		}
   		if (angle>85  && angle<105 || angle>-105 && angle<-85){ //to get the ball not to move just up and down
   			angle = angle+20;
  //		System.out.println("check");
   		}

   		game.moveLasers(); //function to move lasers
	}

	public void mouseClicked(MouseEvent evt){
	}

	public void mouseEntered(MouseEvent evt){
	}

	public void mouseExited(MouseEvent evt){
	}

	public void mousePressed(MouseEvent evt){
	}

	public void mouseReleased(MouseEvent evt){
		game.fireLasers(); //once clicked, they're fired
	}

	public void mouseDragged(MouseEvent evt){
		game.moveWithMouse(evt.getX()); //getting the x-coor so paddle moves w/ mouse
	}

	public void mouseMoved(MouseEvent evt){
		game.moveWithMouse(evt.getX());
	}


	class GamePanel extends JPanel implements KeyListener{
		private int laserx, lasery; //coordinates for the laser
		private boolean [] keys;
		Image boxPic;
		Image bgPic;
		Image playPic;

		public GamePanel(){
			super();
			setFocusable(true);
			grabFocus();

			boxPic = new ImageIcon("boxx.jpg").getImage(); //getting images
			bgPic = new ImageIcon("bgFit.png").getImage();
			playPic = new ImageIcon("space.png").getImage();

			laserx = -50; //so they are off the screen
			lasery = -50;
			addKeyListener(this);
			keys = new boolean[2000];

			newX = box.x; //initially starts w/ the position of the paddle


			setUpBricks(); //sets up the positions for the bricks of the first level
		}

		public void updatePaddleCoor(){ //used for changing the angle of the ball below
			oldX = newX;
			newX = box.x;
		}

		public void moveBall(){
			double newballx = ball.x+(dist*Math.cos(Math.toRadians(angle)));
			double newbally = ball.y+(dist*Math.sin(Math.toRadians(angle)));
			if (newbally < 50 || newbally > 470 || newballx < 50 || newballx > 830){ //when hitting
				ball.y = (int)newbally;
				ball.x = (int)newballx;
				if (newballx < 50 || newballx > 825){ //bounce off sides
					angle = 180 - angle;
					if (newballx < 50){ //resets the ball
						ball.x = 50;
					}
					if (newballx > 850){ //resets the ball
						ball.x = 830;
					}
				}

				else if (newbally < 50){ //bounce off top
					angle = 360 - angle;
					if (newbally < 50){
						ball.y = 50;
					}
				}

				else if (ball.intersects(box) && (newbally+20) < 500) {
					angle = 360 - angle;
					if (newX-oldX>0 && newX-oldX<11){ //moving the paddle when the ball contacts it affects the angle it will bouce off at
						angle = angle+10;
					}
					else{
						angle = angle+20;
					}
					if (newX-oldX<0 && newX-oldX>-11){
						angle = angle-10;
					}
					else{
						angle = angle-20;
					}
				}

				else if (newbally > 530){ //reset back to start
					ball.x = box.x+25;
					ball.y = 470;
					dist = 0;
					lives -=1;
					if (lives<0){ //no lives = you lose
						lose = true;
					}
				}

			}
			else{
				ball.y = (int)newbally; //sets new coor for ball
				ball.x = (int)newballx;
			}

			repaint();
		}

		public void moveWithMouse(int x){
			if (x+70<850 && x>=50){ //moves within the boundaries
				box.x = x;
				repaint();
			}
		}

		public void moveLasers(){
			if (lasery-5>50){
				lasery -= 5; //goes up until the boundary
			}
			else{
				resetLasers();
			}
			for (int i = 0; i < brick.length; i++) {
    			if (brick[i] != null) { //lasers hit bricks
     				if (brick[i].intersects(laserx, lasery, 1, 30) || brick[i].intersects(laserx + 30, lasery, 1, 30)){
      					brick[i] = null; //the following code is the same as above
      					if (lose==false){
      						score += 10;
      					}
      					goneBricks +=1;
      					if (goneBricks == brickNum){
      						if (currLevel == levelInfo.length){
      							if (lose == false){
      								win = true;
      							}
      							break;
      						}
      						else{
      							currLevel += 1;
      							brickNum = 0;          //resetting everything back

      							bricky = 90;
      							brickx = 90;
      							game.setUpBricks();
      							goneBricks = 0;

								ball.x = box.x+25;
								ball.y = 470;
								dist = 0;
      						}
      					}
      					resetLasers();
	     			}
    			}
   			}

			repaint();
		}

		public void fireLasers(){
			if (lasery-30<50 && dist!=0){
				laserx = box.x+20; //starts at the paddle
				lasery = box.y-1;
			}
		}

		public void resetLasers(){ //once fired, goes back off screen
			lasery = -50;
			laserx = -50;
		}

		public void setUpBricks(){ //sets up the brick positions
			brick = new Rectangle[levelInfo[currLevel-1].length()]; //number of levels for each set of bricks in list

			String temp = levelInfo[currLevel-1]; //temporary contents of the current level
			for (int i = 0; i < levelInfo[currLevel-1].length(); i++) {
				if (i+1 == levelInfo[currLevel-1].length()){ //when at last brick
					if (temp.substring(0).equals("1")){
						brick[i] = new Rectangle(brickx, bricky, 60, 30);
						brickNum += 1; //counting the number of bricks in the level
					}
				}
				else{
					if (temp.substring(0,1).equals("1")){
   						brick[i] = new Rectangle(brickx, bricky, 60, 30);
   						brickNum += 1;
					}
					temp = temp.substring(1);
				}

 	  			if (i%8 == 0) { //when there is a row of 8, the bricks will be placed on a new row
    				brickx = 0;
    				bricky += 40;
   				}
   				brickx += 95; //amount of space from one brick to another (horizontally)
  			}
		}

		public void paintComponent(Graphics g){
			g.drawImage(bgPic, 0, 0, this); //background
			g.drawImage(playPic, 50, 50, this); //play area
			g.setColor(Color.blue);
			g.fillRect(box.x, box.y, 70, 20); //paddle
			g.drawImage(boxPic, box.x, box.y, this);
			g.drawLine(pointx, pointy, box.x, box.y); //lines
			g.drawLine(pointx, pointy+19, box.x, box.y+19);

			g.setColor(Color.red);
			if (dist == 0){ //ball
				ball.x = box.x+25;
				g.fillOval((int)ball.x, (int)ball.y, 20, 20);
			}
			else{
				g.fillOval((int)ball.x, (int)ball.y, 20, 20);
			}

			g.setColor(Color.WHITE);

			if (win == true){ //win "screen"
				setFont(new Font("Berlin Sans FB", Font.BOLD, 100));
				g.drawString("YOU WIN!", 300, 150);
				g.drawString("YOUR SCORE: "+score, 150, 350);
				g.drawString("Now enjoy the music!", 100, 500);
			}
			else if(lose == true){ //lose "screen"
				setFont(new Font("Berlin Sans FB", Font.BOLD, 100));
				g.drawString("YOU LOSE!", 300, 150);
				g.drawString("YOUR SCORE: "+score, 150, 350);
				g.drawString("Now enjoy the music!", 100, 500);
			}
			else{ //stats
				setFont(new Font("ALgerian", Font.PLAIN, 30));
				g.drawString("Lives: "+Integer.toString(lives), 900, 100);
				g.drawString("Level: "+Integer.toString(currLevel), 900, 200);
				g.drawString("Score: "+Integer.toString(score), 900, 300);
			}

			g.setColor(Color.green);
			g.drawLine(laserx, lasery, laserx, lasery-30); //lasers
			g.drawLine(laserx+30, lasery, laserx+30, lasery-30);

			g.drawImage(new ImageIcon("Mar Pics\\"+place).getImage(), (int)ball.x, (int)ball.y, this); //image for ball

			g.setColor(Color.magenta);

			for (int i = 0; i < brick.length; i++) { //bricks
   				if (brick[i] != null && lose == false) {
    				g.fill3DRect(brick[i].x, brick[i].y, brick[i].width, brick[i].height, true);
   				}
 		    }
		}

		public void keyPressed(KeyEvent evt){
			int i = evt.getKeyCode();
			keys[i] = true;

			if (keys[KeyEvent.VK_SPACE] == true){ //start ball
				dist = -7;
				if (lose == true){ //goes crazy once user loses
					angle = 60;
					dist = -30;
				}
			}

			if (keys[KeyEvent.VK_Y] == true){ //bonus
				if (lose == false){
					score += (int)(Math.random()*9);
				}
				if (Integer.toString(score).substring(Integer.toString(score).length()-1).equals("7")){ //punishment for abusing the system and being unlucky
					score -= 100;
				}
			}
		}

		public void keyReleased(KeyEvent evt){
			int i = evt.getKeyCode();
			keys[i] = false;
		}

		public void keyTyped(KeyEvent evt){
		}


	}

	public static void main(String[]args){
		Breakout frame=new Breakout();
	}
}