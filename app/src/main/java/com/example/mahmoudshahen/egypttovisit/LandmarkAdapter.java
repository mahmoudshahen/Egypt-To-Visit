package com.example.mahmoudshahen.egypttovisit;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

/**
 * Created by mahmoud shahen on 21/04/2017.
 */

public class LandmarkAdapter extends RecyclerView.Adapter<LandmarkAdapter.LandmarkViewHolder> {

    List<LandmarkData> landmarks;
    Context context;
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;

    public LandmarkAdapter(List<LandmarkData> list, Context con) {
        landmarks = list;
        context = con;
        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference();
    }

    @Override
    public LandmarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.lanmark_item, parent, false);


        return new LandmarkAdapter.LandmarkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LandmarkViewHolder holder, final int position) {
        Random r = new Random();
        int setbar = (r.nextInt(10));
        holder.progressBar.setProgress(setbar);
        holder.progressBar.setMax(10);
        if (setbar < 4) {
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
        } else if (setbar < 7) {
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
        }
        else{
            holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.GREEN));
        }

        holder.title.setText(landmarks.get(position).getName());
        storageRef.child(landmarks.get(position).name + "/" + landmarks.get(position).images.get(0) + ".PNG")
                .getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.v("link0", String.valueOf(uri));
                Picasso.with(context).load(uri).fit().centerCrop().into(holder.imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        Log.v("pic", landmarks.get(position).images.get(0));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivtiy.class);
                intent.putExtra("land", landmarks.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return landmarks.size();
    }

    public static class LandmarkViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView imageView;
        CardView cardView;
        ProgressBar progressBar;

        public LandmarkViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_tile_title);
            imageView = (ImageView) itemView.findViewById(R.id.iv_tile_picture);
            cardView = (CardView) itemView.findViewById(R.id.cv_item);
            progressBar = (ProgressBar) itemView.findViewById(R.id.pr_rate);
        }
    }
}
