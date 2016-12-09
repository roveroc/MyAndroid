package com.example.rover.grow_plant;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by rover on 2016/12/7.
 */

public class TabbarFragment extends Fragment implements View.OnClickListener{

    private View view;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.tabbar_fragment, container, false);

        view.findViewById(R.id.btn1).setOnClickListener(this);
        view.findViewById(R.id.btn2).setOnClickListener(this);
        view.findViewById(R.id.btn3).setOnClickListener(this);
        view.findViewById(R.id.btn4).setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1: {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();

                DeviceFragment device = (DeviceFragment) fm.findFragmentById(R.id.fragment_content);
                GroupFragment  group = (GroupFragment) fm.findFragmentById(R.id.fragment_content_2);
                TimerFragment  timer = (TimerFragment) fm.findFragmentById(R.id.fragment_content_3);
                MoreFragment   more = (MoreFragment)   fm.findFragmentById(R.id.fragment_content_4);

                transaction.show(device);
                transaction.hide(group);
                transaction.hide(timer);
                transaction.hide(more);
                transaction.commit();
            }
                break;
            case R.id.btn2: {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();

                DeviceFragment device = (DeviceFragment) fm.findFragmentById(R.id.fragment_content);
                GroupFragment  group = (GroupFragment) fm.findFragmentById(R.id.fragment_content_2);
                TimerFragment  timer = (TimerFragment) fm.findFragmentById(R.id.fragment_content_3);
                MoreFragment   more = (MoreFragment)   fm.findFragmentById(R.id.fragment_content_4);

                transaction.hide(device);
                transaction.show(group);
                transaction.hide(timer);
                transaction.hide(more);
                transaction.commit();
            }
                break;
            case R.id.btn3: {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();

                DeviceFragment device = (DeviceFragment) fm.findFragmentById(R.id.fragment_content);
                GroupFragment  group = (GroupFragment) fm.findFragmentById(R.id.fragment_content_2);
                TimerFragment  timer = (TimerFragment) fm.findFragmentById(R.id.fragment_content_3);
                MoreFragment   more = (MoreFragment)   fm.findFragmentById(R.id.fragment_content_4);

                transaction.hide(device);
                transaction.hide(group);
                transaction.show(timer);
                transaction.hide(more);
                transaction.commit();
            }
                break;
            case R.id.btn4: {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();

                DeviceFragment device = (DeviceFragment) fm.findFragmentById(R.id.fragment_content);
                GroupFragment  group = (GroupFragment) fm.findFragmentById(R.id.fragment_content_2);
                TimerFragment  timer = (TimerFragment) fm.findFragmentById(R.id.fragment_content_3);
                MoreFragment   more = (MoreFragment)   fm.findFragmentById(R.id.fragment_content_4);

                transaction.hide(device);
                transaction.hide(group);
                transaction.hide(timer);
                transaction.show(more);
                transaction.commit();
            }
                break;
            default:
                break;
        }

    }

}
