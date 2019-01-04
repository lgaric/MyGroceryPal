package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import hr.foi.air.mygrocerypal.AddressBasedOnLocation;
import hr.foi.air.mygrocerypal.LocationBasedOnAddress;
import hr.foi.air.mygrocerypal.myapplication.Core.Cities;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.CitiesListener;
import hr.foi.air.mygrocerypal.myapplication.R;

public class SettingsFragment extends Fragment implements CitiesListener {
    //ZAGLAVLJE
    private Spinner mOptionsSpinner;

    //LOZINKA
    private LinearLayout mPasswordRecoveryLayout;
    private EditText mOldPassword;
    private EditText mNewPassword;
    private EditText mNewLozinkaRepeat;

    //GRADOVI
    private LinearLayout mCityRecoveryLayout;
    private Spinner mSpinnerCityes;
    private EditText mAddress;

    //BROJ
    private LinearLayout mPhoneRecoveryLayout;
    private EditText mPhoneNumber;

    //GUMB
    private Button mChangeButton;

    //PODACI
    private ArrayList<String> mOptions;
    private ArrayList<String> mListOfCities;

    /**
     * Listener slusa promijenu odabira u spinneru mOptionsSpinner
     */
    private Spinner.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = (String)mOptionsSpinner.getItemAtPosition(position);
            switch (selectedItem){
                case "Lozinka":
                    changeLayout(mPasswordRecoveryLayout, mCityRecoveryLayout, mPhoneRecoveryLayout);
                    break;
                case "Grad/Adresa":
                    changeLayout(mCityRecoveryLayout, mPasswordRecoveryLayout, mPhoneRecoveryLayout);
                    break;
                case "Mobitel":
                    changeLayout(mPhoneRecoveryLayout, mCityRecoveryLayout, mPasswordRecoveryLayout);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) { }
    };

    /**
     * Pritisak gumba mChangeButton
     */
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String selectedItem = (String)mOptionsSpinner.getSelectedItem();
            switch (selectedItem){
                case "Lozinka":
                    setNewPassword();
                    break;
                case "Grad/Adresa":
                    changeLayout(mCityRecoveryLayout, mPasswordRecoveryLayout, mPhoneRecoveryLayout);
                    setNewCity();
                    break;
                case "Mobitel":
                    changeLayout(mPhoneRecoveryLayout, mCityRecoveryLayout, mPasswordRecoveryLayout);
                    setNewPhoneNumber();
                    break;
            }
        }
    };

    /**
     * Inicijalizacija
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        //ZAGLAVLJE SPINNER
        mOptionsSpinner = view.findViewById(R.id.spinnerOptions);

        //LOZINKA
        mPasswordRecoveryLayout = view.findViewById(R.id.passwordRecoveryLayout);
        mOldPassword = view.findViewById(R.id.txtOldPassword);
        mNewPassword = view.findViewById(R.id.txtNewPassword);
        mNewLozinkaRepeat = view.findViewById(R.id.txtNewLozinkaRepeat);

        //GRADOVI
        mCityRecoveryLayout = view.findViewById(R.id.cityLayout);
        mSpinnerCityes = view.findViewById(R.id.spinnerCityes);
        mAddress = view.findViewById(R.id.txtAddress);

        //MOBITEL
        mPhoneRecoveryLayout = view.findViewById(R.id.phoneNumberLayout);
        mPhoneNumber = view.findViewById(R.id.txtPhoneNumber);

        //GUMB PROMIJENI
        mChangeButton = view.findViewById(R.id.btnChangeProfile);

        //EVENTI
        mOptionsSpinner.setOnItemSelectedListener(itemSelectedListener);
        mChangeButton.setOnClickListener(clickListener);

        getDataForSpinner();
        fillSpinner(mOptions, mOptionsSpinner);

        return view;
    }

    /**
     * Dohvati podatke za punjenje spinnera
     */
    private void getDataForSpinner(){
        mOptions = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.settingsArray)));
        new Cities(this, getContext()).execute();
    }

    /**
     * Popuni spinner podacima
     * @param data podaci za punjenje spinnera
     * @param toFill spinner koji se puni
     */
    private void fillSpinner(ArrayList<String> data, Spinner toFill){
        if(data != null) {
            ArrayAdapter<String> tempAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, data);
            tempAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            toFill.setAdapter(tempAdapter);
        }
    }

    /**
     * Prikaz i sakrivanje zeljenih komponenti
     * @param toShow Layout koji zelimo prikazati
     * @param toHideFirst Layout koji zelimo sakriti
     * @param toHideSecond Layout koji zelimo sakriti
     */
    private static <T extends View> void changeLayout(T toShow, T toHideFirst, T toHideSecond){
        toShow.setVisibility(View.VISIBLE);
        toHideFirst.setVisibility(View.GONE);
        toHideSecond.setVisibility(View.GONE);
    }

    /**
     * Provijeri vrijednosti varijabli
     * @param strings polje stringova
     * @return false ako jedan string nema vrijednost
     */
    private boolean checkValues(String... strings){
        boolean filled = true;

        for (String string: strings) {
            if(string.isEmpty()){
                filled = false;
                break;
            }
        }

        Toast.makeText(getContext(), "Potrebno je popuniti sva polja", Toast.LENGTH_LONG).show();

        return filled;
    }

    /**
     * Provjera ispravnosti lozinki
     * @param length minimalna duljina lozinke
     * @param passwords [0] stari, [1] novi, [2] novi
     * @return false ako lozinke ne zadovoljavaju
     */
    private boolean arePasswordsValid(int length, String... passwords){
        boolean valid = true;

        if(!checkValues(passwords))
            return false;

        for(String password : passwords){
            if(password.length() < length){
                Toast.makeText(getContext(), "Lozinke su prekratke", Toast.LENGTH_LONG).show();
                valid = false;
                break;
            }
        }

        if(!CurrentUser.getCurrentUser.getPassword().equals(passwords[0])) {
            Toast.makeText(getContext(), "Lozinke se ne podudaraju", Toast.LENGTH_LONG).show();
            return false;
        }

        if(!passwords[1].equals(passwords[2])) {
            Toast.makeText(getContext(), "Lozinke se ne podudaraju", Toast.LENGTH_LONG).show();
            return false;
        }

        return valid;
    }

    /**
     * Postavi novu lozinku
     */
    private void setNewPassword(){
        if(arePasswordsValid(5, mOldPassword.getText().toString(),
                mNewPassword.getText().toString(), mNewLozinkaRepeat.getText().toString())){
            Log.d("setNewPassword", "valid");
        }
    }

    /**
     * Postavi novu adresuu
     */
    private void setNewCity(){
        if(!checkValues(mAddress.getText().toString()))
            return;

        String city = (String) mSpinnerCityes.getSelectedItem();
        Location newPoint = LocationBasedOnAddress.GetLocation(mAddress.getText().toString() + ", "
                + city + ", Croatia", getContext());

        if(newPoint != null){
            //TODO postavi novu adresu
        }
        else{
            Toast.makeText(getContext(), "Pogreska kod dohvacanja adrese", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Postavi novi broj mobitela
     */
    private void setNewPhoneNumber(){
        if(!checkValues(mPhoneNumber.getText().toString()))
            return;

        String phone = mPhoneNumber.getText().toString();
        if(phone.length() != 8 || phone.length() != 9){
            Toast.makeText(getContext(), "Ovo nije broj mobilnog telefona", Toast.LENGTH_LONG).show();
            return;
        }

        //TODO posavi broj mobitela
    }

    /**
     * Gradovi su ucitani
     * @param listOfCities ucitani gradovi
     */
    @Override
    public void citiesLoaded(ArrayList<String> listOfCities) {
        if(listOfCities != null){
            mListOfCities = listOfCities;
            fillSpinner(mListOfCities, mSpinnerCityes);
        }
    }
}
