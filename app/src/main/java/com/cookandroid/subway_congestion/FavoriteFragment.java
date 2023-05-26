package com.cookandroid.subway_congestion;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class FavoriteFragment extends Fragment {

    private ListView favoriteListView;
    private ArrayList<String> favoriteList;
    private FavoriteAdapter favoriteAdapter;
    private long lastClickTime = 0; // 마지막 클릭 시간 변수 초기화
    private static final long DOUBLE_CLICK_TIME_DELTA = 300;    // 두 번 클릭 간의 시간 간격(300밀리초)
    // 데이터를 저장하기 위한 SharedPreferences 객체 선언
    private SharedPreferences sharedPreferences;
    // 데이터 저장에 사용할 Key 값
    private static final String FAVORITE_KEY = "favorite_key";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        // SharedPreferences 객체 초기화
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        favoriteListView = view.findViewById(R.id.favoriteListView);

        favoriteList = new ArrayList<>();

        favoriteAdapter = new FavoriteAdapter(getActivity(), favoriteList);
        favoriteListView.setAdapter(favoriteAdapter);

        loadFavoriteData();

        favoriteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                long clickTime = System.currentTimeMillis();  // 시스템의 현재 시간
                // 이전 클릭 시간과 현재 클릭 시간을 비교하여 두 번 연속으로 클릭된 경우를 체크
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    // 더블 클릭 시 해당 항목을 리스트뷰에서 삭제
                    String station = favoriteList.get(position);
                    favoriteList.remove(position);
                    favoriteAdapter.notifyDataSetChanged();

                    // 데이터 저장
                    saveFavoriteData();

                    // 삭제된 정보를 알림
                    Toast.makeText(getActivity(), station + "을(를) 즐겨찾기에서 삭제했습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 한 번 클릭 시, 리스트에서 클릭한 항목의 위치 가져옴
                    String selectedStation = favoriteList.get(position);
                    // 리스트의 역 명을 클릭 시 클릭한 역 명을 토스트 메시지
                    Toast.makeText(getActivity(), selectedStation, Toast.LENGTH_SHORT).show();
                }
                lastClickTime = clickTime;    // 마지막 클릭 시간을 업데이트
            }
        });
        return view;
    }

    private void loadFavoriteData() {
        // SharedPreferences에서 저장된 데이터를 불러옴
        Set<String> favoriteSet = sharedPreferences.getStringSet(FAVORITE_KEY, new HashSet<>());

        // 불러온 데이터를 리스트에 추가
        favoriteList.clear(); // 기존 데이터를 초기화
        if (favoriteSet != null) {
            favoriteList.addAll(favoriteSet);
        }

        // favoriteListView 업데이트
        if (favoriteList == null) {
            favoriteList = new ArrayList<>(); // favoriteList를 새로 생성
        }
        favoriteAdapter = new FavoriteAdapter(getActivity(), favoriteList);
        favoriteListView.setAdapter(favoriteAdapter);
        favoriteAdapter.notifyDataSetChanged();
    }

    // FavoriteFragment의 데이터를 저장하는 메서드
    private void saveFavoriteData() {
        // 데이터를 저장하기 위한 Editor 객체 생성
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // 리스트뷰의 데이터를 SharedPreferences에 저장하기 위해 Set으로 변환
        Set<String> favoriteSet = new HashSet<>(favoriteList);

        // 데이터를 SharedPreferences에 저장
        editor.putStringSet(FAVORITE_KEY, favoriteSet);
        editor.apply();
    }

    // 즐겨찾기 추가 메소드
    public void addToFavorites(String station) {
        // 데이터 추가
        favoriteList.add(station);
        favoriteAdapter.notifyDataSetChanged();

        // 데이터 저장
        saveFavoriteData();

        // 즐겨찾기 추가 정보를 알림
        Toast.makeText(getActivity(), station + "을(를) 즐겨찾기에 추가했습니다.", Toast.LENGTH_SHORT).show();
    }
}
