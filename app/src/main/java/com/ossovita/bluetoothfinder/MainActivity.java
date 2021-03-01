package com.ossovita.bluetoothfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> devices = new ArrayList<>();
    ArrayList<String> addresses = new ArrayList<>();
    ArrayAdapter aa;
    ListView listView;
    TextView statusTextView;
    Button searchButton;
    BluetoothAdapter bluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        statusTextView = findViewById(R.id.statusTextView);
        searchButton = findViewById(R.id.searchButton);
        aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1,devices);
        listView.setAdapter(aa);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver,intentFilter);
    }



    public void search(View v){
        statusTextView.setText("Searching...");
        searchButton.setEnabled(false);

        devices.clear();
        bluetoothAdapter.startDiscovery();

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Action",action);

            //Arama bittiğinde action objesi finished şeklinde bir değer döndürür
            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){//yani arama bittiğinde
                statusTextView.setText("Finished");
                searchButton.setEnabled(true);
            }else if (BluetoothDevice.ACTION_FOUND.equals(action)){//yeni bulunan cihazı device objesine at
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE));
                //String info = "Name: "+ name + " address: " + address+ " RSSI:" + rssi;
                if(!addresses.contains(address)){
                    addresses.add(address);
                    String deviceString="";
                    addresses.add(address);
                    if(name==null||name.equals("")){
                        deviceString=address+" - RSSI " + rssi+"dBm";
                    }else{
                        deviceString=name+ " -RSSI " + rssi + "dBm";
                    }
                    devices.add(deviceString);
                    aa.notifyDataSetChanged();
                }




            }

        }
    };
}