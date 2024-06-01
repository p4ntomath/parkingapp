package com.example.parkingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;


public class about_fragment extends Fragment {

    TextView rateUs,privacy,terms;
    View viewParam;
    navigationDrawerAcess navigationDrawerAcess;
    public about_fragment(navigationDrawerAcess navigationDrawerAcess) {
        this.navigationDrawerAcess = navigationDrawerAcess;
    }
    public about_fragment(View view,navigationDrawerAcess navigationDrawerAcess) {
        this.viewParam = view;
        this.navigationDrawerAcess = navigationDrawerAcess;
    }
    NavigationView navigationView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        navigationView =  navigationDrawerAcess.getNavigationDrawer();

        TextView navTerms = view.findViewById(R.id.nav_terms);
        navTerms.setOnClickListener(v -> {
            View view1 = inflater.inflate(R.layout.activity_terms_and_conditions, container, false);
            TextView textView = view1.findViewById(R.id.termsTextView);
            textView.setText(Html.fromHtml(getString(R.string.terms_and_conditions), Html.FROM_HTML_MODE_COMPACT));
            Fragment about = new about_fragment(view1,navigationDrawerAcess);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentLayout,about);
            transaction.addToBackStack(null);
            transaction.commit();
        });


        TextView navPrivacy = view.findViewById(R.id.nav_privacy);
        navPrivacy.setOnClickListener(v -> {
            View view1 = inflater.inflate(R.layout.activity_privacy, container, false);
            TextView textView = view1.findViewById(R.id.privacyTextView);
            textView.setText(Html.fromHtml(getString(R.string.privacy_policy), Html.FROM_HTML_MODE_COMPACT));
            Fragment about = new about_fragment(view1,navigationDrawerAcess);
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragmentLayout,about);
            transaction.addToBackStack(null);
            transaction.commit();
        });



        if(viewParam != null){
            return viewParam;
        }
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                toFindParking();
            }
        });
        return view;
    }
    public void toFindParking(){
        navigationView.setCheckedItem(R.id.nav_home);
        Fragment newFragment = new home_fragment(navigationDrawerAcess);
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLayout, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
