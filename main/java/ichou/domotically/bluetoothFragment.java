package ichou.domotically;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.ivbaranov.mli.MaterialLetterIcon;

import ichou.domotically.Arduino;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;


/**
 * Created by HP I7 on 06/02/2017.
 */

public class bluetoothFragment extends Fragment  {

    private final int ENABLE_BLUETOOTH=0;


    //ArrayList<Arduino> arduinoso;


    private BluetoothAdapter bluetoothAdapter;
    //private BluetoothLeScanner bluetoothLeScanner;
    //private Set<BluetoothDevice> bluetoothDevices;
    //private ArrayList<Arduino> arduinos;
    //private ArrayList<String> arduinoName;
    //private ArrayList<String> arduinoMAC;
    private ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<BluetoothDevice>();

    //ArrayList<Arduino> empty;
    private RecyclerView.Adapter adapter;
    private ArrayList<Arduino> bluetoothDevicesParcour = new ArrayList<Arduino>() ;
    private RecyclerView recyclerView;

    //ArrayList<String> names;
    //ArrayList<String> mac;


    private BroadcastReceiver broadcastReceiver;
    private ArrayList<Arduino> arduinos = new ArrayList<Arduino>();
    private Button scan;


    private int[] colors;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bluetooth_main,container,false);



        checkBluetoothEnabled();


        return v;
    }




    public void scanBluetooth(){
        if(bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.startDiscovery();




    }



   /* private void connectToDevice(View v){
        Toast.makeText(getActivity(),"Connection ...",Toast.LENGTH_LONG).show();

    }*/
    /*private void fillArduinos(){
        int j = bluetoothDevicesParcour.size();

        for(int i = 0 ; i<j ; i++){
            String name = bluetoothDevicesParcour.get(i).getDeviceName();
            arduinos.add(  new Arduino(bluetoothDevicesParcour.get(i).getDeviceMAC(),name));
        }


    }*/

    private void checkBluetoothEnabled(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        if(bluetoothAdapter == null){
            Toast.makeText(getActivity(),"Vous ne pouvez pas utiliser cette utilité parceque vous n'avez pas bluetooth sur votre appareil",Toast.LENGTH_LONG).show();
        }
        else{
            if (!bluetoothAdapter.isEnabled()){
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent,ENABLE_BLUETOOTH);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == ENABLE_BLUETOOTH){
            if (resultCode == getActivity().RESULT_OK){
                if(bluetoothAdapter.isDiscovering()){
                    bluetoothAdapter.cancelDiscovery();

                    Toast.makeText(getActivity(),"Bluetooth Activé",Toast.LENGTH_SHORT).show();
                }
            }
            else if (resultCode == getActivity().RESULT_CANCELED){
                Toast.makeText(getActivity(),"Vous ne pouvez pas travailez sur ce segment sans connection Bluetooth",Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private int generator(){
        Random rd = new Random();
        return rd.nextInt(8);
    }

    @Override
    public void onResume() {
        scan = (Button)getView().findViewById(R.id.scanBluetoothButton);


        recyclerView = (RecyclerView) getView().findViewById(R.id.recycler);
        colors = new int[]{R.color.colorAccent,R.color.Scree3_Dark,R.color.Scree4_Dark,R.color.Scree1_Light,R.color.Scree3_Light,R.color.colorPrimary,R.color.colorPrimaryDark,R.color.Scren2,R.color.Scree1_Dark};
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getView().getContext());
        recyclerView.setLayoutManager(layoutManager);
        //adapter = new RecyclerAdapter();
        adapter = new MyAdapter(bluetoothDevicesParcour);
        recyclerView.setAdapter(adapter);







        //filldata();

        /*arduinos = new ArrayList<Arduino>();
        arduinoName = new ArrayList<String>();
        arduinoMAC = new ArrayList<String>();*/         ////////////////////////////////////

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                scanBluetooth();
            }
        });


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if(BluetoothDevice.ACTION_FOUND.equals(action)){
                    BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    //Toast.makeText(getActivity(),remoteDevice.getName(),Toast.LENGTH_LONG).show();



                    bluetoothDevices.add(remoteDevice);


                    //arduinos.add(new Arduino(remoteDevice.getName(),remoteDevice.getAddress()));
                    //arduinoName.add(remoteDevice.getName());
                    //arduinoMAC.add(remoteDevice.getAddress());
                    //bluetoothDevices.add(remoteDevice);
                    bluetoothDevicesParcour.add(new Arduino(remoteDevice.getAddress(),remoteDevice.getName(),remoteDevice.getName().substring(0,1)));
                    //empty.add(0,new Arduino(remoteDevice.getAddress(),remoteDevice.getName()));
                    //adapter.notifyItemInserted(0);

                    adapter.notifyDataSetChanged();

                }
                if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){

                    bluetoothDevices.clear();
                    bluetoothDevicesParcour.clear();

                    scan.setVisibility(View.GONE);

                }
                if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                    scan.setVisibility(View.VISIBLE);
                    //fillArduinos();


                    Toast.makeText(getActivity(),"Recherche des appareils domestiques achevée",Toast.LENGTH_SHORT).show();
                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);




        try {

            getActivity().registerReceiver(broadcastReceiver,intentFilter);


        }catch (Exception e){

        }


        //adapter.notifyDataSetChanged();






        super.onResume();
    }


    @Override
    public void onDestroyView() {

        super.onDestroyView();
    }

    @Override
    public void onStop() {
       try {

           getActivity().unregisterReceiver(broadcastReceiver);

       }catch (Exception e){

        }


        super.onStop();
    }




    /*public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
        class ViewHolder extends RecyclerView.ViewHolder{
            public int currentDevice;
            //public MaterialLetterIcon initial;
            public TextView deviceName;
            public TextView deviceMAC;


            public ViewHolder(View itemView) {
                super(itemView);
                //initial = (MaterialLetterIcon)itemView.findViewById(R.id.initial);
                deviceName = (TextView)itemView.findViewById(R.id.deviceName);
                deviceMAC = (TextView)itemView.findViewById(R.id.deviceMAC);
                itemView.setOnClickListener(new View.OnClickListener() {
                    int position = getAdapterPosition();
                    @Override
                    public void onClick(View v) {
                        if(bluetoothAdapter.isDiscovering()){
                            bluetoothAdapter.cancelDiscovery();
                            connectToDevice(v);
                        }
                    }
                });
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardforbluetoothdevice,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {


            //holder.initial.setLetter(bluetoothDevicesParcour.get(position).getIcon());
            //holder.initial.setLetterColor(getResources().getColor(colors[generator()]));
            //holder.deviceName.setText(bluetoothDevicesParcour.get(position).getDeviceName());
            //holder.deviceMAC.setText(bluetoothDevicesParcour.get(position).getDeviceMAC());

            holder.deviceName.setText(arduinoso[position].getDeviceName());
            holder.deviceMAC.setText(arduinoso[position].getDeviceMAC());

        }

        @Override
        public int getItemCount() {

            return arduinoso.length;
        }
    }*/

    @Override
    public void onDestroy() {
        bluetoothAdapter.cancelDiscovery();
        super.onDestroy();
    }

    public interface onSomeEvent{
        public void someEvents(ArrayList<BluetoothDevice> bluetoothDevices, int position);
    }

    onSomeEvent someEvent;
    @Override
    public void onAttach(Activity activity) {
        try {
            someEvent = (onSomeEvent) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
        super.onAttach(activity);
    }

    /* public void filldata() {
        arduinoso = new ArrayList<Arduino>();
        arduinoso.add(new Arduino("sadsdsf","sdfdfx"));
        arduinoso.add(new Arduino("sdqd","586465"));
        arduinoso.add(new Arduino("sdsd","ghfgh"));
        arduinoso.add(new Arduino("564653edfgf","sdfsdf"));
        arduinoso.add(new Arduino("fdgdfg","6565"));

    }*/

    private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        //setting local data variable so as not to modify the original
        ArrayList<Arduino> myDataSet;

        //must redefine functions of the view holder passed as a param

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //what will happen when the view holder who holds our data is created?
            //we have to determine the view to insert into the holder
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardforbluetoothdevice,parent,false); //getting the view by inflating an xml file
            //creating a new view holder from this view
            ViewHolder s =new ViewHolder(v); //accepts text variable through its definition
            // returning it to be set as default when the view is created
            return s;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            positionclicked = position;
            //when the viewholder is created from the custom layout we have to redifine the parameters of the layout and set the content when the holder is binded
            TextView desc= (TextView) holder.myView.findViewById(R.id.deviceName);
            TextView date= (TextView) holder.myView.findViewById(R.id.deviceMAC);
            MaterialLetterIcon icon = (MaterialLetterIcon) holder.myView.findViewById(R.id.initial);
            desc.setText((myDataSet.get(position)).getDeviceName());
            date.setText(myDataSet.get(position).getDeviceMAC());
            icon.setLetter((myDataSet.get(position)).getDeviceName().substring(0,1));
            icon.setShapeColor(colors[generator()]);

        }


        @Override
        public int getItemCount() {
            //function that gets the number items on the list
            return myDataSet.size();
        }
        public MyAdapter(ArrayList<Arduino> myDataSet) {
            //getting the data set passed as a parameter
            this.myDataSet=myDataSet;
        }
    }

    int positionclicked;
    private class ViewHolder extends RecyclerView.ViewHolder {
        //creating the custum viewholder
        //local variable text view to not damage the original
        //this holder is gonna handle text so we have to construct it from text
        View myView;
        //overriding the constructor
        public ViewHolder(final View itemView) {
            //doing what the super holder can do
            super(itemView);
            //we got the item passed as a parameter because we have to alter it during on bind
            myView=itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bluetoothAdapter.cancelDiscovery();
                    //Toast.makeText(getActivity(),"Connection ........",Toast.LENGTH_SHORT).show();


                    someEvent.someEvents(bluetoothDevices,positionclicked);




                    //establish connection
                }
            });
        }

    }




}
