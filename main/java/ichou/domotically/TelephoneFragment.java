package ichou.domotically;

import android.os.Bundle;
import android.service.carrier.CarrierMessagingService;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by HP I7 on 06/02/2017.
 */

public class TelephoneFragment extends Fragment {



    Spinner spinner;
    ToggleButton toggleButton;
    EditText ifAnalog;
    Button append;
    RadioGroup radioGroup;
    TextView textView;
    Button sendSMS;

    int onoff;
    boolean checked=false;
    boolean selected = false;
    String pin;
    int analdigit;
    String messageToSend = "#";
    String phonenum = "0663629258";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.telephone_main,container,false);

        textView = (TextView)v.findViewById(R.id.message);

        radioGroup = (RadioGroup)v.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                checked = true;
                if(checkedId==R.id.radioAnalog){
                    analdigit = 1;
                    ifAnalog.setVisibility(View.VISIBLE);
                    toggleButton.setVisibility(View.GONE);
                }else if(checkedId == R.id.radioDigital){
                    analdigit = 0;
                    ifAnalog.setVisibility(View.GONE);
                    toggleButton.setVisibility(View.VISIBLE);
                }
            }
        });

        spinner = (Spinner)v.findViewById(R.id.pin);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.pins, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pin = parent.getItemAtPosition(position).toString();
                selected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected=false;
            }
        });

        toggleButton = (ToggleButton)v.findViewById(R.id.onOff);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) onoff=0;
                else onoff=30;
            }
        });

        ifAnalog = (EditText) v.findViewById(R.id.valueIfAnalog);

        append = (Button)v.findViewById(R.id.append);
        append.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(analdigit == 0){
                    if(checked && selected){
                        int pinonoff= Integer.parseInt(pin) + onoff;
                        messageToSend += "D"+pinonoff+"#";
                    }
                    else Toast.makeText(getActivity(),"You must fulfill all the fields",Toast.LENGTH_SHORT).show();
                }
                else if(analdigit == 1){
                    if(checked && selected && !(ifAnalog.getText().equals(null))){
                        messageToSend += "A"+","+pin+","+(ifAnalog.getText())+"#";
                    }
                    else Toast.makeText(getActivity(),"You must fulfill all the fields",Toast.LENGTH_SHORT).show();
                }
                textView.setText(messageToSend);
                //sending it actually would go down here

            }
        });

        sendSMS = (Button)v.findViewById(R.id.sendSMS);
        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendViaSMS();

            }
        });
        return v;
    }

    private void sendViaSMS() {
        try{
            SmsManager smsM = SmsManager.getDefault();
            smsM.sendTextMessage(phonenum,null,messageToSend,null,null);
            Toast.makeText(getActivity(),"Message sent to target",Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(getActivity(),"Message failed to transmit",Toast.LENGTH_SHORT).show();
        }

    }


}
