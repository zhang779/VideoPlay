package drag.mandala.com.componentapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.github.mzule.activityrouter.router.Routers;

import java.util.ArrayList;
import java.util.List;

import drag.mandala.com.componentapp.application.MyApplication;

public class MainActivity extends AppCompatActivity
{
    public  HttpProxyCacheServer proxy; //視頻緩存代理
    private VideoView videoView;
    TextView buttonLoginActivityrouter ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView=findViewById(R.id.videoView);
        buttonLoginActivityrouter = findViewById(R.id.btn_login_activityrouter);

        playVideoOne();

        buttonLoginActivityrouter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Routers.open(MainActivity.this,"modulea://LoginActivity?test=you+are+best");
            }
        });
    }
    int index;
    public void playVideoOne() {

        proxy = new HttpProxyCacheServer.Builder(MainActivity.this)

                .maxCacheSize(1024 * 1024 * 1024) //1Gb 緩存

                .maxCacheFilesCount(5)//最大緩存5個視頻

                .build();

        List<String> urlList=new ArrayList<>();

        urlList.add("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");
        urlList.add("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");
        urlList.add("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4");

        int urlSize = urlList.size();

        index = index % urlSize;

        MediaController mediaController = new MediaController(this);

        //缓存起来这个 url 视频 提供缓存文件的名称默认情况下，AndroidVideoCache使用视频网址的MD5作为文件名


        String proxyUrl = proxy.getProxyUrl(urlList.get(index));

        videoView.setMediaController(mediaController);//如果设置Controller 点击时会有可交互模式，暂停快进或快退；不设置的话更适用于广告，不可交互，只进行展示

        videoView.setVideoPath(proxyUrl); //為videoview設置播放路徑

        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override

            public void onCompletion(MediaPlayer mPlayer) {

                index++;

                playVideoOne();// 監聽視頻一的播放完成事件，播放完畢就播放視頻二 递归

            }

        });

    }

    /**
     * 在onResume()方法控制视频继续播放
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (videoView != null) {
            videoView.seekTo(videoCurrentPosition);
            videoView.start();
        }
    }

    int videoCurrentPosition;
    /**
     * onPause() 控制视频暂停播放
     */
    @Override
    protected void onPause() {
        super.onPause();

        if (videoView != null) {
            videoCurrentPosition = videoView.getCurrentPosition();
            videoView.pause();
        }
    }
}
