package com.example.jurara.webservicemoviles;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    TextView txt;
    JSONObject data = null;
    double peso;
    EditText edi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt=(TextView)findViewById(R.id.txtsan);
        edi=(EditText)findViewById(R.id.txt1);

        getJSON();
        edi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    ((EditText) view).addTextChangedListener(new TextWatcher() {

                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            //
                            DecimalFormat formateador=new DecimalFormat("##.#####");

                            try{
                                Double q;
                                q=Double.parseDouble(edi.getText().toString());
                                txt.setText("El dolar hoy:\n"+peso+"\n"+edi.getText().toString()+" Dolares = "+formateador.format(peso*Double.parseDouble(edi.getText().toString())));

                            }catch (NumberFormatException e){
                                txt.setText("El dolar hoy:\n"+peso);
                            }

                        }

                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                            //

                        }

                        public void afterTextChanged(Editable s) {
                            // affect EditText2

                        }
                    });

                }
                if(!b){
                    //((EditText) view).removeTextChangedListener();
                }
            }
        });

    }

    public void getJSON() {

        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("https://openexchangerates.org/api/latest.json?app_id=6b155114593046398dba98d757ea94bd");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    data = new JSONObject(json.toString());

                    if(data.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }



                } catch (Exception e) {

                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if(data!=null){
                    Log.d("my weather received",data.toString());
                    try {
                        Log.d("-------->>>>",data.getString("main")+"");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    try {


                        JSONObject ab = new JSONObject(data.getString("rates"));

                        txt.setText("El dolar hoy:\n"+ab.getDouble("MXN")+"");
                        peso=ab.getDouble("MXN");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.execute();

    }
}

