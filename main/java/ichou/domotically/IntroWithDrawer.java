package ichou.domotically;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/*---------------------------------*/

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarBadge;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;


/*---------------------------------*/

public class IntroWithDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, bluetoothFragment.onSomeEvent, SendDataViaBluetooth.onSendAction, thread {

    /*--------------------------------*/
    public BottomBar mBottomBar;
    /*---------------------------------*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_with_drawer);

        /*-------------------------------------------------*/
        mBottomBar = BottomBar.attach(this,savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.menu_main, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

                if(menuItemId==R.id.Bottombaritemone){
                    bluetoothFragment bFrag = new bluetoothFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,bFrag).commit();
                    navigationView.setCheckedItem(R.id.connection);

                }
                if(menuItemId==R.id.Bottombaritemtwo){
                    NotificationFragment bFrag = new NotificationFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,bFrag).commit();
                    navigationView.setCheckedItem(R.id.mur);
                }
                if(menuItemId==R.id.Bottombaritemthree){
                    StatsFragment bFrag = new StatsFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,bFrag).commit();
                    navigationView.setCheckedItem(R.id.stats);
                }
                if(menuItemId==R.id.Bottombaritemfour){
                    TelephoneFragment bFrag = new TelephoneFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,bFrag).commit();
                    navigationView.setCheckedItem(R.id.mobile);
                }
                if(menuItemId==R.id.Bottombaritemfive){
                    IotFragment bFrag = new IotFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,bFrag).commit();
                    navigationView.setCheckedItem(R.id.connect_internet);
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {

            }
        });
        mBottomBar.mapColorForTab(0,"#F44336");
        mBottomBar.mapColorForTab(1,"#E91E63");
        mBottomBar.mapColorForTab(2,"#9C27B0");
        mBottomBar.mapColorForTab(3,"#2196F3");
        mBottomBar.mapColorForTab(4,"#009688");

        //unread notifications
        BottomBarBadge unread;
        unread = mBottomBar.makeBadgeForTabAt(2,"#FF0000",5);
        unread.show();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



    }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.connection) {
            bluetoothFragment bFrag = new bluetoothFragment();
            selectFragment(bFrag,1);
        } else if (id == R.id.mur) {
            NotificationFragment bFrag = new NotificationFragment();
            selectFragment(bFrag,0);
        } else if (id == R.id.stats) {
            StatsFragment bFrag = new StatsFragment();
            selectFragment(bFrag,2);
        }  else if (id == R.id.connect_internet) {
            IotFragment bFrag = new IotFragment();
            selectFragment(bFrag,4);
        }else if (id == R.id.bluetooth) {
            SendDataViaBluetooth bFrag = new SendDataViaBluetooth();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,bFrag).commit();
            mBottomBar.hide();
        } else if (id == R.id.mobile) {
            TelephoneFragment bFrag = new TelephoneFragment();
            selectFragment(bFrag,3);
        }
        else if (id == R.id.aboutUs) {
            AboutUs bFrag = new AboutUs();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,bFrag).commit();
            mBottomBar.hide();

        }
        else if (id == R.id.contactUs) {
            ContactUs bFrag = new ContactUs();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame,bFrag).commit();
            mBottomBar.hide();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void selectFragment(Fragment bFrag, int position){
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,bFrag).commit();
        mBottomBar.show();
        mBottomBar.selectTabAtPosition(position,true);
    }



    //TextView receivedtext;
    ConnectedThread connectedThread;
    public void someEvents(ArrayList<BluetoothDevice> bluetoothDevices, int positionclicked){
        ConnectThread connectThread = new ConnectThread(bluetoothDevices.get(positionclicked));
        connectThread.run();
        BluetoothSocket mySocket =connectThread.manageMyConnectedSocket();
        connectedThread = new ConnectedThread(mySocket, mHandler);
        SendDataViaBluetooth sendDataViaBluetooth = new SendDataViaBluetooth();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,sendDataViaBluetooth).commit();
        ((NavigationView)findViewById(R.id.nav_view)).setCheckedItem(R.id.bluetooth);
        mBottomBar.hide();
        //final EditText textToSendFromFrag = (EditText)findViewById(R.id.textToSend);
        //Button sendTextFrag = (Button)findViewById(R.id.sendBtn);
        //receivedtext.setText(""+connectedThread.getStringReceived());
        /*sendTextFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = textToSendFromFrag.getText().toString();
                byte[] mybytes = string.getBytes();
                connectedThread.write(mybytes);
            }
        });*/

    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                /*case thread.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;

                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf,0,msg.arg1);
                    receivedtext = (TextView)findViewById(R.id.textReceived);

                    receivedtext.setText(writeMessage);
                    break;*/
                case MESSAGE_READ:
                    //receivedtext = (TextView)findViewById(R.id.textReceived);

                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf,0 , msg.arg1);
                    //receivedtext.setText(readMessage);
                    Toast.makeText(getApplication(),readMessage,Toast.LENGTH_LONG).show();

                    break;

                /*case thread.MESSAGE_TOAST:

                    Toast.makeText(this, msg.getData().getString(thread.MESSAGE_TOAST),
                            Toast.LENGTH_SHORT).show();

                    break;*/
            }
        }
    };


    @Override
    public void sendAction(byte[] s) {
        connectedThread.write(s);
    }//getBytes()


}
