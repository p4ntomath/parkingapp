package com.example.parkingapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


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
            List<Integer> layouts = new ArrayList<>();
            layouts.add(R.layout.mahlatse_card);
            layouts.add(R.layout.mahlatse_card);
            CardPagerAdapter adapter = new CardPagerAdapter(getContext(), layouts);
            replaceView(supportDevsView);
            viewPager.setAdapter(adapter);

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
    }






}