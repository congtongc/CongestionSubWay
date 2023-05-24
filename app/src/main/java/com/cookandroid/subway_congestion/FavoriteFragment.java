package com.cookandroid.subway_congestion;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class FavoriteFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        return view;
    }

    // 즐겨찾기 추가 메소드
    public void addToFavorites(String stationName) {
        // 즐겨찾기에 추가하는 로직을 구현합니다.
        // 예시: 즐겨찾기 목록에 역 이름을 추가합니다.
        // 이곳에 실제로 즐겨찾기에 추가하는 로직을 구현하면 됩니다.
        Toast.makeText(getActivity(), stationName + "을(를) 즐겨찾기에 추가했습니다.", Toast.LENGTH_SHORT).show();
    }
}
