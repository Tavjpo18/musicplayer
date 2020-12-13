package com.example.serviceboundmusic;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyService extends Service {
    private MyPlayer myPlayer;
    private IBinder binder;
    public boolean Playing;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("ServiceDemo", "Đã gọi onCreate()");

        myPlayer = new MyPlayer(this);
        binder = new MyBinder(); // do MyBinder được extends Binder

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("ServiceDemo", "Đã gọi onBind()");
        myPlayer.play();
        // trả về đối tượng binder cho ActivityMain
        return binder;

    }
    public boolean isPlaying(){
        return myPlayer.playing();
    }
    // Kết thúc một Service
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("ServiceDemo", "Đã gọi onBind()");
        myPlayer.stop();
        return super.onUnbind(intent);
    }

    // Xây dựng các phương thức thực hiện nhiệm vụ,
    // ở đây mình demo phương thức tua bài hát
    public void quickForward(){

        myPlayer.quickForward(60000); // tua đến giây thứ 120
    }
    public void quickStart(){

        myPlayer.quickStart();
    }
    public void pause(){
        myPlayer.pause();
    }

    public class MyBinder extends Binder {

        // phương thức này trả về đối tượng MyService
        public MyService getService() {

            return MyService.this;
        }
    }

}
// Xây dựng một đối tượng riêng để chơi nhạc
class MyPlayer {
    // đối tượng này giúp phát một bài nhạc
    private MediaPlayer mediaPlayer;

    public MyPlayer(Context context) {
        // Nạp bài nhạc vào mediaPlayer
        mediaPlayer = MediaPlayer.create(
                context, R.raw.ttbdty);
        // Đặt chế độ phát lặp lại liên tục
        mediaPlayer.setLooping(true);
    }

    public void quickForward(int pos){
        //mediaPlayer.seekTo(pos);
//        mediaPlayer.pause();
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + pos);

    }
    public void quickStart(){
        mediaPlayer.start();
    }

    // phát nhạc
    public void play() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }else {
            mediaPlayer.reset();
        }
    }
    public boolean playing() {
        return mediaPlayer.isPlaying();
    }


    // dừng phát nhạc
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }
    public void pause() {
        mediaPlayer.pause();
    }

}
