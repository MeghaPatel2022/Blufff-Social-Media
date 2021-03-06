package com.applozic.mobicomkit.uiwidgets.conversation.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NavUtils;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.ApplozicMqttService;
import com.applozic.mobicomkit.api.MobiComKitConstants;
import com.applozic.mobicomkit.api.account.register.RegisterUserClientService;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.account.user.User;
import com.applozic.mobicomkit.api.attachment.FileClientService;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MessageIntentService;
import com.applozic.mobicomkit.api.conversation.MobiComMessageService;
import com.applozic.mobicomkit.api.conversation.service.ConversationService;
import com.applozic.mobicomkit.api.notification.MuteNotificationAsync;
import com.applozic.mobicomkit.api.notification.MuteNotificationRequest;
import com.applozic.mobicomkit.api.notification.MuteUserNotificationAsync;
import com.applozic.mobicomkit.api.people.UserIntentService;
import com.applozic.mobicomkit.broadcast.BroadcastService;
import com.applozic.mobicomkit.broadcast.ConnectivityReceiver;
import com.applozic.mobicomkit.channel.database.ChannelDatabaseService;
import com.applozic.mobicomkit.channel.service.ChannelService;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.contact.BaseContactService;
import com.applozic.mobicomkit.feed.ApiResponse;
import com.applozic.mobicomkit.uiwidgets.AlCustomizationSettings;
import com.applozic.mobicomkit.uiwidgets.ApplozicSetting;
import com.applozic.mobicomkit.uiwidgets.ContactsChangeObserver;
import com.applozic.mobicomkit.uiwidgets.Pref.SharedPref;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.MessageCommunicator;
import com.applozic.mobicomkit.uiwidgets.conversation.MobiComKitBroadcastReceiver;
import com.applozic.mobicomkit.uiwidgets.conversation.adapter.ChatMediaAdapter;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.AudioMessageFragment;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.ConversationFragment;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.MobiComQuickConversationFragment;
import com.applozic.mobicomkit.uiwidgets.conversation.fragment.MultimediaOptionFragment;
import com.applozic.mobicomkit.uiwidgets.instruction.ApplozicPermissions;
import com.applozic.mobicomkit.uiwidgets.instruction.InstructionUtil;
import com.applozic.mobicomkit.uiwidgets.people.fragment.ProfileFragment;
import com.applozic.mobicomkit.uiwidgets.uilistener.ALStoragePermission;
import com.applozic.mobicomkit.uiwidgets.uilistener.ALStoragePermissionListener;
import com.applozic.mobicomkit.uiwidgets.uilistener.CustomToolbarListener;
import com.applozic.mobicomkit.uiwidgets.uilistener.MobicomkitUriListener;
import com.applozic.mobicommons.ALSpecificSettings;
import com.applozic.mobicommons.ApplozicService;
import com.applozic.mobicommons.commons.core.utils.PermissionsUtils;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.file.FileUtils;
import com.applozic.mobicommons.json.GsonUtils;
import com.applozic.mobicommons.people.SearchListFragment;
import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.channel.Conversation;
import com.applozic.mobicommons.people.contact.Contact;
import com.bumptech.glide.Glide;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.github.angads25.toggle.model.ToggleableView;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;


/**
 * Created by devashish on 6/25/2015.
 */
public class ConversationActivity extends AppCompatActivity implements MessageCommunicator, MobiComKitActivityInterface, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ActivityCompat.OnRequestPermissionsResultCallback, MobicomkitUriListener, SearchView.OnQueryTextListener, OnClickReplyInterface, ALStoragePermissionListener, CustomToolbarListener {

    public static final int LOCATION_SERVICE_ENABLE = 1001;
    public static final String TAKE_ORDER = "takeOrder";
    public static final String CONTACT = "contact";
    public static final String CHANNEL = "channel";
    public static final String CONVERSATION_ID = "conversationId";
    public static final String GOOGLE_API_KEY_META_DATA = "com.google.android.geo.API_KEY";
    public static final String ACTIVITY_TO_OPEN_ONCLICK_OF_CALL_BUTTON_META_DATA = "activity.open.on.call.button.click";
    protected static final long UPDATE_INTERVAL = 500;
    protected static final long FASTEST_INTERVAL = 1;
    private static final String LOAD_FILE = "loadFile";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private static final String API_KYE_STRING = "AIzaSyBJTGqodfOE5PY6UQEVCtqKAIyinB7k_Cc";
    private static final String CAPTURED_IMAGE_URI = "capturedImageUri";
    private static final String CAPTURED_VIDEO_URI = "capturedVideoUri";
    private static final String SHARE_TEXT = "share_text";
    public static final String CONTACTS_GROUP_ID = "CONTACTS_GROUP_ID";
    private static Uri capturedImageUri;
    private static String inviteMessage;
    private static int retry;
    public Contact contact;
    Integer parentGroupKey;
    String parentClientGroupKey;
    public LinearLayout layout;
    public boolean isTakePhoto;
    public boolean isAttachment;
    public Integer currentConversationId;
    public Snackbar snackbar;
    protected ConversationFragment conversation;
    protected MobiComQuickConversationFragment quickConversationFragment;
    protected MobiComKitBroadcastReceiver mobiComKitBroadcastReceiver;
    protected ActionBar mActionBar;
    protected GoogleApiClient googleApiClient;
    String geoApiKey;
    String activityToOpenOnClickOfCallButton;
    int resourceId;
    RelativeLayout childFragmentLayout;
    ProfileFragment profilefragment;
    MobiComMessageService mobiComMessageService;
    AlCustomizationSettings alCustomizationSettings;
    ConnectivityReceiver connectivityReceiver;
    File mediaFile;
    File profilePhotoFile;
    SyncAccountStatusAsyncTask accountStatusAsyncTask;
    String contactsGroupId;
    private LocationRequest locationRequest;
    private Channel channel;
    private BaseContactService baseContactService;
    private ApplozicPermissions applozicPermission;
    private Uri videoFileUri;
    private Uri imageUri;
    private ConversationUIService conversationUIService;
    private SearchView searchView;
    private String searchTerm;
    private SearchListFragment searchListFragment;
    ContactsChangeObserver observer;
    private LinearLayout serviceDisconnectionLayout;
    private ALStoragePermission alStoragePermission;

    private ImageView conversationContactPhoto;
    private TextView toolbarTitle;
    private TextView toolbarSubtitle;
    ImageView ic_call, ic_video,ic_more;
    private boolean isActivityDestroyed;
    AppContactService appContactService;

    Contact Singlecontact, savedcontact;
    Channel savedchannel;
    long millisecond;

    private ChatMediaAdapter chatMediaAdapter;

    public ConversationActivity() {

    }

    public static void addFragment(FragmentActivity fragmentActivity, Fragment fragmentToAdd, String fragmentTag) {
        if (fragmentActivity.isFinishing() || (fragmentActivity instanceof ConversationActivity && ((ConversationActivity) fragmentActivity).isActivityDestroyed)) {
            return;
        }
        if (Utils.hasJellyBeanMR1()) {
            if (fragmentActivity.isDestroyed()) {
                return;
            }
        }
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();

        // Fragment activeFragment = UIService.getActiveFragment(fragmentActivity);
        FragmentTransaction fragmentTransaction = supportFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.layout_child_activity, fragmentToAdd,
                fragmentTag);

        if (supportFragmentManager.getBackStackEntryCount() > 1
                && !ConversationUIService.MESSGAE_INFO_FRAGMENT.equalsIgnoreCase(fragmentTag) && !ConversationUIService.USER_PROFILE_FRAMENT.equalsIgnoreCase(fragmentTag)) {
            supportFragmentManager.popBackStackImmediate();
        }

        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
        //Log.i(TAG, "BackStackEntryCount: " + supportFragmentManager.getBackStackEntryCount());
    }

    public static Uri getCapturedImageUri() {
        return capturedImageUri;
    }

    public static void setCapturedImageUri(Uri capturedImageUri) {
        ConversationActivity.capturedImageUri = capturedImageUri;
    }

    @Override
    public void showErrorMessageView(String message) {
        try {
            layout.setVisibility(View.VISIBLE);
            snackbar = Snackbar.make(layout, message, Snackbar.LENGTH_LONG);
            snackbar.setAction(this.getString(R.string.ok_alert), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.setDuration(Snackbar.LENGTH_LONG);
            ViewGroup group = (ViewGroup) snackbar.getView();
            TextView textView = group.findViewById(R.id.snackbar_action);
            textView.setTextColor(Color.YELLOW);
            group.setBackgroundColor(getResources().getColor(R.color.error_background_color));
            TextView txtView = group.findViewById(R.id.snackbar_text);
            txtView.setMaxLines(5);
            snackbar.show();
        } catch (Exception e) {

        }
    }

    @Override
    public void retry() {
        retry++;
    }

    @Override
    public int getRetryCount() {
        return retry;
    }

    public void dismissErrorMessage() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Applozic.disconnectPublish(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Applozic.connectPublish(this);
        if (!Utils.isInternetAvailable(getApplicationContext())) {
            String errorMessage = getResources().getString(R.string.internet_connection_not_available);
            showErrorMessageView(errorMessage);
        }
    }

    @Override
    protected void onPause() {
        //ApplozicMqttService.getInstance(this).unSubscribe();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(CONTACT, contact);
        savedInstanceState.putSerializable(CHANNEL, channel);
        savedInstanceState.putSerializable(CONVERSATION_ID, currentConversationId);

        if (capturedImageUri != null) {
            savedInstanceState.putString(CAPTURED_IMAGE_URI, capturedImageUri.toString());
        }
        if (videoFileUri != null) {
            savedInstanceState.putString(CAPTURED_VIDEO_URI, videoFileUri.toString());
        }
        if (mediaFile != null) {
            savedInstanceState.putSerializable(LOAD_FILE, mediaFile);
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isFromSearch()) {
            return true;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (upIntent != null && isTaskRoot()) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                }
                ConversationActivity.this.finish();
                return true;
            }
            Boolean takeOrder = getIntent().getBooleanExtra(TAKE_ORDER, false);
            if (takeOrder && getSupportFragmentManager().getBackStackEntryCount() == 2) {
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (upIntent != null && isTaskRoot()) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                }
                ConversationActivity.this.finish();
                return true;
            } else {
                // getSupportFragmentManager().popBackStack();
                ConversationActivity.this.finish();
            }
            Utils.toggleSoftKeyBoard(this, true);
            return true;
        } else {
            super.onSupportNavigateUp();
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Applozic logic = new Applozic(ConversationActivity.this);

        if (Applozic.getTheme(ConversationActivity.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }


        super.onCreate(savedInstanceState);
        ApplozicService.initWithContext(getApplication());
        String jsonString = FileUtils.loadSettingsJsonFile(getApplicationContext());
        if (!TextUtils.isEmpty(jsonString)) {
            alCustomizationSettings = (AlCustomizationSettings) GsonUtils.getObjectFromJson(jsonString, AlCustomizationSettings.class);
        } else {
            alCustomizationSettings = new AlCustomizationSettings();
        }
        if (!TextUtils.isEmpty(alCustomizationSettings.getChatBackgroundImageName())) {
            resourceId = getResources().getIdentifier(alCustomizationSettings.getChatBackgroundImageName(), "drawable", getPackageName());
        }
        if (resourceId != 0) {
            getWindow().setBackgroundDrawableResource(resourceId);
        }
        setContentView(R.layout.quickconversion_activity);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        conversationContactPhoto = findViewById(R.id.conversation_contact_photo);
        toolbarTitle = myToolbar.findViewById(R.id.toolbar_title);
        toolbarSubtitle = myToolbar.findViewById(R.id.toolbar_subtitle);
        ic_call = findViewById(R.id.ic_call);
        ic_video = findViewById(R.id.ic_video);
        ic_more = findViewById(R.id.ic_more);
        ic_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("mn13contact", "call click");
                if (Singlecontact != null) {
                    if (Singlecontact.isBlocked()) {
                        conversation.userBlockDialog(false, Singlecontact, false);
                    } else {
                        processCall(Singlecontact, currentConversationId);
                    }
                }


            }
        });

        ic_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Singlecontact != null) {
                    if (Singlecontact.isBlocked()) {
                        conversation.userBlockDialog(false, Singlecontact, false);
                    } else {
                        processVideoCall(Singlecontact, currentConversationId);
                    }
                }

            }
        });

        ic_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMoreOption();
            }
        });

        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Log.d("mn13applozic", Applozic.getTheme(ConversationActivity.this) + "");


      /*  myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);

        if (Applozic.getTheme(ConversationActivity.this).equalsIgnoreCase("black")) {*/
        myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        /*} else {
            myToolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        }*/


        //

        baseContactService = new AppContactService(this);
        conversationUIService = new ConversationUIService(this);
        mobiComMessageService = new MobiComMessageService(this, MessageIntentService.class);
        quickConversationFragment = new MobiComQuickConversationFragment();
        connectivityReceiver = new ConnectivityReceiver();
        geoApiKey = Utils.getMetaDataValue(getApplicationContext(), GOOGLE_API_KEY_META_DATA);
        activityToOpenOnClickOfCallButton = Utils.getMetaDataValue(getApplicationContext(), ACTIVITY_TO_OPEN_ONCLICK_OF_CALL_BUTTON_META_DATA);
        layout = findViewById(R.id.footerAd);
        applozicPermission = new ApplozicPermissions(this, layout);
        childFragmentLayout = findViewById(R.id.layout_child_activity);
        profilefragment = new ProfileFragment();
        profilefragment.setAlCustomizationSettings(alCustomizationSettings);
        contactsGroupId = MobiComUserPreference.getInstance(this).getContactsGroupId();
        serviceDisconnectionLayout = findViewById(R.id.serviceDisconnectionLayout);

        if (!Utils.isDebugBuild(this) && ALSpecificSettings.getInstance(this).isLoggingEnabledForReleaseBuild()) {
            showLogWarningForReleaseBuild();
        }

        if (Utils.hasMarshmallow() && (!alCustomizationSettings.isGlobalStoagePermissionDisabled() || ALSpecificSettings.getInstance(this).isTextLoggingEnabled())) {
            applozicPermission.checkRuntimePermissionForStorage();
        }

       /* mActionBar = getSupportActionBar();
        if (!TextUtils.isEmpty(alCustomizationSettings.getThemeColorPrimary()) && !TextUtils.isEmpty(alCustomizationSettings.getThemeColorPrimaryDark())) {
            mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(alCustomizationSettings.getThemeColorPrimary())));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.parseColor(alCustomizationSettings.getThemeColorPrimaryDark()));
            }
        }*/
        inviteMessage = Utils.getMetaDataValue(getApplicationContext(), SHARE_TEXT);
        retry = 0;
        if (getIntent() != null) {
            parentClientGroupKey = getIntent().getStringExtra(ConversationUIService.PARENT_CLIENT_GROUP_ID);
            if (!TextUtils.isEmpty(parentClientGroupKey)) {
                parentGroupKey = ChannelService.getInstance(this).getParentGroupKeyByClientGroupKey(parentClientGroupKey);
            } else {
                parentGroupKey = getIntent().getIntExtra(ConversationUIService.PARENT_GROUP_KEY, 0);
            }
            if (parentGroupKey != null && parentGroupKey != 0) {
                BroadcastService.parentGroupKey = parentGroupKey;
                MobiComUserPreference.getInstance(this).setParentGroupKey(parentGroupKey);
            }
        }

        if (ApplozicClient.getInstance(this).isServiceDisconnected()) {
            serviceDisconnectionLayout.setVisibility(View.VISIBLE);
        } else {
            if (savedInstanceState != null) {
                capturedImageUri = savedInstanceState.getString(CAPTURED_IMAGE_URI) != null ?
                        Uri.parse(savedInstanceState.getString(CAPTURED_IMAGE_URI)) : null;
                videoFileUri = savedInstanceState.getString(CAPTURED_VIDEO_URI) != null ?
                        Uri.parse(savedInstanceState.getString(CAPTURED_VIDEO_URI)) : null;
                mediaFile = savedInstanceState.getSerializable(LOAD_FILE) != null ? (File) savedInstanceState.getSerializable(LOAD_FILE) : null;

                contact = (Contact) savedInstanceState.getSerializable(CONTACT);
                channel = (Channel) savedInstanceState.getSerializable(CHANNEL);
                currentConversationId = savedInstanceState.getInt(CONVERSATION_ID);
                if (contact != null || channel != null) {
                    if (channel != null) {
                        conversation = ConversationFragment.newInstance(null, channel, currentConversationId, null);
                    } else {
                        conversation = ConversationFragment.newInstance(contact, null, currentConversationId, null);
                    }
                    addFragment(this, conversation, ConversationUIService.CONVERSATION_FRAGMENT);
                }
            } else {
                setSearchListFragment(quickConversationFragment);
                addFragment(this, quickConversationFragment, ConversationUIService.QUICK_CONVERSATION_FRAGMENT);
            }
        }
        mobiComKitBroadcastReceiver = new MobiComKitBroadcastReceiver(this);
        InstructionUtil.showInfo(this, R.string.info_message_sync, BroadcastService.INTENT_ACTIONS.INSTRUCTION.toString());

        /*setToolbarTitle(getString(R.string.conversations));
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);*/

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        onNewIntent(getIntent());

        Boolean takeOrder = getIntent().getBooleanExtra(TAKE_ORDER, false);

        if (!takeOrder) {
            Intent lastSeenStatusIntent = new Intent(this, UserIntentService.class);
            lastSeenStatusIntent.putExtra(UserIntentService.USER_LAST_SEEN_AT_STATUS, true);
            UserIntentService.enqueueWork(this, lastSeenStatusIntent);
        }

        if (ApplozicClient.getInstance(this).isAccountClosed() || ApplozicClient.getInstance(this).isNotAllowed()) {
            accountStatusAsyncTask = new SyncAccountStatusAsyncTask(this, layout, snackbar);
            accountStatusAsyncTask.execute();
        }
        registerReceiver(connectivityReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        if (getIntent() != null) {
            Set<String> userIdLists = new HashSet<String>();
            if (getIntent().getStringArrayListExtra(ConversationUIService.GROUP_NAME_LIST_CONTACTS) != null) {
                MobiComUserPreference.getInstance(this).setIsContactGroupNameList(true);
                userIdLists.addAll(getIntent().getStringArrayListExtra(ConversationUIService.GROUP_NAME_LIST_CONTACTS));
            } else if (getIntent().getStringArrayListExtra(ConversationUIService.GROUP_ID_LIST_CONTACTS) != null) {
                MobiComUserPreference.getInstance(this).setIsContactGroupNameList(false);
                userIdLists.addAll(getIntent().getStringArrayListExtra(ConversationUIService.GROUP_ID_LIST_CONTACTS));
            }

            if (!userIdLists.isEmpty()) {
                MobiComUserPreference.getInstance(this).setContactGroupIdList(userIdLists);
            }
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mobiComKitBroadcastReceiver, BroadcastService.getIntentFilter());

        if (Applozic.getInstance(this).isDeviceContactSync()) {
            observer = new ContactsChangeObserver(null, this);
            getApplicationContext().getContentResolver().registerContentObserver(
                    ContactsContract.Contacts.CONTENT_URI, true, observer);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //setIntent(intent);
        if (!MobiComUserPreference.getInstance(this).isLoggedIn()) {
            //user is not logged in
            Utils.printLog(this, "AL", "user is not logged in yet.");
            return;
        }

        try {
            if (ApplozicClient.getInstance(this).isServiceDisconnected()) {
                serviceDisconnectionLayout.setVisibility(View.VISIBLE);
            } else {
                if (intent.getExtras() != null) {
                    BroadcastService.setContextBasedChat(intent.getExtras().getBoolean(ConversationUIService.CONTEXT_BASED_CHAT));
                    if (BroadcastService.isIndividual() && intent.getExtras().getBoolean(MobiComKitConstants.QUICK_LIST)) {
                        setSearchListFragment(quickConversationFragment);
                        addFragment(this, quickConversationFragment, ConversationUIService.QUICK_CONVERSATION_FRAGMENT);
                    } else {
                        conversationUIService.checkForStartNewConversation(intent);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openMoreOption(){
        final Dialog dialogblock;
        RelativeLayout rl_details,rl_options,rl_change_wall;
        LinearLayout ll_cancel;

        dialogblock = new Dialog(ConversationActivity.this);
        dialogblock.setContentView(R.layout.dialoge_more);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogblock.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        int dialogWindowHeight = (int) (displayHeight * 0.75f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        dialogblock.getWindow().setAttributes(layoutParams);

        dialogblock.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   dialogblock.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        rl_details = dialogblock.findViewById(R.id.rl_details);
        rl_options = dialogblock.findViewById(R.id.rl_options);
        rl_change_wall = dialogblock.findViewById(R.id.rl_change_wall);
        ll_cancel = dialogblock.findViewById(R.id.ll_cancel);

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogblock.isShowing() && dialogblock!=null)
                    dialogblock.dismiss();
            }
        });

        rl_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogblock.isShowing() && dialogblock!=null)
                    dialogblock.dismiss();
                openDetailsPage();
            }
        });

        rl_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rl_change_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (!dialogblock.isShowing()) {
            dialogblock.show();
        }
    }

    private void openDetailsPage(){
        final Dialog dialog = new Dialog(ConversationActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialoge_details);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.show();

        ImageView img_back;
        LabeledSwitch switch_mute_msg;
        LabeledSwitch switch_mute_video;
        TextView tv_see_all;
        RecyclerView rv_media;
        RelativeLayout rl_report,rl_block;

        img_back = dialog.findViewById(R.id.img_back);
        switch_mute_msg = dialog.findViewById(R.id.switch_mute_msg);
        switch_mute_video = dialog.findViewById(R.id.switch_mute_video);
        tv_see_all = dialog.findViewById(R.id.tv_see_all);
        rv_media = dialog.findViewById(R.id.rv_media);
        rl_report = dialog.findViewById(R.id.rl_report);
        rl_block = dialog.findViewById(R.id.rl_block);

        switch_mute_msg.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(ToggleableView toggleableView, boolean isOn) {

                if (isOn) {
                    if (savedchannel!=null){
                        muteGroupChat();
                    }else {
                        openMuteChatDialoge();
                    }
                    Log.d("mn13", "true");
                } else {
                    Log.d("mn13", "false");
                }

            }
        });

        rl_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contact != null && contact.isBlocked()) {
                    if (savedchannel!=null)
                        conversation.userBlockDialog(true,savedcontact,true);
                    else {
                        if (savedcontact.isBlocked()){
                            conversation.userBlockDialog(true, savedcontact, false);
                        }
                    }
                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing() && dialog!=null)
                    dialog.dismiss();
            }
        });

        rv_media.setLayoutManager(new LinearLayoutManager(ConversationActivity.this,RecyclerView.HORIZONTAL,false));
        chatMediaAdapter = new ChatMediaAdapter(ConversationActivity.this);
        rv_media.setAdapter(chatMediaAdapter);


    }

    private void openMuteChatDialoge(){
        final Dialog dialogblock;
        RelativeLayout rl_details,rl_options,rl_change_wall;
        LinearLayout ll_cancel;

        dialogblock = new Dialog(ConversationActivity.this);
        dialogblock.setContentView(R.layout.dialoge_mute_notification);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogblock.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialogblock.getWindow().setAttributes(layoutParams);

        dialogblock.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final RadioGroup radio_grp;
        RadioButton rb_8hrs,rb_1week,rb_1year;
        Button txtcancle,txtok;
        final RadioButton radioSexButton;

        radio_grp = dialogblock.findViewById(R.id.radio_grp);

        // get selected radio button from radioGroup
        int selectedId = radio_grp.getCheckedRadioButtonId();

        radioSexButton = findViewById(selectedId);

        txtcancle = dialogblock.findViewById(R.id.txtcancle);
        txtok = dialogblock.findViewById(R.id.txtok);

        txtok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
                millisecond = date.getTime();

                final MuteUserNotificationAsync.TaskListener listener = new MuteUserNotificationAsync.TaskListener() {

                    @Override
                    public void onSuccess(String status, Context context) {

                    }

                    @Override
                    public void onFailure(String error, Context context) {

                    }
                };

                if (radioSexButton.getText().equals("8 Hours")) {
                    millisecond = millisecond + 28800000;
                }else if (radioSexButton.getText().equals("1 Week")) {
                    millisecond = millisecond + 604800000;
                }else if (radioSexButton.getText().equals("1 Year")) {
                    millisecond = millisecond + 31558000000L;
                }

                new MuteUserNotificationAsync(listener, millisecond, contact.getUserId(), ConversationActivity.this).execute();
                dialogblock.dismiss();
            }
        });

        txtcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogblock!=null && dialogblock.isShowing())
                    dialogblock.dismiss();
            }
        });

    }

    private void muteGroupChat(){
        final Dialog dialogblock;
        RelativeLayout rl_details,rl_options,rl_change_wall;
        LinearLayout ll_cancel;

        dialogblock = new Dialog(ConversationActivity.this);
        dialogblock.setContentView(R.layout.dialoge_mute_notification);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialogblock.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        dialogblock.getWindow().setAttributes(layoutParams);

        dialogblock.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final RadioGroup radio_grp;
        RadioButton rb_8hrs,rb_1week,rb_1year;
        Button txtcancle,txtok;
        final RadioButton radioSexButton;

        radio_grp = dialogblock.findViewById(R.id.radio_grp);

        // get selected radio button from radioGroup
        int selectedId = radio_grp.getCheckedRadioButtonId();

        radioSexButton = findViewById(selectedId);

        txtcancle = dialogblock.findViewById(R.id.txtcancle);
        txtok = dialogblock.findViewById(R.id.txtok);

        txtok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTime();
                millisecond = date.getTime();

                final MuteNotificationAsync.TaskListener taskListener = new MuteNotificationAsync.TaskListener() {
                    @Override
                    public void onSuccess(ApiResponse apiResponse) {

                    }

                    @Override
                    public void onFailure(ApiResponse apiResponse, Exception exception) {

                    }

                    @Override
                    public void onCompletion() {
                        if (dialogblock!=null && dialogblock.isShowing())
                            dialogblock.dismiss();
                    }
                };

                if (radioSexButton.getText().equals("8 Hours")) {
                    millisecond = millisecond + 28800000;
                }else if (radioSexButton.getText().equals("1 Week")) {
                    millisecond = millisecond + 604800000;
                }else if (radioSexButton.getText().equals("1 Year")) {
                    millisecond = millisecond + 31558000000L;
                }

                MuteNotificationRequest muteNotificationRequest = new MuteNotificationRequest(channel.getKey(), millisecond);
                MuteNotificationAsync muteNotificationAsync = new MuteNotificationAsync(ConversationActivity.this, taskListener, muteNotificationRequest);
                muteNotificationAsync.execute((Void) null);
            }
        });

        txtcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogblock!=null && dialogblock.isShowing())
                    dialogblock.dismiss();
            }
        });

    }

    private void animateToolbarTitle() {
        if (toolbarTitle != null) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(toolbarTitle, "translationY", 0f);
            animation.setDuration(0);
            animation.start();
        }
    }

    @Override
    public void setToolbarTitle(String title) {
        Log.e("LLLLL_Ttool: ",title);
        toolbarTitle.setText(title);
        if (toolbarSubtitle != null && toolbarSubtitle.getVisibility() == View.INVISIBLE) {
            animateToolbarTitle();
        }
    }

    @Override
    public void setToolbarSubtitle(String subtitle) {
        if (subtitle.length() == 0) {
            toolbarSubtitle.setVisibility(View.INVISIBLE);
            // animateToolbarTitle();
            return;
        }
        toolbarSubtitle.setVisibility(View.VISIBLE);
        toolbarSubtitle.setText(subtitle);

        String userId = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            userId = Objects.requireNonNull(getIntent().getExtras()).getString("userId");
        }
        if (!TextUtils.isEmpty(userId)) {
            savedcontact = baseContactService.getContactById(userId);
        }

        Integer channelid = null;
        channelid = getIntent().getExtras().getInt("groupId");
        if (channelid != null) {
            savedchannel = ChannelDatabaseService.getInstance(ConversationActivity.this).getChannelByChannelKey(channelid);
        }

        String online = "";
        if (subtitle.equalsIgnoreCase("online")) {
            online = "yes";
        } else {
            online = "no";
        }

        if (savedcontact != null) {
            Log.d("mn13hello", savedcontact.toString() + "");

            if (savedcontact.getMetadata() != null) {
                if (savedcontact.getMetadata().containsKey(savedcontact.getUserId())) {
                    savedcontact.getMetadata().put(savedcontact.getUserId(), online);
                } else {
                    savedcontact.getMetadata().put(savedcontact.getUserId(), online);
                }
            } else {
                savedcontact.getMetadata().put(savedcontact.getUserId(), online);
            }

            Log.d("mn13savecontact", savedcontact.toString() + "");

        }


        ObjectAnimator animation = ObjectAnimator.ofFloat(toolbarTitle, "translationY", -20f);
        animation.setDuration(0);
        animation.start();
        ObjectAnimator animationSub = ObjectAnimator.ofFloat(toolbarSubtitle, "translationY", -20f);
        animationSub.setDuration(0);
        animationSub.start();
    }

    @Override
    public void setToolbarImage(Contact contact, Channel channel) {
        /*if (ApplozicSetting.getInstance(this).isShowImageOnToolbar() || alCustomizationSettings.isShowImageOnToolbar()) {*/
        conversationContactPhoto.setVisibility(View.VISIBLE);
        if (contact != null) {
            ic_call.setVisibility(View.VISIBLE);
            ic_video.setVisibility(View.VISIBLE);
            Singlecontact = contact;
            Log.d("mn13contact", Singlecontact.toString());

//            Glide.with(ConversationActivity.this)
//                    .load(contact.getImageURL())
//                    .error(R.drawable.user)
//                    .into(conversationContactPhoto);

            Picasso.get()
                    .load(contact.getImageURL())
                    .placeholder(R.drawable.user)
                    .error(R.drawable.user)
                    .into(conversationContactPhoto);

            /*Glide.with(this)
                    .load(contact.getImageURL())
                    .into(conversationContactPhoto);*/
        } else if (channel != null) {
            ic_call.setVisibility(View.GONE);
            ic_video.setVisibility(View.GONE);

//            Glide.with(ConversationActivity.this)
//                    .load(channel.getImageUrl())
//                    .error(R.drawable.applozic_group_icon)
//                    .into(conversationContactPhoto);

            Picasso.get()
                    .load(channel.getImageUrl())
                    .placeholder(R.drawable.applozic_group_icon)
                    .error(R.drawable.applozic_group_icon)
                    .into(conversationContactPhoto);

           /* Glide.with(this)
                    .load(channel.getImageUrl())
                    .placeholder(R.drawable.applozic_group_icon)
                    .error(R.drawable.applozic_group_icon)
                    .into(conversationContactPhoto);*/
        } else {
            conversationContactPhoto.setImageResource(R.drawable.user);
        }
        /*}*/
    }

    private void showActionBar() {
//        mActionBar.setDisplayShowTitleEnabled(true);
    }

    /*@SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        showActionBar();
        //return false;
        getMenuInflater().inflate(R.menu.mobicom_basic_menu_for_normal_message, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        if (Utils.hasICS()) {
            searchItem.collapseActionView();
        }
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(true);

        if (quickConversationFragment != null && !TextUtils.isEmpty(quickConversationFragment.getSearchString())) {
            searchView.setIconified(false);
            searchView.setQuery(quickConversationFragment.getSearchString(), false);
        }

        return super.onCreateOptionsMenu(menu);
    }*/

    public void showLogWarningForReleaseBuild() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(R.string.warning);
        dialogBuilder.setMessage(R.string.release_log_warning_message);
        dialogBuilder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            conversationUIService.onActivityResult(requestCode, resultCode, data);
            handleOnActivityResult(requestCode, data);
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    if (data == null) {
                        return;
                    }
                    if (imageUri != null) {
                        imageUri = result.getUri();
                        if (imageUri != null && profilefragment != null) {
                            profilefragment.handleProfileimageUpload(true, imageUri, profilePhotoFile);
                        }
                    } else {
                        imageUri = result.getUri();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + "_" + ".jpeg";
                        profilePhotoFile = FileClientService.getFilePath(imageFileName, this, "image/jpeg");
                        if (imageUri != null && profilefragment != null) {
                            profilefragment.handleProfileimageUpload(true, imageUri, profilePhotoFile);
                        }
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Utils.printLog(this, ConversationActivity.class.getName(), "Cropping failed:" + result.getError());
                }
            }
            if (requestCode == LOCATION_SERVICE_ENABLE) {
                if (((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                        .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    googleApiClient.connect();
                } else {
                    Toast.makeText(ConversationActivity.this, R.string.unable_to_fetch_location, Toast.LENGTH_LONG).show();
                }
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void handleOnActivityResult(int requestCode, Intent intent) {

        switch (requestCode) {

            case ProfileFragment.REQUEST_CODE_ATTACH_PHOTO:
                Uri selectedFileUri = (intent == null ? null : intent.getData());
                imageUri = null;
                beginCrop(selectedFileUri);
                break;

            case ProfileFragment.REQUEST_CODE_TAKE_PHOTO:
                beginCrop(imageUri);
                break;

        }
    }

    void beginCrop(Uri imageUri) {
        try {
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.OFF)
                    .setMultiTouchEnabled(true)
                    .start(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PermissionsUtils.REQUEST_STORAGE) {
            if (alStoragePermission != null) {
                alStoragePermission.onAction(PermissionsUtils.verifyPermissions(grantResults));
            }
            if (PermissionsUtils.verifyPermissions(grantResults)) {
                showSnackBar(R.string.storage_permission_granted);
                if (isAttachment) {
                    isAttachment = false;
                    processAttachment();
                }
            } else {
                showSnackBar(R.string.storage_permission_not_granted);
            }
        } else if (requestCode == PermissionsUtils.REQUEST_LOCATION) {
            if (PermissionsUtils.verifyPermissions(grantResults)) {
                showSnackBar(R.string.location_permission_granted);
                processingLocation();
            } else {
                showSnackBar(R.string.location_permission_not_granted);
            }

        } else if (requestCode == PermissionsUtils.REQUEST_PHONE_STATE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackBar(R.string.phone_state_permission_granted);
            } else {
                showSnackBar(R.string.phone_state_permission_not_granted);
            }
        } else if (requestCode == PermissionsUtils.REQUEST_CALL_PHONE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackBar(R.string.phone_call_permission_granted);
                processCall(contact, currentConversationId);
            } else {
                showSnackBar(R.string.phone_call_permission_not_granted);
            }
        } else if (requestCode == PermissionsUtils.REQUEST_AUDIO_RECORD) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackBar(R.string.record_audio_permission_granted);
                showAudioRecordingDialog();
            } else {
                showSnackBar(R.string.record_audio_permission_not_granted);
            }
        } else if (requestCode == PermissionsUtils.REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackBar(R.string.phone_camera_permission_granted);
                if (isTakePhoto) {
                    processCameraAction();
                } else {
                    processVideoRecording();
                }
            } else {
                showSnackBar(R.string.phone_camera_permission_not_granted);
            }
        } else if (requestCode == PermissionsUtils.REQUEST_CONTACT) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackBar(R.string.contact_permission_granted);
                processContact();
            } else {
                showSnackBar(R.string.contact_permission_not_granted);
            }
        } else if (requestCode == PermissionsUtils.REQUEST_CAMERA_FOR_PROFILE_PHOTO) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSnackBar(R.string.phone_camera_permission_granted);
                if (profilefragment != null) {
                    profilefragment.processPhotoOption();
                }
            } else {
                showSnackBar(R.string.phone_camera_permission_not_granted);
            }
        } else if (requestCode == PermissionsUtils.REQUEST_STORAGE_FOR_PROFILE_PHOTO) {
            if (PermissionsUtils.verifyPermissions(grantResults)) {
                showSnackBar(R.string.storage_permission_granted);
                if (profilefragment != null) {
                    profilefragment.processPhotoOption();
                }
            } else {
                showSnackBar(R.string.storage_permission_not_granted);
            }
        } else if (requestCode == PermissionsUtils.REQUEST_CAMERA_AUDIO) {
            if (PermissionsUtils.verifyPermissions(grantResults)) {
                showSnackBar(R.string.phone_camera_and_audio_permission_granted);
            } else {
                showSnackBar(R.string.audio_or_camera_permission_not_granted);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void processingLocation() {
        Log.d("mn13locationkey", geoApiKey + "");
        if (alCustomizationSettings.isLocationShareViaMap() && !TextUtils.isEmpty(geoApiKey) && API_KYE_STRING.equals(geoApiKey)) {
            Intent toMapActivity = new Intent(this, MobicomLocationActivity.class);
            startActivityForResult(toMapActivity, MultimediaOptionFragment.REQUEST_CODE_SEND_LOCATION);
        } else {
            //================= START GETTING LOCATION WITHOUT LOADING MAP AND SEND LOCATION AS TEXT===============

            if (!((LocationManager) getSystemService(Context.LOCATION_SERVICE))
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.location_services_disabled_title)
                        .setMessage(R.string.location_services_disabled_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.location_service_settings, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivityForResult(intent, LOCATION_SERVICE_ENABLE);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Toast.makeText(ConversationActivity.this, R.string.location_sending_cancelled, Toast.LENGTH_LONG).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                googleApiClient.disconnect();
                googleApiClient.connect();
            }

            //================= END ===============

        }

    }

    public void processLocation() {
        if (Utils.hasMarshmallow()) {
            new ApplozicPermissions(ConversationActivity.this, layout).checkRuntimePermissionForLocation();
        } else {
            processingLocation();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        /*if (id == R.id.start_new) {
            if (!TextUtils.isEmpty(contactsGroupId)) {
                if (Utils.isInternetAvailable(this)) {
                    conversationUIService.startContactActivityForResult();
                } else {
                    Intent intent = new Intent(this, MobiComKitPeopleActivity.class);
                    ChannelDatabaseService channelDatabaseService = ChannelDatabaseService.getInstance(this);
                    String[] userIdArray = channelDatabaseService.getChannelMemberByName(contactsGroupId, null);
                    if (userIdArray != null) {
                        conversationUIService.startContactActivityForResult(intent, null, null, userIdArray);
                    }
                }
            } else {
                conversationUIService.startContactActivityForResult();
            }
        } else if (id == R.id.conversations) {
            Intent intent = new Intent(this, ChannelCreateActivity.class);
            intent.putExtra(ChannelCreateActivity.GROUP_TYPE, Channel.GroupType.PUBLIC.getValue().intValue());
            startActivity(intent);
        } else if (id == R.id.broadcast) {
            Intent intent = new Intent(this, ContactSelectionActivity.class);
            intent.putExtra(ContactSelectionActivity.GROUP_TYPE, Channel.GroupType.BROADCAST.getValue().intValue());
            startActivity(intent);
        } else if (id == R.id.refresh) {
            Toast.makeText(this, getString(R.string.info_message_sync), Toast.LENGTH_LONG).show();
            new SyncMessagesAsyncTask(this).execute();
        } else if (id == R.id.shareOptions) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setAction(Intent.ACTION_SEND)
                    .setType("text/plain").putExtra(Intent.EXTRA_TEXT, inviteMessage);
            startActivity(Intent.createChooser(intent, "Share Via"));
            return super.onOptionsItemSelected(item);
        } else if (id == R.id.applozicUserProfile) {
            profilefragment.setApplozicPermissions(applozicPermission);
            addFragment(this, profilefragment, ProfileFragment.ProfileFragmentTag);
        } else if (id == R.id.logout) {

            if (!TextUtils.isEmpty(alCustomizationSettings.getLogoutPackage())) {
                Applozic.logoutUser(ConversationActivity.this, new AlLogoutHandler() {
                    @Override
                    public void onSuccess(Context context) {
                        try {
                            Class loginActivity = Class.forName(alCustomizationSettings.getLogoutPackage().trim());
                            if (loginActivity != null) {
                                Toast.makeText(getBaseContext(), getString(R.string.user_logout_info), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ConversationActivity.this, loginActivity);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Exception exception) {

                    }
                });
            }
        } else if (id == R.id.sendTextLogs) {
            try {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("vnd.android.cursor.dir/email");
                String receivers[] = {ALSpecificSettings.getInstance(this).getSupportEmailId()};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
                emailIntent.putExtra(Intent.EXTRA_STREAM, Utils.getTextLogFileUri(this));
                emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " " + getString(R.string.log_email_subject));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.select_email_app_chooser_title)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            *//*finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);*//*
        }*/
        return false;
    }

    @Override
    public void onQuickConversationFragmentItemClick(View view, Contact contact, Channel channel, Integer conversationId, String searchString) {
        conversation = ConversationFragment.newInstance(contact, channel, conversationId, searchString);
        addFragment(this, conversation, ConversationUIService.CONVERSATION_FRAGMENT);
        this.channel = channel;
        this.contact = contact;
        this.currentConversationId = conversationId;
    }

    @Override
    public void startContactActivityForResult() {
        conversationUIService.startContactActivityForResult();
    }

    @Override
    public void addFragment(ConversationFragment conversationFragment) {
        addFragment(this, conversationFragment, ConversationUIService.CONVERSATION_FRAGMENT);
        conversation = conversationFragment;
    }

    @Override
    public void onBackPressed() {
        if (isFromSearch()) {
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            try {
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (upIntent != null && isTaskRoot()) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.finish();
            return;
        }

        Boolean takeOrder = getIntent().getBooleanExtra(TAKE_ORDER, false);
        ConversationFragment conversationFragment = (ConversationFragment) getSupportFragmentManager().findFragmentByTag(ConversationUIService.CONVERSATION_FRAGMENT);
        if (conversationFragment != null && conversationFragment.isVisible() && (conversationFragment.multimediaPopupGrid.getVisibility() == View.VISIBLE)) {
            conversationFragment.hideMultimediaOptionGrid();
            return;
        }

        if (takeOrder && getSupportFragmentManager().getBackStackEntryCount() == 2) {
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (upIntent != null && isTaskRoot()) {
                TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
            }
            ConversationActivity.this.finish();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            ConversationActivity.this.finish();
            // getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    public boolean isFromSearch() {
        if (searchView != null && !searchView.isIconified() && quickConversationFragment != null && quickConversationFragment.isVisible()) {
            quickConversationFragment.stopSearching();
            searchView.onActionViewCollapsed();
            return true;
        }
        return false;
    }

    @Override
    public void updateLatestMessage(Message message, String formattedContactNumber) {
        conversationUIService.updateLatestMessage(message, formattedContactNumber);

    }

    @Override
    public void removeConversation(Message message, String formattedContactNumber) {
        conversationUIService.removeConversation(message, formattedContactNumber);
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (mCurrentLocation == null) {
                Toast.makeText(this, R.string.waiting_for_current_location, Toast.LENGTH_SHORT).show();
                locationRequest = new LocationRequest();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(UPDATE_INTERVAL);
                locationRequest.setFastestInterval(FASTEST_INTERVAL);
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            }
            if (mCurrentLocation != null && conversation != null) {
                conversation.attachLocation(mCurrentLocation);
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(((Object) this).getClass().getSimpleName(),
                "onConnectionSuspended() called.");

    }

    @Override
    public void onLocationChanged(Location location) {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            if (conversation != null && location != null) {
                conversation.attachLocation(location);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    public void setChildFragmentLayoutBG() {
        childFragmentLayout.setBackgroundResource(R.color.conversation_list_all_background);
    }

    public void setChildFragmentLayoutBGToTransparent() {
        if (!SharedPref.getWallpaper(ConversationActivity.this).equals("")) {
            String pathName = SharedPref.getWallpaper(ConversationActivity.this);
            Resources res = getResources();
            Bitmap bitmap = BitmapFactory.decodeFile(pathName);
            BitmapDrawable bd = new BitmapDrawable(res, bitmap);
            childFragmentLayout.setBackgroundDrawable(bd);
        } else if (SharedPref.getWallpaperColor(ConversationActivity.this)!=0){
            childFragmentLayout.setBackgroundColor(SharedPref.getWallpaperColor(ConversationActivity.this));
        }
        else {
            childFragmentLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.chat_bg));
        }
    }

    void showErrorDialog(int code) {
        GooglePlayServicesUtil.getErrorDialog(code, this,
                CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
    }

    public Contact getContact() {
        return contact;
    }

    public Channel getChannel() {
        return channel;
    }

    public Integer getConversationId() {
        return currentConversationId;
    }

    public void showSnackBar(int resId) {
        snackbar = Snackbar.make(layout, resId,
                Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    public Uri getVideoFileUri() {
        return videoFileUri;
    }

    public void setVideoFileUri(Uri videoFileUri) {
        this.videoFileUri = videoFileUri;
    }

    public void isTakePhoto(boolean takePhoto) {
        this.isTakePhoto = takePhoto;
    }

    public void isAttachment(boolean attachment) {
        this.isAttachment = attachment;
    }

    public File getFileObject() {
        return mediaFile;
    }

    public void showAudioRecordingDialog() {

        if (Utils.hasMarshmallow() && PermissionsUtils.checkSelfPermissionForAudioRecording(this)) {
            new ApplozicPermissions(this, layout).requestAudio();
        } else if (PermissionsUtils.isAudioRecordingPermissionGranted(this)) {

            FragmentManager supportFragmentManager = getSupportFragmentManager();
            DialogFragment fragment = AudioMessageFragment.newInstance();

            FragmentTransaction fragmentTransaction = supportFragmentManager
                    .beginTransaction().add(fragment, "AudioMessageFragment");

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        } else {

            if (alCustomizationSettings.getAudioPermissionNotFoundMsg() == null) {
                showSnackBar(R.string.applozic_audio_permission_missing);
            } else {
                snackbar = Snackbar.make(layout, alCustomizationSettings.getAudioPermissionNotFoundMsg(),
                        Snackbar.LENGTH_SHORT);
                snackbar.show();
            }

        }
    }

    public void processVideoCall(Contact contactObj, Integer conversationId) {
        this.contact = baseContactService.getContactById(contactObj.getContactIds());
        if (ApplozicClient.getInstance(getApplicationContext()).isIPCallEnabled()) {
            try {
                if (Utils.hasMarshmallow() && !PermissionsUtils.checkPermissionForCameraAndMicrophone(this)) {
                    applozicPermission.checkRuntimePermissionForCameraAndAudioRecording();
                    return;
                }
                String activityName = ApplozicSetting.getInstance(this).getActivityCallback(ApplozicSetting.RequestCode.VIDEO_CALL);
                Class activityToOpen = Class.forName(activityName);
                Intent intent = new Intent(this, activityToOpen);
                intent.putExtra("CONTACT_ID", contact.getUserId());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void processCall(Contact contactObj, Integer conversationId) {
        this.contact = baseContactService.getContactById(contactObj.getContactIds());
        this.currentConversationId = conversationId;
        try {

            if (ApplozicClient.getInstance(getApplicationContext()).isIPCallEnabled()) {
                if (Utils.hasMarshmallow() && !PermissionsUtils.checkPermissionForCameraAndMicrophone(this)) {
                    applozicPermission.checkRuntimePermissionForCameraAndAudioRecording();
                    return;
                }
                //Audio Call
                String activityName = ApplozicSetting.getInstance(this).getActivityCallback(ApplozicSetting.RequestCode.AUDIO_CALL);
                Class activityToOpen = Class.forName(activityName);
                Intent intent = new Intent(this, activityToOpen);
                intent.putExtra("CONTACT_ID", contact.getUserId());
                startActivity(intent);
                return;
            }

            if (activityToOpenOnClickOfCallButton != null) {
                Intent callIntent = new Intent(this, Class.forName(activityToOpenOnClickOfCallButton));
                if (currentConversationId != null) {
                    Conversation conversation = ConversationService.getInstance(this).getConversationByConversationId(currentConversationId);
                    callIntent.putExtra(ConversationUIService.TOPIC_ID, conversation.getTopicId());
                }
                callIntent.putExtra(ConversationUIService.CONTACT, contact);
                startActivity(callIntent);
            } else if (alCustomizationSettings.isShowActionDialWithOutCalling()) {
                if (!TextUtils.isEmpty(contact.getContactNumber())) {
                    Intent callIntent;
                    String uri = "tel:" + contact.getContactNumber().trim();
                    callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse(uri));
                    startActivity(callIntent);
                }
            } else {
                if (Utils.hasMarshmallow() && PermissionsUtils.checkSelfForCallPermission(this)) {
                    applozicPermission.requestCallPermission();
                } else if (PermissionsUtils.isCallPermissionGranted(this)) {
                    if (!TextUtils.isEmpty(contact.getContactNumber())) {
                        Intent callIntent;
                        String uri = "tel:" + contact.getContactNumber().trim();
                        callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(uri));
                        startActivity(callIntent);
                    }
                } else {
                    snackbar = Snackbar.make(layout, R.string.phone_call_permission_not_granted,
                            Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }

        } catch (Exception e) {
            Utils.printLog(this, "ConversationActivity", "Call permission is not added in androidManifest");
        }
    }


    public void processCameraAction() {
        try {
            if (PermissionsUtils.isCameraPermissionGranted(this)) {
                imageCapture();
            } else {
                if (Utils.hasMarshmallow() && PermissionsUtils.checkSelfForCameraPermission(this)) {
                    applozicPermission.requestCameraPermission();
                } else {
                    imageCapture();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processVideoRecording() {
        try {
            if (PermissionsUtils.isCameraPermissionGranted(this)) {
                showVideoCapture();
            } else {
                if (Utils.hasMarshmallow() && PermissionsUtils.checkSelfForCameraPermission(this)) {
                    applozicPermission.requestCameraPermission();
                } else {
                    showVideoCapture();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processContact() {
        if (Utils.hasMarshmallow() && PermissionsUtils.checkSelfForContactPermission(this)) {
            applozicPermission.requestContactPermission();
        } else {
            Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            contactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            startActivityForResult(contactIntent, MultimediaOptionFragment.REQUEST_CODE_CONTACT_SHARE);
        }
    }

    public void imageCapture() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_" + ".jpeg";

            mediaFile = FileClientService.getFilePath(imageFileName, getApplicationContext(), "image/jpeg");

            capturedImageUri = FileProvider.getUriForFile(this, Utils.getMetaDataValue(this, MobiComKitConstants.PACKAGE_NAME) + ".provider", mediaFile);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ClipData clip =
                        ClipData.newUri(getContentResolver(), "a Photo", capturedImageUri);

                cameraIntent.setClipData(clip);
                cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } else {
                List<ResolveInfo> resInfoList =
                        getPackageManager()
                                .queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, capturedImageUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    grantUriPermission(packageName, capturedImageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
            }

            if (cameraIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                if (mediaFile != null) {
                    startActivityForResult(cameraIntent, MultimediaOptionFragment.REQUEST_CODE_TAKE_PHOTO);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processAttachment() {
        if (Utils.hasMarshmallow() && PermissionsUtils.checkSelfForStoragePermission(this)) {
            applozicPermission.requestStoragePermissions();
        } else {
            Intent intentPick = new Intent(this, MobiComAttachmentSelectorActivity.class);
            startActivityForResult(intentPick, MultimediaOptionFragment.REQUEST_MULTI_ATTCAHMENT);
        }
    }

    public void showVideoCapture() {

        try {
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "VID_" + timeStamp + "_" + ".mp4";

            mediaFile = FileClientService.getFilePath(imageFileName, getApplicationContext(), "video/mp4");

            videoFileUri = FileProvider.getUriForFile(this, Utils.getMetaDataValue(this, MobiComKitConstants.PACKAGE_NAME) + ".provider", mediaFile);

            videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoFileUri);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                videoIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                videoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ClipData clip =
                        ClipData.newUri(getContentResolver(), "a Video", videoFileUri);

                videoIntent.setClipData(clip);
                videoIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                videoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            } else {
                List<ResolveInfo> resInfoList =
                        getPackageManager()
                                .queryIntentActivities(videoIntent, PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    grantUriPermission(packageName, videoFileUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    grantUriPermission(packageName, videoFileUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION);

                }
            }

            if (videoIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                if (mediaFile != null) {
                    videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                    startActivityForResult(videoIntent, MultimediaOptionFragment.REQUEST_CODE_CAPTURE_VIDEO_ACTIVITY);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Uri getCurrentImageUri() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_" + ".jpeg";
        profilePhotoFile = FileClientService.getFilePath(imageFileName, getApplicationContext(), "image/jpeg");
        imageUri = FileProvider.getUriForFile(this, Utils.getMetaDataValue(this, MobiComKitConstants.PACKAGE_NAME) + ".provider", profilePhotoFile);
        return imageUri;
    }


    public void processGalleryPhotoSelection() {
        if (Utils.hasMarshmallow() && PermissionsUtils.checkSelfForStoragePermission(this)) {
            applozicPermission.requestStoragePermissionsForProfilePhoto();
        } else {
            Intent getContentIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(getContentIntent, ProfileFragment.REQUEST_CODE_ATTACH_PHOTO);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        this.searchTerm = query;
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        this.searchTerm = query;
        if (getSearchListFragment() != null) {
            getSearchListFragment().onQueryTextChange(query);
        }
        return true;
    }

    public SearchListFragment getSearchListFragment() {
        return searchListFragment;
    }

    public void setSearchListFragment(SearchListFragment searchListFragment) {
        this.searchListFragment = searchListFragment;
    }


    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            isActivityDestroyed = true;

            if (mobiComKitBroadcastReceiver != null) {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(mobiComKitBroadcastReceiver);
            }
            if (connectivityReceiver != null) {
                unregisterReceiver(connectivityReceiver);
            }
            if (accountStatusAsyncTask != null) {
                accountStatusAsyncTask.cancel(true);
            }
            if (observer != null) {
                getApplicationContext().getContentResolver().unregisterContentObserver(observer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickOnMessageReply(Message message) {
        if (message != null && conversation != null) {
            conversation.onClickOnMessageReply(message);
        }
    }

    @Override
    public boolean isPermissionGranted() {
        return !PermissionsUtils.checkSelfForStoragePermission(this);
    }

    @Override
    public void checkPermission(ALStoragePermission storagePermission) {
        PermissionsUtils.requestPermissions(this, PermissionsUtils.PERMISSIONS_STORAGE, PermissionsUtils.REQUEST_STORAGE);
        this.alStoragePermission = storagePermission;
    }

    private class SyncMessagesAsyncTask extends AsyncTask<Boolean, Void, Void> {
        MobiComMessageService messageService;

        public SyncMessagesAsyncTask(Context context) {
            messageService = new MobiComMessageService(context, MessageIntentService.class);
        }

        protected Void doInBackground(Boolean... parms) {
            messageService.syncMessages();
            return null;
        }
    }

    @Override
    public void hideSubtitleAndProfilePic() {
        animateToolbarTitle();
        if (toolbarSubtitle != null) {
            toolbarSubtitle.setVisibility(View.INVISIBLE);
        }
        if (conversationContactPhoto != null) {
            conversationContactPhoto.setVisibility(View.GONE);
        }
    }

    public class SyncAccountStatusAsyncTask extends AsyncTask<Void, Void, Boolean> {
        Context context;
        RegisterUserClientService registerUserClientService;
        String loggedInUserId;
        ApplozicClient applozicClient;
        WeakReference<Snackbar> snackBarWeakReference;
        WeakReference<LinearLayout> linearLayoutWeakReference;

        public SyncAccountStatusAsyncTask(Context context, LinearLayout linearLayout, Snackbar snackbar) {
            this.context = context;
            this.registerUserClientService = new RegisterUserClientService(context);
            this.linearLayoutWeakReference = new WeakReference<LinearLayout>(linearLayout);
            this.snackBarWeakReference = new WeakReference<Snackbar>(snackbar);
            this.applozicClient = ApplozicClient.getInstance(context);
            this.loggedInUserId = MobiComUserPreference.getInstance(context).getUserId();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            User applozicUser = new User();
            applozicUser.setUserId(loggedInUserId);
            try {
                registerUserClientService.updateRegisteredAccount(applozicUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }


        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (applozicClient.isAccountClosed() || applozicClient.isNotAllowed()) {
                LinearLayout linearLayout = null;
                Snackbar snackbar = null;
                if (snackBarWeakReference != null) {
                    snackbar = snackBarWeakReference.get();
                }
                if (linearLayoutWeakReference != null) {
                    linearLayout = linearLayoutWeakReference.get();
                }
                if (snackbar != null && linearLayout != null) {
                    snackbar = Snackbar.make(linearLayout, applozicClient.isAccountClosed() ?
                                    R.string.applozic_account_closed : R.string.applozic_free_version_not_allowed_on_release_build,
                            Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();
                }
            }
        }
    }
}
