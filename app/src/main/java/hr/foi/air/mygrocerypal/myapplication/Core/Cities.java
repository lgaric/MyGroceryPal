package hr.foi.air.mygrocerypal.myapplication.Core;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.CitiesListener;

public class Cities extends AsyncTask<Void, Void, ArrayList<String>> {

    private CitiesListener mListener;
    private Context mContext;

    /**
     * Konstruktor
     * @param listener
     * @param context
     */
    public Cities(CitiesListener listener, Context context){
        this.mListener = listener;
        this.mContext = context;
    }

    /**
     * Ucitaj sve gradove iz CroatianCities.json
     * @param voids
     * @return
     */
    @Override
    protected ArrayList<String> doInBackground(Void... voids) {
        ArrayList<String> mListOfCities = new ArrayList<>();

        String json = null;
        try (InputStream is = mContext.getAssets().open("CroatianCities.json")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            while(is.read(buffer) > 0){
                json = new String(buffer, "UTF-8");
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i<jsonArray.length(); i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    mListOfCities.add(obj.getString("mjesto"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(getClass().toString(), e.getMessage());
        }
        catch (JSONException e){
            e.printStackTrace();
            Log.e(getClass().toString(), e.getMessage());
        }

        return mListOfCities;
    }

    /**
     * Javi fragmentu / activityu da je citanje gradova gotovo
     * @param mListOfCities
     */
    @Override
    protected void onPostExecute(ArrayList<String> mListOfCities) {
        mListener.citiesLoaded(mListOfCities);
    }
}
