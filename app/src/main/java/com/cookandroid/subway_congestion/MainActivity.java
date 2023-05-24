package com.cookandroid.subway_congestion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    /*하단 탭 선언*/
    private BottomNavigationView bottomNavigationView;
    /*하단 탭 클릭 시 이동 프래그먼트*/
    private FavoriteFragment favoriteFragment;
    private SearchFragment searchFragment;
    private SettingFragment settingFragment;
    /*화면에 표시해주는 프래그먼트*/
    private Fragment activeFragment;
    private Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favorite:
                        // 즐겨찾기 프래그먼트로 전환
                        switchFragment(favoriteFragment);
                        return true;
                    case R.id.search:
                        // 검색 프래그먼트로 전환
                        switchFragment(searchFragment);
                        return true;
                    case R.id.setting:
                        // 설정 프래그먼트로 전환
                        switchFragment(settingFragment);
                        return true;
                }
                return false;
            }
        });

        /*프래그먼트 초기화*/
        favoriteFragment = new FavoriteFragment();
        searchFragment = new SearchFragment();
        settingFragment = new SettingFragment();

        // 두 번째 탭(검색 탭)을 첫 번째로 지정
        bottomNavigationView.setSelectedItemId(R.id.search);
    }

    // 선택한 프래그먼트로 변경할 수 있는 메소드
    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
}