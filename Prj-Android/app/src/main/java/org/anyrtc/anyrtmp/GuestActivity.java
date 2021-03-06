package org.anyrtc.anyrtmp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.anyrtc.core.AnyRTMP;
import org.anyrtc.core.RTMPGuestHelper;
import org.anyrtc.core.RTMPGuestKit;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoRenderer;

/**
 * Created by Eric on 2016/9/16.
 */
public class GuestActivity extends Activity implements RTMPGuestHelper {
    private RTMPGuestKit mGuest = null;

    private TextView mTxtStatus = null;
    private SurfaceViewRenderer mSurfaceView = null;
    private VideoRenderer mRenderer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        {//* Init UI
            mTxtStatus = (TextView) findViewById(R.id.txt_rtmp_status);
            mSurfaceView = (SurfaceViewRenderer) findViewById(R.id.suface_view);
            mSurfaceView.init(AnyRTMP.Inst().Egl().getEglBaseContext(), null);
            mRenderer = new VideoRenderer(mSurfaceView);
        }

        {
            String rtmpUrl = getIntent().getExtras().getString("rtmp_url");
            mGuest = new RTMPGuestKit(this, this);
            mGuest.StartRtmpPlay(rtmpUrl, mRenderer.GetRenderPointer());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mGuest != null) {
            mGuest.StopRtmpPlay();
            mGuest.Clear();
            mGuest = null;
        }
    }


    /**
     * Implements for RTMPGuestHelper
     */
    @Override
    public void OnRtmplayerOK() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTxtStatus.setText("RTMP play OK!");
            }
        });
    }

    @Override
    public void OnRtmplayerStatus(final int cacheTime, final int curBitrate) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTxtStatus.setText(String.format("RTMP Cache(%d) network(%d)", cacheTime, curBitrate));
            }
        });
    }

    @Override
    public void OnRtmplayerCache(int time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }

    @Override
    public void OnRtmplayerClosed(int errcode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTxtStatus.setText("RTMP");
            }
        });
    }
}
