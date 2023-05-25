package com.cookandroid.subway_congestion;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    private FavoriteFragment favoriteFragment;

    public void setFavoriteFragment(FavoriteFragment fragment) {
        favoriteFragment = fragment;
    }

    private List<String> list;  // 데이터를 넣은 리스트변수
    private ListView stationListView;   // 검색을 보여줄 리스트변수
    private EditText searchEditText;    // 검색어를 입력할 Input창
    private SearchAdapter adapter;      // 리스트뷰에 연결할 아답터
    private ArrayList<String> arraylist;    // 데이터를 옮기기 위한 리스트

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        stationListView = view.findViewById(R.id.stationListView);

        // 리스트를 생성한다.
        list = new ArrayList<String>();

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList(getActivity().getResources().getAssets());

        // 리스트의 모든 데이터를 arraylist에 복사한다.
        arraylist = new ArrayList<String>();

        // list 복사본을 만든다.
        arraylist.addAll(list);

        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchAdapter(list, getActivity());   // Fragment에서 getActivity()를 사용하여 Context를 가져옴

        // 역 리스트뷰에 아답터를 연결한다.
        stationListView.setAdapter(adapter);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할때마다 호출된다.
                String text = searchEditText.getText().toString();
                // search 메소드를 호출한다.
                search(text);
            }
        });

        stationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private long lastClickTime = 0; // 마지막 클릭 시간 변수 초기화
            private static final long DOUBLE_CLICK_TIME_DELTA = 300;    // 두 번 클릭 간의 시간 간격(300밀리초)

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                long clickTime = System.currentTimeMillis();  // 시스템의 현재 시간
                // 이전 클릭 시간과 현재 클릭 시간을 비교하여 두 번 연속으로 클릭된 경우를 체크
                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    // 두 번 연속 클릭 시
                    String selectedStation = list.get(position);
                    if (favoriteFragment != null) {
                        favoriteFragment.addToFavorites(selectedStation);
                    }
                    // 두 번 터치 테스트 코드
                    Toast.makeText(getActivity(), selectedStation + " 두 번 터치", Toast.LENGTH_SHORT).show();
                } else {
                    // 한 번 클릭 시, 리스트에서 클릭한 항목의 위치 가져옴
                    String selectedStation = list.get(position);
                    // 리스트의 역 명을 클릭 시 클릭한 역 명을 토스트 메시지
                    Toast.makeText(getActivity(), selectedStation, Toast.LENGTH_SHORT).show();
                }
                lastClickTime = clickTime;    // 마지막 클릭 시간을 업데이트
            }

        });
        return view;
    }


    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력 시
        else {
            // 리스트의 모든 데이터를 검색한다.
            for (int i = 0; i < arraylist.size(); i++) {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).toLowerCase().contains(charText)) {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    //Json 파일을 String으로 변형
    private String getJsonString(AssetManager am) {
        StringBuffer response = new StringBuffer();
        try {
            InputStream inputStream = am.open("second.json");
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }


    private void settingList(AssetManager am) {
        jsonParsing(getJsonString(am));
    }

    //String으로 변형된 Json파일을 파싱하여, List에 역이름을 순서대로 저장한다.
    private void jsonParsing(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);

            JSONArray movieArray = jsonObject.getJSONArray("DATA");

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieObject = movieArray.getJSONObject(i);
                list.add(movieObject.getString("station_nm")); //역명인 "station_nm" id를 이용하여, 데이터를 가져온다.
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
