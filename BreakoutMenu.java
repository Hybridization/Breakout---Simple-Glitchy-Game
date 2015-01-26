//BreakoutMenu.java
//Yasin Avci
//Place where user can choose their ball of choice and press the Play button. The music is started from here
//from the Music.java class.


import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;



class BreakoutMenu extends JFrame implements ActionListener{
   	JButton [] butns = new JButton[4];
    String []pics = {"Blue Mushroom.png","Flower.png","Red Mushroom.png",
    				"Star.png"};
    Breakout mainplay;
    Music msic;
    JButton done;
    String chosenBall = ""; //file name of ball user has chosen to play with


    public  BreakoutMenu(Breakout main){
    	super("Breakout Menu");
		mainplay = main;
		msic.music(); //plays music
    	ImageIcon [] picIcon = new ImageIcon[4];
    	setLayout(null);

    	for(int i = 0; i<4;i++){
    		Image img = Toolkit.getDefaultToolkit().getImage("Mar Pics\\"+pics[i]);
    		picIcon[i] = new ImageIcon(img.getScaledInstance(40,40,Image.SCALE_SMOOTH));
    		butns[i] = new JButton(picIcon[i]);
    		butns[i].addActionListener(this);
    		butns[i].setSize(40,40);
    		if (i>1){
	   			butns[i].setLocation(i*120 + 20, 100);
    		}
    		else{
    			butns[i].setLocation((i+2)*120 + 20, 30);
    		}
    		add(butns[i]);
    	}

    	done = new JButton("PLAY");
    	done.addActionListener(this);
    	done.setSize(100,30);
    	done.setLocation(50, 70);
    	add(done);

    	setSize (500,200);
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		setVisible (true);
	}

    public void actionPerformed (ActionEvent evt)
    {
		Object source = evt.getSource ();
		for(int i=0; i<4; i++)
			if(butns[i] == source){
				JOptionPane.showMessageDialog (null, "You have chosen the "+pics[i].substring(0,pics[i].length()-3));

				for (int j=0; j<4; j++){
					if (pics[j] == chosenBall){
						butns[j].setEnabled(true); //if another ball is chosen then it highlights again
					}
				}
				chosenBall = pics[i];
				butns[i].setEnabled(false);
			}
		if(source == done){
			mainplay.setVisible(true);
			setVisible(false);
		}

    }

    public String getnewBall(){
    	return chosenBall;
    }

    public static void main(String []args)
    {
    	BreakoutMenu bm = new BreakoutMenu(null);

	}

}
