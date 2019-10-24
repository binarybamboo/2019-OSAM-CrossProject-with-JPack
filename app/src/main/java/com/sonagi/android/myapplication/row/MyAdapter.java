package com.sonagi.android.myapplication.row;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.sonagi.android.myapplication.R;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    /* 아이템을 세트로 담기 위한 어레이 */
    private ArrayList<Address_Item> mItems = new ArrayList<>();



    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Address_Item getItem(int position) {

        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_chkbox, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        EditText s_date = (EditText) convertView.findViewById(R.id.s_date) ;
        EditText e_date = (EditText) convertView.findViewById(R.id.e_date) ;
        CheckBox chkbox = (CheckBox)convertView.findViewById(R.id.checkBox);
        EditText plan = (EditText) convertView.findViewById(R.id.plan) ;

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        final Address_Item myItem = getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        s_date.setText(myItem.getsDate());
        e_date.setText(myItem.geteDate());

        plan.setText(myItem.getPlan());
        chkbox.setChecked(myItem.isCheckBoxState());

        chkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myItem.setCheckBoxState(isChecked);
            }
        });

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */


        return convertView;
    }

    public void removeItem(){
        ArrayList<Address_Item> remove_object = new ArrayList<Address_Item>();

        for(Address_Item item : mItems){
            if(item.isCheckBoxState() == true){
                remove_object.add(item);
            }
        }

        for(Address_Item item : remove_object){
            mItems.remove(item);
        }


    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem( String sdate, String edate,String plan,boolean chk) {

        Address_Item mItem = new Address_Item();

        /* MyItem에 아이템을 setting한다. */
        mItem.setCheckBoxState(chk);
        mItem.setplan(plan);
        mItem.setsDate(sdate);
        mItem.seteDate(edate);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(mItem);

    }
}