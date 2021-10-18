package com.applozic.mobicomkit.uiwidgets.uikit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.ContextThemeWrapper;

import com.applozic.mobicomkit.api.account.user.MobiComUserPreference;
import com.applozic.mobicomkit.api.conversation.MobiComConversationService;
import com.applozic.mobicomkit.channel.service.ChannelService;
import com.applozic.mobicomkit.contact.AppContactService;
import com.applozic.mobicomkit.contact.BaseContactService;
import com.applozic.mobicomkit.uiwidgets.R;
import com.applozic.mobicomkit.uiwidgets.async.ApplozicChannelDeleteTask;
import com.applozic.mobicomkit.uiwidgets.async.ApplozicChannelLeaveMember;
import com.applozic.mobicomkit.uiwidgets.conversation.DeleteConversationAsyncTask;
import com.applozic.mobicommons.ApplozicService;
import com.applozic.mobicommons.commons.core.utils.Utils;
import com.applozic.mobicommons.people.channel.Channel;
import com.applozic.mobicommons.people.channel.ChannelUtils;
import com.applozic.mobicommons.people.contact.Contact;

import dmax.dialog.SpotsDialog;

/**
 * Created by ashish on 30/05/18.
 */

public class AlUIService {
    private Context context;
    private BaseContactService contactService;

    public AlUIService(Context context) {
        this.context = context;
        contactService = new AppContactService(context);
    }

    static AlertDialog SpotsDialog;

    public static void Show_Progressdialog(Context context) {

        if (SpotsDialog != null) {
            if (SpotsDialog.isShowing()) {
                SpotsDialog.dismiss();
            }
        }
        SpotsDialog = new SpotsDialog.Builder()
                .setContext(context)
                .setTheme(R.style.Custom)
                .setCancelable(true)
                .build();
        SpotsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SpotsDialog.show();

    }

    public static void Hide_ProgressDialogue() {

        if (SpotsDialog != null) {
            SpotsDialog.dismiss();
        }
    }


    /**
     * This method takes channel as argument and creates a dialog alerting the user to delete the group.
     * If clicked yes, the channel delete task will be started.
     *
     * @param channel The group object that is to be deleted
     */
    public void deleteGroupConversation(final Channel channel) {

        if (!Utils.isInternetAvailable(context)) {
            showToastMessage(context.getString(R.string.you_dont_have_any_network_access_info));
            return;
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_chatdelete, null);
        alertDialog.setView(dialogLayout);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txttitle, txtmsg;
        Button txtdelete, txtcancle;

        txtdelete = dialogLayout.findViewById(R.id.txtdelete);
        txtcancle = dialogLayout.findViewById(R.id.txtcancle);
        txttitle = dialogLayout.findViewById(R.id.txttitle);
        txtmsg = dialogLayout.findViewById(R.id.txtmsg);
        txttitle.setVisibility(View.GONE);
        txtdelete.setText("Delete");

        txtdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Show_Progressdialog(context);

                ApplozicChannelDeleteTask.TaskListener channelDeleteTask = new ApplozicChannelDeleteTask.TaskListener() {
                    @Override
                    public void onSuccess(String response) {
                        Hide_ProgressDialogue();
                        alertDialog.dismiss();
                        Log.d("mn13complete", "successs");
                    }

                    @Override
                    public void onFailure(String response, Exception exception) {
                        Hide_ProgressDialogue();
                        showToastMessage(context.getString(Utils.isInternetAvailable(context) ? R.string.applozic_server_error : R.string.you_dont_have_any_network_access_info));
                    }

                    @Override
                    public void onCompletion() {
                        Hide_ProgressDialogue();
                        Log.d("mn13complete", "complete");
                    }
                };
                ApplozicChannelDeleteTask applozicChannelDeleteTask = new ApplozicChannelDeleteTask(context, channelDeleteTask, channel);
                applozicChannelDeleteTask.execute((Void) null);

            }
        });

        txtcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        txtmsg.setText(context.getString(R.string.delete_channel_messages_and_channel_info).replace(context.getString(R.string.group_name_info), channel.getName()).replace(context.getString(R.string.groupType_info), Channel.GroupType.BROADCAST.getValue().equals(channel.getType()) ? context.getString(R.string.broadcast_string) : context.getString(R.string.group_string)));
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    /**
     * This method deletes the conversation with a contact or channel.
     *
     * @param contact The contact object whose conversation thread is to be deleted
     * @param channel The group object whose conversation thread is to be deleted
     */
    public void deleteConversationThread(final Contact contact, final Channel channel) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_chatdelete, null);
        alertDialog.setView(dialogLayout);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView txttitle, txtmsg;
        Button txtdelete, txtcancle;

        txtdelete = dialogLayout.findViewById(R.id.txtdelete);
        txtcancle = dialogLayout.findViewById(R.id.txtcancle);
        txttitle = dialogLayout.findViewById(R.id.txttitle);
        txtmsg = dialogLayout.findViewById(R.id.txtmsg);
        txtdelete.setText("Delete");


        txtdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                new DeleteConversationAsyncTask(new MobiComConversationService(context), contact, channel, null, context).execute();
            }
        });

        txtcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        String name = "";
        if (channel != null) {
            if (Channel.GroupType.GROUPOFTWO.getValue().equals(channel.getType())) {
                String userId = ChannelService.getInstance(context).getGroupOfTwoReceiverUserId(channel.getKey());
                if (!TextUtils.isEmpty(userId)) {
                    Contact withUserContact = contactService.getContactById(userId);
                    name = withUserContact.getDisplayName();
                }
            } else {
                name = ChannelUtils.getChannelTitleName(channel, MobiComUserPreference.getInstance(context).getUserId());
            }
        } else {
            name = contact.getDisplayName();
        }
        txttitle.setText(context.getString(R.string.dialog_delete_conversation_title).replace("[name]", name));
        txtmsg.setText(context.getString(R.string.dialog_delete_conversation_confir).replace("[name]", name));
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    /**
     * This method takes the channel object and removes the logged in user from this channel.
     *
     * @param channel the group object from which the logged in user decides to leave.
     */
    public void channelLeaveProcess(final Channel channel) {
        if (!Utils.isInternetAvailable(context)) {
            showToastMessage(context.getString(R.string.you_dont_have_any_network_access_info));
            return;
        }
        TextView txttitle, txtmsg;
        Button txtdelete, txtcancle;

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.dialog_chatdelete, null);
        alertDialog.setView(dialogLayout);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        txtdelete = dialogLayout.findViewById(R.id.txtdelete);
        txtcancle = dialogLayout.findViewById(R.id.txtcancle);
        txttitle = dialogLayout.findViewById(R.id.txttitle);
        txtmsg = dialogLayout.findViewById(R.id.txtmsg);
        txttitle.setVisibility(View.GONE);
        txtdelete.setText("Exit");

        txtcancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        txtdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Show_Progressdialog(context);
                ApplozicChannelLeaveMember.ChannelLeaveMemberListener applozicLeaveMemberListener = new ApplozicChannelLeaveMember.ChannelLeaveMemberListener() {
                    @Override
                    public void onSuccess(String response, Context context) {
                        alertDialog.dismiss();
                        Hide_ProgressDialogue();
                    }

                    @Override
                    public void onFailure(String response, Exception e, Context context) {
                        showToastMessage(context.getString(Utils.isInternetAvailable(context) ? R.string.applozic_server_error : R.string.you_dont_have_any_network_access_info));
                        Hide_ProgressDialogue();
                    }
                };
                ApplozicChannelLeaveMember applozicChannelLeaveMember = new ApplozicChannelLeaveMember(context, channel.getKey(), MobiComUserPreference.getInstance(context).getUserId(), applozicLeaveMemberListener);
                applozicChannelLeaveMember.setEnableProgressDialog(true);
                applozicChannelLeaveMember.execute((Void) null);

            }
        });
        txtmsg.setText(context.getString(R.string.exit_channel_message_info).replace(context.getString(R.string.group_name_info), channel.getName()).replace(context.getString(R.string.groupType_info), Channel.GroupType.BROADCAST.getValue().equals(channel.getType()) ? context.getString(R.string.broadcast_string) : context.getString(R.string.group_string)));
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    /**
     * Creates a Toast message for displaying warnings and error messages
     *
     * @param messageToShow The message that will be displayed in the toast
     */
    private void showToastMessage(final String messageToShow) {
        Toast toast = Toast.makeText(context, messageToShow, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
