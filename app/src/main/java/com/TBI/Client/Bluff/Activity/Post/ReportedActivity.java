package com.TBI.Client.Bluff.Activity.Post;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.TBI.Client.Bluff.Activity.Profile.OtherUserProfile;
import com.TBI.Client.Bluff.Activity.Profile.ProfilePage;
import com.TBI.Client.Bluff.Fragment.Look_Fragment;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportedActivity extends AppCompatActivity {

    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.rl_spam)
    RelativeLayout rl_spam;
    @BindView(R.id.rl_inappropriate)
    RelativeLayout rl_inappropriate;

    int position, u_id;
    String come = "";
    String report = "";
    int status = 0;
    String spam_status1 = "";
    String spam_status2 = "";
    Dialog dialog;

    String post_id = "";
    int reported = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reported);

        ButterKnife.bind(ReportedActivity.this);
        AndroidNetworking.initialize(ReportedActivity.this);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        u_id = Objects.requireNonNull(getIntent().getExtras()).getInt("u_id");
        position = Objects.requireNonNull(getIntent().getExtras()).getInt("position");
        come = getIntent().getExtras().getString("come");
        report = getIntent().getExtras().getString("report");

        if (come.equalsIgnoreCase("wall")) {
            post_id = Look_Fragment.postarray.get(position).getId() + "";
        } else if (come.equalsIgnoreCase("profile")) {
            post_id = ProfilePage.postarray.get(position).getId() + "";
        } else if (come.equalsIgnoreCase("otherprofile")) {
            post_id = OtherUserProfile.postarray.get(position).getId() + "";
        }

        getPostLike();
        spam_status1 = "This is spam";
        spam_status2 = "This is inappropriate";

        if (report.equals("post")){
            rl_spam.setOnClickListener(v -> {
                if (reported == 0) {
                    spamPost(1, spam_status1);
                } else {
                    spamPost(0, spam_status1);
                }
            });

            rl_inappropriate.setOnClickListener(v -> {
                if (reported == 0) {
                    spamPost(1, spam_status2);
                } else {
                    spamPost(0, spam_status2);
                }
            });
        } else {
            rl_spam.setOnClickListener(v -> {
                if (reported == 0) {
                    spamUser(1, spam_status1);
                } else {
                    spamUser(0, spam_status1);
                }
            });

            rl_inappropriate.setOnClickListener(v -> {
                if (reported == 0) {
                    spamUser(1, spam_status2);
                } else {
                    spamUser(0, spam_status2);
                }
            });
        }

    }

    private void spamPost(int status, String spam_status) {
        AndroidNetworking.post(geturl.BASE_URL + "report_post")
                .addHeaders("Authorization", sharedpreference.getUserToken(ReportedActivity.this))
                .addBodyParameter("user_id", String.valueOf(u_id))
                .addBodyParameter("post_id", post_id)
                .addBodyParameter("reason", spam_status)
                .addBodyParameter("status", String.valueOf(status))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                dialog = new Dialog(ReportedActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                                dialog.setContentView(R.layout.dialoge_submited_report);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
                                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                dialog.show();

                                ImageView img_back;
                                img_back = dialog.findViewById(R.id.img_back);

                                img_back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (dialog != null && dialog.isShowing())
                                            dialog.dismiss();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            Log.e("LLLLLL_Report_Error: ", e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLLLL_Report_Error: ", anError.getLocalizedMessage());
                    }
                });
    }

    private void spamUser(int status, String spam_status) {
        AndroidNetworking.post(geturl.BASE_URL + "report_user")
                .addHeaders("Authorization", sharedpreference.getUserToken(ReportedActivity.this))
                .addBodyParameter("user_id", String.valueOf(u_id))
                .addBodyParameter("reported_by", sharedpreference.getUserId(ReportedActivity.this))
                .addBodyParameter("reason", spam_status)
                .addBodyParameter("status", String.valueOf(status))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                dialog = new Dialog(ReportedActivity.this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
                                dialog.setContentView(R.layout.dialoge_submited_report);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.black);
                                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                                dialog.show();

                                ImageView img_back;
                                img_back = dialog.findViewById(R.id.img_back);

                                img_back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (dialog != null && dialog.isShowing())
                                            dialog.dismiss();
                                    }
                                });

                            }
                        } catch (JSONException e) {
                            Log.e("LLLLLL_Report_Error: ", e.getLocalizedMessage());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLLLL_Report_Error: ", anError.getLocalizedMessage());
                    }
                });
    }

    private void getPostLike() {
        AndroidNetworking.post(geturl.BASE_URL + "view_post")
                .addHeaders("Authorization", sharedpreference.getUserToken(ReportedActivity.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(ReportedActivity.this))
                .addBodyParameter("post_id", post_id)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            reported = response.getInt("reported");

                            Log.e("LLLLLL_Like_Point: ", String.valueOf(reported));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLLLL_Like_Error: ", anError.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        else {
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}
