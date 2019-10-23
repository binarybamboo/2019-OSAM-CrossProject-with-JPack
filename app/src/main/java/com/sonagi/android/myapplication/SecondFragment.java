package com.sonagi.android.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonagi.android.myapplication.today_tab.MyAdapter;
import com.sonagi.android.myapplication.today_tab.MyListDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private RecyclerView listview;
    private MyAdapter adapter;


    ArrayList<String> Items;
    ArrayAdapter<String> list_Adapter;
    ListView list_listView;
    Button btnAdd, btnDel;
    EditText editText;
    String token;
    JSONArray monthSchedule;
    JSONArray currentSchedule;

    private ListView m_oListView = null;


    //일정 목록을 띄워주는 뷰를



    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_second, container, false);
        SharedPreferences sf = getActivity().getSharedPreferences("auth_token", MODE_PRIVATE);
        token = sf.getString("token", "null");

        if (token.equals("null")) {
            Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
        init(view);


        return view;
    }

    public JSONArray getSchedule(final int year, final int month) {
        class Get extends AsyncTask<String, Void, JSONArray> {
            @Override
            public JSONArray doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0] + "schedule/list/" + Integer.toString(year) + "/" + Integer.toString(month) + "/");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestProperty("Authorization", "jwt " + token);
                    httpURLConnection.setRequestProperty("Content-Type","application/json");
                    httpURLConnection.setRequestProperty("Accept","application/json");
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setUseCaches(false);

                    int statusCode = httpURLConnection.getResponseCode();

                    if (statusCode == 200) {
                        InputStream is = httpURLConnection.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        byte[] byteBuffer = new byte[1024];
                        byte[] byteData = null;
                        int nLength = 0;
                        while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                            baos.write(byteBuffer, 0, nLength);
                        }
                        byteData = baos.toByteArray();

                        String response = new String(byteData);
                        System.out.println(response);

                        JSONArray jsonArray = new JSONArray(response);
                        return jsonArray;
                    } else {
                        Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                        throw new Exception();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_LONG).show();

                    JSONArray jsonArray = new JSONArray();
                    return jsonArray;
                }
            }
        }
        try {
            Get get = new Get();
            return get.execute("http://13.125.196.191/").get();
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public Boolean postSchedule(final int type, final String title, final String start_date, final String end_date) {
        class Post extends AsyncTask<String, Void, Boolean> {
            public Boolean doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0] + "schedule/write/");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestProperty("Authorization", "jwt " + token);
                    httpURLConnection.setRequestProperty("Content-Type","application/json");
                    httpURLConnection.setRequestProperty("Accept","application/json");
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setUseCaches(false);

                    JSONObject obj = new JSONObject();
                    obj.put("type", type);
                    obj.put("title", title);
                    obj.put("start_date", start_date);
                    obj.put("end_date", end_date);
                    obj.put("content", title);

                    OutputStream os = httpURLConnection.getOutputStream();
                    os.write(obj.toString().getBytes());
                    os.flush();

                    int statusCode = httpURLConnection.getResponseCode();

                    if (statusCode == 201) {
                        Toast.makeText(getActivity().getApplicationContext(), "성공적으로 등록되었습니다.", Toast.LENGTH_LONG).show();
                        return true;
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "서버와의 통신이 원활하지 않습니다.", Toast.LENGTH_LONG).show();
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        try {
            Post post = new Post();
            return post.execute("http://13.125.196.191/").get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean putSchedule(final int pk, final int type, final String title, final String start_date, final String end_date) {
        class Put extends AsyncTask<String, Void, Boolean> {
            public Boolean doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0] + "schedule/" + Integer.toString(pk) +"/");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestProperty("Authorization", "jwt " + token);
                    httpURLConnection.setRequestProperty("Content-Type","application/json");
                    httpURLConnection.setRequestProperty("Accept","application/json");
                    httpURLConnection.setRequestMethod("PUT");
                    httpURLConnection.setUseCaches(false);

                    JSONObject obj = new JSONObject();
                    obj.put("type", type);
                    obj.put("title", title);
                    obj.put("start_date", start_date);
                    obj.put("end_date", end_date);
                    obj.put("content", title);

                    OutputStream os = httpURLConnection.getOutputStream();
                    os.write(obj.toString().getBytes());
                    os.flush();

                    int statusCode = httpURLConnection.getResponseCode();

                    if (statusCode == 202) {
                        Toast.makeText(getActivity().getApplicationContext(), "성공적으로 등록되었습니다.", Toast.LENGTH_LONG).show();
                        return true;
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "서버와의 통신이 원활하지 않습니다. 잠시후 다시 시도해 주세요", Toast.LENGTH_LONG).show();
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        try {
            Put put = new Put();
            return put.execute("http://13.125.196.191/").get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean deleteSchedule(final int pk) {
        class Delete extends AsyncTask<String, Void, Boolean> {
            public Boolean doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0] + "schedule/" + Integer.toString(pk) +"/");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setReadTimeout(3000);
                    httpURLConnection.setConnectTimeout(3000);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestProperty("Authorization", "jwt " + token);
                    httpURLConnection.setRequestProperty("Content-Type","application/json");
                    httpURLConnection.setRequestProperty("Accept","application/json");
                    httpURLConnection.setRequestMethod("PUT");
                    httpURLConnection.setUseCaches(false);

                    int statusCode = httpURLConnection.getResponseCode();

                    if (statusCode == 202) {
                        Toast.makeText(getActivity().getApplicationContext(), "성공적으로 제거되었습니다.", Toast.LENGTH_LONG).show();
                        return true;
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "서버와의 통신이 원활하지 않습니다. 잠시후 다시 시도해 주세요", Toast.LENGTH_LONG).show();
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(), "오류가 발생했습니다.", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        try {
            Delete delete = new Delete();
            return delete.execute("http://13.125.196.191/").get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void init(View view){
        Calendar calendar = Calendar.getInstance();
        monthSchedule = getSchedule(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH));

        listview = (RecyclerView)view.findViewById(R.id.main_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);
        ArrayList<String> itemList = new ArrayList<>(); //itemlist를 불러와서 서버에 저장하세요
        itemList.add("2019-10-23\n 유격 훈련");
        itemList.add("2019-10-23\n 혹한기 훈련");
        itemList.add("2019-10-23\n 끔직한 훈련");


        adapter = new MyAdapter(getActivity(), itemList, onClickItem);
        listview.setAdapter(adapter);

        MyListDecoration decoration = new MyListDecoration();
        listview.addItemDecoration(decoration);


        Items = new ArrayList<String>();
        Items.add("First");
        Items.add("Second");
        Items.add("Third");
        list_Adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, Items);
        list_listView = (ListView) view.findViewById(R.id.listview1);
        list_listView.setAdapter(list_Adapter);
        list_listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        editText = (EditText) view.findViewById(R.id.editText);
        btnAdd  = (Button) view.findViewById(R.id.btnAdd);
        btnDel  = (Button) view.findViewById(R.id.btnDel);



        btnAdd  = (Button) view.findViewById(R.id.btnAdd);
        btnDel = (Button)view.findViewById(R.id.btnDel);

         View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.btnAdd:
                        String text = editText.getText().toString();
                        if (text.length() != 0) {
                            Items.add(text);
                            editText.setText("");
                            list_Adapter.notifyDataSetChanged();
                        }
                        break;
                    case R.id.btnDel:
                        int pos;
                        pos= list_listView.getCheckedItemPosition();
                        if(pos!=ListView.INVALID_POSITION){
                            Items.remove(pos);
                            list_listView.clearChoices();
                            list_Adapter.notifyDataSetChanged();
                        }
                }
            }
        };

        btnAdd.setOnClickListener(listener);
        btnDel.setOnClickListener(listener);

    }
    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        }
    };
}