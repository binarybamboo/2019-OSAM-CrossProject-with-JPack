package com.sonagi.android.myapplication;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonagi.android.myapplication.row.Address_Item;
import com.sonagi.android.myapplication.today_tab.MyAdapter;
import com.sonagi.android.myapplication.today_tab.MyListDecoration;
import com.sonagi.android.myapplication.today_tab.Schedule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {

    private RecyclerView listview;
    private MyAdapter adapter;


    Button btnAdd, btnDel;
    EditText editText;
    String token;
    JSONArray monthSchedule;
    JSONArray currentSchedule;

    //일정 목록을 띄워주는 뷰를
    private ArrayList<Address_Item> arrayList=new ArrayList<Address_Item>();
    private ListView mListView;
    com.sonagi.android.myapplication.row.MyAdapter myAdapter=new com.sonagi.android.myapplication.row.MyAdapter();




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

    public JSONArray getNearSchedule() {
        class Get extends AsyncTask<String, Void, JSONArray> {
            @Override
            public JSONArray doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0] + "schedule/near/");
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

                        JSONArray responseJSON = new JSONArray(response);
                        return responseJSON;
                    } else {
                        return new JSONArray();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return new JSONArray();
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

        mListView=(ListView)view.findViewById(R.id.listview1);
        mListView.setAdapter(myAdapter);

        Calendar calendar = Calendar.getInstance();
        monthSchedule = getSchedule(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH));

        listview = (RecyclerView)view.findViewById(R.id.main_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);
        ArrayList<Schedule> itemList = new ArrayList<>(); //itemlist를 불러와서 서버에 저장하세요
        JSONArray jsonArray = getNearSchedule();

        try {
            if (jsonArray.length() == 0) {
                Schedule schedule = new Schedule();
                schedule.schedule_type = -1;
                schedule.start_date = "null";
                schedule.end_date = "null";
                schedule.title = "null";
                schedule.pk = -1;
                itemList.add(schedule);
            } else {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    Schedule schedule = new Schedule();
                    schedule.schedule_type = object.getInt("schedule_type");
                    schedule.start_date = object.getString("start_date");
                    schedule.end_date = object.getString("end_date");
                    schedule.title = object.getString("title");
                    schedule.pk = object.getInt("id");
                    itemList.add(schedule);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(), "에러 발생", Toast.LENGTH_LONG).show();
        }

        btnAdd=(Button)view.findViewById(R.id.btnAdd);
        btnDel=(Button)view.findViewById(R.id.btnDel);

        adapter = new MyAdapter(getActivity(), itemList, onClickItem);
        listview.setAdapter(adapter);

        MyListDecoration decoration = new MyListDecoration();
        listview.addItemDecoration(decoration);


         View.OnClickListener listener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.btnAdd:
                        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                        builder.setTitle("일정 입력");
                        LayoutInflater inflater=getLayoutInflater();
                        View v1=inflater.inflate(R.layout.add_dialog,null);
                        builder.setView(v1);

                        DialogListener listener=new DialogListener();
                        builder.setPositiveButton("확인",listener);
                        builder.setNegativeButton("취소",null);
                        builder.show();
                        break;
                    case R.id.btnDel:
                        myAdapter.removeItem();

                        myAdapter.notifyDataSetChanged();

                        break;
                }
            }
        };

        btnAdd.setOnClickListener(listener);
        btnDel.setOnClickListener(listener);

    }
    class DialogListener implements Dialog.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //얼럿다이올로그가 가지고 있는 뷰 가져온다
            AlertDialog alert =(AlertDialog)dialog;
            EditText sdate=(EditText)alert.findViewById(R.id.sdateD);
            EditText edate=(EditText)alert.findViewById(R.id.edateD);
            EditText plan=(EditText)alert.findViewById(R.id.planD);
            myAdapter.addItem(sdate.getText().toString(),edate.getText().toString(),plan.getText().toString(),false);
            Address_Item item=new Address_Item(sdate.getText().toString(),edate.getText().toString(),plan.getText().toString(),false);
            arrayList.add(item);
            myAdapter.notifyDataSetChanged();//리스너에게 바꼇다고 알람
        }
    }


    private View.OnClickListener onClickItem = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str = (String) v.getTag();
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        }
    };
}