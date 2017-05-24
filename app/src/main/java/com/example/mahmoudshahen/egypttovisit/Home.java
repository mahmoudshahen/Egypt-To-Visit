package com.example.mahmoudshahen.egypttovisit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.nearby.messages.Strategy;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    RecyclerView landmarksRecycler;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<LandmarkData> landmarks;
    ChildEventListener mylistener;
    LandmarkAdapter landmarkAdapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Landmarks");

        landmarks = new ArrayList<>();
        landmarksRecycler = (RecyclerView) findViewById(R.id.rv_landmarks);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        landmarksRecycler.setLayoutManager(llm);
        firebaseDatabase=FirebaseDatabase.getInstance();

        landmarkAdapter = new LandmarkAdapter(landmarks, this);
        landmarksRecycler.setAdapter(landmarkAdapter);


        getLandmarks();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(mylistener);
    }

    void getLandmarks() {
        databaseReference = firebaseDatabase.getReference();
//        databaseReference.removeEventListener(mylistener);
        mylistener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.v("data",dataSnapshot.getKey());
                LandmarkData landmarkData = new LandmarkData();
                List<String> images = new ArrayList<>();
                for (DataSnapshot postSnapshot: dataSnapshot.child(getString(R.string.images)).getChildren()) {
                    // TODO: handle the post
                    images.add(postSnapshot.getKey());
                }
                landmarkData.setName(dataSnapshot.getKey().toString());

                landmarkData.setImageUrl(dataSnapshot.child(getString(R.string.image_url)).getValue(String.class));
                Log.v("lat", String.valueOf(dataSnapshot.child(getString(R.string.latitude)).getValue(Double.class)));
                Log.v("long", String.valueOf(dataSnapshot.child(getString(R.string.longitude)).getValue(Double.class)));
                landmarkData.setLatitude(dataSnapshot.child(getString(R.string.latitude)).getValue(Double.class));
                landmarkData.setLongitude(dataSnapshot.child(getString(R.string.longitude)).getValue(Double.class));
                landmarkData.setRate(dataSnapshot.child(getString(R.string.Rate)).getValue(Double.class));
                landmarkData.setDescription(dataSnapshot.child(getString(R.string.descriptions)).getValue(String.class));
                landmarkData.setImages(images);
                landmarks.add(landmarkData);
                landmarkAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(mylistener);

    }
}
