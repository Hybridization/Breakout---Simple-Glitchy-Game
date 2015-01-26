//Music.java
//Yasin Avci
//Music is played from this class. The following code is modified from the link below.

import sun.audio.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;

//used http://stackoverflow.com/questions/20811728/adding-music-sound-to-java-programs

public class Music{
public static void main(String[] args){
    music();
}

    public static void music(){
        AudioPlayer MGP = AudioPlayer.player;
        AudioStream BGM;
        AudioData MD;

        ContinuousAudioDataStream loop = null;

        try{
            InputStream test = new FileInputStream("Song\\#TheBiasedGoldFisherSpacemanLikesFreshElectricityOnMyOldSchoolCreakySleigh.wav");
            BGM = new AudioStream(test);
            AudioPlayer.player.start(BGM);

        }
        catch(FileNotFoundException e){
            System.out.print(e.toString());
        }
        catch(IOException error){
            System.out.print(error.toString());
        }
    }
}