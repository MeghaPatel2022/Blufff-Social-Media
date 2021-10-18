package com.TBI.Client.Bluff.Activity.Post;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.TBI.Client.Bluff.Adapter.Post.LikeAdapter;
import com.TBI.Client.Bluff.Model.Like.Like;
import com.TBI.Client.Bluff.Model.Like.LikeData;
import com.TBI.Client.Bluff.R;
import com.TBI.Client.Bluff.Utils.sharedpreference;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

import static com.TBI.Client.Bluff.Utils.geturl.BASE_URL;

public class LikeActivity extends AppCompatActivity {

    @BindView(R.id.rv_postLike)
    RecyclerView rv_postLike;
    @BindView(R.id.noData)
    TextView noData;
    @BindView(R.id.img_back)
    ImageView img_back;
    @BindView(R.id.searchdiscover)
    SearchView searchdiscover;

    String PostId = "";
    LikeAdapter likeAdapter;
    List<LikeData> likeDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);

        AndroidNetworking.initialize(LikeActivity.this);
        ButterKnife.bind(LikeActivity.this);
        PostId = getIntent().getStringExtra("PostId");

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rv_postLike.setLayoutManager(new LinearLayoutManager(LikeActivity.this,RecyclerView.VERTICAL,false));
        likeAdapter = new LikeAdapter(likeDataList,LikeActivity.this);
        rv_postLike.setAdapter(likeAdapter);

        searchdiscover.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                likeAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                likeAdapter.getFilter().filter(newText);
                return false;
            }
        });

        getLike();
    }

    private void getLike(){
        AndroidNetworking.post(BASE_URL+"view_post_likes")
                .addHeaders("Authorization", sharedpreference.getUserToken(LikeActivity.this))
                .addBodyParameter("user_id", sharedpreference.getUserId(LikeActivity.this))
                .addBodyParameter("post_id", PostId)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Like login = new Gson().fromJson(response.toString(), Like.class);
                        likeAdapter.addAll(login.getData());
                        Log.e("LLLLLL_Like: ",response.toString());
                        if (login.getData().isEmpty()) {
                            noData.setVisibility(View.VISIBLE);
                            rv_postLike.setVisibility(View.GONE);
                        } else {
                            noData.setVisibility(View.GONE);
                            rv_postLike.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("LLLL_LikeList: ",anError.getErrorBody());
                        Toasty.error(LikeActivity.this,"Oops something went wrong...").show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
