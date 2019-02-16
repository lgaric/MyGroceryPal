package hr.foi.air.mygrocerypal.myapplication.PaymentHelper;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.google.android.gms.common.util.IOUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import hr.foi.air.mygrocerypal.myapplication.Controller.MainActivity;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.CreateNewGroceryListHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.GroceryChangeStatusHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.AddGroceryListListener;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListProductsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.GroceryListsModel;
import hr.foi.air.mygrocerypal.myapplication.Model.StoresModel;
import hr.foi.air.mygrocerypal.myapplication.Model.UserModel;
import hr.foi.air.mygrocerypal.myapplication.R;

public class PaymentActivity extends AppCompatActivity implements PaymentListener{
    private static final int PAYMENT_REQUEST = 1111;
    private static final int REQUEST_CODE = 1234;
    private static final String CHECKOUT = "http://cortex.foi.hr/grocerypal/checkout.php";
    private Payment mPayment;
    private String mToken;

    private Double mTotalPayment = 0.0;
    private GroceryListsModel mModel;
    private UserModel mUserModel;

    private TextView mPrice;
    private Button mPay;
    private LinearLayout mWaiting;

    /**
     * Inicijalizacija
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mPrice = findViewById(R.id.totalPrice);
        mPay = findViewById(R.id.startPayment);
        mWaiting = findViewById(R.id.waiting);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //mTotalPayment = extras.getDouble("TOTAL_PAYMENT");
            mModel = (GroceryListsModel) extras.getSerializable("MODEL_GL");
            mUserModel = (UserModel) extras.getSerializable("USER_MODEL");

            for(GroceryListProductsModel product: mModel.getProductsModels())
                mTotalPayment += product.getPrice() * product.getBought();

            mTotalPayment += Double.parseDouble(mModel.getCommision());

            mPrice.setText(mPrice.getText() + " " + String.format("%.2f", mTotalPayment) + " KN");
            new Token(this).execute();
        }

        mPay.setEnabled(false);
        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
    }

    /**
     * Prikaz Braintree Activitya za placanje
     */
    private void startPayment(){
        this.mWaiting.setVisibility(View.VISIBLE);
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(mToken.trim());
        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }

    /**
     * Rad s Braintreeom
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == REQUEST_CODE) {
            if (resultCode == PaymentActivity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String stringNonce = nonce.getNonce();

                if(mTotalPayment != null) {
                    mPayment = new Payment(stringNonce, String.format("%.2f", mTotalPayment));
                    sendPayments();
                }
                else {
                    Toast.makeText(this, getResources().getString(R.string.paymentFailed), Toast.LENGTH_LONG).show();
                    this.mWaiting.setVisibility(View.GONE);
                }

            } else if (resultCode == PaymentActivity.RESULT_CANCELED) {
                Toast.makeText(this, getResources().getString(R.string.postponed), Toast.LENGTH_LONG).show();
                this.mWaiting.setVisibility(View.GONE);
            } else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("ERROR ONACTIVITYSRESULT", error.getMessage());
                Toast.makeText(this, getResources().getString(R.string.paymentFailed), Toast.LENGTH_LONG).show();
                this.mWaiting.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Pokreni proces placanja
     * Slanje upita PHP serveru (checkout.php)
     */
    private void sendPayments() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("payment_method_nonce", mPayment.getmNonce());
        params.put("order_id", mModel.getGrocerylist_key());
        params.put("amount", mPayment.getmAmount());
        params.put("deliverer_name", mUserModel.getFirst_name() + " " + mUserModel.getLast_name());
        params.put("deliverer_uid", mUserModel.getUserUID());
        params.put("deliverer_phone", mUserModel.getPhone_number());

        client.post(CHECKOUT, params,
                new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String str = new String(responseBody, "UTF-8");
                            Log.d("RESULT POST-a", str);
                            if(str.contains("Successful"))
                                showFinalMessage(getResources().getString(R.string.paymentSuccess), true);
                            else
                                showFinalMessage(getResources().getString(R.string.paymentFailed), false);
                        }
                        catch (Exception e){
                            Log.d("CONVERT_EXCEPTION", e.getMessage());
                            showFinalMessage(getResources().getString(R.string.paymentFailed), false);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d("ONFAILURE POST", error.getMessage());
                        showFinalMessage(getResources().getString(R.string.paymentFailed), false);
                    }
                }
        );
    }

    /**
     * Prikazi konacni status placanja
     * @param message
     */
    private void showFinalMessage(String message, boolean passed){
        if(passed) {
            mPay.setEnabled(false);
            GroceryChangeStatusHelper helper = new GroceryChangeStatusHelper(this);
            boolean mSuccess = helper.setStatusToFinished(mModel);

            if(mSuccess) {
                setResult(RESULT_OK);
                this.finish();
            }
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        mWaiting.setVisibility(View.GONE);
    }

    /**
     * Token dohvaćen s PHP servera
     * Implementacija sučelja PaymentListener
     * @param token
     */
    @Override
    public void tokenReceived(String token) {
        if(token != null) {
            Log.d("TOKEN", token);
            this.mToken = token;
            this.mWaiting.setVisibility(View.GONE);
            this.mPay.setEnabled(true);
        }
    }
}
