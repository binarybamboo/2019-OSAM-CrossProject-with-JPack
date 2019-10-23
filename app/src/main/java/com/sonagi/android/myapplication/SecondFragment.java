package com.sonagi.android.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sonagi.android.myapplication.today_tab.MyAdapter;
import com.sonagi.android.myapplication.today_tab.MyListDecoration;

import java.util.ArrayList;


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
        init(view);


        return view;
    }
    public void init(View view){
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