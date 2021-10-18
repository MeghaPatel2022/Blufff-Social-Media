package com.TBI.Client.Bluff.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.TBI.Client.Bluff.Activity.Chat.CreateChat;
import com.TBI.Client.Bluff.Activity.Chat.OptionChatActivity;
import com.TBI.Client.Bluff.Adapter.Chat.ChatFrendListAdapter;
import com.TBI.Client.Bluff.Adapter.Chat.ChatListAdapter;
import com.TBI.Client.Bluff.Model.GetFreinds.Datum;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.AlMessageProperties;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.Applozic;
import com.applozic.mobicomkit.api.MobiComKitConstants;
import com.applozic.mobicomkit.api.conversation.ApplozicConversation;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.database.MessageDatabaseService;
import com.applozic.mobicomkit.exception.ApplozicException;
import com.applozic.mobicomkit.listners.ApplozicUIListener;
import com.applozic.mobicomkit.listners.MessageListHandler;
import com.applozic.mobicomkit.uiwidgets.uikit.AlScrollListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Talk_Fragment extends Fragment implements ApplozicUIListener {

    private static Context context;
    @BindView(R.id.conversationRecyclerView)
    RecyclerView conversationRecyclerView;
    @BindView(R.id.searchchat)
    SearchView searchchat;
    @BindView(R.id.imgsearch)
    ImageView imgsearch;
    @BindView(R.id.txtunreadcount)
    TextView txtunreadcount;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipe_container;
    @BindView(R.id.imgarchieve)
    ImageView imgarchieve;
    @BindView(R.id.img_menu)
    ImageView img_menu;
    @BindView(R.id.rv_allFrend)
    RecyclerView rv_allFrend;
    @BindView(R.id.img_back)
    ImageView img_back;

    List<Message> conversationList;
    List<Message> freqConversationList;
    List<Message> searcharray;
    ChatListAdapter chatListAdapter;
    ChatFrendListAdapter chatFrendListAdapter;

    List<Datum> freindlist = new ArrayList<>();

    int pastVisibleItems, visibleItemCount, totalItemCount;
    String user_id = "";
    boolean isLoading = true;
    int limit = 20, offset = 0;
    String query = "";
    private OnFragmentInteractionListener mListener;
    private boolean loading = true;
    private BroadcastReceiver unreadCountBroadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (conversationList == null) {
            conversationList = new ArrayList<>();
            freqConversationList = new ArrayList<>();
            searcharray = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);

        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        EditText searchEditText = searchchat.findViewById(androidx.appcompat.R.id.search_src_text);

        user_id = sharedpreference.getUserId(getContext());

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Objects.requireNonNull(getActivity()).onBackPressed();
            }
        });

        Typeface myFont = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            myFont = getResources().getFont(R.font.poppins_semibold);
            searchEditText.setTypeface(myFont);
        }
        searchEditText.setTextSize(14);
        searchEditText.setTextColor(getActivity().getResources().getColor(R.color.blacklight));
        searchEditText.setHintTextColor(getActivity().getResources().getColor(R.color.blacklight));

        ImageView closeButton = searchchat.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(v -> searchEditText.setText(""));

        rv_allFrend.setLayoutManager(new GridLayoutManager(getContext(), 3));

//        GetFreinds("");
//
//        rv_allFrend.addOnScrollListener(new AlScrollListener() {
//            @Override
//            public void onScrollUp() {
//
//            }
//
//            @Override
//            public void onScrollDown() {
//
//            }
//
//            @Override
//            public void onLoadMore() {
//                if (isLoading == false) {
//                    offset = offset + 20;
//                    isLoading = true;
//                    if (freindlist.size() == 20) {
//                        Loadmore(query);
//                    }
//
//                }
//            }
//        });

        imgarchieve.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CreateChat.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
        });

        imgsearch.setOnClickListener(view1 -> {
            imgsearch.setVisibility(View.GONE);
            searchchat.setVisibility(View.VISIBLE);
        });

        searchchat.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.equalsIgnoreCase("")) {
                    setadaptrview(searcharray);
                } else {
                    new SearchUser(newText).execute();
                    Log.d("mn13searcharray", conversationList.toString());
                }
                return false;
            }
        });

        swipe_container.setOnRefreshListener(() -> ApplozicConversation.getLatestMessageList(getActivity(), false, new MessageListHandler() {
            @Override
            public void onResult(List<Message> messageList, ApplozicException e) {
                swipe_container.setRefreshing(false);
                if (e == null) {
//                    chatFrendListAdapter.removeAll();
//                    GetFreinds("");
                    setView(messageList);
                } else {
                    e.printStackTrace();
                }
            }
        }));

        img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OptionChatActivity.class);
                startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        conversationRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
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
                chatFrendListAdapter.showLoading(true);
                chatFrendListAdapter.notifyDataSetChanged();
                chatListAdapter.showLoading(true);
                chatListAdapter.notifyDataSetChanged();
                ApplozicConversation.getLatestMessageList(getActivity(), true, new MessageListHandler() {
                    @Override
                    public void onResult(List<Message> messageList, ApplozicException e) {
                        Log.d("mn13enter", messageList.toString() + "12");
                        if (e == null) {

                            conversationList.addAll(messageList);
                            freqConversationList.addAll(messageList);
                            searcharray.addAll(messageList);

                            AlMessageProperties properties;
                            properties = new AlMessageProperties(getActivity());
                            for (int i = 0; i < messageList.size(); i++) {
                                properties.setMessage(messageList.get(i));

                                if (properties.getChannel() != null) {
                                    if (properties.getChannel().getMetadata() != null) {
                                        if (!properties.getChannel().getMetadata().isEmpty()) {
                                            if (properties.getChannel().getMetadata().containsKey(sharedpreference.getUserId(context.getApplicationContext()))) {
                                                if (properties.getChannel().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                                                    conversationList.remove(messageList.get(i));
                                                    freqConversationList.remove(messageList.get(i));
                                                    searcharray.remove(messageList.get(i));
                                                }
                                            }
                                        }
                                    }
                                } else if (properties.getContact() != null) {

                                    if (properties.getContact().getMetadata() != null) {
                                        if (!properties.getContact().getMetadata().isEmpty()) {
                                            if (properties.getContact().getMetadata().containsKey(sharedpreference.getUserId(context))) {
                                                if (properties.getContact().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                                                    conversationList.remove(messageList.get(i));
                                                    freqConversationList.remove(messageList.get(i));
                                                    searcharray.remove(messageList.get(i));
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                            Log.d("mn13searcharray", searcharray.toString());
                            chatFrendListAdapter.notifyDataSetChanged();
                            chatFrendListAdapter.showLoading(false);
                            chatListAdapter.notifyDataSetChanged();
                            chatListAdapter.showLoading(false);

                        } else {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });

        unreadCountBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (MobiComKitConstants.APPLOZIC_UNREAD_COUNT.equals(intent.getAction())) {
                    int unreadCount = (new MessageDatabaseService(getActivity())).getTotalUnreadCount();
                    Log.d("mn13totalunreadcount", unreadCount + ":");
                    if (txtunreadcount != null) {
                        if (unreadCount != 0) {
                            txtunreadcount.setText("You got " + unreadCount + " unread messages.");
                            txtunreadcount.setVisibility(View.VISIBLE);
                        } else {
                            txtunreadcount.setVisibility(View.GONE);
                        }
                    }

                    //Update unread count in UI
                }
            }
        };


        return view;
    }

    public void setView(List<Message> messageList) {

        conversationList = new ArrayList<>();
        freqConversationList = new ArrayList<>();
        searcharray = new ArrayList<>();
        conversationList = messageList;
        freqConversationList = messageList;
        searcharray = messageList;
        AlMessageProperties properties;
        properties = new AlMessageProperties(getActivity());
        for (int i = 0; i < messageList.size(); i++) {
            properties.setMessage(messageList.get(i));

            if (properties.getChannel() != null) {
                if (properties.getChannel().getMetadata() != null) {
                    if (!properties.getChannel().getMetadata().isEmpty()) {
                        if (properties.getChannel().getMetadata().containsKey(sharedpreference.getUserId(context))) {
                            if (properties.getChannel().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                                conversationList.remove(messageList.get(i));
                                freqConversationList.remove(messageList.get(i));
                                searcharray.remove(messageList.get(i));
                            }
                        }
                    }
                }
            } else if (properties.getContact() != null) {
                if (properties.getContact().getMetadata() != null) {
                    if (!properties.getContact().getMetadata().isEmpty()) {
                        if (properties.getContact().getMetadata().containsKey(sharedpreference.getUserId(context))) {
                            if (properties.getContact().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                                conversationList.remove(messageList.get(i));
                                freqConversationList.remove(messageList.get(i));
                                searcharray.remove(messageList.get(i));
                            }
                        }
                    }
                }
            }
        }

        Log.d("mn13size", conversationList.size() + "");
        Log.d("mn13size", freqConversationList.size() + "");
        chatFrendListAdapter = new ChatFrendListAdapter(getActivity(), freqConversationList, Talk_Fragment.this);
        conversationRecyclerView.setAdapter(chatFrendListAdapter);
        chatListAdapter = new ChatListAdapter(getActivity(), conversationList, Talk_Fragment.this);
        rv_allFrend.setAdapter(chatListAdapter);
        chatListAdapter.notifyDataSetChanged();
        chatFrendListAdapter.notifyDataSetChanged();
    }

    public void setadaptrview(List<Message> messageList) {
        conversationList = messageList;
        freqConversationList = messageList;
        Log.d("mn13searcharray1", searcharray.toString());
        chatFrendListAdapter = new ChatFrendListAdapter(getActivity(), freqConversationList, Talk_Fragment.this);
        conversationRecyclerView.setAdapter(chatFrendListAdapter);
        chatFrendListAdapter.notifyDataSetChanged();

        chatListAdapter = new ChatListAdapter(getActivity(), conversationList, Talk_Fragment.this);
        rv_allFrend.setAdapter(chatListAdapter);
        chatListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedpreference.getTheme(context).equalsIgnoreCase("white")) {
            getActivity().getTheme().applyStyle(R.style.ActivityTheme_Primary_Base_Light, true);
        } else {
            getActivity().getTheme().applyStyle(R.style.ActivityTheme_Primary_Base_Dark, true);
        }

        ApplozicConversation.getLatestMessageList(getActivity(), false, new MessageListHandler() {
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
        Applozic.connectPublish(getActivity());
        Applozic.getInstance(getActivity()).registerUIListener(this);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(unreadCountBroadcastReceiver, new IntentFilter(MobiComKitConstants.APPLOZIC_UNREAD_COUNT));

        int unreadCount = (new MessageDatabaseService(getActivity())).getTotalUnreadCount();
        Log.d("mn13totalunreadcount", unreadCount + ":");
        if (txtunreadcount != null) {
            if (unreadCount != 0) {
                txtunreadcount.setText("You got " + unreadCount + " unread messages.");
                txtunreadcount.setVisibility(View.VISIBLE);
            } else {
                txtunreadcount.setVisibility(View.GONE);
            }
        }

        //Update unread count in UI


    }

    @Override
    public void onPause() {
        super.onPause();
        Applozic.disconnectPublish(getActivity());
        Applozic.getInstance(getActivity());
        Applozic.getInstance(getActivity()).unregisterUIListener();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(unreadCountBroadcastReceiver);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onMessageSent(Message message) {
        Log.d("Attachment Sent ", "Message acknowledge");
        ApplozicConversation.addLatestMessage(message, conversationList);
        ApplozicConversation.addLatestMessage(message, freqConversationList);
        if (chatFrendListAdapter != null) {
            chatFrendListAdapter.notifyDataSetChanged();
        }
        if (chatListAdapter != null) {
            chatListAdapter.notifyDataSetChanged();
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
        Toast.makeText(getActivity(), "New Message received", Toast.LENGTH_SHORT).show();
        ApplozicConversation.addLatestMessage(message, conversationList);
        ApplozicConversation.addLatestMessage(message, freqConversationList);
        if (chatFrendListAdapter != null) {
            chatFrendListAdapter.notifyDataSetChanged();
        }
        if (chatListAdapter != null) {
            chatListAdapter.notifyDataSetChanged();
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
        ApplozicConversation.addLatestMessage(message, freqConversationList);
        chatFrendListAdapter.notifyDataSetChanged();
        chatListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMessageDeleted(String messageKey, String userId) {
        Log.d("MyTest", "Message deleted : " + messageKey + ", For user : " + userId);
        if (chatFrendListAdapter != null) {
            chatFrendListAdapter.notifyDataSetChanged();
        }
        if (chatListAdapter != null) {
            chatListAdapter.notifyDataSetChanged();
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
        ApplozicConversation.removeLatestMessage(userId, channelKey, freqConversationList);
        chatFrendListAdapter.notifyDataSetChanged();
        chatListAdapter.notifyDataSetChanged();


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
        Applozic.connectPublish(getActivity());
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
        if (chatFrendListAdapter != null) {
            chatFrendListAdapter.notifyDataSetChanged();
        }
        if (chatListAdapter != null) {
            chatListAdapter.notifyDataSetChanged();
        }


    }

    public void removemessage(Message message) {
        if (chatFrendListAdapter != null) {
            conversationList.remove(message);
            freqConversationList.remove(message);
            searcharray.remove(message);
            chatFrendListAdapter.notifyDataSetChanged();
        }

        if (chatListAdapter != null) {
            conversationList.remove(message);
            freqConversationList.remove(message);
            searcharray.remove(message);
            chatListAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onConversationRead(String userId, boolean isGroup) {
        Log.d("MyTest", "Conversation read for " + userId + ", isGroup : " + isGroup);
    }

    @Override
    public void onUserDetailUpdated(String userId) {
        Log.d("MyTest", "User details updated for user : " + userId);
        if (chatListAdapter != null) {
            chatListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMessageMetadataUpdated(String keyString) {

        if (chatListAdapter != null) {
            chatListAdapter.notifyDataSetChanged();
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

    class SearchUser extends AsyncTask<Void, Void, List<Message>> {

        String search;

        public SearchUser(String search) {
            this.search = search;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Message> doInBackground(Void... params) {
            conversationList = new ArrayList<>();
            freqConversationList = new ArrayList<>();
            try {
                for (int i = 0; i < searcharray.size(); i++) {
                    AlMessageProperties alMessageProperties = new AlMessageProperties(getContext());
                    alMessageProperties.setMessage(searcharray.get(i));
                    if (alMessageProperties.getReceiver().toLowerCase().contains(search.toLowerCase())) {

                        conversationList.add(searcharray.get(i));
                        freqConversationList.add(searcharray.get(i));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return conversationList;
        }

        @Override
        protected void onPostExecute(final List<Message> messageList) {

            setadaptrview(conversationList);

        }

    }

//    public void GetFreinds(String keyword) {
//
//        ArrayList<param> paramArrayList = new ArrayList<>();
//        paramArrayList.add(new param("user_id", user_id));
//        paramArrayList.add(new param("keyword", keyword));
//        paramArrayList.add(new param("limit", "20"));
//        paramArrayList.add(new param("offset", "0"));
//
//        new geturl().getdata(getActivity(), data -> {
//            Log.d("mn13data", "in");
//
//            freindlist = new ArrayList<>();
//            try {
//                JSONObject object = new JSONObject(data);
//                boolean message = object.optBoolean("success");
//                String status = "";
//                status = object.optString("message");
//
//                if (message) {
//                    Getfreinds login = new Gson().fromJson(data, Getfreinds.class);
//
//                    Log.e("LLLL_ChatUser_List: ",login.getData().toString() + login.getData().size());
//                    freindlist.addAll(login.getData());
//                    chatFrendListAdapter.addAll(freindlist);
//
//                    if (freindlist.isEmpty()) {
//                        isLoading = true;
//                    } else {
//                        isLoading = false;
//                    }
//
//                } else {
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }, paramArrayList, "search_followers");
//    }
//
//    public void Loadmore(String keyword) {
//
//        ArrayList<param> paramArrayList = new ArrayList<>();
//        paramArrayList.add(new param("user_id", user_id));
//        paramArrayList.add(new param("keyword", keyword));
//        paramArrayList.add(new param("limit", limit + ""));
//        paramArrayList.add(new param("offset", offset + ""));
//
//        new geturl().getdata(getActivity(), data -> {
//            try {
//                JSONObject object = new JSONObject(data);
//                boolean message = object.optBoolean("success");
//                String status = "";
//                status = object.optString("message");
//
//                if (message) {
//                    Getfreinds login = new Gson().fromJson(data, Getfreinds.class);
//
//                    Log.e("LLLL_ChatUser_List: ",login.getData().toString() + login.getData().size());
//                    freindlist.addAll(login.getData());
//                    chatFrendListAdapter.addAll(freindlist);
//
//                    if (freindlist.isEmpty()) {
//                        isLoading = true;
//                    } else {
//                        isLoading = false;
//                    }
//
//
//                } else {
//
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }, paramArrayList, "search_followers");
//    }

}