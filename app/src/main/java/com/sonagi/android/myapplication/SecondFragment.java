package com.sonagi.android.myapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonagi.android.myapplication.row.Address_Item;
import com.sonagi.android.myapplication.today_tab.MyAdapter;
import com.sonagi.android.myapplication.today_tab.MyListDecoration;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SecondFragment extends Fragment {


    private RecyclerView listview;
    private MyAdapter adapter;


    Button btnAdd, btnDel;
    EditText editText;

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

        init(view);


        return view;
    }
    public void init(View view){
        mListView=(ListView)view.findViewById(R.id.listview1);
        mListView.setAdapter(myAdapter);

        listview = (RecyclerView)view.findViewById(R.id.main_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        listview.setLayoutManager(layoutManager);
        ArrayList<String> itemList = new ArrayList<>(); //itemlist를 불러와서 서버에 저장하세요
        itemList.add("2019-10-23\n 유격 훈련");
        itemList.add("2019-10-23\n 혹한기 훈련");
        itemList.add("2019-10-23\n 끔직한 훈련");

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