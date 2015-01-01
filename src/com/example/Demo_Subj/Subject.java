package com.example.Demo_Subj;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by johannes on 22.11.2014.
 */

public class Subject {

    private MediaPlayer mediaplayer;

    private Intelligence intel;
    
    public Subject(){
        intel = new Intelligence();
        mediaplayer = new MediaPlayer();
    }

    /*
    private void startSound(int soundRes){
        Uri path = Uri.parse("android.resource://com.example.Demo_Subj/" + soundRes);
        mediaplayer = new MediaPlayer();
        mediaplayer.reset();
        try{
            mediaplayer.setDataSource(context, path);
        }catch (IOException e){
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        try{
            mediaplayer.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }catch (IllegalStateException e) {
            e.printStackTrace();
        }

        Thread sound = new Thread(new Runnable() {
            @Override
            public void run() {
            mediaplayer.start();
            mediaplayer.setVolume(1.0f, 1.0f);

            AudioManager mAudioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume (AudioManager.STREAM_MUSIC), 0);

            while (mediaplayer.getCurrentPosition() != mediaplayer.getDuration()) {

            }
            mediaplayer.start();
            mediaplayer.setVolume(1,1);
            }
        });
        sound.start();
    }*/

}
