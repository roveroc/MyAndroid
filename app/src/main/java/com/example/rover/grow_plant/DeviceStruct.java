package com.example.rover.grow_plant;

import java.io.Serializable;

/**
 * Created by rover on 2016/12/8.
 */

public class DeviceStruct implements Serializable {

    private int device_id;
    private String device_name;
    private int device_slave;

    public DeviceStruct(int d_id,String d_name,int d_slave){
        this.device_id = d_id;
        this.device_name = d_name;
        this.device_slave = d_slave;
    }


    public int getDevice_id(){
        return device_id;
    }

    public int getDevice_slave(){
        return device_slave;
    }

    public String getDevice_name(){
        return device_name;
    }

}
