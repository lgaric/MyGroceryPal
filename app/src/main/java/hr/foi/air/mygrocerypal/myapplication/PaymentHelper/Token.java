package hr.foi.air.mygrocerypal.myapplication.PaymentHelper;

import android.os.AsyncTask;
import android.util.Log;

import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;

public class Token extends AsyncTask<Void, Void, Void> {

    private static final String CLIENT_TOKEN = "http://cortex.foi.hr/grocerypal/main.php";
    private PaymentListener mListener;

    public Token(PaymentListener listener){
        mListener = listener;
    }

    /**
     * Dohvati token
     * @param voids
     * @return
     */
    @Override
    protected Void doInBackground(Void... voids) {
        HttpClient client = new HttpClient();
        client.get(CLIENT_TOKEN, new HttpResponseCallback() {
            @Override
            public void success(String responseBody) {
                Log.d("doInBackground", responseBody);
                mListener.tokenReceived(responseBody);
            }

            @Override
            public void failure(Exception exception) {
                mListener.tokenReceived(null);
            }
        });

        return null;
    }
}
