package com.groslaids.chatapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.groslaids.chatapp.adapters.WizzViewPagerAdapter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private WizzViewPagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public static void show(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //If no user connected
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            LoginActivity.show(this);
            finish();
        }

        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        pagerAdapter = new WizzViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        findViewById(R.id.settings_btn).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.settings_btn){
            //Faire apparaître l'activité de profil
        }
    }
}
