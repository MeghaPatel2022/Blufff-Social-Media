package com.TBI.Client.Bluff.Fragment;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.TBI.Client.Bluff.Activity.BannerDetail;
import com.TBI.Client.Bluff.Activity.BusinessForm;
import com.TBI.Client.Bluff.Activity.Post.PostDetailPage1;
import com.TBI.Client.Bluff.Adapter.WallPage.CatchAdapter;
import com.TBI.Client.Bluff.Model.GetDiscover.GetCatch;
import com.TBI.Client.Bluff.Model.GetDiscover.Post;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class Catch_Fragment extends Fragment {

    @BindView(R.id.listwall)
    ListView listwall;
    @BindView(R.id.imgsearch)
    FloatingActionButton imgsearch;
    @BindView(R.id.griddiscover)
    GridView griddiscover;
    Boolean loading = false;

   /* WallList_Adapter wallList_adapter;
    List<DatumProfession> peoplearray = new ArrayList<DatumProfession>();*/

    ConnectionDetector cd;
    boolean isInternetPresent = false;

    CatchAdapter catchAdapter;
    List<Post> discoverarray = new ArrayList<Post>();
    List<Integer> exculdeid = new ArrayList<Integer>();
    Dialog dialog_discover;
    WebView weburl;
    ImageView imgsubscribed, imgshare, imgclose;
    TextView txtsubscribed, txtthirdparty;
    LinearLayout lnthird, lnsubscribed;
    AVLoadingIndicatorView avloading;
    GestureDetector gestureDetector;
    LinearLayout lnbottom;
    private int visibleThreshold = 6;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fagment_discover, container, false);
        ButterKnife.bind(this, view);

        lnbottom = getActivity().findViewById(R.id.lnbottom);

        catchAdapter = new CatchAdapter(getActivity(), discoverarray);
        griddiscover.setAdapter(catchAdapter);

        discoverarray.add(new Post());

        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();

        if (!isInternetPresent) {
            Toasty.warning(getActivity(), "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
        } else {
            Get_Discover();
        }


        griddiscover.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (loading) {

                    Boolean c = (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold);
                    //Log.v("result", String.valueOf(c));

                    if (loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {

                        Log.d("mn13load", "enter");
                        loading = false;
                        Get_Discover();
                    }
                }

            }
        });


        griddiscover.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    Intent i1 = new Intent(getContext(), BusinessForm.class);
                    getContext().startActivity(i1);
                    getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    if (discoverarray.get(i).getType().equalsIgnoreCase("post")) {

                        Intent i1 = new Intent(getContext(), PostDetailPage1.class);
                        i1.putExtra("post_id", discoverarray.get(i).getId() + "");
                        i1.putExtra("comment", "no");
                        i1.putExtra("showsubscribe", "yes");
                        getContext().startActivity(i1);
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    } else if (discoverarray.get(i).getType().equalsIgnoreCase("banner")) {

                        Log.d("mn13banner", discoverarray.get(i).toString());

                        Intent i1 = new Intent(getContext(), BannerDetail.class);
                        i1.putExtra("post_id", discoverarray.get(i).getId() + "");
                        getContext().startActivity(i1);
                        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    } else {
                        showdialog(i);
                    }
                }
            }
        });

//        if (lnbottom.getVisibility() == View.VISIBLE)
//            lnbottom.setVisibility(View.INVISIBLE);

        return view;
    }

    public void showdialog(int i) {


        Log.d("mn13discover", discoverarray.get(i).toString() + "");
        dialog_discover = new Dialog(getActivity());
        dialog_discover.setContentView(R.layout.dialog_discover);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog_discover.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        int dialogWindowHeight = (int) (displayHeight * 0.90f);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = dialogWindowHeight;
        layoutParams.gravity = Gravity.BOTTOM;
        dialog_discover.getWindow().setAttributes(layoutParams);

        dialog_discover.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_discover.getWindow().getAttributes().windowAnimations = R.style.animationName;


        weburl = dialog_discover.findViewById(R.id.weburl);
        avloading = dialog_discover.findViewById(R.id.avloading);
        imgsubscribed = dialog_discover.findViewById(R.id.imgsubscribed);
        imgshare = dialog_discover.findViewById(R.id.imgshare);
        imgclose = dialog_discover.findViewById(R.id.imgclose);
        txtsubscribed = dialog_discover.findViewById(R.id.txtsubscribed);
        txtthirdparty = dialog_discover.findViewById(R.id.txtthirdparty);
        lnthird = dialog_discover.findViewById(R.id.lnthird);
        lnsubscribed = dialog_discover.findViewById(R.id.lnsubscribed);
        avloading.setVisibility(View.VISIBLE);
        avloading.show();
        txtthirdparty.setText(discoverarray.get(i).getType() + "");

        if (!discoverarray.get(i).getPhoto().equals("") && !discoverarray.get(i).getPhoto().equals(null) && !discoverarray.get(i).getPhoto().equals("null")) {
            Glide.with(getContext())
                    .load(discoverarray.get(i).getPhoto())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .dontAnimate()
                    .into(imgshare);
        }

        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (dialog_discover.isShowing()) {
                    dialog_discover.dismiss();

                }
            }
        });

        if (!discoverarray.get(i).getUsername().equalsIgnoreCase("")) {

            weburl.getSettings().setJavaScriptEnabled(true);
            weburl.setWebViewClient(new WebViewClient() {
                @SuppressWarnings("deprecation")
                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getContext(), description, Toast.LENGTH_SHORT).show();
                }

                @TargetApi(android.os.Build.VERSION_CODES.M)
                @Override
                public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                    // Redirect to deprecated method, so you can use it in all SDK versions
                    onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                }
            });

            weburl.loadUrl(discoverarray.get(i).getUsername());

            weburl.setWebViewClient(new WebViewClient() {

                public void onPageFinished(WebView view, String url) {
                    avloading.setVisibility(View.GONE);
                    Log.d("mn13webviewload", "complte");
                    // do your stuff here
                }
            });


        }

        if (!dialog_discover.isShowing()) {
            dialog_discover.show();

        }

        if (discoverarray.get(i).getFollowPost() == 0) {

            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.gesture1));
            txtsubscribed.setText("Subscribed");
        } else {
            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.blufffscribed));
            txtsubscribed.setText("Unsubscribed");
        }


        lnsubscribed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cd = new ConnectionDetector(getActivity());
                isInternetPresent = cd.isConnectingToInternet();

                if (!isInternetPresent) {
                    Toasty.warning(getActivity(), "Please check your internet connection and try again!", Toast.LENGTH_LONG, true).show();
                } else {
                    FollowPost(i);
                }


            }
        });


    }


    public void FollowPost(int i) {

        Log.d("mn13post", discoverarray.get(i).toString());
        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(getActivity())));
        paramArrayList.add(new param("post_id", discoverarray.get(i).getId() + ""));
        paramArrayList.add(new param("type", discoverarray.get(i).getType() + ""));
        if (discoverarray.get(i).getFollowPost() == 0) {
            paramArrayList.add(new param("status", "1"));
        } else {
            paramArrayList.add(new param("status", "0"));
        }


        new geturl().getdata(getActivity(), new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");

                    if (message) {
                        if (discoverarray.get(i).getFollowPost() == 0) {
                            discoverarray.get(i).setFollowPost(1);
                        } else {
                            discoverarray.get(i).setFollowPost(0);
                        }

                        if (discoverarray.get(i).getFollowPost() == 0) {

                            //   if (sharedpreference.getTheme(getActivity()).equalsIgnoreCase("white")) {
                            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.gesture1));
                            txtsubscribed.setText("Subscribed");
                           /* } else {
                                imgsubscribed.setImageDrawable(getResources().getDrawable(RM.drawable.gesture1_white));
                            }*/
                        } else {
                            // if (sharedpreference.getTheme(getActivity()).equalsIgnoreCase("white")) {
                            imgsubscribed.setImageDrawable(getResources().getDrawable(R.drawable.blufffscribed));
                            txtsubscribed.setText("Unsubscribed");
                            /*} else {
                                imgsubscribed.setImageDrawable(getResources().getDrawable(RM.drawable.blufffscribed_white));
                            }*/
                        }


                    } else {
                        // Toasty.error(getActivity(), status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    // Toasty.error(Login.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }
            }
        }, paramArrayList, "followPost");
    }

    public void Get_Discover() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(getActivity())));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));
        paramArrayList.add(new param("location", "start"));
        paramArrayList.add(new param("exclude_post_ids", TextUtils.join(",", exculdeid).replaceAll(",$", "")));

        new geturl().getdata(getActivity(), new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {

                try {
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");

                    if (message) {
                        GetCatch login = new Gson().fromJson(data, GetCatch.class);
                        List<Post> temparray = login.getPosts();
                        catchAdapter.addAll(temparray);
                        discoverarray.addAll(temparray);

                        loading = !temparray.isEmpty();

                    } else {
                        // Toasty.error(getActivity(), status + "", Toast.LENGTH_LONG, true).show();
                    }
                } catch (Exception e) {
                    // Toasty.error(Login.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }
                exculdeid.clear();
                for (int i = 0; i < discoverarray.size(); i++) {
                    exculdeid.add(discoverarray.get(i).getId());
                }
            }
        }, paramArrayList, "get_post_near_user");
    }

}
