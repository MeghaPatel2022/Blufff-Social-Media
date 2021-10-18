package com.TBI.Client.Bluff.Activity.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.TBI.Client.Bluff.Adapter.Chat.ChatFrendListAdapter;
import com.TBI.Client.Bluff.Adapter.Chat.ChatListAdapter;
import com.TBI.Client.Bluff.Fragment.Talk_Fragment;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.AlMessageProperties;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.MobiComKitConstants;
import com.applozic.mobicomkit.api.conversation.ApplozicConversation;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.MobiComConversationService;
import com.applozic.mobicomkit.exception.ApplozicException;
import com.applozic.mobicomkit.listners.ApplozicUIListener;
import com.applozic.mobicomkit.listners.MessageListHandler;
import com.applozic.mobicomkit.uiwidgets.ChatBackgroundSettings;
import com.applozic.mobicomkit.uiwidgets.conversation.DeleteConversationAsyncTask;
import com.applozic.mobicomkit.uiwidgets.uikit.AlUIService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class ChatSettingsActivity extends AppCompatActivity implements ApplozicUIListener {

    @BindView(R.id.rl_chat_background)
    RelativeLayout rl_chat_background;
    @BindView(R.id.rl_clear_chats)
    RelativeLayout rl_clear_chats;
    @BindView(R.id.rl_delete_chats)
    RelativeLayout rl_delete_chats;
    @BindView(R.id.img_back)
    ImageView img_back;

    MobiComConversationService mobiComConversationService;
    AlUIService uiService;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    List<Message> conversationList;
    private AlMessageProperties messageProperties;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_settings);

        ButterKnife.bind(ChatSettingsActivity.this);
        mobiComConversationService = new MobiComConversationService(ChatSettingsActivity.this);
        messageProperties = new AlMessageProperties(ChatSettingsActivity.this);
        uiService = new AlUIService(ChatSettingsActivity.this);

        if (conversationList == null) {
            conversationList = new ArrayList<>();
        }
        getChat();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rl_chat_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatSettingsActivity.this, ChatBackgroundSettings.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
            }
        });

        rl_clear_chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd = new ConnectionDetector(ChatSettingsActivity.this);
                isInternetPresent = cd.isConnectingToInternet();

                for (int i = 0; i < conversationList.size(); i++) {
                    messageProperties.setMessage(conversationList.get(i));

                    if (!isInternetPresent) {
                        Toasty.warning(ChatSettingsActivity.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        if (messageProperties.getChannel() != null && messageProperties.getChannel().isDeleted()) {
                            uiService.deleteGroupConversation(messageProperties.getChannel());
                        } else {
                            Message message = conversationList.get(i);
                            mobiComConversationService.deleteMessage(message, messageProperties.getContact());
                        }
                    }

                }

            }
        });

        rl_delete_chats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cd = new ConnectionDetector(ChatSettingsActivity.this);
                isInternetPresent = cd.isConnectingToInternet();

                for (int i = 0; i < conversationList.size(); i++) {
                    messageProperties.setMessage(conversationList.get(i));

                    if (!isInternetPresent) {
                        Toasty.warning(ChatSettingsActivity.this, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        if (messageProperties.getChannel() != null && messageProperties.getChannel().isDeleted()) {
                            uiService.deleteGroupConversation(messageProperties.getChannel());
                        } else {
                            uiService.deleteConversationThread(messageProperties.getContact(), messageProperties.getChannel());
                        }
                    }

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        ApplozicConversation.getLatestMessageList(ChatSettingsActivity.this, false, new MessageListHandler() {
            @Override
            public void onResult(List<Message> messageList, ApplozicException e) {
                Log.d("mn133", messageList.toString() + "");
                if (e == null) {
                    setView(messageList);
                } else {
                    e.printStackTrace();
                    Log.d("mn13", e.getMessage() + "");
                }
            }
        });
    }

    @Override
    public void onMessageSent(Message message) {
        Log.d("Attachment Sent ", "Message acknowledge");
        ApplozicConversation.addLatestMessage(message, conversationList);
    }

    @Override
    public void onMessageReceived(Message message) {
        Toast.makeText(ChatSettingsActivity.this, "New Message received", Toast.LENGTH_SHORT).show();
        ApplozicConversation.addLatestMessage(message, conversationList);

    }

    @Override
    public void onLoadMore(boolean loadMore) {
        Log.d("MyTest", "Load more called : ");
    }

    @Override
    public void onMessageSync(Message message, String key) {
        Log.d("MyTest", "Message sync finished: " + message + ", Message key : " + key);
        ApplozicConversation.addLatestMessage(message, conversationList);
    }

    @Override
    public void onMessageDeleted(String messageKey, String userId) {
        Log.d("MyTest", "Message deleted : " + messageKey + ", For user : " + userId);
    }

    @Override
    public void onMessageDelivered(Message message, String userId) {
        Log.d("MyTest", "Message delivered : " + message + ", For user : " + userId);
    }

    @Override
    public void onAllMessagesDelivered(String userId) {
        Log.d("MyTest", "All Messages delivered  " + " For user : " + userId);

    }

    @Override
    public void onAllMessagesRead(String userId) {
        Log.d("MyTest", "All Messages read  " + " For user : " + userId);
    }

    @Override
    public void onConversationDeleted(String userId, Integer channelKey, String response) {
        Log.d("MyTest", "Conversation deleted  " + " For user : " + userId + " For channel : " + channelKey + " with response : " + response);

        ApplozicConversation.removeLatestMessage(userId, channelKey, conversationList);

    }

    @Override
    public void onUpdateTypingStatus(String userId, String isTyping) {
        Log.d("MyTest", "Typing status updated :  " + isTyping + " For user : " + userId);
    }

    @Override
    public void onUpdateLastSeen(String userId) {
        Log.d("MyTest", "Last seen updated  " + " For user : " + userId);
    }

    @Override
    public void onMqttDisconnected() {
        Log.d("MyTest", "Mqtt disconnected ");
        Applozic.connectPublish(ChatSettingsActivity.this);
    }

    @Override
    public void onMqttConnected() {

    }

    @Override
    public void onUserOnline() {

    }

    @Override
    public void onUserOffline() {

    }

    @Override
    public void onChannelUpdated() {

    }

    public void removemessage(Message message) {

        conversationList.remove(message);
        getChat();

    }

    @Override
    public void onConversationRead(String userId, boolean isGroup) {
        Log.d("MyTest", "Conversation read for " + userId + ", isGroup : " + isGroup);
    }

    @Override
    public void onUserDetailUpdated(String userId) {
        Log.d("MyTest", "User details updated for user : " + userId);

    }

    @Override
    public void onMessageMetadataUpdated(String keyString) {

    }

    @Override
    public void onUserMute(boolean mute, String userId) {

    }


    public void setView(List<Message> messageList) {

        conversationList = new ArrayList<>();

        conversationList = messageList;

        AlMessageProperties properties;
        properties = new AlMessageProperties(ChatSettingsActivity.this);
        for (int i = 0; i < messageList.size(); i++) {
            properties.setMessage(messageList.get(i));

            if (properties.getChannel() != null) {
                if (properties.getChannel().getMetadata() != null) {
                    if (!properties.getChannel().getMetadata().isEmpty()) {
                        if (properties.getChannel().getMetadata().containsKey(sharedpreference.getUserId(ChatSettingsActivity.this))) {
                            if (properties.getChannel().getMetadata().get(sharedpreference.getUserId(ChatSettingsActivity.this)).equalsIgnoreCase("archived")) {
                                conversationList.remove(messageList.get(i));
                            }
                        }
                    }
                }
            } else if (properties.getContact() != null) {
                if (properties.getContact().getMetadata() != null) {
                    if (!properties.getContact().getMetadata().isEmpty()) {
                        if (properties.getContact().getMetadata().containsKey(sharedpreference.getUserId(ChatSettingsActivity.this))) {
                            if (properties.getContact().getMetadata().get(sharedpreference.getUserId(ChatSettingsActivity.this)).equalsIgnoreCase("archived")) {
                                conversationList.remove(messageList.get(i));
                            }
                        }
                    }
                }
            }
        }

        Log.d("mn13size", conversationList.size() + "");

    }

    private void getChat() {
        ApplozicConversation.getLatestMessageList(ChatSettingsActivity.this, true, new MessageListHandler() {
            @Override
            public void onResult(List<Message> messageList, ApplozicException e) {
                Log.d("mn13enter", messageList.toString() + "12");
                if (e == null) {

                    conversationList.addAll(messageList);

                    AlMessageProperties properties;
                    properties = new AlMessageProperties(ChatSettingsActivity.this);

                    for (int i = 0; i < messageList.size(); i++) {
                        properties.setMessage(messageList.get(i));

                        if (properties.getChannel() != null) {
                            if (properties.getChannel().getMetadata() != null) {
                                if (!properties.getChannel().getMetadata().isEmpty()) {
                                    if (properties.getChannel().getMetadata().containsKey(sharedpreference.getUserId(ChatSettingsActivity.this))) {
                                        if (properties.getChannel().getMetadata().get(sharedpreference.getUserId(ChatSettingsActivity.this)).equalsIgnoreCase("archived")) {
                                            conversationList.remove(messageList.get(i));
                                        }
                                    }
                                }
                            }
                        } else if (properties.getContact() != null) {

                            if (properties.getContact().getMetadata() != null) {
                                if (!properties.getContact().getMetadata().isEmpty()) {
                                    if (properties.getContact().getMetadata().containsKey(sharedpreference.getUserId(ChatSettingsActivity.this))) {
                                        if (properties.getContact().getMetadata().get(sharedpreference.getUserId(ChatSettingsActivity.this)).equalsIgnoreCase("archived")) {
                                            conversationList.remove(messageList.get(i));
                                        }
                                    }
                                }
                            }
                        }
                    }


                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
