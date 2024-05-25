package com.example.parkingapp;



import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardPagerAdapter extends RecyclerView.Adapter<CardPagerAdapter.ViewHolder> {
    Quartet<Map<String, List<String>>, List<String>, List<Integer>, List<String>> cards;
    private Context context;
    Map<String, Integer> socialsIcons = new HashMap<>();
    List<String> names ;
    Map<String, Map<String, String>> socialLinks = new HashMap<>();

    public CardPagerAdapter(Context context, Quartet<Map<String, List<String>>, List<String>, List<Integer>, List<String>> cards,
                            Map<String, Integer> socials, Map<String, Map<String, String>> links) {
        this.context = context;
        this.cards = cards;
        this.socialsIcons = socials;
        this.names = new ArrayList<>(cards.getFirst().keySet());
        names.sort(String::compareTo);
        this.socialLinks = links;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mahlatse_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageResource(cards.getThird().get(position));
        holder.name.setText(names.get(position));
        holder.role.setText(cards.getSecond().get(position));
        holder.contact.setText(cards.getFourth().get(position));
        String contactType = cards.getFourth().get(position).split(" ")[0];
        if(socialsIcons.containsKey(contactType)){
            int icon = socialsIcons.get(contactType);
            holder.contact.setIconResource(icon);
        }
        holder.contact.setOnClickListener(v->{
            if(contactType.equals("Whatsapp")){
                openWhatsApp(socialLinks.get(names.get(position)).get(contactType));
            }else{
                openUrl(socialLinks.get(names.get(position)).get(contactType));
            }
        });


        holder.linearLayout.removeAllViews();
        List<String>preferedSocial = cards.getFirst().get(names.get(position));
        assert preferedSocial != null;
        holder.linearLayout = addImage(preferedSocial, holder.linearLayout,position);
    }

    public LinearLayout addImage(List<String> socialsList, LinearLayout linearLayout,int position){


        Map<String, Integer> image = new HashMap<>();
        for(String social : socialsList){
            if(socialsIcons.containsKey(social)){
                image.put(social,socialsIcons.get(social));
            }
        }
        List<String> keys = new ArrayList<>(image.keySet());
        keys.sort(String::compareTo);


        for(int i=0;i<image.size();i++){
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(8, 5, 8, 5);
            imageView.setImageResource(image.get(keys.get(i)));

            int finalI = i;
            imageView.setOnClickListener(v->{
                if(socialLinks.get(names.get(position)) != null){
                    String url = socialLinks.get(names.get(position)).get(keys.get(finalI));
                    Log.d("type",keys.get(finalI));
                    if(keys.get(finalI).equals("WhatsApp")){
                        openWhatsApp(url);
                    }else{
                        openUrl(url);
                    }
                }

            });
            linearLayout.addView(imageView,layoutParams);
        }
        return linearLayout;
    }

    public void openUrl(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
    private void openWhatsApp(String phoneNumber) {
            String url = "https://api.whatsapp.com/send/?phone=" + phoneNumber + "&text=Hey+I+Have+Question+About+The+Parking+app&type=phone_number&app_absent=0";
            openUrl(url);
    }




    @Override
    public int getItemCount() {
        return cards.getFirst().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView role;
        MaterialButton contact;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profilePicImage);
            name = itemView.findViewById(R.id.name);
            role = itemView.findViewById(R.id.role);
            contact = itemView.findViewById(R.id.contact);
            linearLayout = itemView.findViewById(R.id.social);
        }
    }
}
