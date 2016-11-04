package xhydew.buses;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    boolean lock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Chronometer chronometer = (Chronometer) findViewById(R.id.Main_chronometer);
        assert chronometer != null;
       // chronometer.setFormat("00:%s");

    }

    public ArrayList<Bus> getBuses() {
        EditText numPar = (EditText) findViewById(R.id.Main_Number_1);
        ArrayList<Bus> buses = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.infobuselche.es/ws/paradas/" + (numPar != null ? numPar.getText().toString() : "1") + ".php").openConnection();
            String json = IOUtils.toString(con.getInputStream(), con.getContentEncoding());

            JSONObject jsonObject = new JSONObject(json);
            jsonObject = jsonObject.getJSONObject("GetPasoParadaResult");
            JSONArray jsonArr = jsonObject.getJSONArray("PasoParada");
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject linea = jsonArr.getJSONObject(i);
                String lineaStr = linea.getString("linea");
                for (int j = 1; j <= 2; j++) {
                    JSONObject bus1 = linea.getJSONObject("e" + j);
                    int min = bus1.getInt("minutos");
                    int mtr = bus1.getInt("metros");
                    buses.add(new Bus(min, 1, mtr, lineaStr));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();

        }
        return buses;
    }

    public void click(View v) {
        if (!lock) {
            lock = true;
            findViewById(R.id.Main_Relative).setBackgroundColor(Color.BLACK);
            final Chronometer chronometer = (Chronometer) findViewById(R.id.Main_chronometer);
            assert chronometer != null;
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.start();
            //findViewById(R.id.Main_chronometer).;
            new Thread(new Runnable() {
                @Override
                public void run() {


                    String strBuses = "";
                    ArrayList<Bus> buses = getBuses();
                    for (Bus bus : buses) {
                        strBuses += bus.getLinea() + "\t\t--\t\t" + bus.getTiempo() + "\t\t--\t\t" + bus.getMetros() + " m\n";
                    }
                    final String finalStrBuses = strBuses;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView texto = (TextView) findViewById(R.id.Main_Texto_1);

                            assert texto != null;
                            texto.setText(finalStrBuses);
                            findViewById(R.id.Main_Relative).setBackgroundColor(Color.BLUE);
                            chronometer.stop();
                        }
                    });
                }
            }, "GET parada").start();


            lock = false;
        }
    }
}
