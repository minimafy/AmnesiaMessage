package com.example.prueba;


import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;

import androidx.viewpager.widget.ViewPager;

import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.example.prueba.Adaptadores.PagerAdapter;

import com.example.prueba.Adaptadores.UserChatDisplayAdapter;
import com.example.prueba.Objetos.ServicioNotificaciones;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;




public class HomePrincipal extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ViewPager pager;
    private TabLayout tabLayout;

    private TabItem firstItem, secondItem, thirdItem;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeprincipal);



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        firstItem = findViewById(R.id.firstitem);
        secondItem = findViewById(R.id.secondItem);
        thirdItem = findViewById(R.id.thirdItem);

        drawerLayout = findViewById(R.id.drawer);

        adapter = new PagerAdapter(
                getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
                tabLayout.getTabCount()
        );

        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, ServicioNotificaciones.class));
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if(level== ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(getApplicationContext(), ServicioNotificaciones.class));
            } else {
                startService(new Intent(getApplicationContext(), ServicioNotificaciones.class));
            }
        }

    }
}