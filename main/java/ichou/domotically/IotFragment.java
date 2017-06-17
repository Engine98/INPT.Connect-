package ichou.domotically;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amigold.fundapter.BindDictionary;
import com.amigold.fundapter.FunDapter;
import com.amigold.fundapter.extractors.StringExtractor;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.kosalgeek.android.json.JsonConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HP I7 on 06/02/2017.
 */

public class IotFragment extends Fragment {


    Button refresh;
    ListView listView;
    EditText enterIP,enterPort,enterDomain;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.iot_main,container,false);
        listView=(ListView)v.findViewById(R.id.listFromVolley);
        listView.setVisibility(View.GONE);
        refresh =(Button)v.findViewById(R.id.refreshVolley);
        enterIP = (EditText)v.findViewById(R.id.enterIP);
        enterPort = (EditText)v.findViewById(R.id.enterPort);
        enterDomain = (EditText)v.findViewById(R.id.enterDomain);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setVisibility(View.VISIBLE);
                String myURL = "http://172.16.108.197:8000/api/rooms";
                if(enterIP.getText()!=null && enterPort.getText()!=null && enterDomain.getText()!=null){
                    if(!enterIP.getText().equals("")  && !enterDomain.getText().equals("")){
                        myURL = "http://"+enterIP.getText()+":"+enterPort.getText()+"/"+enterDomain.getText();
                    }
                }
                StringRequest stringRequest = new StringRequest(myURL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String[] a = response.split("\\[");
                        String[] b = a[1].split("\\]");
                        String json = "["+b[0]+"]";
                        ArrayList<Rooms> jsondata= new JsonConverter<Rooms>().toArrayList(json, Rooms.class);
                        BindDictionary<Rooms> bD = new BindDictionary();
                        bD.addStringField(R.id.field1, new StringExtractor<Rooms>() {
                            @Override
                            public String getStringValue(Rooms item, int position) {
                                return item.room_name;
                            }
                        });
                        bD.addStringField(R.id.field2, new StringExtractor<Rooms>() {
                            @Override
                            public String getStringValue(Rooms item, int position) {
                                return ""+item.volume;
                            }
                        });
                        bD.addStringField(R.id.field3, new StringExtractor<Rooms>() {
                            @Override
                            public String getStringValue(Rooms item, int position) {
                                return ""+item.surface;
                            }
                        });
                        bD.addStringField(R.id.field4, new StringExtractor<Rooms>() {
                            @Override
                            public String getStringValue(Rooms item, int position) {
                                return ""+item.temperature;
                            }
                        });
                        bD.addStringField(R.id.field5, new StringExtractor<Rooms>() {
                            @Override
                            public String getStringValue(Rooms item, int position) {
                                return ""+item.humidity;
                            }
                        });
                        FunDapter<Rooms> fA = new FunDapter<Rooms>(getContext(), jsondata, R.layout.rooms_layout, bD);
                        listView.setAdapter(fA);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Error occured", Toast.LENGTH_SHORT).show();
                    }
                });/*{
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> info = new HashMap<>();
                info.put("username","ichou");
                info.put("password", "cyd");
                return info;
            }
        }*/
                MySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
            }
        });


        return v;
    }

}
