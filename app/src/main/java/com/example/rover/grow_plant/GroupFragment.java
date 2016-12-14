package com.example.rover.grow_plant;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rover on 2016/12/7.
 */

public class GroupFragment extends ListFragment{

    private ListView groupList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.group_fragment, container, false);

        groupList = (ListView)view.findViewById(android.R.id.list);


        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        MyAdapter adapter = new MyAdapter();

        for (int i = 0; i < 10; i++) {
            DeviceStruct device = new DeviceStruct(i,"gggggg "+i*2,i/2);
            adapter.addItem(device);
        }



        setListAdapter(adapter);


    }


    class MyAdapter extends BaseAdapter {
//        String[] s = {"A", "B", "C", "D"};

        private ArrayList list_ = new ArrayList();


        @Override
        public int getCount() {
            return list_.size();
        }

        @Override
        public Object getItem(int position) {
            return list_.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(final DeviceStruct item) {
            list_.add(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            TextView textView = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.device_fragment_listview,
                        null);
                textView = (TextView)convertView.findViewById(R.id.devicename);

                ImageButton btn = (ImageButton) convertView.findViewById(R.id.devicearrow);
                btn.setTag(position);

                btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {


                        int index = (int)v.getTag();
                        DeviceStruct ds = (DeviceStruct) list_.get(index);
                        Log.i("test", "A A:" + ds.getDevice_name());


                    }

                });

                DeviceStruct device = (DeviceStruct) list_.get(position);

                textView.setText(device.getDevice_name());

                convertView.setTag(holder);


            } else {
                holder = (ViewHolder) convertView.getTag();

                textView = (TextView)convertView.findViewById(R.id.devicename);

                ImageButton btn = (ImageButton) convertView.findViewById(R.id.devicearrow);
                btn.setTag(position);

                btn.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        int index = (int)v.getTag();
                        DeviceStruct ds = (DeviceStruct) list_.get(index);
                        Log.i("test", "A A:" + ds.getDevice_name());



                    }
                });


                DeviceStruct device = (DeviceStruct) list_.get(position);

                textView.setText(device.getDevice_name());


                Log.i("test", "B B:" + position + "," + holder.hashCode());
            }

            return convertView;
        }
    }

    class ViewHolder {
        TextView tv;
    }



}
