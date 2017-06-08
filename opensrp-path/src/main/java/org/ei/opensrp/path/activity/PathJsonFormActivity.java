package org.ei.opensrp.path.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.util.ViewUtil;
import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.utils.FormUtils;

import org.apache.commons.lang3.StringUtils;
import org.ei.opensrp.Context;
import org.ei.opensrp.path.R;
import org.ei.opensrp.path.application.VaccinatorApplication;
import org.ei.opensrp.path.fragment.PathJsonFormFragment;
import org.ei.opensrp.path.repository.PathRepository;
import org.ei.opensrp.path.repository.StockRepository;
import org.ei.opensrp.path.repository.Vaccine_typesRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import util.JsonFormUtils;

/**
 * Created by keyman on 11/04/2017.
 */
public class PathJsonFormActivity extends JsonFormActivity {

    private int generatedId = -1;
    public MaterialEditText balancetextview;
    private PathJsonFormFragment pathJsonFormFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initializeFormFragment() {
         pathJsonFormFragment = PathJsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        getSupportFragmentManager().beginTransaction()
                .add(com.vijay.jsonwizard.R.id.container, pathJsonFormFragment).commit();
    }

    @Override
    public void writeValue(String stepName, String key, String value, String openMrsEntityParent, String openMrsEntity, String openMrsEntityId) throws JSONException {
        super.writeValue(stepName, key, value, openMrsEntityParent, openMrsEntity, openMrsEntityId);
        refreshCalculateLogic(key,value);

    }



    private void refreshCalculateLogic(String key, String value) {
        stockVialsenteredinReceivedForm(key,value);
        stockDateEnteredinReceivedForm(key,value);
        stockDateEnteredinIssuedForm(key, value);
        stockVialsEnteredinIssuedForm(key, value);
    }

    private void stockDateEnteredinIssuedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains("Stock Issued")) {
                StockRepository str = new StockRepository((PathRepository) VaccinatorApplication.getInstance().getRepository(), VaccinatorApplication.createCommonFtsObject(), Context.getInstance().alertService());
                if (key.equalsIgnoreCase("Date_Stock_Issued") && value != null && !value.equalsIgnoreCase("")) {
                    if(balancetextview == null) {
                        ArrayList<View> views = getFormDataViews();
                        for (int i = 0; i < views.size(); i++) {
                            if (views.get(i) instanceof MaterialEditText) {
                                if (((String) views.get(i).getTag(com.vijay.jsonwizard.R.id.key)).equalsIgnoreCase("Vials_Issued")) {
                                    balancetextview = (MaterialEditText) views.get(i);
                                }
                            }
                        }
                    }
                    String label = "";
                    int currentBalance = 0;
                    int displaybalance = 0;
                    int newBalance = 0;
                    Date encounterDate = new Date();
                    String vialsvalue = "";
                    String vaccineName = object.getString("title").replace("Stock Issued", "").trim();
                    JSONArray fields = object.getJSONArray("fields");
                    for (int i = 0; i < fields.length(); i++) {
                        JSONObject questions = fields.getJSONObject(i);
                        if (questions.has("key")) {
                            if (questions.getString("key").equalsIgnoreCase("Date_Stock_Issued")) {
                                if (questions.has("value")) {
                                    label = questions.getString("value");
                                    if (label != null) {
                                        if (StringUtils.isNotBlank(label)) {
                                            Date dateTime = JsonFormUtils.formatDate(label, false);
                                            if (dateTime != null) {
                                                encounterDate = dateTime;
                                            }
                                        }
                                    }

                                    currentBalance = str.getVaccineUsedToday(encounterDate.getTime(),vaccineName.toLowerCase());
                                }
                            }
                            int DosesPerVial = 0;
                            int vialsused = 0;
                            Vaccine_typesRepository vaccine_typesRepository = new Vaccine_typesRepository((PathRepository) VaccinatorApplication.getInstance().getRepository(), VaccinatorApplication.createCommonFtsObject(), Context.getInstance().alertService());
                            int dosesPerVial = vaccine_typesRepository.getDosesPerVial(vaccineName);
                            if(currentBalance % dosesPerVial == 0){
                                vialsused = currentBalance/dosesPerVial;
                            }else if (currentBalance != 0){
                                vialsused = (currentBalance/dosesPerVial) +1;
                            }
                            if (questions.getString("key").equalsIgnoreCase("Vials_Issued")) {
                                if (questions.has("value")) {
                                    if(!StringUtils.isBlank(questions.getString("value"))){
                                        newBalance = str.getBalanceFromNameAndDate(vaccineName,encounterDate.getTime())+Integer.parseInt(questions.getString("value"));
                                        pathJsonFormFragment.getLabelViewFromTag("Balance","New balance : " + newBalance);
                                    }
                                }else{
                                    pathJsonFormFragment.getLabelViewFromTag("Balance","");
                                }
                            }
                            ;
                            if (currentBalance != 0) {
                                displaybalance = vialsused;
                                if (balancetextview != null) {
                                    balancetextview.setErrorColor(Color.BLACK);
                                    balancetextview.setError(currentBalance + " children vaccinated today.Assuming " + displaybalance+" vials used.");
//                                    writeValue("step1","labelHeaderImage","checkwritetolabel","","","");
                                }
                            }else{
                                balancetextview.setErrorColor(Color.BLACK);
                                balancetextview.setError("");

                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void stockVialsEnteredinIssuedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains("Stock Issued")) {
                StockRepository str = new StockRepository((PathRepository) VaccinatorApplication.getInstance().getRepository(), VaccinatorApplication.createCommonFtsObject(), Context.getInstance().alertService());
                if (key.equalsIgnoreCase("Vials_Issued") ) {
                    if(balancetextview == null) {
                        ArrayList<View> views = getFormDataViews();
                        for (int i = 0; i < views.size(); i++) {
                            if (views.get(i) instanceof MaterialEditText) {
                                if (((String) views.get(i).getTag(com.vijay.jsonwizard.R.id.key)).equalsIgnoreCase("Vials_Issued")) {
                                    balancetextview = (MaterialEditText) views.get(i);
                                }
                            }
                        }
                    }
                    String label = "";
                    int currentBalance = 0;
                    int displaybalance = 0;
                    int newBalance = 0;
                    Date encounterDate = new Date();
                    String vialsvalue = "";
                    String vaccineName = object.getString("title").replace("Stock Issued", "").trim();
                    JSONArray fields = object.getJSONArray("fields");
                    for (int i = 0; i < fields.length(); i++) {
                        JSONObject questions = fields.getJSONObject(i);
                        if (questions.has("key")) {
                            if (questions.getString("key").equalsIgnoreCase("Date_Stock_Issued")) {
                                if (questions.has("value")) {
                                    label = questions.getString("value");
                                    if (label != null) {
                                        if (StringUtils.isNotBlank(label)) {
                                            Date dateTime = JsonFormUtils.formatDate(label, false);
                                            if (dateTime != null) {
                                                encounterDate = dateTime;
                                            }
                                        }
                                    }
                                    currentBalance = str.getVaccineUsedToday(encounterDate.getTime(),vaccineName.toLowerCase());

                                }
                            }
                            int existingbalance = str.getBalanceFromNameAndDate(vaccineName,encounterDate.getTime());
                            pathJsonFormFragment.getLabelViewFromTag("Balance","");
                            if (value!=null && !StringUtils.isBlank(value)) {

                                        newBalance = existingbalance - Integer.parseInt(value);
                                        pathJsonFormFragment.getLabelViewFromTag("Balance","New balance : " + newBalance);
                                    }else{
                                        pathJsonFormFragment.getLabelViewFromTag("Balance","");
                                    }
                            int DosesPerVial = 0;
                            int vialsused = 0;
                            Vaccine_typesRepository vaccine_typesRepository = new Vaccine_typesRepository((PathRepository) VaccinatorApplication.getInstance().getRepository(), VaccinatorApplication.createCommonFtsObject(), Context.getInstance().alertService());
                            int dosesPerVial = vaccine_typesRepository.getDosesPerVial(vaccineName);
                            if(currentBalance % dosesPerVial == 0){
                                vialsused = currentBalance/dosesPerVial;
                            }else if (currentBalance != 0){
                                vialsused = (currentBalance/dosesPerVial) +1;
                            }
                            if (currentBalance != 0) {
                                displaybalance = vialsused;
                                if (balancetextview != null) {
                                    balancetextview.setErrorColor(Color.BLACK);
                                    balancetextview.setError(currentBalance + " children vaccinated today.Assuming " + displaybalance+" vials used.");
//                                    writeValue("step1","labelHeaderImage","checkwritetolabel","","","");
                                }
                            }else{
                                balancetextview.setErrorColor(Color.BLACK);
                                balancetextview.setError("");

                            }

                                                   ;

                        }
                    }
                }else{
                    pathJsonFormFragment.getLabelViewFromTag("Balance","");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void stockDateEnteredinReceivedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains("Stock Received")) {
                if (key.equalsIgnoreCase("Date_Stock_Received") && value != null && !value.equalsIgnoreCase("")) {
//                    if(balancetextview == null) {
//                        ArrayList<View> views = getFormDataViews();
//                        for (int i = 0; i < views.size(); i++) {
//                            if (views.get(i) instanceof MaterialEditText) {
//                                if (((String) views.get(i).getTag(com.vijay.jsonwizard.R.id.key)).equalsIgnoreCase("Vials_Received")) {
//                                    balancetextview = (MaterialEditText) views.get(i);
//                                }
//                            }
//                        }
//                    }
                    String label = "";
                    int currentBalance = 0;
                    int displaybalance = 0;
                    String vialsvalue = "";
                    JSONArray fields = object.getJSONArray("fields");
                    for (int i = 0; i < fields.length(); i++) {
                        JSONObject questions = fields.getJSONObject(i);
                        if (questions.has("key")) {
                            if (questions.getString("key").equalsIgnoreCase("Date_Stock_Received")) {
                                if (questions.has("value")) {
                                    Date encounterDate = new Date();
                                    label = questions.getString("value");
                                    if (label != null) {
                                        if (StringUtils.isNotBlank(label)) {
                                            Date dateTime = JsonFormUtils.formatDate(label, false);
                                            if (dateTime != null) {
                                                encounterDate = dateTime;
                                            }
                                        }
                                    }
                                    String vaccineName = object.getString("title").replace("Stock Received", "").trim();
                                    StockRepository str = new StockRepository((PathRepository) VaccinatorApplication.getInstance().getRepository(), VaccinatorApplication.createCommonFtsObject(), Context.getInstance().alertService());
                                    currentBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                                }
                            }
                            if (questions.getString("key").equalsIgnoreCase("Vials_Received")) {
                                if (questions.has("value")) {
                                    label = questions.getString("value");
                                    vialsvalue = label;
                              }
                            }
                            if (vialsvalue != null && !vialsvalue.equalsIgnoreCase("")) {
                                displaybalance = currentBalance + Integer.parseInt(vialsvalue);
//                                if (balancetextview != null) {
//                                    balancetextview.setErrorColor(getResources().getColor(R.color.dark_grey));
//                                    balancetextview.setError("New balance : " + displaybalance);
//                                }
                                pathJsonFormFragment.getLabelViewFromTag("Balance","New balance : " + displaybalance);

                            }else{
                                pathJsonFormFragment.getLabelViewFromTag("Balance","");

                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }

    private void stockVialsenteredinReceivedForm(String key, String value) {
        JSONObject object = getStep("step1");
        try {
            if (object.getString("title").contains("Stock Received")) {
                if (key.equalsIgnoreCase("Vials_Received") && value != null && !value.equalsIgnoreCase("")) {
//                    if(balancetextview == null) {
//                        ArrayList<View> views = getFormDataViews();
//                        for (int i = 0; i < views.size(); i++) {
//                            if (views.get(i) instanceof MaterialEditText) {
//                                if (((String) views.get(i).getTag(com.vijay.jsonwizard.R.id.key)).equalsIgnoreCase(key)) {
//                                    balancetextview = (MaterialEditText) views.get(i);
//                                }
//                            }
//                        }
//                    }
                    String label = "";
                    int currentBalance = 0;
                    int displaybalance = 0;
                    JSONArray fields = object.getJSONArray("fields");
                    for (int i = 0; i < fields.length(); i++) {
                        JSONObject questions = fields.getJSONObject(i);
                        if (questions.has("key")) {
                            if (questions.getString("key").equalsIgnoreCase("Date_Stock_Received")) {
                                if (questions.has("value")) {
                                    Date encounterDate = new Date();
                                    label = questions.getString("value");
                                    if (label != null) {
                                        if (StringUtils.isNotBlank(label)) {
                                            Date dateTime = JsonFormUtils.formatDate(label, false);
                                            if (dateTime != null) {
                                                encounterDate = dateTime;
                                            }
                                        }
                                    }
                                    String vaccineName = object.getString("title").replace("Stock Received", "").trim();
                                    StockRepository str = new StockRepository((PathRepository) VaccinatorApplication.getInstance().getRepository(), VaccinatorApplication.createCommonFtsObject(), Context.getInstance().alertService());
                                    currentBalance = str.getBalanceFromNameAndDate(vaccineName, encounterDate.getTime());
                                }
                            }
                            String vialsvalue = value;
                            if (vialsvalue != null && !vialsvalue.equalsIgnoreCase("")) {
                                displaybalance = currentBalance + Integer.parseInt(vialsvalue);
//                                if (balancetextview != null) {
//                                    balancetextview.setErrorColor(Color.BLACK);
//                                    balancetextview.setError("New balance : " + displaybalance);
//                                }
                                pathJsonFormFragment.getLabelViewFromTag("Balance","New balance : " + displaybalance);

                            }else{
                                pathJsonFormFragment.getLabelViewFromTag("Balance","");
                            }
                        }
                    }
                }else{
                    pathJsonFormFragment.getLabelViewFromTag("Balance","");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
