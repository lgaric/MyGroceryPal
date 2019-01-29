package hr.foi.air.mygrocerypal.myapplication.Controller;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import java.util.Locale;

import hr.foi.air.mygrocerypal.LocationBasedOnAddress;
import hr.foi.air.mygrocerypal.myapplication.Core.Cities;
import hr.foi.air.mygrocerypal.myapplication.Core.CurrentUser;
import hr.foi.air.mygrocerypal.myapplication.Core.Listeners.CitiesListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.Listeners.PasswordRecoveryListener;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.PasswordRecoveryHelper;
import hr.foi.air.mygrocerypal.myapplication.FirebaseHelper.UserInformationHelper;
import hr.foi.air.mygrocerypal.myapplication.R;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class SettingsFragment extends Fragment implements CitiesListener, PasswordRecoveryListener{

    //ZAGLAVLJE
    private Spinner mOptionsSpinner;

    //LOZINKA
    private LinearLayout mPasswordRecoveryLayout;

    //GRADOVI
    private LinearLayout mCityRecoveryLayout;
    private SpinnerDialog mSpinnerDialog;
    private EditText mCity;
    private EditText mAddress;

    //BROJ
    private LinearLayout mPhoneRecoveryLayout;
    private EditText mPhoneNumber;

    //GUMB
    private Button mChangeButton;

    //PODACI
    private ArrayList<String> mOptions;
    private ArrayList<String> mListOfCities;

    //OBNOVA LOZINKE
    private PasswordRecoveryHelper passwordRecoveryHelper;

    //ONBNOVA ADRESE I TELEFONA
    private UserInformationHelper userInformationHelper;

    /**
     * Listener slusa promijenu odabira u spinneru mOptionsSpinner
     */
    private Spinner.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = (String)mOptionsSpinner.getItemAtPosition(position);
            if(selectedItem.equals(getString(R.string.password)))
                changeLayout(mPasswordRecoveryLayout, mCityRecoveryLayout, mPhoneRecoveryLayout);
            else if(selectedItem.equals(getString(R.string.cityArray)))
                changeLayout(mCityRecoveryLayout, mPasswordRecoveryLayout, mPhoneRecoveryLayout);
            else if(selectedItem.equals(getString(R.string.phoneArray)))
                changeLayout(mPhoneRecoveryLayout, mCityRecoveryLayout, mPasswordRecoveryLayout);
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
            switch (v.getId()) {
                case R.id.btnChangeProfile:
                    doOperationOnClick();
                    break;
                case R.id.txtCity:
                    mSpinnerDialog.showSpinerDialog();
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
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.settings));

        //ZAGLAVLJE SPINNER
        mOptionsSpinner = view.findViewById(R.id.spinnerOptions);

        //LOZINKA
        mPasswordRecoveryLayout = view.findViewById(R.id.passwordRecoveryLayout);

        //GRADOVI
        mCityRecoveryLayout = view.findViewById(R.id.cityLayout);
        mCity = view.findViewById(R.id.txtCity);
        mAddress = view.findViewById(R.id.txtAddress);

        //MOBITEL
        mPhoneRecoveryLayout = view.findViewById(R.id.phoneNumberLayout);
        mPhoneNumber = view.findViewById(R.id.txtPhoneNumber);

        //GUMB PROMIJENI
        mChangeButton = view.findViewById(R.id.btnChangeProfile);

        //EVENTI
        mOptionsSpinner.setOnItemSelectedListener(itemSelectedListener);
        mChangeButton.setOnClickListener(clickListener);
        mCity.setOnClickListener(clickListener);

        getDataForSpinner();
        fillSpinner(mOptions, mOptionsSpinner);

        userInformationHelper = new UserInformationHelper(getContext());

        return view;
    }

    /**
     * Nastavljanje izvodenja fragmenta
     */
    @Override
    public void onResume() {
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.settings));
        super.onResume();
    }

    /**
     * Na klik gumba napravi zeljenu operaciju
     */
    private void doOperationOnClick(){
        String selectedItem = (String)mOptionsSpinner.getSelectedItem();
        if(selectedItem.equals(getString(R.string.password)))
            setNewPassword();
        else if(selectedItem.equals(getString(R.string.cityArray)))
            setNewCity();
        else if(selectedItem.equals(getString(R.string.phoneArray)))
            setNewPhoneNumber();
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
            ArrayAdapter<String> tempAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, data);
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
    private void changeLayout(LinearLayout toShow, LinearLayout toHideFirst, LinearLayout toHideSecond){
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

        return filled;
    }

    /**
     * Postavi novu lozinku
     */
    private void setNewPassword(){
        if(passwordRecoveryHelper == null)
            passwordRecoveryHelper = new PasswordRecoveryHelper(this);

        passwordRecoveryHelper.sendRecoveryMail(CurrentUser.getCurrentUser.getEmail());
    }

    /**
     * Postavi novu adresuu
     */
    private void setNewCity(){
        if(!checkValues(mAddress.getText().toString()))
            return;

        String city = mCity.getText().toString();
        String address = mAddress.getText().toString();
        Location newPoint = LocationBasedOnAddress.GetLocation( address + ", "
                + city + ", Croatia", getContext());

        if(newPoint != null){
            String result = userInformationHelper
                    .updateUserAdress(CurrentUser.getCurrentUser.getUserUID(), address, city, newPoint);

            Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getContext(), getResources().getString(R.string.fetchAddressError), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Postavi novi broj mobitela
     */
    private void setNewPhoneNumber(){
        if(!checkValues(mPhoneNumber.getText().toString()))
            return;

        String phone = mPhoneNumber.getText().toString();
        if(phone.length() != 10 && phone.length() != 9){
            Toast.makeText(getContext(), getResources().getString(R.string.phoneError), Toast.LENGTH_LONG).show();
            return;
        }

        String result = userInformationHelper.updateUserPhoneNumber(CurrentUser.getCurrentUser.getUserUID(), phone);
        Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
    }

    /**
     * Gradovi su ucitani
     * @param listOfCities ucitani gradovi
     */
    @Override
    public void citiesLoaded(ArrayList<String> listOfCities) {
        if(listOfCities != null) {
            mListOfCities = listOfCities;

            mSpinnerDialog = new SpinnerDialog(getActivity(), mListOfCities,
                    getResources().getString(R.string.chooseCity), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.close));

            mSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                @Override
                public void onClick(String item, int position) {
                    mCity.setText(item);
                }
            });
        }
    }

    /**
     * Lozinka je promijenjena
     * @param mMessage
     */
    @Override
    public void onRecoverySuccess(String mMessage) {
        Toast.makeText(getContext(), mMessage, Toast.LENGTH_LONG).show();
    }

    /**
     * Pogreska prilikom promijene lozinke
     * @param mMessage
     */
    @Override
    public void onRecoveryFail(String mMessage) {
        Toast.makeText(getContext(), mMessage, Toast.LENGTH_LONG).show();
    }

}
