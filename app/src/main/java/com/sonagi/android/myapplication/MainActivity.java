package com.sonagi.android.myapplication;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    //프레그 먼트 객체 생성
    FirstFragment first =new FirstFragment();
    SecondFragment second = new SecondFragment();
    ThirdFragment third=new ThirdFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container_init();// 첫 화면 띄워주기 벡스택 없음

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //프래그 먼트를 관리하는 객체 추출
                FragmentManager manager = getSupportFragmentManager();
                // 프로그먼트 변경을 관리하는 객체를 추출
                FragmentTransaction tran= manager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.action_recents:
                        //프레그먼트를 추가한다.
                        //tran.add(R.id.container,first); // first는 아이디임 배치할
                        //프레그 먼트를 교체한다
                        tran.replace(R.id.container,first);
                        //back stack 추가
                        tran.addToBackStack(null);

                        //적용한다.
                        tran.commit();
                        //Toast.makeText(MainActivity.this, "Recents", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_favorites:;
                        //프레그먼트를 추가한다.
                        //tran.add(R.id.container,second); // first는 아이디임 배치할
                        //적용한다.
                        tran.replace(R.id.container,second);
                        //프레그먼트 변경사항을 백스텍에 포함시킨다 보통 처음화면은 안함
                        tran.addToBackStack(null);

                        tran.commit();
                        //Toast.makeText(MainActivity.this, "Favorites", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_plus:

                        //프레그먼트를 추가한다.
                        //tran.add(R.id.container,first); // first는 아이디임 배치할
                        //프레그 먼트를 교체한다
                        tran.replace(R.id.container,third);
                        //back stack 추가
                        tran.addToBackStack(null);

                        //적용한다.
                        tran.commit();
                        //Toast.makeText(MainActivity.this, "Nearby", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
    public void container_init(){ // 처음 디폴트 값에 첫 화면 띄워주기
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction tran= manager.beginTransaction();
        tran.add(R.id.container,first);
        tran.commit();
    }
}