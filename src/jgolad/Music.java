package jgolad;

import java.io.FileNotFoundException;
import java.io.IOException;
//import java.net.URL;
import java.io.File;
import java.lang.Thread;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Music implements Runnable{

	private String filename;
	
    private AudioInputStream audioStream = null;
    private AudioFormat audioFormat = null;
    private SourceDataLine sourceLine = null;
    
    private volatile boolean stopFlag = false;
    private volatile boolean pauseFlag = false;
    private volatile boolean isPlayingFlag = false;
    private volatile float volume_dB = 0.0f;
    
    private Thread player = new Thread(this);
    String[] playlist;
    
    public Music(){}
    public Music(String[] playlist)
    {
    	this.playlist = playlist;
    }
    
    public void playSound() throws FileNotFoundException, UnsupportedAudioFileException, IOException, LineUnavailableException
    {
    	isPlayingFlag = true;
    	
        if (filename.toLowerCase().endsWith(".txt"))
        {
        	System.out.println("Text Files Not Supported!");
        }
        else //if (filename.toLowerCase().endsWith(".mp3") || filename.toLowerCase().endsWith(".ogg"))
        {
        	
        	//final URL fileurl = new URL("file:///"+filename);
        	File sound = new File(filename);
        	if(!sound.exists()){
        		return;
        	}
            final AudioInputStream in = AudioSystem.getAudioInputStream(sound);

            final AudioFormat baseFormat = in.getFormat();
            audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate(),
                    16,
                    baseFormat.getChannels(),
                    baseFormat.getChannels() * 2,
                    baseFormat.getSampleRate(),
                    false);
              
            audioStream = AudioSystem.getAudioInputStream(audioFormat, in);        
            final byte[] data = new byte[4096];  
            try{
                SourceDataLine res = null;
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
                res = (SourceDataLine) AudioSystem.getLine(info);
                res.open(audioFormat);
                sourceLine = res;
                onPlay();
                sourceLine.start();
                int nBytesRead = 0;// nBytesWritten = 0;
                while ((nBytesRead != -1) && (!stopFlag))
                {
                	if(!pauseFlag)
                	{
                		isPlayingFlag = true;
	                    nBytesRead = audioStream.read(data, 0, data.length);
	                    if (nBytesRead != -1)
	                    {
	                    	if (sourceLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) 
	                    	{
	                            ((FloatControl) sourceLine.getControl(FloatControl.Type.MASTER_GAIN)).setValue(volume_dB);
	                        }
	                    	sourceLine.write(data, 0, nBytesRead);
	                        //nBytesWritten = sourceLine.write(data, 0, nBytesRead);
	                        //System.out.println("... -->" + data[0] + " bytesWritten:" + nBytesWritten);
	                    }
                	}
                	else
                	{
                		isPlayingFlag = false;
                	}
                	if(stopFlag){
                		throw new IOException();
                	}
                }         

                sourceLine.drain();
                sourceLine.stop();
                sourceLine.close();
                audioStream.close();
            }catch(LineUnavailableException e){
                e.printStackTrace();
            }
            in.close();
            
            isPlayingFlag = false;
            onStop();
        	 	
        }
    }
	public void run() 
	{
		stopFlag = false;
		pauseFlag = false;
		
		try {
			while(true){
				for(String song:playlist){
					filename = song;
					playSound();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}	
	}
	
	public void play() 
	{
		if(player.isAlive()){
			player.interrupt();
		}
		player = new Thread(this);
		player.start();
	}
	
	public void onPlay(){}
	
	public void pause() 
	{
		pauseFlag = true;
		onPause();
	}
	
	public void onPause(){}
	
	public void resume() 
	{
		pauseFlag = false;
		onResume();
	}
	
	public void onResume(){}
	
	public void stop() 
	{	pauseFlag = true;
		isPlayingFlag=false;
		stopFlag = true;
		player.interrupt();
	}
	
	public void onStop(){
		
	}
	
	public boolean isPlaying()
	{
		return isPlayingFlag;
	}
	
	public boolean isPaused()
	{
		return pauseFlag;
	}
	
	public void setFile(String filename)
    {
    	this.filename = filename;
    }
	public void setList(String[] playlist){
		this.playlist = playlist;
	}
	
	public void setVolume(Float volume)
	{
		volume = volume==null ? 1.0F : volume;
		volume = volume<=0.0F ? 0.0001F : volume;
		setVolumeInDecibels((float)( 20.0*(Math.log(volume)/Math.log(10.0)) ));
	}
	
	public void setVolumeInDecibels(Float decibels)
	{
		decibels = decibels==null ? 0.0F : decibels;
		this.volume_dB = decibels;
	}
}