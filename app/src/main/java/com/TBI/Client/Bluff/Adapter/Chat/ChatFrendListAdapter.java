package com.TBI.Client.Bluff.Adapter.Chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Fragment.Talk_Fragment;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.AlMessageProperties;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.channel.service.ChannelService;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.uiwidgets.uikit.AlUIService;
import com.applozic.mobicomkit.uiwidgets.uikit.adapters.AlFooterAdapter;
import com.applozic.mobicommons.people.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

/**
 * Created by ashish on 28/05/18.
 */

public class ChatFrendListAdapter extends AlFooterAdapter implements AdapterView.OnItemClickListener {
    public Context context;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    Talk_Fragment talk_fragment;
    private AlMessageProperties messageProperties;

    public ChatFrendListAdapter(Context context, List<Message> mList, Talk_Fragment talk_fragment) {
        super(context, mList);
        this.context = context;
        this.talk_fragment = talk_fragment;
        messageProperties = new AlMessageProperties(context);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        messageProperties.handleConversationClick(mItems.get(position));
    }

    @Override
    public RecyclerView.ViewHolder getConversationViewHolder(ViewGroup parent) {
        return new ConversationViewHolder(mInflater.inflate(R.layout.layout_chat, parent, false));
    }

    @Override
    public void bindConversationViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ConversationViewHolder) {
            Message message = mItems.get(position);
            messageProperties.setMessage(message);

            ConversationViewHolder mHolder = (ConversationViewHolder) holder;

            mHolder.receiverName.setText(messageProperties.getReceiver());
            mHolder.createdAtTime.setText(messageProperties.getCreatedAtTime());
//            messageProperties.setMessageAndAttchmentIcon(mHolder.messageTv, mHolder.attachmentIcon);
            messageProperties.setUnreadCount(mHolder.unreadCount, mHolder.profileImage);
            messageProperties.loadProfileImage(mHolder.img_1,
                    mHolder.img_2,
                    mHolder.img_3,
                    mHolder.img_4,
                    mHolder.img_5,
                    mHolder.profileImage,
                    mHolder.alphabeticImage);

//            }
        }
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {

        TextView alphabeticImage;
        ImageView profileImage;
        TextView receiverName;
        TextView messageTv;
        TextView unreadCount;
        TextView createdAtTime;
        //        ImageView attachmentIcon;
        AlMessageProperties properties;
        AlUIService uiService;
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        if (properties.getChannel() != null && properties.getChannel().isDeleted()) {
                            uiService.deleteGroupConversation(properties.getChannel());
                        } else {
                            uiService.deleteConversationThread(properties.getContact(), properties.getChannel());
                        }
                        break;
                    case 1:
                        uiService.deleteGroupConversation(properties.getChannel());
                        break;
                    case 2:
                        uiService.channelLeaveProcess(properties.getChannel());
                        break;
                    default:
                }
                return true;
            }
        };
        CircleImageView img_1, img_2, img_3, img_4, img_5;

        public ConversationViewHolder(View view) {
            super(view);
            alphabeticImage = view.findViewById(R.id.alphabeticImage);
            profileImage = view.findViewById(R.id.imgavtar);
            receiverName = view.findViewById(R.id.smReceivers);
            messageTv = view.findViewById(R.id.message);
            unreadCount = view.findViewById(R.id.unreadSmsCount);
            createdAtTime = view.findViewById(R.id.createdAtTime);
//            attachmentIcon = view.findViewById(RM.id.attachmentIcon);

            img_1 = view.findViewById(R.id.img_1);
            img_2 = view.findViewById(R.id.img_2);
            img_3 = view.findViewById(R.id.img_3);
            img_4 = view.findViewById(R.id.img_4);
            img_5 = view.findViewById(R.id.img_5);

            properties = new AlMessageProperties(context);
            uiService = new AlUIService(context);

            profileImage.setClipToOutline(true);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            // view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();

            if (itemPosition != -1 && !mItems.isEmpty()) {
                Message message = mItems.get(itemPosition);

                if (message != null) {
                    messageProperties.handleConversationClick(message);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            int position = this.getLayoutPosition();
            if (mItems.size() <= position) {
                return;
            }
            Message message = mItems.get(position);
            menu.setHeaderTitle(R.string.conversation_options);

            String[] menuItems = context.getResources().getStringArray(R.array.conversation_options_menu);

            properties.setMessage(message);

            boolean isChannelDeleted = properties.getChannel() != null && properties.getChannel().isDeleted();
            boolean isUserPresentInGroup = properties.getChannel() != null && ChannelService.getInstance(context).processIsUserPresentInChannel(properties.getChannel().getKey());

            for (int i = 0; i < menuItems.length; i++) {

                if ((message.getGroupId() == null || (properties.getChannel() != null && (Channel.GroupType.GROUPOFTWO.getValue().equals(properties.getChannel().getType()) || Channel.GroupType.SUPPORT_GROUP.getValue().equals(properties.getChannel().getType())))) && (menuItems[i].equals(context.getResources().getString(R.string.delete_group)) ||
                        menuItems[i].equals(context.getResources().getString(R.string.exit_group)))) {
                    Log.d("mn131", menuItems[i]);
                    continue;
                }

                if (menuItems[i].equals(context.getResources().getString(R.string.exit_group)) && (isChannelDeleted || !isUserPresentInGroup)) {
                    Log.d("mn132", menuItems[i]);
                    continue;
                }

                if (menuItems[i].equals(context.getResources().getString(R.string.delete_group)) && (isUserPresentInGroup || !isChannelDeleted)) {
                    Log.d("mn133", menuItems[i]);
                    continue;
                }
                if (menuItems[i].equals(context.getResources().getString(R.string.delete_conversation))) {
                    Log.d("mn134", menuItems[i]);
                    continue;
                }

                MenuItem item = menu.add(Menu.NONE, i, i, menuItems[i]);
                item.setOnMenuItemClickListener(onEditMenu);
            }
        }

        @Override
        public boolean onLongClick(View view) {

            Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(10);

            int position = getAdapterPosition();
            if (mItems.size() <= position) {
                return true;
            }

            Message message = mItems.get(position);

            properties.setMessage(message);

            boolean isUserPresentInGroup = false;
            boolean isChannelDeleted = false;
            Channel channel = null;
            if (message.getGroupId() != null) {
                channel = ChannelService.getInstance(context).getChannelByChannelKey(message.getGroupId());
                if (channel != null) {
                    isChannelDeleted = channel.isDeleted();
                }
                isUserPresentInGroup = ChannelService.getInstance(context).processIsUserPresentInChannel(message.getGroupId());
            }

            String[] menuItems = context.getResources().getStringArray(R.array.conversation_options_menu);
            ArrayList<String> abc = new ArrayList<>();

            for (int i = 0; i < menuItems.length; i++) {

                if ((message.getGroupId() == null || (properties.getChannel() != null && (Channel.GroupType.GROUPOFTWO.getValue().equals(properties.getChannel().getType()) || Channel.GroupType.SUPPORT_GROUP.getValue().equals(properties.getChannel().getType())))) && (menuItems[i].equals(context.getResources().getString(R.string.delete_group)) ||
                        menuItems[i].equals(context.getResources().getString(R.string.exit_group)))) {
                    Log.d("mn131", menuItems[i]);
                    continue;
                }

                if (menuItems[i].equals(context.getResources().getString(R.string.exit_group)) && (isChannelDeleted || !isUserPresentInGroup)) {
                    Log.d("mn132", menuItems[i]);
                    continue;
                }

                if (menuItems[i].equals(context.getResources().getString(R.string.delete_group)) && (isUserPresentInGroup || !isChannelDeleted)) {
                    Log.d("mn133", menuItems[i]);
                    continue;
                }
                if (menuItems[i].equals(context.getResources().getString(R.string.delete_conversation))) {
                    Log.d("mn134", menuItems[i]);
                    continue;
                }
                abc.add(menuItems[i]);
            }

            Log.d("mn13click", isUserPresentInGroup + "\n" + isChannelDeleted);

            Dialog dialogblock;
            LinearLayout lndeletecon, lnexistgroup, lndeletgrop, lnarchieved;
            TextView txthide, txtarchieved;

            dialogblock = new Dialog(context);
            dialogblock.setContentView(R.layout.dialog_chatoption);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int displayWidth = displayMetrics.widthPixels;
            int displayHeight = displayMetrics.heightPixels;
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialogblock.getWindow().getAttributes());
            int dialogWindowWidth = (int) (displayWidth * 0.8f);
            int dialogWindowHeight = (int) (displayHeight * 0.75f);
            layoutParams.width = dialogWindowWidth;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.CENTER;
            dialogblock.getWindow().setAttributes(layoutParams);

            dialogblock.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogblock.getWindow().getAttributes().windowAnimations = R.style.animationName;


            lndeletecon = dialogblock.findViewById(R.id.lndeletecon);
            lnexistgroup = dialogblock.findViewById(R.id.lnexistgroup);
            lndeletgrop = dialogblock.findViewById(R.id.lndeletgrop);
            //  lnhidechat = (LinearLayout) dialogblock.findViewById(RM.id.lnhidechat);
            lnarchieved = dialogblock.findViewById(R.id.lnarchieved);
            txthide = dialogblock.findViewById(R.id.txthide);
            txtarchieved = dialogblock.findViewById(R.id.txtarchieved);

            lndeletecon.setVisibility(View.VISIBLE);
            lnexistgroup.setVisibility(View.GONE);
            lndeletgrop.setVisibility(View.GONE);

            if (properties.getChannel() != null) {
                Log.d("mn13channel", properties.getChannel().toString());
                txthide.setText("Hide Group");
                txtarchieved.setText("Archived Group Chat");

                if (properties.getChannel().getMetadata() != null) {

                    if (properties.getChannel().getMetadata().containsKey(sharedpreference.getUserId(context))) {

                        if (!properties.getChannel().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                            lnarchieved.setVisibility(View.VISIBLE);
                        }
                    } else {
                        lnarchieved.setVisibility(View.VISIBLE);
                    }
                } else {
                    lnarchieved.setVisibility(View.VISIBLE);
                }
            } else {
                txthide.setText("Hide Chat");
                txtarchieved.setText("Archived  Chat");

                Log.d("mn13propertie", properties.getContact() + "");
                if (properties.getContact().getMetadata() != null) {

                    if (properties.getContact().getMetadata().containsKey(sharedpreference.getUserId(context))) {

                        if (!properties.getContact().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                        }
                        if (!properties.getContact().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                            lnarchieved.setVisibility(View.VISIBLE);
                        }
                    } else {

                        lnarchieved.setVisibility(View.VISIBLE);
                    }
                } else {
                    lnarchieved.setVisibility(View.VISIBLE);
                }

            }

            for (int i = 0; i < abc.size(); i++) {
                if (abc.get(i).equalsIgnoreCase("Exit group")) {
                    lnexistgroup.setVisibility(View.VISIBLE);
                }
            }

            lnarchieved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    properties.setMessage(message);

                    if (properties.getChannel() != null) {
                        if (properties.getChannel().getMetadata() != null) {
                            if (properties.getChannel().getMetadata().isEmpty()) {

                                Map<String, String> hidedata = new HashMap<>();
                                hidedata.put(sharedpreference.getUserId(context) + "", "archived");
                                properties.getChannel().setMetadata(hidedata);
                            } else {
                                if (properties.getChannel().getMetadata().containsKey(sharedpreference.getUserId(context))) {
                                    if (properties.getChannel().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                                    } else {
                                        properties.getChannel().getMetadata().put(sharedpreference.getUserId(context), "archived");
                                    }
                                } else {
                                    properties.getChannel().getMetadata().put(sharedpreference.getUserId(context), "archived");
                                }
                            }
                        } else {
                            Map<String, String> hidedata = new HashMap<>();
                            hidedata.put(sharedpreference.getUserId(context) + "", "archived");
                            properties.getChannel().setMetadata(hidedata);
                        }


                        ChannelService channelService = ChannelService.getInstance(context);
                        channelService.updateChannel(properties.getChannel());

                        talk_fragment.removemessage(message);
                        if (!properties.getChannel().getMetadata().isEmpty()) {
                            Log.d("mn13hidedata", properties.getChannel().getMetadata().toString());
                        }


                    } else {
                        Log.d("mn13hide", properties.getContact().toString() + "");
                        if (properties.getContact().getMetadata() != null) {
                            if (properties.getContact().getMetadata().isEmpty()) {

                                Map<String, String> hidedata = new HashMap<>();
                                hidedata.put(sharedpreference.getUserId(context) + "", "archived");
                                properties.getContact().setMetadata(hidedata);
                            } else {

                                if (properties.getContact().getMetadata().containsKey(sharedpreference.getUserId(context))) {

                                    if (properties.getContact().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                                    } else {
                                        properties.getContact().getMetadata().put(sharedpreference.getUserId(context), "archived");
                                    }
                                } else {
                                    properties.getContact().getMetadata().put(sharedpreference.getUserId(context), "archived");
                                }
                            }
                        } else {

                            Map<String, String> hidedata = new HashMap<>();
                            hidedata.put(sharedpreference.getUserId(context) + "", "archived");
                            properties.getContact().setMetadata(hidedata);
                        }


                        AppContactService contactService = new AppContactService(context);
                        contactService.updateContact(properties.getContact());


                        if (!properties.getContact().getMetadata().isEmpty()) {
                            Log.d("mn13hidedatacontact", properties.getContact().getMetadata().toString());
                        }

                    }

                    talk_fragment.removemessage(message);

                    if (dialogblock.isShowing()) {
                        dialogblock.dismiss();
                    }

                }
            });
            lndeletecon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (dialogblock.isShowing()) {
                        dialogblock.dismiss();
                    }


                    cd = new ConnectionDetector(context);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(context, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        if (properties.getChannel() != null && properties.getChannel().isDeleted()) {
                            uiService.deleteGroupConversation(properties.getChannel());
                        } else {
                            uiService.deleteConversationThread(properties.getContact(), properties.getChannel());
                        }
                    }
                }
            });

            lnexistgroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (dialogblock.isShowing()) {
                        dialogblock.dismiss();
                    }
                    cd = new ConnectionDetector(context);
                    isInternetPresent = cd.isConnectingToInternet();

                    if (!isInternetPresent) {
                        Toasty.warning(context, "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                    } else {
                        uiService.channelLeaveProcess(properties.getChannel());
                    }

                }
            });
            if (!dialogblock.isShowing()) {
                dialogblock.show();
            }


            return false;
        }
    }


}
