package hr.foi.air.mygrocerypal.myapplication.PaymentHelper;

public class Payment{
    private String mNonce;
    private String mAmount;

    public Payment(String nonce, String amount){
        this.mAmount = amount;
        this.mNonce = nonce;
    }

    public String getmNonce() {
        return mNonce;
    }

    public void setmNonce(String mNonce) {
        this.mNonce = mNonce;
    }

    public String getmAmount() {
        return mAmount;
    }

    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }
}
