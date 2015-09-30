package com.example.vamsivikash.clicker;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.Inet4Address;


public class Connect extends ActionBarActivity {

    private static final String IPADDRESS_PATTERN = "\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3} (?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        com.beardedhen.androidbootstrap.BootstrapButton btnConnect;

        btnConnect = (com.beardedhen.androidbootstrap.BootstrapButton) findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText ipaddress;
                ipaddress = (EditText) findViewById(R.id.ipaddress);
                GetIP IP = new GetIP();
                String ip = ipaddress.getText().toString();

                    // Check if it's a valid ip address or not

                    if(ipaddress.getText().toString().matches("^\\d{1,3}(\\." +
                            "(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        IP.setIP(ipaddress.getText().toString());
                        Intent intent = new Intent(Connect.this, Login.class);
                        startActivity(intent);
                    }

                    else{
                        Toast toast = Toast.makeText(Connect.this, "Please enter a valid IP Address", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 25, 100);
                        toast.show();
                    }
            }
        });
    }
}
