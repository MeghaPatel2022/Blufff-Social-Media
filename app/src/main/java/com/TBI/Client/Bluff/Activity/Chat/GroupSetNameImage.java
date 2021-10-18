package com.TBI.Client.Bluff.Activity.Chat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.TBI.Client.Bluff.Adapter.Chat.GroupCreate_Adapter;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.ApplozicClient;
import com.applozic.mobicomkit.api.MobiComKitConstants;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.attachment.FileClientService;
import com.applozic.mobicomkit.api.people.ChannelInfo;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.feed.ChannelFeedApiResponse;
import com.applozic.mobicomkit.feed.ErrorResponseFeed;
import com.applozic.mobicomkit.uiwidgets.AlCustomizationSettings;
import com.applozic.mobicomkit.uiwidgets.async.AlChannelCreateAsyncTask;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ChannelCreateActivity;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.file.FileUtils;
import com.applozic.mobicommons.json.GsonUtils;
import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.contact.Contact;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static com.TBI.Client.Bluff.Utils.Public_Function.hasPermissions;

public class GroupSetNameImage extends AppCompatActivity {

    public static final int PERMISSION_ALL = 200;
    private static final String IMAGE_DIRECTORY = "/Blufff";
    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.imggropicon)
    CircleImageView imggropicon;
    @BindView(R.id.edtgroupname)
    EditText edtgroupname;
    @BindView(R.id.imgdone)
    ImageView imgdone;
    File profilePhotoFile;
    boolean flag = false;
    int groupType;
    String channelName = "";
    AlCustomizationSettings alCustomizationSettings;
    AppContactService appContactService;
    private Uri imageChangeUri;
    private String groupIconImageLink;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(GroupSetNameImage.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_groupsetname);
        ButterKnife.bind(this);

        setSupportActionBar(tool_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(GroupSetNameImage.this).equalsIgnoreCase("white")) {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            tool_bar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }

        Applozic.init(this, getString(R.string.application_key));
        String jsonString = FileUtils.loadSettingsJsonFile(getApplicationContext());
        if (!TextUtils.isEmpty(jsonString)) {
            alCustomizationSettings = (AlCustomizationSettings) GsonUtils.getObjectFromJson
                    (jsonString, AlCustomizationSettings.class);
        } else {
            alCustomizationSettings = new AlCustomizationSettings();
        }

        appContactService = new AppContactService(this);

        groupType = Channel.GroupType.PUBLIC.getValue().intValue();

        imggropicon.setOnClickListener(view -> {

            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA};

            if (!hasPermissions(GroupSetNameImage.this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(GroupSetNameImage.this, PERMISSIONS, PERMISSION_ALL);
            } else {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(GroupSetNameImage.this);
            }
        });
        /*cd = new ConnectionDetector(BlockList.this);
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(BlockList.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            GetBlockList();
        }*/

        imgdone.setOnClickListener(view -> {
            channelName = edtgroupname.getText().toString().trim();
            if (channelName.equalsIgnoreCase("")) {
                edtgroupname.requestFocus();
                edtgroupname.setError("Required");
                Toasty.warning(GroupSetNameImage.this, "Please enter group name", Toasty.LENGTH_LONG).show();
            } else {
                if (GroupCreate_Adapter.selestgroupid != null && GroupCreate_Adapter.selestgroupid.size() == 0) {
                    Toasty.warning(GroupSetNameImage.this, com.applozic.mobicomkit.uiwidgets.R.string.select_at_least, Toast.LENGTH_SHORT).show();
                } else {
                    AlChannelCreateAsyncTask.TaskListenerInterface taskListenerInterface = new AlChannelCreateAsyncTask.TaskListenerInterface() {
                        @Override
                        public void onSuccess(Channel channel, Context context) {
                            if (channel != null) {
                                Intent intent = new Intent(GroupSetNameImage.this, ConversationActivity.class);
                                if (ApplozicClient.getInstance(GroupSetNameImage.this.getApplicationContext()).isContextBasedChat()) {
                                    intent.putExtra(ConversationUIService.CONTEXT_BASED_CHAT, true);
                                }
                                intent.putExtra(ConversationUIService.GROUP_ID, channel.getKey());
                                intent.putExtra(ConversationUIService.GROUP_NAME, channel.getName());
                                startActivity(intent);
                            }

                            if (channelName != null) {
                                sendBroadcast(new Intent(ChannelCreateActivity.ACTION_FINISH_CHANNEL_CREATE));
                            }
                            finish();
                        }

                        @Override
                        public void onFailure(ChannelFeedApiResponse channelFeedApiResponse, Context context) {
                            if (channelFeedApiResponse != null) {
                                List<ErrorResponseFeed> error = channelFeedApiResponse.getErrorResponse();
                                if (error != null && error.size() > 0) {
                                    ErrorResponseFeed errorResponseFeed = error.get(0);
                                    String errorDescription = errorResponseFeed.getDescription();
                                    if (!TextUtils.isEmpty(errorDescription)) {
                                        if (MobiComKitConstants.GROUP_USER_LIMIT_EXCEED.equalsIgnoreCase(errorDescription)) {
                                            Toast.makeText(context, com.applozic.mobicomkit.uiwidgets.R.string.group_members_limit_exceeds, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, com.applozic.mobicomkit.uiwidgets.R.string.applozic_server_error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            } else {
                                Toast.makeText(context, Utils.isInternetAvailable(context) ? com.applozic.mobicomkit.uiwidgets.R.string.applozic_server_error : com.applozic.mobicomkit.uiwidgets.R.string.you_dont_have_any_network_access_info, Toast.LENGTH_SHORT).show();
                            }
                        }
                    };

                    if (GroupCreate_Adapter.selestgroupid != null && GroupCreate_Adapter.selestgroupid.size() > 0) {
                        if (TextUtils.isEmpty(channelName)) {
                            StringBuffer stringBuffer = new StringBuffer();
                            int i = 0;
                            for (String userId : GroupCreate_Adapter.selestgroupid) {
                                i++;
                                if (i > 10)
                                    break;
                                Contact contactDisplayName = appContactService.getContactById(userId);
                                stringBuffer.append(contactDisplayName.getDisplayName()).append(",");
                            }
                            int lastIndex = stringBuffer.lastIndexOf(",");
                            channelName = stringBuffer.replace(lastIndex, lastIndex + 1, "").toString();
                        }

                        ChannelInfo channelInfo = new ChannelInfo(channelName, GroupCreate_Adapter.selestgroupid);
                        if (!TextUtils.isEmpty(groupIconImageLink)) {
                            channelInfo.setImageUrl(groupIconImageLink);
                        }

                        if (groupType == Channel.GroupType.BROADCAST.getValue()) {
                            channelInfo.setType(groupType);
                        } else if (alCustomizationSettings != null) {
                            channelInfo.setType(alCustomizationSettings.getDefaultGroupType());
                        } else {
                            channelInfo.setType(groupType);
                        }

                        if (MobiComUserPreference.getInstance(GroupSetNameImage.this).getParentGroupKey() != null && MobiComUserPreference.getInstance(GroupSetNameImage.this).getParentGroupKey() != 0) {
                            channelInfo.setParentKey(MobiComUserPreference.getInstance(GroupSetNameImage.this).getParentGroupKey());
                        }

                        AlChannelCreateAsyncTask alChannelCreateAsyncTask = new AlChannelCreateAsyncTask(GroupSetNameImage.this, channelInfo, taskListenerInterface);
                        alChannelCreateAsyncTask.execute((Void) null);
                    }

                }
            }

        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult res = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = res.getUri();

                try {

                    if (imageChangeUri != null) {
                        imageChangeUri = res.getUri();
                        imggropicon.setImageDrawable(null); // <--- added to force redraw of ImageView
                        imggropicon.setImageURI(imageChangeUri);
                        new ProfilePictureUpload(true, profilePhotoFile, imageChangeUri, GroupSetNameImage.this).execute((Void[]) null);
                    } else {
                        imageChangeUri = res.getUri();
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String imageFileName = "JPEG_" + timeStamp + "_" + ".jpeg";
                        imggropicon.setImageDrawable(null); // <--- added to force redraw of ImageView
                        imggropicon.setImageURI(imageChangeUri);
                        profilePhotoFile = FileClientService.getFilePath(imageFileName, this, "image/jpeg");
                        new ProfilePictureUpload(true, profilePhotoFile, imageChangeUri, GroupSetNameImage.this).execute((Void[]) null);
                    }

                    /*Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                    imggropicon.setImageBitmap(bitmap);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
                    if (!wallpaperDirectory.exists()) {
                        wallpaperDirectory.mkdirs();
                    }
                    try {
                        File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
                        f.createNewFile();
                        f.getAbsoluteFile();
                        selectedimage = f;
                        flag = true;
                        //   uploadpic();
                        Log.e("selectedfile", f + "");
                        FileOutputStream fo = new FileOutputStream(f);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        //  Toasty.success(Admin_Add_Gallery.this, "Image Saved!", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        //    Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = res.getError();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        int length = grantResults.length;
        if (length > 0) {
            int grantResult = grantResults[0];
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(GroupSetNameImage.this);
            } else {
            }
        }
    }


    class ProfilePictureUpload extends AsyncTask<Void, Void, Boolean> {

        Context context;
        Uri fileUri;
        String displayName;
        File file;
        boolean isSaveFile;
        FileClientService fileClientService;

        public ProfilePictureUpload(boolean isSaveFile, File file, Uri fileUri, Context context) {
            this.context = context;
            this.fileUri = fileUri;
            this.file = file;
            this.isSaveFile = isSaveFile;
            this.fileClientService = new FileClientService(context);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Public_Function.Show_Progressdialog(GroupSetNameImage.this);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                if (fileUri != null) {
                    String filePath = file.getAbsolutePath();
                    if (isSaveFile) {
                        fileClientService.writeFile(fileUri, file);
                    }
                    groupIconImageLink = fileClientService.uploadProfileImage(filePath);
                    Log.d("mn13link", groupIconImageLink + "");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean result) {
            Public_Function.Hide_ProgressDialogue();
        }
    }
}
