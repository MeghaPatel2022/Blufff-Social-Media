package com.TBI.Client.Bluff.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.TBI.Client.Bluff.R;
import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.conversation.Message;
import com.applozic.mobicomkit.api.conversation.database.MessageDatabaseService;
import com.applozic.mobicomkit.api.people.ChannelCreate;
import com.applozic.mobicomkit.api.people.ContactContent;
import com.applozic.mobicomkit.channel.database.ChannelDatabaseService;
import com.applozic.mobicomkit.channel.service.ChannelService;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.contact.BaseContactService;
import com.applozic.mobicomkit.uiwidgets.alphanumbericcolor.AlphaNumberColorUtil;
import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.applozic.mobicommons.commons.core.utils.DateUtils;
import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.channel.ChannelUserMapper;
import com.applozic.mobicommons.people.channel.ChannelUtils;
import com.applozic.mobicommons.people.contact.Contact;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This class returns the properties of a message for your Recycler view's adapter.
 * for e.g the receiver's name from a message object.
 */

public class AlMessageProperties {
    private Context context;
    private BaseContactService contactService;
    private ChannelDatabaseService channelService;
    private MessageDatabaseService messageDatabase;
    private Message message;
    private Contact contact;
    private Channel channel;

    /**
     * This constructor should be initialised only once. You can do this in the constructor of your Adapter.
     *
     * @param context pass the calling Context
     */
    public AlMessageProperties(final Context context) {
        this.context = context;
        contactService = new AppContactService(context);
        messageDatabase = new MessageDatabaseService(context);
        channelService = ChannelDatabaseService.getInstance(context);
    }

    /**
     * This method is used to set message to this object. This will save you headache passing message object into every method.
     * This has to called in your bindView/getView method of your adapter.
     * This method creates a contact object, if message is from a user and a channel object if the message is from a group.
     *
     * @param message Pass the current Message object from the position.
     * @return Instance of this.
     */
    public AlMessageProperties setMessage(Message message) {
        this.message = message;
        if (message.getGroupId() == null) {
            contact = contactService.getContactById(message.getContactIds());
            channel = null;
        } else {
            channel = channelService.getChannelByChannelKey(message.getGroupId());
            contact = null;
        }
        return this;
    }

    /**
     * This method returns the receiver's name from the message object that this instance holds currently.
     *
     * @return If the message belongs to group it will return group's name and if it belongs to user, the display name of the user will be returned.
     */
    public String getReceiver() {
        if (message.getGroupId() == null) {
            return contact.getDisplayName();
        } else {
            if (channel != null) {
                if (Channel.GroupType.GROUPOFTWO.getValue().equals(channel.getType())) {
                    Contact withUserContact = contactService.getContactById(ChannelService.getInstance(context).getGroupOfTwoReceiverUserId(channel.getKey()));
                    if (withUserContact != null) {
                        return withUserContact.getDisplayName();
                    }
                } else {
                    return ChannelUtils.getChannelTitleName(channel, MobiComUserPreference.getInstance(context).getUserId());
                }
            }
        }
        return null;
    }

    /**
     * Returns the Message string that needs to be displayed in the conversation list item.
     *
     * @return Message string if the message is of type text.
     * File's name if the message has attachment.
     * "Location" if the message is of type location etc
     */
    public void setMessageAndAttchmentIcon(TextView messageTv, ImageView attachmentIcon) {
        if (message.hasAttachment() && !Message.ContentType.TEXT_URL.getValue().equals(message.getContentType())) {
            messageTv.setText(message.getFileMetas() == null && message.getFilePaths() != null ? message.getFilePaths().get(0).substring(message.getFilePaths().get(0).lastIndexOf("/") + 1) :
                    message.getFileMetas() != null ? message.getFileMetas().getName() : "");
            if (attachmentIcon != null) {
                attachmentIcon.setVisibility(View.VISIBLE);
                attachmentIcon.setImageResource(R.drawable.applozic_ic_action_attachment);
            }
        } else if (Message.ContentType.LOCATION.getValue().equals(message.getContentType())) {
            messageTv.setText(context.getString(R.string.Location));
            if (attachmentIcon != null) {
                attachmentIcon.setVisibility(View.VISIBLE);
                attachmentIcon.setImageResource(R.drawable.mobicom_notification_location_icon);
            }
        } else if (Message.ContentType.TEXT_HTML.getValue().equals(message.getContentType())) {
            messageTv.setText(message.getMessage());
            if (attachmentIcon != null) {
                attachmentIcon.setVisibility(View.GONE);
            }
        } else if (Message.ContentType.PRICE.getValue().equals(message.getContentType())) {
            if (attachmentIcon != null) {
                attachmentIcon.setVisibility(View.GONE);
            }
            //return EmoticonUtils.getSmiledText(context, ConversationUIService.FINAL_PRICE_TEXT + message.getMessage(), emojiconHandler)
        } else {
            messageTv.setText((!TextUtils.isEmpty(message.getMessage()) ? message.getMessage().substring(0, Math.min(message.getMessage().length(), 50)) : ""));
            if (attachmentIcon != null) {
                attachmentIcon.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Sets the unread count to a textView for the current conversation.
     *
     * @param textView The TextView to display the unread count.
     */
    public void setUnreadCount(TextView textView, ImageView imageView) {
        int unreadCount = 0;
        if (message.getGroupId() == null) {
            unreadCount = messageDatabase.getUnreadMessageCountForContact(message.getContactIds());
        } else {
            unreadCount = messageDatabase.getUnreadMessageCountForChannel(message.getGroupId());
        }

        if (unreadCount > 0) {
            textView.setVisibility(View.VISIBLE);
            imageView.setBackgroundResource(R.drawable.item_message);
            textView.setText(String.valueOf(unreadCount));
        } else {
            textView.setVisibility(View.GONE);
            imageView.setBackgroundResource(R.drawable.chat_box);
        }
    }

    /**
     * Returns the formatted time for the conversation.
     *
     * @return Formatted time as String.
     */
    public String getCreatedAtTime() {
        return DateUtils.getFormattedDateAndTime(context, message.getCreatedAtTime(), R.string.JUST_NOW, R.plurals.MINUTES, R.plurals.HOURS);
    }

    /**
     * This method loads the image for a Contact into the ImageView. If the user does not have image url set, it will create an alphabeticText image.
     * This will automatically check if the image is set and handle the views visibility by itself.
     *
     * @param imageView CircularImageView which loads the image for the user.
     * @param textView  TextView which will display the alphabeticText image.
     * @param contact   The Contact object whose image is to be displayed.
     */
    public void loadContactImage(ImageView imageView,
                                 TextView textView,
                                 Contact contact) {
        try {

            textView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);

            String contactNumber = "";
            char firstLetter = 0;
            contactNumber = contact.getDisplayName().toUpperCase();
            firstLetter = contact.getDisplayName().toUpperCase().charAt(0);

            if (firstLetter != '+') {
                textView.setText(String.valueOf(firstLetter));
            } else if (contactNumber.length() >= 2) {
                textView.setText(String.valueOf(contactNumber.charAt(1)));
            }

           /* Character colorKey = AlphaNumberColorUtil.alphabetBackgroundColorMap.containsKey(firstLetter) ? firstLetter : null;
            GradientDrawable bgShape = (GradientDrawable) textView.getBackground();
            bgShape.setColor(context.getResources().getColor(AlphaNumberColorUtil.alphabetBackgroundColorMap.get(colorKey)));
*/
            if (contact.isDrawableResources()) {
                Log.e("LLL_if_1st: ", String.valueOf(contact.isDrawableResources()));
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                int drawableResourceId = context.getResources().getIdentifier(contact.getrDrawableName(), "drawable", context.getPackageName());
                imageView.setImageResource(drawableResourceId);
            } else if (contact.getImageURL() != null) {
                Log.e("LLL_if_else_if: ", contact.getImageURL());
                textView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                loadImage(imageView, textView, contact.getImageURL(), R.drawable.applozic_group_icon);
            } else {
                Log.e("LLL_else: ", "contact.getImageURL()");
                textView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
    }

    /**
     * @return Returns the channel object if the message is from channel, null otherwise.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * @return Returns the Contact object if the message is from a user, null otherwise.
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * This method loads the channel's image into the ImageView
     *
     * @param imageView CircularImageView in which the image is to be loaded
     * @param textView  Although we do not display alphabeticImage for a group, but this is needed to handle the visibility for recycler view.
     * @param channel   Channel object whose image is to be loaded
     */
    public void loadChannelImage(CircleImageView img_1,
                                 CircleImageView img_2,
                                 CircleImageView img_3,
                                 CircleImageView img_4,
                                 CircleImageView img_5,
                                 ImageView imageView,
                                 TextView textView, Channel channel) {

        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);

        img_1.setVisibility(View.VISIBLE);
        img_2.setVisibility(View.VISIBLE);
        img_3.setVisibility(View.VISIBLE);
        img_4.setVisibility(View.VISIBLE);
        img_5.setVisibility(View.VISIBLE);

        List<ChannelUserMapper> channelUserMapperList;
        channelUserMapperList = ChannelService.getInstance(context).getListOfUsersFromChannelUserMapper(channel.getKey());

        BaseContactService baseContactService;
        baseContactService = new AppContactService(context);

        Contact contact;

        if (channelUserMapperList.size() > 4) {
            for (int i = 0; i < 4; i++) {
                ChannelUserMapper channelUserMapper = channelUserMapperList.get(i);
                contact = baseContactService.getContactById(channelUserMapper.getUserKey());
                if (i == 0)
                    loadContactImage(img_1, textView, contact);
                if (i == 1)
                    loadContactImage(img_2, textView, contact);
                if (i == 2)
                    loadContactImage(img_3, textView, contact);
                if (i == 3)
                    loadContactImage(img_4, textView, contact);
            }
        } else {
            for (int i = 0; i < channelUserMapperList.size(); i++) {
                ChannelUserMapper channelUserMapper = channelUserMapperList.get(i);
                contact = baseContactService.getContactById(channelUserMapper.getUserKey());
                Log.e("LLLLL_Group: ", contact.getDisplayName() + contact.getContactNumber());
                if (channelUserMapperList.size() == 2) {
                    if (i == 0)
                        loadContactImage(img_1, textView, contact);
                    if (i == 1)
                        loadContactImage(img_2, textView, contact);
                    img_3.setVisibility(View.GONE);
                    img_4.setVisibility(View.GONE);
                } else if (channelUserMapperList.size() == 3) {
                    if (i == 0)
                        loadContactImage(img_1, textView, contact);
                    if (i == 1)
                        loadContactImage(img_2, textView, contact);
                    if (i == 2)
                        loadContactImage(img_3, textView, contact);
                    img_4.setVisibility(View.GONE);
                }
            }
        }

        if (channel.getImageUrl() != null) {
            loadImage(imageView, textView, channel.getImageUrl(), R.drawable.applozic_group_icon);
        } else {
            imageView.setImageResource(R.drawable.applozic_group_icon);
        }
    }

    /**
     * This methods saves you a lot of work by check. Use this method in your bindView/getView.
     *
     * @param imageView CircularImageView to load the image
     * @param textView  TextView to display AlphabeticImage
     */
    public void loadProfileImage(CircleImageView img_1,
                                 CircleImageView img_2,
                                 CircleImageView img_3,
                                 CircleImageView img_4,
                                 CircleImageView img_5,
                                 ImageView imageView,
                                 TextView textView) {
        if (channel != null) {
            loadChannelImage(img_1, img_2, img_3, img_4, img_5, imageView, textView, channel);
        } else if (contact != null) {
            img_1.setVisibility(View.GONE);
            img_2.setVisibility(View.GONE);
            img_3.setVisibility(View.GONE);
            img_4.setVisibility(View.GONE);
            img_5.setVisibility(View.GONE);
            loadContactImage(imageView, textView, contact);
        }
    }

    /**
     * This method loads the image into ImageView using Glide.
     *
     * @param imageView        CircularImageView
     * @param textImage        TextView
     * @param imageUrl         Image Url
     * @param placeholderImage The res id for the placeholder image
     */
    private void loadImage(final ImageView imageView, final TextView textImage, String imageUrl, int placeholderImage) {

        Glide.with(context).load(imageUrl).placeholder(placeholderImage).error(placeholderImage).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                Log.e("LLL_rez_error: ", e.getMessage());
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                Log.e("LLL_res_ready: ", imageUrl);
                return false;
            }
        }).into(imageView);
    }

    /**
     * This method launches the Message thread from conversation click.
     *
     * @param message
     */
    public void handleConversationClick(Message message) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("takeOrder", true);
        if (message.getGroupId() == null) {
            intent.putExtra(ConversationUIService.USER_ID, message.getContactIds());
        } else {
            intent.putExtra("groupId", message.getGroupId());
        }
        context.startActivity(intent);
    }
}
