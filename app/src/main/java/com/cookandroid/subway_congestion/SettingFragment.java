package com.cookandroid.subway_congestion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

public class SettingFragment extends Fragment {

    private ImageButton CloseBtn, DarkTheme, LightTheme;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        CloseBtn = view.findViewById(R.id.CloseBtn);
        DarkTheme = view.findViewById(R.id.DarkTheme);
        LightTheme = view.findViewById(R.id.LightTheme);

        // 종료 버튼 클릭 시 어플 종료
        CloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 21) {
                    getActivity().finishAndRemoveTask();
                } else {
                    getActivity().finish();
                }
                System.exit(0);
            }
        });

        // 다크 테마 변경 버튼 클릭 시
        DarkTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 테마 변경
                changeTheme(AppCompatDelegate.MODE_NIGHT_YES); // 어두운 테마 모드로 변경
            }
        });

        // 라이트 테마 변경 버튼 클릭 시
        LightTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 테마 변경
                changeTheme(AppCompatDelegate.MODE_NIGHT_NO); // 밝은 테마 모드로 변경
            }
        });

        return view;
    }

    // 테마 변경 메소드
    private void changeTheme(int themeMode) {
        int currentThemeMode = AppCompatDelegate.getDefaultNightMode();
        if (currentThemeMode == themeMode) {
            // 이미 선택된 테마 모드인 경우
            Toast.makeText(getActivity(), "이미 선택된 테마입니다.", Toast.LENGTH_SHORT).show();
            return;
        }
        AppCompatDelegate.setDefaultNightMode(themeMode);   // 원하는 테마 모드로 테마 설정

        // 액티비티 재시작
        restartActivity();
    }

    // 재시작 매소드
    private void restartActivity() {
        if (getActivity() != null) {
            // 이전의 액티비티 인텐트 가져옴
            Intent intent = getActivity().getIntent();
            // 액티비티 재시작을 위해 FLAG_ACTIVITY_NEW_TASK와 FLAG_ACTIVITY_CLEAR_TASK 플래그를 추가
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // 새로운 액티비티를 시작하고 이전의 액티비티 스택을 모두 제거
            getActivity().startActivity(intent);
            getActivity().finish(); // 현재 액티비티 종료
        }
    }
}
