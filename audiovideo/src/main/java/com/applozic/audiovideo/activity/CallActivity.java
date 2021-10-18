package com.applozic.audiovideo.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.MobiComKitConstants;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.conversation.MessageIntentService;
import com.applozic.mobicomkit.api.conversation.MobiComMessageService;
import com.applozic.mobicomkit.api.notification.VideoCallNotificationHelper;
import com.applozic.mobicomkit.broadcast.BroadcastService;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.contact.BaseContactService;
import com.applozic.mobicommons.commons.image.ImageLoader;
import com.applozic.mobicommons.people.contact.Contact;
import com.squareup.picasso.Picasso;

import applozic.com.audiovideo.R;

import static com.applozic.mobicomkit.api.notification.VideoCallNotificationHelper.CALL_MISSED;


public class CallActivity extends Activity {

    private static final String TAG = CallActivity.class.getName();

    BaseContactService baseContactService;
    MobiComMessageService messageService;
    ImageLoader mImageLoader;
    boolean responded;
    Contact contact;
    String inComingCallId;
    boolean isAudioOnly;
    Vibrator vibrator;
    Ringtone r;
    private BroadcastReceiver applozicBroadCastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Applozic logic = new Applozic(CallActivity.this);

        if (Applozic.getTheme(CallActivity.this).equalsIgnoreCase("white")) {
            setTheme(com.applozic.mobicomkit.uiwidgets.R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(com.applozic.mobicomkit.uiwidgets.R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.call_received);
        Log.i(TAG, "Reached CallActivity");

        //Notifications and Vibrations...
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 1000, 1000};
        vibrator.vibrate(pattern, 0);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        setRinging(true);
        baseContactService = new AppContactService(this);
        messageService = new MobiComMessageService(this, MessageIntentService.class);
        Intent intent = getIntent();

        //// contactId /////////
        final String contactId = intent.getStringExtra("CONTACT_ID");
        inComingCallId = intent.getStringExtra(VideoCallNotificationHelper.CALL_ID);
        isAudioOnly = intent.getBooleanExtra(VideoCallNotificationHelper.CALL_AUDIO_ONLY, false);

        Log.i(TAG, "contactId: " + contactId + ", inComingCallId: " + inComingCallId + ", isAudioOnly: " + isAudioOnly);

        contact = baseContactService.getContactById(contactId);

        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shaking_ani);
        shake.setRepeatCount(Animation.INFINITE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        ImageView accept = findViewById(R.id.alarmlistitem_acceptButton);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    responded = true;

                    Class activityClass = isAudioOnly ? AudioCallActivityV2.class : VideoActivity.class;
                    Intent intent = new Intent(getApplicationContext(), activityClass);
                    intent.putExtra("CONTACT_ID", contactId);
                    intent.putExtra("INCOMING_CALL", Boolean.TRUE);
                    intent.putExtra("CALL_ID", inComingCallId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    vibrator.cancel();
                    if (r.isPlaying()) {
                        r.stop();
                    }
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        accept.startAnimation(shake);

        ImageView reject = findViewById(R.id.alarmlistitem_rejectButton);
        ImageView profileImage = findViewById(R.id.notification_profile_image);
        TextView textView = findViewById(R.id.notification_user_name);
        TextView callType = findViewById(R.id.call_type);

        if (isAudioOnly) {
            callType.setText(R.string.incoming_audio_call);
            callType.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_call_white_24px), null, null, null);
        }

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectCall();
            }
        });

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!responded) {
                        Log.i(TAG, "Rejecting call due to responded being false.");
                        responded = true;
                        vibrator.cancel();
                        if (r.isPlaying()) {
                            r.stop();
                        }
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, VideoCallNotificationHelper.MAX_NOTIFICATION_RING_DURATION);

      /*  mImageLoader = new ImageLoader(this, profileImage.getHeight()) {
            @Override
            protected Bitmap processBitmap(Object data) {
                return baseContactService.downloadContactImage(CallActivity.this, (Contact) data);
            }
        };*/
        /*mImageLoader.setLoadingImage(R.drawable.applozic_ic_contact_picture_180_holo_light);
        // Add a cache to the image loader
        mImageLoader.setImageFadeIn(false);
        mImageLoader.loadImage(contact, profileImage);*/

        if (contact != null) {
            if (!TextUtils.isEmpty(contact.getImageURL())) {
                Picasso.get()
                        .load(contact.getImageURL())
                        .placeholder(com.applozic.mobicomkit.uiwidgets.R.drawable.applozic_group_icon)
                        .error(com.applozic.mobicomkit.uiwidgets.R.drawable.applozic_group_icon)
                        .into(profileImage);
            }

        }
        textView.setText(contact.getDisplayName() + "Calling you...");
        applozicBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String callId = intent.getStringExtra(VideoCallNotificationHelper.CALL_ID);
                boolean isNotificationForSameId = (inComingCallId.equals(callId));
                if ((CALL_MISSED.equals(intent.getAction()) ||
                        MobiComKitConstants.APPLOZIC_VIDEO_CALL_REJECTED.equals(intent.getAction()) ||
                        MobiComKitConstants.APPLOZIC_VIDEO_CALL_ANSWER.equals(intent.getAction()))
                        && isNotificationForSameId) {
                    responded = true;
                    vibrator.cancel();
                    if (r.isPlaying()) {
                        r.stop();
                    }
                    finish();
                }
            }
        };
        registerForBroadcast();
    }

    private void rejectCall() {
        try {
            responded = true;
            VideoCallNotificationHelper helper = new VideoCallNotificationHelper(CallActivity.this, isAudioOnly);
            helper.sendVideoCallReject(contact, inComingCallId);
            MobiComUserPreference.getInstance(CallActivity.this).setNewMessageFlag(true);
            vibrator.cancel();
            if (r.isPlaying()) {
                r.stop();
            }
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void registerForBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(VideoCallNotificationHelper.CALL_CANCELED);
        intentFilter.addAction(MobiComKitConstants.APPLOZIC_VIDEO_CALL_REJECTED);
        intentFilter.addAction(MobiComKitConstants.APPLOZIC_VIDEO_CALL_ANSWER);
        intentFilter.addAction(CALL_MISSED);
        LocalBroadcastManager.getInstance(this).registerReceiver(applozicBroadCastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        setRinging(false);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(applozicBroadCastReceiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        rejectCall();
        setRinging(false);
    }


    public void setRinging(boolean ringing) {
        BroadcastService.callRinging = ringing;
    }

}