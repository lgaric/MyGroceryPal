package hr.foi.air.mygrocerypal.myapplication.PaymentHelper;

public class Payment{
    private String mNonce;
    private String mAmount;

    /**
     * Konstruktor
     * @param nonce
     * @param amount
     */
    public Payment(String nonce, String amount){
        this.mAmount = amount;
        this.mNonce = nonce;
    }

    /**
     * Dostavi Nonce
     * @return
     */
    public String getmNonce() {
        return mNonce;
    }

    /**
     * Postavi Nonce
     * @param mNonce
     */
    public void setmNonce(String mNonce) {
        this.mNonce = mNonce;
    }

    /**
     * Dohvati iznos
     * @return
     */
    public String getmAmount() {
        return mAmount;
    }

    /**
     * Postavi iznos
     * @param mAmount
     */
    public void setmAmount(String mAmount) {
        this.mAmount = mAmount;
    }
}
