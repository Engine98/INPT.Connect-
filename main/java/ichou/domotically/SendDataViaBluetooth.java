package ichou.domotically;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import ichou.domotically.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import java.util.logging.LogRecord;

/**
 * Created by HP I7 on 06/02/2017.
 */

public class SendDataViaBluetooth extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.senddataviabluetooth,container,false);



        return v;
    }

    public interface onSendAction{
        public void sendAction(byte[] s);////
    }

    onSendAction sendActions;

    //EditText textToSend;
    //Button send;
    //TextView received;
    Button machFirst,machSecond,machThird,machFourth;
    @Override
    public void onStart() {
        //textToSend = (EditText)getView().findViewById(R.id.textToSend);
        //send = (Button)getView().findViewById(R.id.sendBtn);
        //received = (TextView)getView().findViewById(R.id.textReceived);
        machFirst = (Button)getView().findViewById(R.id.machFirst);
        machSecond = (Button)getView().findViewById(R.id.machSecond);
        machThird = (Button)getView().findViewById(R.id.machThird);
        machFourth = (Button)getView().findViewById(R.id.machFourth);


        /*send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendActions.sendAction(""+textToSend.getText().toString());
            }
        });*/

        machFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] x;

                if(machFirst.getText().equals("Machine 1 - OFF")){
                    x = new byte[]{36};
                    sendActions.sendAction(x);
                    machFirst.setText("Machine 1 - ON");
                    machFirst.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                }
                else if(machFirst.getText().equals("Machine 1 - ON")){
                    x = new byte[]{66};
                    sendActions.sendAction(x);
                    machFirst.setText("Machine 1 - OFF");
                    machFirst.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));

                }
            }
        });
        machSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] x;

                if(machSecond.getText().equals("Machine 2 - OFF")){
                    x = new byte[]{37};
                    sendActions.sendAction(x);
                    machSecond.setText("Machine 2 - ON");
                    machSecond.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                }
                else if(machSecond.getText().equals("Machine 2 - ON")){
                    x = new byte[]{67};

                    sendActions.sendAction(x);
                    machSecond.setText("Machine 2 - OFF");
                    machSecond.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }
        });
        machThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] x;

                if(machThird.getText().equals("Machine 3 - OFF")){
                    x = new byte[]{38};

                    sendActions.sendAction(x);
                    machThird.setText("Machine 3 - ON");
                    machThird.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                }
                else if(machThird.getText().equals("Machine 3 - ON")){
                    x = new byte[]{68};

                    sendActions.sendAction(x);
                    machThird.setText("Machine 3 - OFF");
                    machThird.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }
        });
        machFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] x;

                if(machFourth.getText().equals("Machine 4 - OFF")){
                    x = new byte[]{39};

                    sendActions.sendAction(x);
                    machFourth.setText("Machine 4 - ON");
                    machFourth.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                }
                else if(machFourth.getText().equals("Machine 1 - ON")){
                    x = new byte[]{69};

                    sendActions.sendAction(x);
                    machFourth.setText("Machine 4 - OFF");
                    machFourth.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }
        });
        super.onStart();
    }
    @Override
    public void onAttach(Activity activity) {
        try {
            sendActions = (onSendAction) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
        super.onAttach(activity);
    }



}
