package com.example.parkingapp;



import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator2;
import me.relex.circleindicator.CircleIndicator3;


public class help_fragment extends Fragment {


        View viewParam;
    public help_fragment() {
        // Required empty public constructor
    }
    public help_fragment(View view) {
        this.viewParam = view;
    }

    RelativeLayout howToUse,faq,support;
    TextView howToUseText,faqText,supportText;
    Quartet<Map<String, List<String>>, List<String>, List<Integer>, List<String>> cards = new Quartet<>(
            new HashMap<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>()
    );
    Map<String, Integer> socials = new HashMap<>();
     Map<String, Map<String, String>> socialLinks = new HashMap<>();

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.help_fragment, container, false);
        howToUse = view.findViewById(R.id.howToUse);
        faq = view.findViewById(R.id.faqs);
        support = view.findViewById(R.id.supportDevs);
        howToUseText = view.findViewById(R.id.howToUseText);
        faqText = view.findViewById(R.id.faqsText);
        supportText = view.findViewById(R.id.supportDevsText);

        support.setOnClickListener(v->{
            View supportDevsView = inflater.inflate(R.layout.supportdevelopers, container, false);
            ViewPager2 viewPager = supportDevsView.findViewById(R.id.viewPager);
            addCard();
            addSocials();
            addSocialLinks();
            CardPagerAdapter adapter = new CardPagerAdapter(getContext(), cards,socials,socialLinks);
            replaceView(supportDevsView);
            CircleIndicator3 indicator = supportDevsView.findViewById(R.id.indicator);
            viewPager.setAdapter(adapter);
            indicator.setViewPager(viewPager);


        });
        if(viewParam!=null){
            view = viewParam;

        }

        return view;
    }

    public void replaceView(View view){
        Fragment about = new help_fragment(view);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLayout,about);
        transaction.addToBackStack(null);
        transaction.commit();
    }public void addCard(){


        cards.getFirst().put("Mahlatse Rabothata", Arrays.asList("Facebook","Github","Instagram","Linkedin","Youtube"));
        cards.getSecond().add("Front-End Engineer");
        cards.getThird().add(R.drawable.mahlatse);
        cards.getFourth().add("Whatsapp Me");

        cards.getFirst().put("Mbalenhle Baloyi", Arrays.asList("Facebook","Github","Linkedin"));
        cards.getSecond().add("Database Manager");
        cards.getThird().add(R.drawable.blankprofile);
        cards.getFourth().add("Whatsapp Me");

        cards.getFirst().put("Nhlakanipho Mavefua", Arrays.asList("Facebook","Github","Linkedin"));
        cards.getSecond().add("Back-End Engineer \n Front-End Engineer");
        cards.getThird().add(R.drawable.blankprofile);
        cards.getFourth().add("Whatsapp Me");

        cards.getFirst().put("Thato Khoza", Arrays.asList("Facebook","Github","Instagram","Linkedin"));
        cards.getSecond().add("Database Manager \n Data Processor");
        cards.getThird().add(R.drawable.blankprofile);
        cards.getFourth().add("Whatsapp Me");


    }
    public void addSocials(){
        socials.put("Instagram",R.drawable.instagramicon);
        socials.put("Facebook",R.drawable.facebookicon);
        socials.put("Linkedin",R.drawable.linkedinicon);
        socials.put("Github",R.drawable.githubicon);
        socials.put("Youtube",R.drawable.youtubeicon);
        socials.put("Whatsapp",R.drawable.whatsappicon);
    }

    public void addSocialLinks(){
        socialLinks.put("Mahlatse Rabothata",new HashMap<>());
        socialLinks.get("Mahlatse Rabothata").put("Facebook","https://www.facebook.com/hlatse.rabothata.73");
        socialLinks.get("Mahlatse Rabothata").put("Instagram","https://www.instagram.com/hlatse_p4ntomath/");
        socialLinks.get("Mahlatse Rabothata").put("Linkedin","https://www.linkedin.com/in/mahlatse-rabothata-14641a287/");
        socialLinks.get("Mahlatse Rabothata").put("Youtube","https://www.youtube.com/channel/UCe_PvjJ2p1joYRH5NfoxJgA");
        socialLinks.get("Mahlatse Rabothata").put("Github","https://github.com/HlatseP4ntomath");

        socialLinks.get("Mahlatse Rabothata").put("Whatsapp","27630311427");


    }








}