package com.TBI.Client.Bluff.Activity.Chat;

import android.content.BroadcastReceiver;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.TBI.Client.Bluff.Adapter.Chat.Archieved_Adapter;
import com.TBI.Client.Bluff.ExceptionHandler.DefaultExceptionHandler;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.AlMessageProperties;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.conversation.ApplozicConversation;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.exception.ApplozicException;
import com.applozic.mobicomkit.listners.ApplozicUIListener;
import com.applozic.mobicomkit.listners.MessageListHandler;
import com.applozic.mobicomkit.uiwidgets.uikit.AlScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Hide_ArchievedChat extends AppCompatActivity implements ApplozicUIListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.conversationRecyclerView)
    RecyclerView conversationRecyclerView;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipe_container;
    @BindView(R.id.txtempty)
    TextView txtempty;


    List<Message> conversationList = new ArrayList<>();
    Archieved_Adapter conversationAdapter;

    private BroadcastReceiver unreadCountBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        if (sharedpreference.getTheme(Hide_ArchievedChat.this).equalsIgnoreCase("white")) {
            setTheme(R.style.ActivityTheme_Primary_Base_Light);
        } else {
            setTheme(R.style.ActivityTheme_Primary_Base_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archivedchat);
        ButterKnife.bind(this);
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(Hide_ArchievedChat.this));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (sharedpreference.getTheme(Hide_ArchievedChat.this).equalsIgnoreCase("white")) {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
        } else {
            toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(Hide_ArchievedChat.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        conversationRecyclerView.setLayoutManager(layoutManager);
        conversationRecyclerView.setNestedScrollingEnabled(true);

        conversationRecyclerView.addOnScrollListener(new AlScrollListener() {
            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onLoadMore() {
                conversationAdapter.showLoading(true);
                conversationAdapter.notifyDataSetChanged();
                ApplozicConversation.getLatestMessageList(Hide_ArchievedChat.this, true, new MessageListHandler() {
                    @Override
                    public void onResult(List<Message> messageList, ApplozicException e) {
                        Log.d("mn13enter", messageList.toString() + "12");
                        if (e == null) {

                            List<Message> archiedlist = new ArrayList<>();
                            AlMessageProperties properties;
                            properties = new AlMessageProperties(Hide_ArchievedChat.this);
                            for (int i = 0; i < messageList.size(); i++) {
                                properties.setMessage(messageList.get(i));

                                if (properties.getChannel() != null) {
                                    if (properties.getChannel().getMetadata() != null) {
                                        if (!properties.getChannel().getMetadata().isEmpty()) {
                                            if (properties.getChannel().getMetadata().containsKey(sharedpreference.getUserId(Hide_ArchievedChat.this))) {
                                                if (properties.getChannel().getMetadata().get(sharedpreference.getUserId(Hide_ArchievedChat.this)).equalsIgnoreCase("archived")) {
                                                    archiedlist.add(messageList.get(i));
                                                }
                                            }
                                        }
                                    }
                                } else if (properties.getContact() != null) {

                                    if (properties.getContact().getMetadata() != null) {
                                        if (!properties.getContact().getMetadata().isEmpty()) {
                                            if (properties.getContact().getMetadata().containsKey(sharedpreference.getUserId(Hide_ArchievedChat.this))) {
                                                if (properties.getContact().getMetadata().get(sharedpreference.getUserId(Hide_ArchievedChat.this)).equalsIgnoreCase("archived")) {
                                                    archiedlist.add(messageList.get(i));
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            conversationList.addAll(archiedlist);
                            Log.d("mn13searcharray", conversationList.toString());
                            conversationAdapter.notifyDataSetChanged();
                            conversationAdapter.showLoading(false);
                        } else {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        swipe_container.setOnRefreshListener(() -> ApplozicConversation.getLatestMessageList(Hide_ArchievedChat.this, false, new MessageListHandler() {
            @Override
            public void onResult(List<Message> messageList, ApplozicException e) {
                swipe_container.setRefreshing(false);
                if (e == null) {
                    setView(messageList);
                } else {
                    e.printStackTrace();
                }
            }
        }));


        ApplozicConversation.getLatestMessageList(Hide_ArchievedChat.this, false, (messageList, e) -> {
            if (e == null) {
                setView(messageList);
            } else {
                e.printStackTrace();
            }
        });
        Applozic.connectPublish(Hide_ArchievedChat.this);
        Applozic.getInstance(Hide_ArchievedChat.this).registerUIListener(this);
        //  LocalBroadcastManager.getInstance(Hide_ArchievedChat.this).registerReceiver(unreadCountBroadcastReceiver, new IntentFilter(MobiComKitConstants.APPLOZIC_UNREAD_COUNT));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        overridePendingTransition(R.anim.stay, R.anim.slide_down);
        return super.onOptionsItemSelected(item);
    }

    public void setView(List<Message> messageList) {

        List<Message> archiedlist = new ArrayList<>();
        AlMessageProperties properties;
        properties = new AlMessageProperties(Hide_ArchievedChat.this);
        for (int i = 0; i < messageList.size(); i++) {
            properties.setMessage(messageList.get(i));

            if (properties.getChannel() != null) {
                if (properties.getChannel().getMetadata() != null) {
                    if (!properties.getChannel().getMetadata().isEmpty()) {
                        if (properties.getChannel().getMetadata().containsKey(sharedpreference.getUserId(Hide_ArchievedChat.this))) {
                            if (properties.getChannel().getMetadata().get(sharedpreference.getUserId(Hide_ArchievedChat.this)).equalsIgnoreCase("archived")) {
                                archiedlist.add(messageList.get(i));
                            }
                        }
                    }
                }
            } else if (properties.getContact() != null) {

                if (properties.getContact().getMetadata() != null) {
                    if (!properties.getContact().getMetadata().isEmpty()) {
                        if (properties.getContact().getMetadata().containsKey(sharedpreference.getUserId(Hide_ArchievedChat.this))) {
                            if (properties.getContact().getMetadata().get(sharedpreference.getUserId(Hide_ArchievedChat.this)).equalsIgnoreCase("archived")) {
                                archiedlist.add(messageList.get(i));
                            }
                        }
                    }
                }
            }
        }
        conversationList = new ArrayList<>();
        conversationList.addAll(archiedlist);
        Log.d("mn13size", conversationList.size() + "");
        conversationAdapter = new Archieved_Adapter(Hide_ArchievedChat.this, conversationList, Hide_ArchievedChat.this);
        conversationRecyclerView.setAdapter(conversationAdapter);
        conversationAdapter.notifyDataSetChanged();

        if (conversationList.isEmpty()) {
            txtempty.setVisibility(View.VISIBLE);
            conversationRecyclerView.setVisibility(View.GONE);
        } else {
            conversationRecyclerView.setVisibility(View.VISIBLE);
            txtempty.setVisibility(View.GONE);
        }
    }

    public void removemessage(Message message) {
        if (conversationAdapter != null) {
            conversationList.remove(message);
            conversationAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Applozic.disconnectPublish(Hide_ArchievedChat.this);
        Applozic.getInstance(Hide_ArchievedChat.this);
        Applozic.getInstance(Hide_ArchievedChat.this).unregisterUIListener();
        LocalBroadcastManager.getInstance(Hide_ArchievedChat.this).unregisterReceiver(unreadCountBroadcastReceiver);
    }

    @Override
    public void onMessageSent(Message message) {
        Log.d("Attachment Sent ", "Message acknowledge");
        ApplozicConversation.addLatestMessage(message, conversationList);
        if (conversationAdapter != null) {
            conversationAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Overridden method from ApplozicUIListener. It is called whenever a new message is received.
     * This function updates the recycler view by adding the newly received message in message list.
     *
     * @param message The new message received.
     */
    @Override
    public void onMessageReceived(Message message) {
        Toast.makeText(Hide_ArchievedChat.this, "New Message received", Toast.LENGTH_SHORT).show();
        ApplozicConversation.addLatestMessage(message, conversationList);
        if (conversationAdapter != null) {
            conversationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoadMore(boolean loadMore) {
        Log.d("MyTest", "Load more called : ");
    }

    @Override
    public void onMessageSync(Message message, String key) {
        Log.d("MyTest", "Message sync finished: " + message + ", Message key : " + key);
        ApplozicConversation.addLatestMessage(message, conversationList);
        conversationAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageDeleted(String messageKey, String userId) {
        Log.d("MyTest", "Message deleted : " + messageKey + ", For user : " + userId);
        if (conversationAdapter != null) {
            conversationAdapter.notifyDataSetChanged();
        }
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
        conversationAdapter.notifyDataSetChanged();


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
        Applozic.connectPublish(Hide_ArchievedChat.this);
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
        if (conversationAdapter != null) {
            conversationAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onConversationRead(String userId, boolean isGroup) {
        Log.d("MyTest", "Conversation read for " + userId + ", isGroup : " + isGroup);
    }

    @Override
    public void onUserDetailUpdated(String userId) {
        Log.d("MyTest", "User details updated for user : " + userId);
        if (conversationAdapter != null) {
            conversationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMessageMetadataUpdated(String keyString) {

        if (conversationAdapter != null) {
            conversationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onUserMute(boolean mute, String userId) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}



