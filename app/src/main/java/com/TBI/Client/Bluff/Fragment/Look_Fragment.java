package com.TBI.Client.Bluff.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.TBI.Client.Bluff.Activity.Post.CreateStory;
import com.TBI.Client.Bluff.Adapter.Home.ScrollAdapter;
import com.TBI.Client.Bluff.Adapter.Post.Stories_Adapter;
import com.TBI.Client.Bluff.Adapter.WallPage.Feed_Adapter;
import com.TBI.Client.Bluff.Database.DBHelper;
import com.TBI.Client.Bluff.Model.GetFeed.GetFeed;
import com.TBI.Client.Bluff.Model.GetFeed.Image;
import com.TBI.Client.Bluff.Model.GetFeed.NearByUser;
import com.TBI.Client.Bluff.Model.GetFeed.PersonYouKnow;
import com.TBI.Client.Bluff.Model.GetFeed.Rating;
import com.TBI.Client.Bluff.Model.GetFeed.RequestedUser;
import com.TBI.Client.Bluff.Model.GetFeed.Stock;
import com.TBI.Client.Bluff.Model.GetFeed.Story;
import com.TBI.Client.Bluff.Model.GetProfile.Post;
import com.TBI.Client.Bluff.Model.PostDetail.Comment;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.StoryStatus.StatusStoriesActivity;
import com.TBI.Client.Bluff.Utils.ConnectionDetector;
import com.TBI.Client.Bluff.Utils.MyAsyncTaskCallback;
import com.TBI.Client.Bluff.Utils.Public_Function;
import com.TBI.Client.Bluff.Utils.geturl;
import com.TBI.Client.Bluff.Utils.param;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.widget.Container;

import static com.TBI.Client.Bluff.Adapter.Post.Stories_Adapter.selectmodel;

public class Look_Fragment extends Fragment {


    public static ImageView imgadd2;
    //    public static LinearLayout lnforeground;
    public static Stories_Adapter stories_adapter;
    public static List<Post> postarray = new ArrayList<>();
    public static Feed_Adapter demo_adapter;
    public static boolean is_simmar = true;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    public List<Post> demo = new ArrayList<>();
    public List<Rating> ratings = new ArrayList<>();
    public List<NearByUser> nearByUsers = new ArrayList<>();
    public List<PersonYouKnow> personYouKnows = new ArrayList<>();
    public List<RequestedUser> requestedUsers = new ArrayList<>();
    public List<Integer> postid = new ArrayList<>();

    @BindView(R.id.recystory)
    RecyclerView recystory;
    @BindView(R.id.txtnewpost)
    TextView txtnewpost;
    @BindView(R.id.marqueList)
    RecyclerView marqueList;
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            final int duration = 50;
            final int pixelsToMove = 10;
            marqueList.smoothScrollBy(pixelsToMove, 0);
            mHandler.postDelayed(this, duration);
        }
    };
    @BindView(R.id.txtusername)
    TextView txtusername;
    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipe_container;
    @BindView(R.id.scrollload)
    NestedScrollView scrollload;
    @BindView(R.id.imgadd)
    ImageView imgadd;
    @BindView(R.id.imgadd1)
    ImageView imgadd1;
    @BindView(R.id.recyclefeed)
    Container recyclefeed;
    @BindView(R.id.simmar_id1)
    ShimmerFrameLayout simmar_id1;
    @BindView(R.id.simmar_id2)
    ShimmerFrameLayout simmar_id2;
    @BindView(R.id.simmar_id3)
    ShimmerFrameLayout simmar_id3;
    @BindView(R.id.simmar_id4)
    ShimmerFrameLayout simmar_id4;
    @BindView(R.id.simmar_id5)
    ShimmerFrameLayout simmar_id5;
    @BindView(R.id.simmar_id6)
    ShimmerFrameLayout simmar_id6;
    @BindView(R.id.ll_simmar)
    LinearLayout ll_simmar;
    @BindView(R.id.llMian)
    LinearLayout llMian;
    @BindView(R.id.rlLoading)
    RelativeLayout rlLoading;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

    int currentOffset;
    List<Story> storyarray = new ArrayList<Story>();
    Dialog dialo_newpost;
    ConnectionDetector cd;
    boolean isInternetPresent = false;
    List<Stock> stockarray = new ArrayList<>();
    List<Post> tempPostList = new ArrayList<>();
    ScrollAdapter scrollAdapter;
    int random;
    boolean isLoadData = true;
    boolean flag = false;
    List<Story> userstory = new ArrayList<Story>();
    LinearLayout lnbottom;
    DBHelper dbHelper;
    GridLayoutManager gridLayoutManager;
    private boolean foundTotalPixel = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private int totalMovedPixel;
    private int totalPixel;

    void startAnim(){
        rlLoading.setVisibility(View.VISIBLE);
        avi.smoothToShow();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        rlLoading.setVisibility(View.GONE);
        avi.hide();
        // or avi.smoothToHide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_look, container, false);
        ButterKnife.bind(this, view);

        dbHelper = new DBHelper(getContext());

        simmar_id1.startShimmer();
        simmar_id2.startShimmer();
        simmar_id3.startShimmer();
        simmar_id4.startShimmer();
        simmar_id5.startShimmer();
        simmar_id6.startShimmer();

        imgadd2 = view.findViewById(R.id.imgadd2);
        storyarray = new ArrayList<>();
        imgadd.setClipToOutline(true);
        imgadd1.setClipToOutline(true);
        imgadd2.setClipToOutline(true);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int devicewidth = (int) (displaymetrics.widthPixels / 7.3);
        setimagewidth(devicewidth);

        lnbottom = getActivity().findViewById(R.id.lnbottom);

        txtusername.setText(sharedpreference.getusername(getActivity()) + "");

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        marqueList.setLayoutManager(layoutManager);

        random = generaterandom();
        Log.d("mn13random", generaterandom() + "");
        demo_adapter = new Feed_Adapter(getActivity(), demo, ratings, nearByUsers, personYouKnows, requestedUsers, Look_Fragment.this);
//        demo_adapter.randome(random);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        ViewCompat.setNestedScrollingEnabled(recyclefeed, false);
        recyclefeed.setLayoutManager(gridLayoutManager);
        recyclefeed.setAdapter(demo_adapter);
        recyclefeed.setNestedScrollingEnabled(false);

        marqueList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalMovedPixel = totalMovedPixel + dx;
                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                if (foundTotalPixel) {
                    if (totalItemCount > 2) {
                        View headerView = layoutManager.getChildAt(0);
                        View itemView = layoutManager.getChildAt(1);

                        if (itemView != null && headerView != null) {
                            totalPixel = /*-c.getTop() +*/ ((totalItemCount - 2) * itemView.getWidth()) + (1 * headerView.getWidth());
                            Log.v("...", "Total pixel x!" + totalPixel);
                            foundTotalPixel = false;
                        }
                    }
                }

                if (!foundTotalPixel && totalMovedPixel >= totalPixel) {
                    Log.v("...", "Last Item Wow !");
                    Log.v("...", "totalMovedPixel !" + totalMovedPixel);
                  /*  marqueList.setAdapter(null);
                    marqueList.setAdapter(scrollAdapter);*/
                    scrollAdapter = new ScrollAdapter(getActivity(), ratings);
                    marqueList.setAdapter(scrollAdapter);

                    // scrollAdapter.addAll(stockarray, getActivity());
                    pastVisiblesItems = visibleItemCount = totalItemCount = 0;
                    totalMovedPixel = 0;

                }
            }
        });

        llMian.setVisibility(View.GONE);
        ll_simmar.setVisibility(View.VISIBLE);
        apicalling();
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoadData = true;
                random = generaterandom();
                demo_adapter.removeAll();
                demo_adapter.removRatingeAll();
                demo_adapter.removPoepleKnowAll();
                demo_adapter.removRequestKnowAll();
                demo_adapter.removNearByAll();
//                demo_adapter.randome(random);
                startAnim();
                postarray = new ArrayList<>();
                postid = new ArrayList<>();
                flag = false;
                stockarray = new ArrayList<Stock>();
                ratings = new ArrayList<Rating>();
                storyarray = new ArrayList<Story>();
                userstory = new ArrayList<Story>();
                llMian.setVisibility(View.GONE);
                ll_simmar.setVisibility(View.VISIBLE);
                simmar_id1.startShimmer();
                simmar_id2.startShimmer();
                simmar_id3.startShimmer();
                simmar_id4.startShimmer();
                simmar_id5.startShimmer();
                simmar_id6.startShimmer();
                apicalling();
            }
        });

        imgadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userstory.isEmpty()) {
                    Intent i1 = new Intent(getActivity(), CreateStory.class);
                    startActivity(i1);
                    getActivity().overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
                } else {

                    boolean isCacheEnabled = true;
                    boolean isImmersiveEnabled = false;
                    boolean isTextEnabled = false;
                    long storyDuration = 5000L;

                    selectmodel = userstory.get(0);
                    Intent a = new Intent(getActivity(), StatusStoriesActivity.class);
                    a.putExtra("from", "fragment");
                    a.putExtra(StatusStoriesActivity.STATUS_RESOURCES_KEY, userstory.get(0));
                    a.putExtra(StatusStoriesActivity.STATUS_DURATION_KEY, storyDuration);
                    a.putExtra(StatusStoriesActivity.IS_IMMERSIVE_KEY, isImmersiveEnabled);
                    a.putExtra(StatusStoriesActivity.IS_CACHING_ENABLED_KEY, isCacheEnabled);
                    a.putExtra(StatusStoriesActivity.IS_TEXT_PROGRESS_ENABLED_KEY, isTextEnabled);
                    startActivity(a);

                }
            }
        });

//        scrollload.smoothScrollTo(0,0);
//        scrollload.pageScroll(ScrollView.FOCUS_DOWN);
//        scrollload.fullScroll(ScrollView.FOCUS_DOWN);
        scrollload.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                            scrollY > oldScrollY) {

                        visibleItemCount = gridLayoutManager.getChildCount();
                        totalItemCount = gridLayoutManager.getItemCount();
                        pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                        Log.e("LLLLL_couhnt: ","Visible: "+visibleItemCount+"   totalItem: "+totalItemCount+
                                "   pastVisible: "+pastVisiblesItems);

                        if (isLoadData) {
                            if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                                isLoadData = false;
                                GetFeed();
                            }
                        }
                    }
                }
            }
        });


        txtnewpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showdialog();
            }
        });


        return view;
    }

    public int generaterandom() {
        Random r = new Random();
        int i1 = r.nextInt(10) + 0;
        if (i1 % 2 == 0) {
            i1 = 3;
        } else {
            i1 = 5;
        }
        Log.d("mn13post:", i1 + "");
        return i1;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!sharedpreference.getphoto(getActivity()).equals("") && !sharedpreference.getphoto(getActivity()).equals(null) && !sharedpreference.getphoto(getActivity()).equals("null")) {
            Glide.with(getContext())
                    .load(sharedpreference.getphoto(getActivity()))
                    .placeholder(R.drawable.story_placeholder)
                    .error(R.drawable.story_placeholder)
                    .into(imgadd);
        } else {

        }

//        if (lnforeground != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                lnforeground.getForeground().setAlpha(0);
//            }
//        }


        if (sharedpreference.getTheme(getActivity()).equalsIgnoreCase("white")) {
            getActivity().getTheme().applyStyle(R.style.ActivityTheme_Primary_Base_Light, true);
        } else {
            getActivity().getTheme().applyStyle(R.style.ActivityTheme_Primary_Base_Dark, true);
        }

    }

    public void showdialog() {


        dialo_newpost = new Dialog(getActivity());
        dialo_newpost.setContentView(R.layout.dialog_newpost);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayWidth = displayMetrics.widthPixels;
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialo_newpost.getWindow().getAttributes());
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        int dialogWindowHeight = (int) (displayHeight * 0.75f);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        dialo_newpost.getWindow().setAttributes(layoutParams);

        dialo_newpost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialo_newpost.getWindow().getAttributes().windowAnimations = R.style.animationName;


        if (!dialo_newpost.isShowing()) {
            dialo_newpost.show();
        }
    }

    public void apicalling() {

        cd = new ConnectionDetector(getActivity());
        isInternetPresent = cd.isConnectingToInternet();
        startAnim();
        GetFeed();
    }

    public void GetFeed() {

        ArrayList<param> paramArrayList = new ArrayList<>();
        String id = TextUtils.join(",", postid).replaceAll(",$", "");
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(getActivity())));
        paramArrayList.add(new param("exclude_post_ids", TextUtils.join(",", postid).replaceAll(",$", "")));
        paramArrayList.add(new param("timezone", Public_Function.gettimezone()));

        Log.e("LLLLLL_Exclude_id: ", postid.toString() + "   id: " + id);

        if (!isInternetPresent) {
                tempPostList.addAll(getPostRecords());
                ratings.addAll(getRateRecords());
                if (!postarray.isEmpty()) {
                    postarray.clear();
                    postarray.addAll(tempPostList);
                } else {
                    postarray.addAll(tempPostList);
                }
                stopAnim();

                demo_adapter.addAll(postarray);
                demo_adapter.adRatingdAll(ratings);
                simmar_id1.stopShimmer();
                simmar_id2.stopShimmer();
                simmar_id3.stopShimmer();
                simmar_id4.stopShimmer();
                simmar_id5.stopShimmer();
                simmar_id6.stopShimmer();
                llMian.setVisibility(View.VISIBLE);
                ll_simmar.setVisibility(View.GONE);
        } else {

            new geturl().getdata(getActivity(), data -> {

                imgadd2.setBackground(getResources().getDrawable(R.drawable.round_box2));
                mHandler.post(SCROLLING_RUNNABLE);

                swipe_container.setRefreshing(false);
                tempPostList = new ArrayList<>();
                stockarray = new ArrayList<>();
                ratings = new ArrayList<>();
                postid = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");

                    Log.e("LLLLLL_Response: ", data);

                    if (message) {
                        GetFeed login = new Gson().fromJson(data, GetFeed.class);
                        if (login.getPosts().size() > getPostRecords().size()) {
                            isLoadData = true;
                            for (int i = 0; i < login.getPosts().size(); i++) {
                                if (!dbHelper.chceckPostExits(login.getPosts().get(i).getId(), login.getPosts().get(i).getUserId())) {
                                    dbHelper.insertOtherUserPost(login.getPosts().get(i).getId(),
                                            login.getPosts().get(i).getUserId(),
                                            login.getPosts().get(i).getDescription(),
                                            login.getPosts().get(i).getLocation(),
                                            login.getPosts().get(i).getLat(),
                                            login.getPosts().get(i).getLang(),
                                            login.getPosts().get(i).getUsername(),
                                            login.getPosts().get(i).getFullName(),
                                            login.getPosts().get(i).getPhoto(),
                                            login.getPosts().get(i).getImage(),
                                            login.getPosts().get(i).getFileType(),
                                            object.getJSONArray("posts").getJSONObject(i).getJSONArray("images") + "",
                                            object.getJSONArray("posts").getJSONObject(i).getJSONArray("comments") + "",
                                            login.getPosts().get(i).getMultipleImages(),
                                            login.getPosts().get(i).getBookmarked(),
                                            login.getPosts().get(i).getMore_comments(),
                                            login.getPosts().get(i).getTime_duration(),
                                            login.getPosts().get(i).getHeight(),
                                            login.getPosts().get(i).getWidth());

                                } else {
                                    Log.e("LLL_Data: ", "....");
                                }
                            }
                        }

                        Log.e("LLL_Length: ", getPostRecords().size() + "");
                        tempPostList.addAll(getPostRecords());


                        Log.d("LLLLL_Data_size:", postarray.size() + "");
                        Log.d("LLLLL_Data_size2:", login.getPeopleYouKnow().size() + "");
                        Log.d("LLLLL_Data_size3:", login.getNearByUsers().size() + "");
                        Log.d("LLLLL_Data_size4:", login.getRequestedUsers().size() + "");
                        for (int i = 0; i < login.getPosts().size(); i++) {
                            postid.add(login.getPosts().get(i).getId());
                        }
                        Log.d("LLLLL_Data_size1:", postid.size() + "");
                        List<Post> pos = new ArrayList<>();
                        for (int i = 0; i < login.getPosts().size(); i++) {
                            Post post = login.getPosts().get(i);
                            if (!post.getUserId().equals(sharedpreference.getUserId(getContext()))) {
                                if (!post.getImage().equals("")) {
                                    pos.add(login.getPosts().get(i));
                                }
                            }
                        }
                        postarray.addAll(pos);
                        demo_adapter.addAll(pos);
                        simmar_id1.stopShimmer();
                        simmar_id2.stopShimmer();
                        simmar_id3.stopShimmer();
                        simmar_id4.stopShimmer();
                        simmar_id5.stopShimmer();
                        simmar_id6.stopShimmer();
                        llMian.setVisibility(View.VISIBLE);
                        ll_simmar.setVisibility(View.GONE);
                        stopAnim();

                        stockarray = login.getStocks();
                        scrollAdapter = new ScrollAdapter(getActivity(), login.getRating());
                        marqueList.setAdapter(scrollAdapter);

                        personYouKnows.addAll(login.getPeopleYouKnow());
                        demo_adapter.addPeopleKnowAll(login.getPeopleYouKnow(),
                                                    login.getRequestedUsers(),
                                                    login.getNearByUsers());

                        requestedUsers.addAll(login.getRequestedUsers());
                        nearByUsers.addAll(login.getNearByUsers());


                        if (login.getPeopleYouKnow().isEmpty() && login.getRequestedUsers().isEmpty() && login.getNearByUsers().isEmpty()){
                            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(int position) {
                                        return 1;
                                }
                            });
                        } else if (!login.getPeopleYouKnow().isEmpty() && !login.getRequestedUsers().isEmpty() && !login.getNearByUsers().isEmpty()){
                            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(int position) {
                                    if (position >= 4 && position < 9)
                                        return (position != 4) ? 1 : 2;
                                    if (position >= 9 && position < 14)
                                        return (position != 9) ? 1 : 2;
                                    if (position >= 14)
                                        return (position != 14) ? 1 : 2;
                                    else
                                        return 1;
                                }
                            });
                        } else if (login.getPeopleYouKnow().isEmpty() && !login.getRequestedUsers().isEmpty() && !login.getNearByUsers().isEmpty()){
                            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(int position) {
                                    if (position >= 8 && position < 13)
                                        return (position != 8) ? 1 : 2;
                                    if (position >= 13)
                                        return (position != 13) ? 1 : 2;
                                    else
                                        return 1;
                                }
                            });
                        } else if (!login.getPeopleYouKnow().isEmpty() && requestedUsers.isEmpty() && !login.getNearByUsers().isEmpty()){
                            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(int position) {
                                    if (position >= 4 && position < 13)
                                        return (position != 4) ? 1 : 2;
                                    if (position >= 13)
                                        return (position != 13) ? 1 : 2;
                                    else
                                        return 1;
                                }
                            });
                        } else if (login.getPeopleYouKnow().isEmpty() && !login.getRequestedUsers().isEmpty() && login.getNearByUsers().isEmpty()){
                            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                                @Override
                                public int getSpanSize(int position) {
                                    if (position >= 8)
                                        return (position != 8) ? 1 : 2;
                                    else
                                        return 1;
                                }
                            });
                        }

                        recyclefeed.setLayoutManager(gridLayoutManager);
                        demo_adapter.notifyDataSetChanged();
                        Log.e("LLLLLL_Data: ",login.getPeopleYouKnow()+"       "+login.getRequestedUsers()+"       "+login.getNearByUsers());

                        if (!flag) {

                            ratings = login.getRating();
                            demo_adapter.adRatingdAll(login.getRating());
//                            for (int i = 0; i < login.getPosts().size()-1; i++) {
//                                if (!dbHelper.chceckUserRateDetails(login.getRating().get(i).getUserId())) {
//                                    Rating rating = login.getRating().get(i);
//                                    dbHelper.insertUserRateDetails(rating.getUserId(),
//                                            rating.getName(),
//                                            rating.getPhoto(),
//                                            rating.getValue(),
//                                            rating.getActivity());
//                                } else {
//                                    Rating rating = login.getRating().get(i);
//                                    dbHelper.updateUserRateDetails(rating.getUserId(),
//                                            rating.getName(),
//                                            rating.getPhoto(),
//                                            rating.getValue(),
//                                            rating.getActivity());
//                                }
//                            }


                            storyarray = login.getStories();
                            stories_adapter = new Stories_Adapter(getActivity(), storyarray);
                            recystory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                            recystory.setAdapter(stories_adapter);

                            userstory = login.getUserStories();
                            if (userstory.isEmpty()) {
                                imgadd2.setImageDrawable(getResources().getDrawable(R.mipmap.ic_add));
                                imgadd1.setVisibility(View.VISIBLE);
                                imgadd2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.transparent)));
                            } else {
                                imgadd2.setImageDrawable(null);
                                imgadd1.setVisibility(View.GONE);
                                if (userstory.get(0).getAll_seen() == 1) {
                                    imgadd2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.darkgrey)));
                                } else {
                                    imgadd2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.textcolor)));
                                }

                                imgadd.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View view) {
                                        Intent i1 = new Intent(getActivity(), CreateStory.class);
                                        startActivity(i1);
                                        getActivity().overridePendingTransition(R.anim.slide_enter, R.anim.slide_out);
                                        return true;
                                    }
                                });

                            }
                        }

                        flag = true;

                    } else {
                        if (postarray.isEmpty()) {
                            tempPostList.addAll(getPostRecords());
                            ratings.addAll(getRateRecords());
                            if (!postarray.isEmpty()) {
                                postarray.clear();
                                postarray.addAll(tempPostList);
                            } else {
                                postarray.addAll(tempPostList);
                            }
                            simmar_id1.stopShimmer();
                            simmar_id2.stopShimmer();
                            simmar_id3.stopShimmer();
                            simmar_id4.stopShimmer();
                            simmar_id5.stopShimmer();
                            simmar_id6.stopShimmer();
                            llMian.setVisibility(View.VISIBLE);
                            ll_simmar.setVisibility(View.GONE);

                            Log.d("LLLLL_Data_size1:", postid.size() + "");
                            List<Post> pos = new ArrayList<>();
                            for (int i = 0; i < postarray.size(); i++) {
                                Post post = postarray.get(i);
                                if (!post.getImage().equals("")) {
                                    pos.add(postarray.get(i));
                                }
                            }

                            demo_adapter.addAll(pos);

                            demo_adapter.addAll(postarray);
                            demo_adapter.adRatingdAll(ratings);
                        }
                    }
                } catch (Exception e) {
                    Log.e(": ", e.getMessage());
                    e.printStackTrace();
                }
            }, paramArrayList, "get_feed");
        }
    }

    private ArrayList<Post> getPostRecords() {
        Cursor cur = dbHelper.getAllOtgherPost();
        ArrayList<Post> posts = new ArrayList<>();
        ArrayList<Image> images = null;
        ArrayList<Comment> comments = new ArrayList<>();
        Image[] image;
        Comment[] comment;
        Gson gson = new Gson();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {
                Post post = new Post();

                post.setId(cur.getInt(1));
                post.setUserId(cur.getInt(2));
                post.setDescription(cur.getString(3));
                post.setLocation(cur.getString(4));
                post.setLat(cur.getString(5));
                post.setLang(cur.getString(6));
                post.setUsername(cur.getString(7));
                post.setFullName(cur.getString(8));
                post.setPhoto(cur.getString(9));
                post.setImage(cur.getString(10));
//                Log.e("LLLL_data_image: ", cur.getString(11));
                post.setFileType(cur.getString(11));
                image = gson.fromJson(cur.getString(12), Image[].class);
                post.setImages(Arrays.asList(image));
                comment = gson.fromJson(cur.getString(13), Comment[].class);
                post.setComments(Arrays.asList(comment));
                post.setMultipleImages(cur.getInt(14));
                post.setBookmarked(cur.getInt(15));
                post.setMore_comments(cur.getInt(16));
                post.setTime_duration(cur.getString(17));
                post.setHeight(cur.getString(18));
                post.setWidth(cur.getString(19));

                posts.add(post);
            } while (cur.moveToNext());
            cur.close();
        }
        return posts;
    }

    private List<Rating> getRateRecords() {
        Cursor cur = dbHelper.getAllUserRateDetails();

        List<Rating> ratings = new ArrayList<>();
        if (cur.getCount() > 0) {
            cur.moveToFirst();
            do {

                Rating rating = new Rating();

                rating.setUserId(cur.getInt(1));
                rating.setName(cur.getString(2));
                rating.setPhoto(cur.getString(3));
                rating.setValue(cur.getFloat(4));
                rating.setActivity(cur.getString(5));

                ratings.add(rating);
            } while (cur.moveToNext());
            cur.close();
        }
        return ratings;
    }

    public void Save_Post(Integer id) {

        ArrayList<param> paramArrayList = new ArrayList<>();
        paramArrayList.add(new param("user_id", sharedpreference.getUserId(getActivity())));
        paramArrayList.add(new param("post_id", id + ""));
        paramArrayList.add(new param("keep", "1"));

        new geturl().getdata(getActivity(), new MyAsyncTaskCallback() {
            @Override
            public void onAsyncTaskComplete(String data) {
                try {
                    JSONObject object = new JSONObject(data);
                    boolean message = object.optBoolean("success");
                    String status = "";
                    status = object.optString("message");

                    if (message) {
                        Toast.makeText(getActivity(), status + "", Toast.LENGTH_LONG).show();
                    } else {
                    }
                } catch (Exception e) {
                    // Toasty.error(Login.this, "Oops something went wrong..", Toast.LENGTH_LONG, true).show();
                    e.printStackTrace();
                }
            }
        }, paramArrayList, "bookmark_post");
    }

    public void setimagewidth(int width) {

        imgadd.getLayoutParams().width = width;
        imgadd.getLayoutParams().height = width;

        imgadd1.getLayoutParams().width = width;
        imgadd1.getLayoutParams().height = width;

        imgadd2.getLayoutParams().width = width;
        imgadd2.getLayoutParams().height = width;

    }
}
