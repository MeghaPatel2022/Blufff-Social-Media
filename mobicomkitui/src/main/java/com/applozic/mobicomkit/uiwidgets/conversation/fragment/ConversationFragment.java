package com.applozic.mobicomkit.uiwidgets.conversation.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.conversation.MessageIntentService;
import com.applozic.mobicomkit.api.conversation.MobiComConversationService;
import com.applozic.mobicomkit.api.conversation.SyncCallService;
import com.applozic.mobicomkit.channel.service.ChannelService;
import com.applozic.mobicomkit.listners.AlCallback;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationCallbackHandler;
import com.applozic.mobicomkit.uiwidgets.conversation.MultimediaOptionsGridView;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicomkit.uiwidgets.uilistener.ALStoragePermission;
import com.applozic.mobicomkit.uiwidgets.uilistener.ALStoragePermissionListener;
import com.applozic.mobicommons.commons.core.utils.LocationUtils;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.people.SearchListFragment;
import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.contact.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ConversationFragment extends MobiComConversationFragment implements SearchListFragment, AlCallback {

    public static final int ATTCHMENT_OPTIONS = 6;
    private static final String TAG = "ConversationFragment";
    private final static String CONTACT = "CONTACT";
    private final static String CHANNEL = "CHANNEL";
    private final static String CONVERSATION_ID = "CONVERSATION_ID";
    private final static String SEARCH_STRING = "SEARCH_STRING";
    InputMethodManager inputMethodManager;
    Bundle bundle;
    private MultimediaOptionsGridView popupGrid;
    private List<String> attachmentKey = new ArrayList<>();
    private List<String> attachmentText = new ArrayList<>();
    private List<String> attachmentIcon = new ArrayList<>();
    private ConversationCallbackHandler conversationCallbackHandler;
    private ALStoragePermissionListener storagePermissionListener;


    public static ConversationFragment newInstance(Contact contact, Channel channel, Integer conversationId, String searchString) {
        ConversationFragment f = new ConversationFragment();
        Bundle args = new Bundle();
        if (contact != null) {
            args.putSerializable(CONTACT, contact);
        }
        if (channel != null) {
            args.putSerializable(CHANNEL, channel);
        }
        if (conversationId != null) {
            args.putInt(CONVERSATION_ID, conversationId);
        }
        args.putString(SEARCH_STRING, searchString);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.messageIntentClass = MessageIntentService.class;
        bundle = getArguments();
        if (bundle != null) {
            contact = (Contact) bundle.getSerializable(CONTACT);
            channel = (Channel) bundle.getSerializable(CHANNEL);
            currentConversationId = bundle.getInt(CONVERSATION_ID);
            searchString = bundle.getString(SEARCH_STRING);
            if (searchString != null) {
                SyncCallService.refreshView = true;
            }
        }
    }

    public void attachLocation(Location mCurrentLocation) {
        String address = LocationUtils.getAddress(getActivity(), mCurrentLocation);
        if (!TextUtils.isEmpty(address)) {
            address = "Address: " + address + "\n";
        } else {
            address = "";
        }
        this.messageEditText.setText(address + "http://maps.google.com/?q=" + mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.title = getResources().getString(R.string.chats);
        this.conversationService = new MobiComConversationService(getActivity());
        hideExtendedSendingOptionLayout = true;

        View view = super.onCreateView(inflater, container, savedInstanceState);
        populateAttachmentOptions();

        conversationCallbackHandler = new ConversationCallbackHandler(getContext(), this);

        if (alCustomizationSettings.isHideAttachmentButton()) {

            attachButton.setVisibility(View.GONE);
            messageEditText.setPadding(20, 0, 0, 0);
        }
        sendType.setSelection(1);

        messageEditText.setHint(R.string.enter_message_hint);

        multimediaPopupGrid.setVisibility(View.GONE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.secret_message_timer_array, R.layout.mobiframework_custom_spinner);
        adapter.setDropDownViewResource(R.layout.mobiframework_custom_spinner);

        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);

        messageEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multimediaPopupGrid.setVisibility(View.GONE);
            }
        });


        attch_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getContext() instanceof ALStoragePermissionListener) {
                    storagePermissionListener = (ALStoragePermissionListener) getContext();
                } else {
                    storagePermissionListener = new ALStoragePermissionListener() {
                        @Override
                        public boolean isPermissionGranted() {
                            return false;
                        }

                        @Override
                        public void checkPermission(ALStoragePermission storagePermission) {

                        }
                    };
                }

                if (storagePermissionListener.isPermissionGranted()) {
                    ((ConversationActivity) getContext()).isTakePhoto(true);
                    ((ConversationActivity) getContext()).processCameraAction();
                } else {
                    storagePermissionListener.checkPermission(new ALStoragePermission() {
                        @Override
                        public void onAction(boolean didGrant) {
                            if (didGrant) {
                                ((ConversationActivity) getContext()).isTakePhoto(true);
                                ((ConversationActivity) getContext()).processCameraAction();
                            }
                        }
                    });
                }


            }
        });


        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (contact != null && !contact.isBlocked() || channel != null) {
                    if (attachmentLayout.getVisibility() == View.VISIBLE) {
                        Toast.makeText(getActivity(), R.string.select_file_count_limit, Toast.LENGTH_LONG).show();
                        return;
                    }
                }

                if (channel != null) {
                    if (Channel.GroupType.GROUPOFTWO.getValue().equals(channel.getType())) {
                        String userId = ChannelService.getInstance(getActivity()).getGroupOfTwoReceiverUserId(channel.getKey());
                        if (!TextUtils.isEmpty(userId)) {
                            Contact withUserContact = appContactService.getContactById(userId);
                            if (withUserContact.isBlocked()) {
                                userBlockDialog(false, withUserContact, true);
                            } else {
                                processAttachButtonClick(view);
                            }
                        }
                    } else {
                        processAttachButtonClick(view);
                    }
                } else if (contact != null) {
                    if (contact.isBlocked()) {
                        userBlockDialog(false, contact, false);
                    } else {
                        processAttachButtonClick(view);
                    }
                }
            }
        });
        return view;
    }

    @Override
    protected void processMobiTexterUserCheck() {

    }

    public void hideMultimediaOptionGrid() {
        if (multimediaPopupGrid.getVisibility() == View.VISIBLE) {
            multimediaPopupGrid.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            recyclerDetailConversationAdapter.getFilter().filter(null);
        } else {
            recyclerDetailConversationAdapter.getFilter().filter(newText);
        }
        return true;
    }

    @Override
    public void onResume() {
        conversationCallbackHandler.registerUICallback();
        super.onResume();

    }

    @Override
    public void onPause() {
        conversationCallbackHandler.unregisterUICallbacks();
        super.onPause();
    }

    void processAttachButtonClick(View view) {

        final Dialog dialogblock;
        TextView txtfile, txtlocation, txtcontact,txtvideo;
        LinearLayout ll_cancel;

        dialogblock = new Dialog(getContext());
        dialogblock.setContentView(R.layout.dialog_attachoption);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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

        txtvideo = dialogblock.findViewById(R.id.txtvideo);
        txtfile = dialogblock.findViewById(R.id.txtfile);
        txtlocation = dialogblock.findViewById(R.id.txtlocation);
        txtcontact = dialogblock.findViewById(R.id.txtcontact);
        ll_cancel = dialogblock.findViewById(R.id.ll_cancel);

        ll_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogblock.isShowing()&&dialogblock!=null){
                    dialogblock.dismiss();
                }
            }
        });


        if (getContext() instanceof ALStoragePermissionListener) {
            storagePermissionListener = (ALStoragePermissionListener) getContext();
        } else {
            storagePermissionListener = new ALStoragePermissionListener() {
                @Override
                public boolean isPermissionGranted() {
                    return false;
                }

                @Override
                public void checkPermission(ALStoragePermission storagePermission) {

                }
            };
        }

        txtfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogblock.isShowing()) {
                    dialogblock.dismiss();
                }

                if (storagePermissionListener.isPermissionGranted()) {
                    ((ConversationActivity) getContext()).isAttachment(true);
                    ((ConversationActivity) getContext()).processAttachment();
                } else {
                    storagePermissionListener.checkPermission(new ALStoragePermission() {
                        @Override
                        public void onAction(boolean didGrant) {
                            if (didGrant) {
                                ((ConversationActivity) getContext()).isAttachment(true);
                                ((ConversationActivity) getContext()).processAttachment();
                            }
                        }
                    });
                }


            }
        });

        txtcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogblock.isShowing()) {
                    dialogblock.dismiss();
                }
                if (storagePermissionListener.isPermissionGranted()) {
                    ((ConversationActivity) getContext()).processContact();
                } else {
                    storagePermissionListener.checkPermission(new ALStoragePermission() {
                        @Override
                        public void onAction(boolean didGrant) {
                            if (didGrant) {
                                ((ConversationActivity) getContext()).processContact();
                            }
                        }
                    });
                }


            }
        });

        txtlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogblock.isShowing()) {
                    dialogblock.dismiss();
                }

                ((ConversationActivity) getContext()).processLocation();

            }
        });


        txtvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dialogblock.isShowing()) {
                    dialogblock.dismiss();
                }

                if (storagePermissionListener.isPermissionGranted()) {
                    ((ConversationActivity) getContext()).isTakePhoto(false);
                    ((ConversationActivity) getContext()).processVideoRecording();
                } else {
                    storagePermissionListener.checkPermission(new ALStoragePermission() {
                        @Override
                        public void onAction(boolean didGrant) {
                            if (didGrant) {
                                ((ConversationActivity) getContext()).isTakePhoto(false);
                                ((ConversationActivity) getContext()).processVideoRecording();
                            }
                        }
                    });
                }

            }
        });
        if (!dialogblock.isShowing()) {
            dialogblock.show();
        }


        /*MobicomMultimediaPopupAdapter mobicomMultimediaPopupAdapter = new MobicomMultimediaPopupAdapter(getActivity(), attachmentIcon, attachmentText);
        mobicomMultimediaPopupAdapter.setAlCustomizationSettings(alCustomizationSettings);
        multimediaPopupGrid.setAdapter(mobicomMultimediaPopupAdapter);

        int noOfColumn = (attachmentKey.size() == ATTCHMENT_OPTIONS) ? 3 : attachmentKey.size();
        multimediaPopupGrid.setNumColumns(noOfColumn);
        multimediaPopupGrid.setVisibility(View.VISIBLE);
        if (inputMethodManager.isActive()) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        MultimediaOptionsGridView itemClickHandler = new MultimediaOptionsGridView(getActivity(), multimediaPopupGrid);
        itemClickHandler.setMultimediaClickListener(attachmentKey);*/

    }

    private void populateAttachmentOptions() {

        if (attachmentKey != null && attachmentKey.size() > 0) {
            attachmentKey.clear();
            attachmentText.clear();
            attachmentIcon.clear();
        }

        String[] allKeys = getResources().getStringArray(R.array.multimediaOptions_without_price_key);
        String[] allValues = getResources().getStringArray(R.array.multimediaOptions_without_price_text);
        String[] allIcons = getResources().getStringArray(R.array.multimediaOptionIcons_without_price);

        Map<String, Boolean> maps = alCustomizationSettings.getAttachmentOptions();

        for (int index = 0; index < allKeys.length; index++) {

            String key = allKeys[index];
            if (maps == null || maps.get(key) == null || maps.get(key)) {
                attachmentKey.add(key);
                attachmentText.add(allValues[index]);
                attachmentIcon.add(allIcons[index]);
            }
        }
    }

    public void reload() {

        try {
            StringBuffer stringBufferTitle = new StringBuffer();
            if (contact != null) {
                Contact updatedInfoContact = appContactService.getContactById(contact.getContactIds());
                if (updatedInfoContact.isDeleted()) {
                    Utils.toggleSoftKeyBoard(getActivity(), true);
                    bottomlayoutTextView.setText(R.string.user_has_been_deleted_text);
                    userNotAbleToChatLayout.setVisibility(View.VISIBLE);
                    individualMessageSendLayout.setVisibility(View.GONE);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
                }
                if (updatedInfoContact != null && (!TextUtils.isEmpty(contact.getDisplayName())) && (!contact.getDisplayName().equals(updatedInfoContact.getDisplayName()))) {
                    stringBufferTitle.append(updatedInfoContact.getDisplayName());
                }
                enableOrDisableChat(updatedInfoContact);
            } else if (channel != null) {
                if (Channel.GroupType.GROUPOFTWO.getValue().equals(channel.getType())) {
                    String userId = ChannelService.getInstance(getActivity()).getGroupOfTwoReceiverUserId(channel.getKey());
                    if (!TextUtils.isEmpty(userId)) {
                        Contact withUserContact = appContactService.getContactById(userId);
                        if (withUserContact != null && (!TextUtils.isEmpty(contact.getDisplayName())) && (!contact.getDisplayName().equals(withUserContact.getDisplayName()))) {
                            stringBufferTitle.append(withUserContact.getDisplayName());
                        }
                    }
                }
            }
            if (!TextUtils.isEmpty(stringBufferTitle)) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(stringBufferTitle.toString());
            }
            Log.e("LLLLLL_Reload_Message: ","message");
            recyclerDetailConversationAdapter.refreshContactData();
            recyclerDetailConversationAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(Object response) {
        if (response instanceof ConversationCallbackHandler.CallbackEvent) {
            ConversationCallbackHandler.CallbackEvent callbackEvent = (ConversationCallbackHandler.CallbackEvent) response;

            if (ConversationCallbackHandler.CallbackEvent.EVENT_MQTT_CONNECTED.equals(callbackEvent.getAction())) {
                Applozic.subscribeToTyping(getContext(), channel, contact);
            }
        }
    }

    @Override
    public void onError(Object error) {

        if (error instanceof ConversationCallbackHandler.CallbackEvent) {
            ConversationCallbackHandler.CallbackEvent callbackEvent = (ConversationCallbackHandler.CallbackEvent) error;

            if (ConversationCallbackHandler.CallbackEvent.EVENT_MQTT_DISCONNECTED.equals(callbackEvent.getAction())) {
                Applozic.unSubscribeToTyping(getContext(), channel, contact);
            }
        }
    }
}