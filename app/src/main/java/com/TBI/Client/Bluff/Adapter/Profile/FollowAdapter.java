package com.TBI.Client.Bluff.Adapter.Profile;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.TBI.Client.Bluff.Adapter.Profile.FriendsFollowers.FriendsAdapter;
import com.TBI.Client.Bluff.Model.A2Z.A;
import com.TBI.Client.Bluff.Model.A2Z.B;
import com.TBI.Client.Bluff.Model.A2Z.C;
import com.TBI.Client.Bluff.Model.A2Z.D;
import com.TBI.Client.Bluff.Model.A2Z.E;
import com.TBI.Client.Bluff.Model.A2Z.F;
import com.TBI.Client.Bluff.Model.A2Z.G;
import com.TBI.Client.Bluff.Model.A2Z.H;
import com.TBI.Client.Bluff.Model.A2Z.I;
import com.TBI.Client.Bluff.Model.A2Z.J;
import com.TBI.Client.Bluff.Model.A2Z.K;
import com.TBI.Client.Bluff.Model.A2Z.L;
import com.TBI.Client.Bluff.Model.A2Z.M;
import com.TBI.Client.Bluff.Model.A2Z.MainDetum.Friends;
import com.TBI.Client.Bluff.Model.A2Z.N;
import com.TBI.Client.Bluff.Model.A2Z.O;
import com.TBI.Client.Bluff.Model.A2Z.P;
import com.TBI.Client.Bluff.Model.A2Z.Q;
import com.TBI.Client.Bluff.Model.A2Z.RM;
import com.TBI.Client.Bluff.Model.A2Z.S;
import com.TBI.Client.Bluff.Model.A2Z.T;
import com.TBI.Client.Bluff.Model.A2Z.U;
import com.TBI.Client.Bluff.Model.A2Z.V;
import com.TBI.Client.Bluff.Model.A2Z.W;
import com.TBI.Client.Bluff.Model.A2Z.X;
import com.TBI.Client.Bluff.Model.A2Z.Y;
import com.TBI.Client.Bluff.Model.A2Z.Z;
import com.TBI.Client.Bluff.Model.GetProfile.Friend;
import com.TBI.Client.Bluff.R;

import org.bytedeco.opencv.opencv_cudaobjdetect.HOG;

import java.util.ArrayList;
import java.util.List;


public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.MyClassView> {

    List<Friends> friendsList;
    Activity activity;

    public FollowAdapter(List<Friends> friendsList, Activity activity) {
        this.friendsList = friendsList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyClassView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_a_z_follow_follower,parent,false);

        return new MyClassView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyClassView holder, int position) {
        Friends friends = friendsList.get(position);

        holder.rv_A.setNestedScrollingEnabled(false);
        holder.rv_B.setNestedScrollingEnabled(false);
        holder.rv_C.setNestedScrollingEnabled(false);
        holder.rv_D.setNestedScrollingEnabled(false);
        holder.rv_E.setNestedScrollingEnabled(false);
        holder.rv_F.setNestedScrollingEnabled(false);
        holder.rv_G.setNestedScrollingEnabled(false);
        holder.rv_H.setNestedScrollingEnabled(false);
        holder.rv_I.setNestedScrollingEnabled(false);
        holder.rv_J.setNestedScrollingEnabled(false);
        holder.rv_K.setNestedScrollingEnabled(false);
        holder.rv_L.setNestedScrollingEnabled(false);
        holder.rv_M.setNestedScrollingEnabled(false);
        holder.rv_N.setNestedScrollingEnabled(false);
        holder.rv_O.setNestedScrollingEnabled(false);
        holder.rv_P.setNestedScrollingEnabled(false);
        holder.rv_Q.setNestedScrollingEnabled(false);
        holder.rv_R.setNestedScrollingEnabled(false);
        holder.rv_S.setNestedScrollingEnabled(false);
        holder.rv_T.setNestedScrollingEnabled(false);
        holder.rv_U.setNestedScrollingEnabled(false);
        holder.rv_V.setNestedScrollingEnabled(false);
        holder.rv_W.setNestedScrollingEnabled(false);
        holder.rv_X.setNestedScrollingEnabled(false);
        holder.rv_Y.setNestedScrollingEnabled(false);
        holder.rv_Z.setNestedScrollingEnabled(false);

        if (!friends.getA().isEmpty()){
            holder.tv_alphabet_A.setVisibility(View.VISIBLE);
            holder.rv_A.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>(friends.getA());

            holder.rv_A.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_A.setAdapter(friendsAdapter);
        }
        if (!friends.getB().isEmpty()){
            holder.tv_alphabet_B.setVisibility(View.VISIBLE);
            holder.rv_B.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getB().size(); i++) {
                A a = new A();
                B r1 = friends.getB().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_B.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_B.setAdapter(friendsAdapter);
        }
        if (!friends.getC().isEmpty()){
            holder.tv_alphabet_C.setVisibility(View.VISIBLE);
            holder.rv_C.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getC().size(); i++) {
                A a = new A();
                C r1 = friends.getC().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_C.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_C.setAdapter(friendsAdapter);
        }
        if (!friends.getD().isEmpty()){
            holder.tv_alphabet_D.setVisibility(View.VISIBLE);
            holder.rv_D.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getD().size(); i++) {
                A a = new A();
                D r1 = friends.getD().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_D.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_D.setAdapter(friendsAdapter);
        }
        if (!friends.getE().isEmpty()){
            holder.tv_alphabet_E.setVisibility(View.VISIBLE);
            holder.rv_E.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getE().size(); i++) {
                A a = new A();
                E r1 = friends.getE().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_E.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_E.setAdapter(friendsAdapter);
        }
        if (!friends.getF().isEmpty()){
            holder.tv_alphabet_F.setVisibility(View.VISIBLE);
            holder.rv_F.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getF().size(); i++) {
                A a = new A();
                F r1 = friends.getF().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_F.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_F.setAdapter(friendsAdapter);
        }
        if (!friends.getG().isEmpty()){
            holder.tv_alphabet_G.setVisibility(View.VISIBLE);
            holder.rv_G.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getG().size(); i++) {
                A a = new A();
                G r1 = friends.getG().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_G.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_G.setAdapter(friendsAdapter);
        }
        if (!friends.getH().isEmpty()){
            holder.tv_alphabet_H.setVisibility(View.VISIBLE);
            holder.rv_H.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getH().size(); i++) {
                A a = new A();
                H r1 = friends.getH().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_H.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_H.setAdapter(friendsAdapter);
        }
        if (!friends.getI().isEmpty()){
            holder.tv_alphabet_I.setVisibility(View.VISIBLE);
            holder.rv_I.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getI().size(); i++) {
                A a = new A();
                I r1 = friends.getI().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_I.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_I.setAdapter(friendsAdapter);
        }
        if (!friends.getJ().isEmpty()){
            holder.tv_alphabet_J.setVisibility(View.VISIBLE);
            holder.rv_J.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getJ().size(); i++) {
                A a = new A();
                J r1 = friends.getJ().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_J.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_J.setAdapter(friendsAdapter);
        }
        if (!friends.getK().isEmpty()){
            holder.tv_alphabet_K.setVisibility(View.VISIBLE);
            holder.rv_K.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getK().size(); i++) {
                A a = new A();
                K r1 = friends.getK().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_K.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_K.setAdapter(friendsAdapter);
        }
        if (!friends.getL().isEmpty()){
            holder.tv_alphabet_L.setVisibility(View.VISIBLE);
            holder.rv_L.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getL().size(); i++) {
                A a = new A();
                L r1 = friends.getL().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_L.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_L.setAdapter(friendsAdapter);
        }
        if (!friends.getM().isEmpty()){
            holder.tv_alphabet_M.setVisibility(View.VISIBLE);
            holder.rv_M.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getM().size(); i++) {
                A a = new A();
                M r1 = friends.getM().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_M.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_M.setAdapter(friendsAdapter);
        }
        if (!friends.getN().isEmpty()){
            holder.tv_alphabet_N.setVisibility(View.VISIBLE);
            holder.rv_N.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getN().size(); i++) {
                A a = new A();
                N r1 = friends.getN().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_N.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_N.setAdapter(friendsAdapter);
        }
        if (!friends.getO().isEmpty()){
            holder.tv_alphabet_O.setVisibility(View.VISIBLE);
            holder.rv_O.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getO().size(); i++) {
                A a = new A();
                O r1 = friends.getO().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_O.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_O.setAdapter(friendsAdapter);
        }
        if (!friends.getP().isEmpty()){
            holder.tv_alphabet_P.setVisibility(View.VISIBLE);
            holder.rv_P.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getP().size(); i++) {
                A a = new A();
                P r1 = friends.getP().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_P.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_P.setAdapter(friendsAdapter);
        }
        if (!friends.getQ().isEmpty()){
            holder.tv_alphabet_Q.setVisibility(View.VISIBLE);
            holder.rv_Q.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getQ().size(); i++) {
                A a = new A();
                Q r1 = friends.getQ().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_Q.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_Q.setAdapter(friendsAdapter);
        }
        if (!friends.getR().isEmpty()){

            holder.tv_alphabet_R.setVisibility(View.VISIBLE);
            holder.rv_R.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getR().size(); i++) {
                A a = new A();
                RM r1 = friends.getR().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_R.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_R.setAdapter(friendsAdapter);
        }
        if (!friends.getS().isEmpty()){
            holder.tv_alphabet_S.setVisibility(View.VISIBLE);
            holder.rv_S.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getS().size(); i++) {
                A a = new A();
                S r1 = friends.getS().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_S.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_S.setAdapter(friendsAdapter);
        }
        if (!friends.getT().isEmpty()){
            FriendsAdapter friendsAdapter;

            holder.tv_alphabet_T.setVisibility(View.VISIBLE);
            holder.rv_T.setVisibility(View.VISIBLE);

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getT().size(); i++) {
                A a = new A();
                T r1 = friends.getT().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_T.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_T.setAdapter(friendsAdapter);
        }
        if (!friends.getU().isEmpty()){
            holder.tv_alphabet_U.setVisibility(View.VISIBLE);
            holder.rv_U.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getU().size(); i++) {
                A a = new A();
                U r1 = friends.getU().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_U.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_U.setAdapter(friendsAdapter);
        }
        if (!friends.getV().isEmpty()){
            holder.tv_alphabet_V.setVisibility(View.VISIBLE);
            holder.rv_V.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getV().size(); i++) {
                A a = new A();
                V r1 = friends.getV().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_V.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_V.setAdapter(friendsAdapter);
        }
        if (!friends.getW().isEmpty()){
            holder.tv_alphabet_W.setVisibility(View.VISIBLE);
            holder.rv_W.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getW().size(); i++) {
                A a = new A();
                W r1 = friends.getW().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_W.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_W.setAdapter(friendsAdapter);
        }
        if (!friends.getX().isEmpty()){
            holder.tv_alphabet_X.setVisibility(View.VISIBLE);
            holder.rv_X.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getX().size(); i++) {
                A a = new A();
                X r1 = friends.getX().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_X.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_X.setAdapter(friendsAdapter);
        }
        if (!friends.getY().isEmpty()){
            FriendsAdapter friendsAdapter;

            holder.tv_alphabet_Y.setVisibility(View.VISIBLE);
            holder.rv_Y.setVisibility(View.VISIBLE);

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getY().size(); i++) {
                A a = new A();
                Y r1 = friends.getY().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_Y.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_Y.setAdapter(friendsAdapter);
        }
        if (!friends.getZ().isEmpty()){
            holder.tv_alphabet_Z.setVisibility(View.VISIBLE);
            holder.rv_Z.setVisibility(View.VISIBLE);

            FriendsAdapter friendsAdapter;

            List<A> aList = new ArrayList<>();
            for (int i = 0; i < friends.getZ().size(); i++) {
                A a = new A();
                Z r1 = friends.getZ().get(i);
                a.setId(r1.getId());
                a.setUsername(r1.getUsername());
                a.setFullName(r1.getFullName());
                a.setPhoto(r1.getPhoto());
                a.setCoverPhoto(r1.getCoverPhoto());
                a.setFollowedBy(r1.getFollowedBy());
                a.setFollowByUser(r1.getFollowByUser());

                aList.add(a);
            }

            holder.rv_Z.setLayoutManager(new GridLayoutManager(activity,3));
            friendsAdapter = new FriendsAdapter(aList,activity);
            holder.rv_Z.setAdapter(friendsAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public class MyClassView extends RecyclerView.ViewHolder {

        TextView tv_alphabet_A;
        TextView tv_alphabet_B;
        TextView tv_alphabet_C;
        TextView tv_alphabet_D;
        TextView tv_alphabet_E;
        TextView tv_alphabet_F;
        TextView tv_alphabet_G;
        TextView tv_alphabet_H;
        TextView tv_alphabet_I;
        TextView tv_alphabet_J;
        TextView tv_alphabet_K;
        TextView tv_alphabet_L;
        TextView tv_alphabet_M;
        TextView tv_alphabet_N;
        TextView tv_alphabet_O;
        TextView tv_alphabet_P;
        TextView tv_alphabet_Q;
        TextView tv_alphabet_R;
        TextView tv_alphabet_S;
        TextView tv_alphabet_T;
        TextView tv_alphabet_U;
        TextView tv_alphabet_V;
        TextView tv_alphabet_W;
        TextView tv_alphabet_X;
        TextView tv_alphabet_Y;
        TextView tv_alphabet_Z;

        RecyclerView rv_A;
        RecyclerView rv_B;
        RecyclerView rv_C;
        RecyclerView rv_D;
        RecyclerView rv_E;
        RecyclerView rv_F;
        RecyclerView rv_G;
        RecyclerView rv_H;
        RecyclerView rv_I;
        RecyclerView rv_J;
        RecyclerView rv_K;
        RecyclerView rv_L;
        RecyclerView rv_M;
        RecyclerView rv_N;
        RecyclerView rv_O;
        RecyclerView rv_P;
        RecyclerView rv_Q;
        RecyclerView rv_R;
        RecyclerView rv_S;
        RecyclerView rv_T;
        RecyclerView rv_U;
        RecyclerView rv_V;
        RecyclerView rv_W;
        RecyclerView rv_X;
        RecyclerView rv_Y;
        RecyclerView rv_Z;

        public MyClassView(@NonNull View itemView) {
            super(itemView);

            tv_alphabet_A = itemView.findViewById(R.id.tv_alphabet_A);
            tv_alphabet_B = itemView.findViewById(R.id.tv_alphabet_B);
            tv_alphabet_C = itemView.findViewById(R.id.tv_alphabet_C);
            tv_alphabet_D = itemView.findViewById(R.id.tv_alphabet_D);
            tv_alphabet_E = itemView.findViewById(R.id.tv_alphabet_E);
            tv_alphabet_F = itemView.findViewById(R.id.tv_alphabet_F);
            tv_alphabet_G = itemView.findViewById(R.id.tv_alphabet_G);
            tv_alphabet_H = itemView.findViewById(R.id.tv_alphabet_H);
            tv_alphabet_I = itemView.findViewById(R.id.tv_alphabet_I);
            tv_alphabet_J = itemView.findViewById(R.id.tv_alphabet_J);
            tv_alphabet_K = itemView.findViewById(R.id.tv_alphabet_K);
            tv_alphabet_L = itemView.findViewById(R.id.tv_alphabet_L);
            tv_alphabet_M = itemView.findViewById(R.id.tv_alphabet_M);
            tv_alphabet_N = itemView.findViewById(R.id.tv_alphabet_N);
            tv_alphabet_O = itemView.findViewById(R.id.tv_alphabet_O);
            tv_alphabet_P = itemView.findViewById(R.id.tv_alphabet_P);
            tv_alphabet_Q = itemView.findViewById(R.id.tv_alphabet_Q);
            tv_alphabet_R = itemView.findViewById(R.id.tv_alphabet_R);
            tv_alphabet_S = itemView.findViewById(R.id.tv_alphabet_S);
            tv_alphabet_T = itemView.findViewById(R.id.tv_alphabet_T);
            tv_alphabet_U = itemView.findViewById(R.id.tv_alphabet_U);
            tv_alphabet_V = itemView.findViewById(R.id.tv_alphabet_V);
            tv_alphabet_W = itemView.findViewById(R.id.tv_alphabet_W);
            tv_alphabet_X = itemView.findViewById(R.id.tv_alphabet_X);
            tv_alphabet_Y = itemView.findViewById(R.id.tv_alphabet_Y);
            tv_alphabet_Z = itemView.findViewById(R.id.tv_alphabet_Z);


            rv_A = itemView.findViewById(R.id.rv_A);
            rv_B = itemView.findViewById(R.id.rv_B);
            rv_C = itemView.findViewById(R.id.rv_C);
            rv_D = itemView.findViewById(R.id.rv_D);
            rv_E = itemView.findViewById(R.id.rv_E);
            rv_F = itemView.findViewById(R.id.rv_F);
            rv_G = itemView.findViewById(R.id.rv_G);
            rv_H = itemView.findViewById(R.id.rv_H);
            rv_I = itemView.findViewById(R.id.rv_I);
            rv_J = itemView.findViewById(R.id.rv_J);
            rv_K = itemView.findViewById(R.id.rv_K);
            rv_L = itemView.findViewById(R.id.rv_L);
            rv_M = itemView.findViewById(R.id.rv_M);
            rv_N = itemView.findViewById(R.id.rv_N);
            rv_O = itemView.findViewById(R.id.rv_O);
            rv_P = itemView.findViewById(R.id.rv_P);
            rv_Q = itemView.findViewById(R.id.rv_Q);
            rv_R = itemView.findViewById(R.id.rv_R);
            rv_S = itemView.findViewById(R.id.rv_S);
            rv_T = itemView.findViewById(R.id.rv_T);
            rv_U = itemView.findViewById(R.id.rv_U);
            rv_V = itemView.findViewById(R.id.rv_V);
            rv_W = itemView.findViewById(R.id.rv_W);
            rv_X = itemView.findViewById(R.id.rv_X);
            rv_Y = itemView.findViewById(R.id.rv_Y);
            rv_Z = itemView.findViewById(R.id.rv_Z);
        }
    }
}
