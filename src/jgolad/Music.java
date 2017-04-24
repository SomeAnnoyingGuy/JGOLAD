package jgolad;

import java.io.File;
import java.io.IOException;
import java.lang.Thread;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Music implements Runnable{
	private final int BUFFER_SIZE = 128000;
    private File soundFile;
    private AudioInputStream audioStream;
    private AudioFormat audioFormat;
    private SourceDataLine sourceLine;
    private String[] songs;
    private Thread t;
    
    public Music(String[] queue){
    	this.songs = queue;
    }
    public void newSong(String[] queue){
    	stop();
		setQueue(queue);
		start();
    }
    public void run(){
    	
    	while(true){
    		for(String song:songs){
				try{
					playSound(song);
				}catch(Exception e){}
    		}
    	}
    }
    public void start(){
    	 if (t == null) {
             t = new Thread(this);
             t.start();
          }
    }
    public void stop(){  	
    	songs = new String[0];	
    	sourceLine.stop();
    	sourceLine = null;
    	t.interrupt();
    	t = null;
    }
    public void setQueue(String[] queue){
    	this.songs = queue;
    }
    public void playSound(String filename){

        String strFilename = filename;

        try {
            soundFile = new File(strFilename);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        audioFormat = audioStream.getFormat();

        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        sourceLine.start();

        int nBytesRead = 0;
        byte[] abData = new byte[BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioStream.read(abData, 0, abData.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
            }
        }

        sourceLine.drain();
        sourceLine.close();
    }

}
