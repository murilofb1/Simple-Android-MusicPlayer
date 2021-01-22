package com.murilofb.mediaplayer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private TextView txtPassTime;
    private TextView txtDuration;
    private SeekBar seekDuration;
    private Button btnPlay;
    private Button btnPause;
    private Button btnStop;

    private SimpleDateFormat mmSS = new SimpleDateFormat("mm:ss");

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private long musicDuration;
    private Handler handler = new Handler();
    private Runnable runnable;

    private boolean changeSeek = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlay = findViewById(R.id.btn_play);
        btnPause = findViewById(R.id.btn_pause);
        btnStop = findViewById(R.id.btn_stop);
        seekDuration = findViewById(R.id.seek_vol);
        txtPassTime = findViewById(R.id.txtPassTime);
        txtDuration = findViewById(R.id.txtDuration);

        runnable = new Runnable() {
            @Override
            public void run() {
                //Set Progress on Seekbar
                if (changeSeek) {
                    seekDuration.setProgress((int) TimeUnit.MILLISECONDS.toSeconds(mediaPlayer.getCurrentPosition()));
                }
                //Handler post delay for 0.5 sec
                handler.postDelayed(this, 500);
            }
        };
        //O Runnable muda a seekbar ao decorrer da musica e o valor do texto de duration é igual ao valor da seekbar
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.panic_dont_threathen);

        musicDuration = mediaPlayer.getDuration();
        txtDuration.setText(mmSS.format(musicDuration));

        seekDuration.setProgress(0);
        seekDuration.setMax((int) TimeUnit.MILLISECONDS.toSeconds(musicDuration));

        seekDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                txtPassTime.setText(mmSS.format(TimeUnit.SECONDS.toMillis(seekBar.getProgress())));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                changeSeek = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //      Log.i("AudioPlayer", String.valueOf(seekPosition));
                mediaPlayer.seekTo((int) TimeUnit.SECONDS.toMillis(seekBar.getProgress()));
                changeSeek = true;
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                setPlay();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPause();
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStop();
            }
        });
    }


    public void setPlay() {
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.panic_dont_threathen);
        mediaPlayer.seekTo((int) TimeUnit.SECONDS.toMillis(seekDuration.getProgress()));
        mediaPlayer.start();
        handler.postDelayed(runnable, 0);
    }

    public void setStop() {
        mediaPlayer.stop();
        seekDuration.setProgress(0);
        handler.removeCallbacks(runnable);
    }

    public void setPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        handler.removeCallbacks(runnable);
    }
}