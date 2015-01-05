package com.example.Demo_Subj;

/**
 * Created by Jürgen on 01.01.2015.
 */


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import java.io.IOException;

public class Sound {

    private MediaPlayer mediaplayer;
    private Context ctx;

    public Sound(Context context){
        mediaplayer = new MediaPlayer();
        ctx = context;
    }

    public void startSound(int soundRes){                                                      //Ton abspielen
        Uri path = Uri.parse("android.resource://com.example.Demo_Subj/" + soundRes);
        mediaplayer = new MediaPlayer();
        mediaplayer.reset();
        try{
            mediaplayer.setDataSource(ctx, path);
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

                AudioManager mAudioManager = (AudioManager)ctx.getSystemService(ctx.AUDIO_SERVICE);
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume (AudioManager.STREAM_MUSIC), 0);

                while (mediaplayer.getCurrentPosition() != mediaplayer.getDuration()) {

                }
                mediaplayer.start();
                mediaplayer.setVolume(1,1);
            }
        });
        sound.start();
    }
}
