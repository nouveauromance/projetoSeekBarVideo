package br.gov.sp.cps.seekbarvideo;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView; //player
    private SeekBar videoSeekBar;
    private TextView textTempo;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        videoSeekBar = findViewById(R.id.videoSeekBar);
        videoView = findViewById(R.id.videoView);
        textTempo = findViewById(R.id.textTempo);


        //Config para buscar o video e carregar no Videoview
        Uri videoUri = Uri.parse("android.resource://" +
                getPackageName() + "/" + R.raw.videoa);
        videoView.setVideoURI(videoUri);

        //Listener
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                //Atualiza seekBar
                handler.post(atualizaSeekBar);

                //Inicia o video automatimanete
                videoView.start();
            }
        });

        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    videoView.seekTo(progress);
                }
                atualizaTextTempo();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    //Atualizar o tempo do video e possição do seekBar
    private Runnable atualizaSeekBar = new Runnable(){
        public void run(){
            if (videoView.isPlaying()) {
                videoSeekBar.setProgress(videoView.getCurrentPosition());
                atualizaTextTempo();
            }
            handler.postDelayed(this, 1000);
        }
    };



    private void atualizaTextTempo(){
        int tempoAtual = videoView.getCurrentPosition();
        int tempoTotal = videoView.getDuration();

        String tempoFormatado = formataTempo(tempoAtual) + "/" +
                formataTempo(tempoTotal);
        textTempo.setText(tempoFormatado);
    }

    private String formataTempo(int tempo){
        int min = (tempo/1000) / 60;
        int sec = (tempo/1000) % 60;

        return String.format("%2d:%2d", min, sec);
    }

    //Metodo play
    public void startVideo(View view){
        videoView.start();
    }

    //Metodo pause
    public void pauseVideo(View view){
        videoView.pause();
    }
}