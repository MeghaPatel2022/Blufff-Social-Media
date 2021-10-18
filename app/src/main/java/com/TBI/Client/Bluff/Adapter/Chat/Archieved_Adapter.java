package com.TBI.Client.Bluff.Adapter.Chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Activity.Chat.Hide_ArchievedChat;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.AlMessageProperties;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.channel.service.ChannelService;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.uiwidgets.uikit.AlUIService;
import com.applozic.mobicomkit.uiwidgets.uikit.adapters.AlFooterAdapter;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ashish on 28/05/18.
 */

public class Archieved_Adapter extends AlFooterAdapter implements AdapterView.OnItemClickListener {
    public Context context;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    Hide_ArchievedChat hide_archievedChat;
    private AlMessageProperties messageProperties;

    public Archieved_Adapter(Context context, List<Message> mList, Hide_ArchievedChat hide_archievedChat) {
        super(context, mList);
        this.context = context;
        this.hide_archievedChat = hide_archievedChat;
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
            messageProperties.setMessageAndAttchmentIcon(mHolder.messageTv, mHolder.attachmentIcon);
            messageProperties.setUnreadCount(mHolder.unreadCount, mHolder.profileImage);
            messageProperties.loadProfileImage(mHolder.img_1,
                    mHolder.img_2,
                    mHolder.img_3,
                    mHolder.img_4,
                    mHolder.img_5,
                    mHolder.profileImage,
                    mHolder.alphabeticImage);
        }
    }

    public class ConversationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView alphabeticImage;
        ImageView profileImage;
        TextView receiverName;
        TextView messageTv;
        TextView unreadCount;
        TextView createdAtTime;
        ImageView attachmentIcon;
        AlMessageProperties properties;
        AlUIService uiService;
        CircleImageView img_1, img_2, img_3, img_4, img_5;

        public ConversationViewHolder(View view) {
            super(view);
            alphabeticImage = view.findViewById(R.id.alphabeticImage);
            profileImage = view.findViewById(R.id.imgavtar);
            receiverName = view.findViewById(R.id.smReceivers);
            messageTv = view.findViewById(R.id.message);
            unreadCount = view.findViewById(R.id.unreadSmsCount);
            createdAtTime = view.findViewById(R.id.createdAtTime);
            attachmentIcon = view.findViewById(R.id.attachmentIcon);

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
        public boolean onLongClick(View view) {

            Vibrator vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(10);

            int position = getAdapterPosition();
            if (mItems.size() <= position) {
                return true;
            }
            Message message = mItems.get(position);
            properties.setMessage(message);

            Dialog dialogblock;
            TextView txtunarchieved;

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


            txtunarchieved = dialogblock.findViewById(R.id.txtunarchieved);

            if (properties.getChannel() != null) {
                Log.d("mn13channel", properties.getChannel().toString());
                txtunarchieved.setText("UnArchived Group Chat");

                if (properties.getChannel().getMetadata() != null) {

                    if (properties.getChannel().getMetadata().containsKey(sharedpreference.getUserId(context))) {

                        if (properties.getChannel().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                            txtunarchieved.setVisibility(View.VISIBLE);
                        }
                    } else {
                    }
                } else {
                }
            } else {
                txtunarchieved.setText("UnArchived  Chat");

                if (properties.getContact().getMetadata() != null) {

                    if (properties.getContact().getMetadata().containsKey(sharedpreference.getUserId(context))) {

                        if (properties.getContact().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                            txtunarchieved.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }


            txtunarchieved.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    properties.setMessage(message);

                    if (properties.getChannel() != null) {
                        if (properties.getChannel().getMetadata() != null) {
                            if (!properties.getChannel().getMetadata().isEmpty()) {
                                if (properties.getChannel().getMetadata().containsKey(sharedpreference.getUserId(context))) {
                                    if (properties.getChannel().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                                        properties.getChannel().getMetadata().put(sharedpreference.getUserId(context), "unarchieved");
                                    }

                                }
                            }
                        }

                        ChannelService channelService = ChannelService.getInstance(context);
                        channelService.updateChannel(properties.getChannel());

                        if (!properties.getChannel().getMetadata().isEmpty()) {
                            Log.d("mn13hidedata", properties.getChannel().getMetadata().toString());
                        }


                    } else {
                        Log.d("mn13hide", properties.getContact().toString() + "");
                        if (properties.getContact() != null) {
                            if (properties.getContact().getMetadata() != null) {
                                if (!properties.getContact().getMetadata().isEmpty()) {
                                    if (properties.getContact().getMetadata().containsKey(sharedpreference.getUserId(context))) {
                                        if (properties.getContact().getMetadata().get(sharedpreference.getUserId(context)).equalsIgnoreCase("archived")) {
                                            properties.getContact().getMetadata().put(sharedpreference.getUserId(context), "unarchieved");
                                        }

                                    }
                                }
                            }
                            AppContactService contactService = new AppContactService(context);
                            contactService.updateContact(properties.getContact());
                            if (!properties.getContact().getMetadata().isEmpty()) {
                                Log.d("mn13hidedatacontact", properties.getContact().getMetadata().toString());
                            }

                        }
                    }

                    hide_archievedChat.removemessage(message);
                    if (dialogblock.isShowing()) {
                        dialogblock.dismiss();
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
