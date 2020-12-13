package com.example.serviceboundmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.IpSecManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.serviceboundmusic.MyService.MyBinder;

import java.text.SimpleDateFormat;
import java.time.Duration;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private MyService myService;
    private boolean isBound = false;
    private ServiceConnection connection;
    SeekBar seekBar;
    TextView tvstart, tvend ;
    CircleImageView circleImageView;
    ObjectAnimator animator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton play = (ImageButton) findViewById(R.id.imgbtnplay);
        final ImageButton pause = (ImageButton) findViewById(R.id.imgbtnPause);
        final ImageButton moveon = (ImageButton) findViewById(R.id.imgbtnmoveon);
        final Button start =(Button) findViewById(R.id.btnStart);
        circleImageView = findViewById(R.id.imgviewdia);
//        tvstart = findViewById(R.id.tvstart);
//        tvend = findViewById(R.id.tvstart);
        seekBar = findViewById(R.id.sb);
        animation();


        // Khởi tạo ServiceConnection
        connection = new ServiceConnection() {

            // Phương thức này được hệ thống gọi khi kết nối tới service bị lỗi
            @Override
            public void onServiceDisconnected(ComponentName name) {

                isBound = false;
            }

            // Phương thức này được hệ thống gọi khi kết nối tới service thành công
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyBinder binder = (MyBinder) service;
                myService = binder.getService(); // lấy đối tượng MyService
                isBound = true;
            }
        };

        // Khởi tạo intent
        final Intent intent =
                new Intent(MainActivity.this,
                MyService.class);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Bắt đầu một service sủ dụng bind
                if (isBound){
                    myService.quickStart();
                    animator.start();

                }
                // Đối thứ ba báo rằng Service sẽ tự động khởi tạo
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Nếu Service đang hoạt động
                if(isBound){
                    // Tắt Service
                    myService.pause();
                    animator.cancel();
                }
            }
        });

        moveon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // nếu service đang hoạt động
                if(isBound){
                    // tua bài hát
                    myService.quickForward();
                    Toast.makeText(myService,"tua 1 phut", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,
                            "Service chưa hoạt động", Toast.LENGTH_SHORT).show();
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isBound){
                    bindService(intent, connection,
                            Context.BIND_AUTO_CREATE);
                    animator.start();
                }else{
                    unbindService(connection);
                    isBound = false;
                    animator.end();

                }
            }
        });

    }
//    private void Time(){
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
//    }
    private void animation() {
        animator = ObjectAnimator.ofFloat(circleImageView, "rotation", 360f,0f );
        animator.setDuration(10000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
    }
}