package com.example.rover.grow_plant;

import android.app.Fragment;
import android.app.ListFragment;
import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.widget.BaseAdapter;
import android.widget.Toast;

/**
 * Created by rover on 2016/12/7.
 */

public class DeviceFragment extends ListFragment implements View.OnClickListener {

    private ListView listView;
    private SimpleAdapter adapter;


    public interface enterDimmerController{
        public void roverListItemSelected(DeviceStruct device);
    }
    enterDimmerController lcallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            lcallback = (enterDimmerController) context;
            Log.i("onAttach onAttach","112");
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.device_fragment, container, false);

        listView = (ListView)view.findViewById(android.R.id.list);

        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.devicearrow:{
                Log.i("vvv ",".tag = "+ v.getTag());
            }
                break;
            default:
                break;
        }

    }


    public int getCount(){
        Log.i("here  here  ?"," here ");
        return 4;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("onCreate "," onCreate ");


//        listView.setAdapter(new MyAdapter());





        MyAdapter adapter = new MyAdapter();

        for (int i = 0; i < 50; i++) {
            DeviceStruct device = new DeviceStruct(i,"deviceff "+i*2,i/2);
            adapter.addItem(device);
        }



        setListAdapter(adapter);


//        btn.setOnClickListener(this);


//        adapter = new SimpleAdapter(getActivity(),
//                getData(),
//                R.layout.device_fragment_listview,
//                new String[]{"name"},
//                new int[]{
//                        R.id.devicename,
//
//                        Log.i("Heelo  dfaf","afdsfasd /n/n"),
//            });
//        setListAdapter(adapter);

    }

    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", "one");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "tow");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "three");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "four");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("name", "five");
        list.add(map);

        return list;
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

                        lcallback.roverListItemSelected(ds);


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



    //listview 点击某一行
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);


        Log.i("list view 点击行 "," = " + position);


    }


}
