package xhydew.buses;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public ArrayList<Bus> getBuses() {
        EditText numPar = (EditText) findViewById(R.id.Main_Number_1);
        ArrayList<Bus> buses = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.infobuselche.es/ws/paradas/"+numPar.getText().toString()+".php").openConnection();
            String json = IOUtils.toString(con.getInputStream(), con.getContentEncoding());

            JSONObject jsonObject = new JSONObject(json);
            String j = jsonObject.toString();
            System.out.println(j);
            jsonObject = jsonObject.getJSONObject("GetPasoParadaResult");
            JSONArray jsonArr = jsonObject.getJSONArray("PasoParada");
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject linea = jsonArr.getJSONObject(i);
                String lineaStr = linea.getString("linea");
                JSONObject bus1 = linea.getJSONObject("e1");
                JSONObject bus2 = linea.getJSONObject("e2");
                int min = bus1.getInt("minutos");
                int min2 = bus2.getInt("minutos");
                buses.add(new Bus(min, 1, 1, lineaStr));
                buses.add(new Bus(min2, 1, 1, lineaStr));
            }
        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return buses;
    }

    public void click(View v) {
        TextView texto = (TextView) findViewById(R.id.Main_Texto_1);
        String strBuses = "";
        ArrayList<Bus> buses = getBuses();
        Iterator<Bus> it = buses.iterator();
        while (it.hasNext()) {
            Bus bus = it.next();
            strBuses += bus.getLinea() + "\t\t--\t\t" + bus.getTiempo()  + "\n";
        }
        texto.setText(strBuses);
    }
}
