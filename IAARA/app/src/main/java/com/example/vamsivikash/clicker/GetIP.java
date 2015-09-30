package com.example.vamsivikash.clicker;

import android.app.Application;

/**
 * Created by Vamsi on 4/8/2015.
 */

// This class is used for finding the embedding the static IP address of the server.
// If in case you have installed the server at a different IP address, please enter the IP address here!
public class GetIP extends Application{

    public static String IP = "192.168.1.112";

    public String getIP(){
        return IP;
    }
    public void setIP(String s){
        IP = s;
    }

}
