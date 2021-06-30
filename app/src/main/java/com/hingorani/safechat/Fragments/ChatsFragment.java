package com.hingorani.safechat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hingorani.safechat.Adapter.UserAdapter;
import com.hingorani.safechat.LoginActivity;
import com.hingorani.safechat.Model.Chatlist;
import com.hingorani.safechat.Model.User;
import com.hingorani.safechat.Notifications.Token;
import com.hingorani.safechat.R;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Boolean> mUserSeen;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private ArrayList<Long> mUserTime = new ArrayList<>();
    FirebaseUser fuser;
    DatabaseReference reference;
    DatabaseReference referenceTime;
    private List<Chatlist> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        recyclerView = view.findViewById(R.id.recycler_viewChats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        updateToken(FirebaseInstanceId.getInstance().getToken());


        return view;
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void chatList() {
        mUsers = new ArrayList<>();
        mUserSeen = new ArrayList<>();
        mUserTime.clear();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                mUserTime.clear();
                mUserSeen.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList){
                        if (user.getId().equals(chatlist.getId())){
                            mUsers.add(user);
                            mUserTime.add(chatlist.lastmessage);
                            mUserSeen.add(chatlist.seen);
                        }
                    }
                }
                sort(mUsers,mUserTime);
                userAdapter = new UserAdapter(getContext(), mUsers, true, mUserSeen);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<User> sort(List<User> Users, List<Long> UserTime){

        for(int i=0;i<UserTime.size();i++){
            for (int j=1;j<(UserTime.size()-i);j++){
                if(UserTime.get(j-1)<UserTime.get(j)){
                    User temp = Users.get(j);
                    Long ltemp = UserTime.get(j);
                    Boolean btemp = mUserSeen.get(j);
                    Users.set(j,Users.get(j-1));
                    UserTime.set(j,UserTime.get(j-1));
                    mUserSeen.set(j,mUserSeen.get(j-1));
                    Users.set(j-1,temp);
                    UserTime.set(j-1,ltemp);
                    mUserSeen.set(j-1,btemp);
                }
            }
        }

        return Users;
    }

}