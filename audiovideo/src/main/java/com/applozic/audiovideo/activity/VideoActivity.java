package com.applozic.audiovideo.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.applozic.mobicomkit.Applozic;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.twilio.video.CameraCapturer;

import applozic.com.audiovideo.R;

public class VideoActivity extends AudioCallActivityV2 {
    private static final String TAG = VideoActivity.class.getName();

    LinearLayout videoOptionlayout;


    public VideoActivity() {
        super(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Applozic logic = new Applozic(VideoActivity.this);

        if (Applozic.getTheme(VideoActivity.this).equalsIgnoreCase("white")) {
            setTheme(com.applozic.mobicomkit.uiwidgets.R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(com.applozic.mobicomkit.uiwidgets.R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        init();

        contactName = findViewById(R.id.contact_name);
        //profileImage = (ImageView) findViewById(R.id.applozic_audio_profile_image);
        txtCount = findViewById(R.id.applozic_audio_timer);

        contactName.setText(contactToCall.getDisplayName());
        pauseVideo = true;

        primaryVideoView = findViewById(R.id.primary_video_view);
        thumbnailVideoView = findViewById(R.id.thumbnail_video_view);

        videoStatusTextView = findViewById(R.id.video_status_textview);
        videoStatusTextView.setVisibility(View.GONE);

        connectActionFab = (FloatingActionButton) findViewById(R.id.call_action_fab);
        switchCameraActionFab = findViewById(R.id.switch_camera_action_fab);
        //    localVideoActionFab = (FloatingActionButton) findViewById(R.id.local_video_action_fab);
        mutevideo = findViewById(R.id.mutevideo);
        speakervideo = findViewById(R.id.speakervideo);
        videoOptionlayout = findViewById(R.id.video_call_option);
        FrameLayout frameLayout = findViewById(R.id.video_container);

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
            //    hideShowWithAnimation();
                return false;
            }
        });

        /*
         * Enable changing the volume using the up/down keys during a conversation
         */
        setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

        /*
         * Needed for setting/abandoning audio focus during call
         */
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        /*
         * Check camera and microphone permissions. Needed in Android M.
         */
        if (!checkPermissionForCameraAndMicrophone()) {
            requestPermissionForCameraAndMicrophone();
        } else {
            createAudioAndVideoTracks();
            intializeUI();
            initializeApplozic();
        }

    }

    private void hideShowWithAnimation() {

        //Camera Actions
        if (switchCameraActionFab.isShown()) {
            switchCameraActionFab.hide();
        } else {
            switchCameraActionFab.show();

        }
        //Mute Actions
        if (mutevideo.isShown()) {
            mutevideo.hide();
        } else {
            mutevideo.show();

        }

        /*if (localVideoActionFab.isShown()) {
            localVideoActionFab.hide();
        } else {
            localVideoActionFab.show();
        }*/

        if (speakervideo.isShown()) {
            speakervideo.hide();
        } else {
            speakervideo.show();
        }
    }

    @Override
    public void initializeApplozic() {
        super.initializeApplozic();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*
     * The initial state when there is no active conversation.
     */
    @Override
    protected void setDisconnectAction() {
        super.setDisconnectAction();
        if (isFrontCamAvailable(getBaseContext())) {
            switchCameraActionFab.show();
            switchCameraActionFab.setOnClickListener(switchCameraClickListener());
        } else {
            switchCameraActionFab.hide();
        }
        /*localVideoActionFab.show();
        localVideoActionFab.setOnClickListener(localVideoClickListener());*/
    }

    private View.OnClickListener switchCameraClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (cameraCapturer != null) {
                        CameraCapturer.CameraSource cameraSource = cameraCapturer.getCameraSource();
                        cameraCapturer.switchCamera();
                        if (thumbnailVideoView.getVisibility() == View.VISIBLE) {
                            thumbnailVideoView.setMirror(cameraSource == CameraCapturer.CameraSource.BACK_CAMERA);
                        } else {
                            primaryVideoView.setMirror(cameraSource == CameraCapturer.CameraSource.BACK_CAMERA);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private View.OnClickListener localVideoClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * Enable/disable the local video track
                 */
                if (localVideoTrack != null) {
                    boolean enable = !localVideoTrack.isEnabled();
                    localVideoTrack.enable(enable);
                    int icon;
                    if (enable) {
                       // icon = R.drawable.ic_videocam_green_24px;
                        switchCameraActionFab.show();
                    } else {
                     //   icon = R.drawable.ic_videocam_off_red_24px;
                        switchCameraActionFab.hide();
                    }
                    /*localVideoActionFab.setImageDrawable(
                            ContextCompat.getDrawable(VideoActivity.this, icon));*/
                }
            }
        };
    }

    public boolean isFrontCamAvailable(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

}
