package org.ei.opensrp.vaccinator.woman;


import android.annotation.SuppressLint;

import android.view.View;

import org.ei.opensrp.Context;
import org.ei.opensrp.adapter.SmartRegisterPaginatedAdapter;
import org.ei.opensrp.commonregistry.CommonPersonObjectClient;
import org.ei.opensrp.cursoradapter.CursorCommonObjectFilterOption;
import org.ei.opensrp.cursoradapter.CursorCommonObjectSort;
import org.ei.opensrp.cursoradapter.CursorSortOption;
import org.ei.opensrp.cursoradapter.SmartRegisterCursorBuilder;
import org.ei.opensrp.cursoradapter.SmartRegisterPaginatedCursorAdapter;
import org.ei.opensrp.vaccinator.R;

import org.ei.opensrp.vaccinator.application.common.BasicSearchOption;
import org.ei.opensrp.vaccinator.application.common.SmartClientRegisterFragment;
import org.ei.opensrp.vaccinator.application.common.VaccinationServiceModeOption;

import org.ei.opensrp.repository.db.Client;
import org.ei.opensrp.view.activity.SecuredNativeSmartRegisterActivity;
import org.ei.opensrp.view.contract.SmartRegisterClient;
import org.ei.opensrp.view.controller.FormController;
import org.ei.opensrp.view.dialog.FilterOption;
import org.ei.opensrp.view.dialog.SearchFilterOption;
import org.ei.opensrp.view.dialog.ServiceModeOption;
import org.ei.opensrp.view.dialog.SortOption;
import org.ei.opensrp.view.template.DetailActivity;
import org.ei.opensrp.view.template.SmartRegisterClientsProvider;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.json.JSONException;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import static org.ei.opensrp.util.Utils.getValue;
import static org.ei.opensrp.util.Utils.nonEmptyValue;
import static util.VaccinatorUtils.getObsValue;
import static util.VaccinatorUtils.providerDetails;

public class WomanSmartRegisterFragment extends SmartClientRegisterFragment {
    private final ClientActionHandler clientActionHandler = new ClientActionHandler();


    String text;

    public WomanSmartRegisterFragment() {
        super(null);
    }

    @SuppressLint("ValidFragment")
    public WomanSmartRegisterFragment(FormController formController) {
        super(formController);
    }

    @Override
    protected SmartRegisterPaginatedAdapter adapter() {
        return new SmartRegisterPaginatedCursorAdapter(getActivity(),
                new SmartRegisterCursorBuilder("pkwoman", null, (CursorSortOption) getDefaultOptionsProvider().sortOption())
                , clientsProvider(), SmartRegisterCursorBuilder.DB.DRISHTI);
    }

    @Override
    protected SecuredNativeSmartRegisterActivity.DefaultOptionsProvider getDefaultOptionsProvider() {
        return new SecuredNativeSmartRegisterActivity.DefaultOptionsProvider() {

            @Override
            public SearchFilterOption searchFilterOption() {
                return new BasicSearchOption("", BasicSearchOption.Type.getByRegisterName(getDefaultOptionsProvider().nameInShortFormForTitle()));
            }

            @Override
            public ServiceModeOption serviceMode() {
                return new VaccinationServiceModeOption(null, "TT", new int[]{
                        R.string.woman_profile, R.string.age, R.string.epi_number,
                        R.string.woman_edd, R.string.woman_contact_number, R.string.woman_last_vaccine, R.string.woman_next_vaacine
                }, new int[]{6, 2, 2, 3, 3, 3, 4});
            }

            @Override
            public FilterOption villageFilter() {
                return new CursorCommonObjectFilterOption("no village filter", "");
            }

            @Override
            public SortOption sortOption() {
                return new CursorCommonObjectSort(getResources().getString(R.string.woman_alphabetical_sort), "first_name");
            }

            @Override
            public String nameInShortFormForTitle() {
                return Context.getInstance().getStringResource(R.string.woman_register_title);
            }
        };
    }


    @Override
    protected SmartRegisterClientsProvider clientsProvider() {
        return new WomanSmartClientsProvider(getActivity(), clientActionHandler, context.alertService());
    }

    @Override
    protected void onInitialization() {

        //   context.formSubmissionRouter().getHandlerMap().put("woman_followup", new WomanFollowupHandler(new WomanService(context.allTimelineEvents(), context.allCommonsRepositoryobjects("pkwoman"), context.alertService())));
    }

    @Override
    protected void onCreation() {

    }


    //end of method

    private class ClientActionHandler implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.woman_profile_info_layout:
                    DetailActivity.startDetailActivity(getActivity(), (CommonPersonObjectClient) view.getTag(), WomanDetailActivity.class);
                    //getActivity().finish();
                    break;
                case R.id.woman_next_visit_holder:
                    HashMap<String, String> map = new HashMap<>();
                    CommonPersonObjectClient client = (CommonPersonObjectClient) view.getTag();
                    map.putAll(followupOverrides(client));
                    map.putAll(providerDetails());
                    startForm("woman_followup", (SmartRegisterClient) view.getTag(), map);
                    break;
            }
        }
    }

    private HashMap<String, String> getPreloadVaccineData(CommonPersonObjectClient client) {
        HashMap<String, String> map = new HashMap<>();
        map.put("e_tt1", nonEmptyValue(client.getColumnmaps(), true, false, "tt1", "tt1_retro"));
        map.put("e_tt2", nonEmptyValue(client.getColumnmaps(), true, false, "tt2", "tt2_retro"));
        map.put("e_tt3", nonEmptyValue(client.getColumnmaps(), true, false, "tt3", "tt3_retro"));
        map.put("e_tt4", nonEmptyValue(client.getColumnmaps(), true, false, "tt4", "tt4_retro"));
        map.put("e_tt5", nonEmptyValue(client.getColumnmaps(), true, false, "tt5", "tt5_retro"));

        return map;
    }

    private HashMap<String, String> getPreloadVaccineData(Client client) throws JSONException, ParseException {
        HashMap<String, String> map = new HashMap<>();
        map.put("e_tt1", getObsValue(getClientEventDb(), client, true, "tt1", "tt1_retro"));
        map.put("e_tt2", getObsValue(getClientEventDb(), client, true, "tt2", "tt2_retro"));
        map.put("e_tt3", getObsValue(getClientEventDb(), client, true, "tt3", "tt3_retro"));
        map.put("e_tt4", getObsValue(getClientEventDb(), client, true, "tt4", "tt4_retro"));
        map.put("e_tt5", getObsValue(getClientEventDb(), client, true, "tt5", "tt5_retro"));

        return map;
    }

    @Override
    protected String getRegistrationForm(HashMap<String, String> overrides) {
        return "woman_enrollment";
    }

    @Override
    protected String getOAFollowupForm(Client client, HashMap<String, String> overridemap) {
        overridemap.putAll(followupOverrides(client));
        return "offsite_woman_followup";
    }

    @Override
    protected Map<String, String> customFieldOverrides() {
        Map<String, String> m = new HashMap<>();
        m.put("gender", "female");
        return m;
    }

    private Map<String, String> followupOverrides(CommonPersonObjectClient client) {
        Map<String, String> map = new HashMap<>();
        map.put("existing_full_address", getValue(client.getColumnmaps(), "address1", true)
                + ", UC: " + getValue(client.getColumnmaps(), "union_council", true)
                + ", Town: " + getValue(client.getColumnmaps(), "town", true)
                + ", City: " + getValue(client, "city_village", true)
                + ", Province: " + getValue(client, "province", true));
        map.put("existing_program_client_id", getValue(client.getColumnmaps(), "program_client_id", false));
        map.put("program_client_id", getValue(client.getColumnmaps(), "program_client_id", false));

        map.put("existing_first_name", getValue(client.getColumnmaps(), "first_name", true));
        map.put("existing_father_name", getValue(client.getColumnmaps(), "father_name", true));
        map.put("existing_husband_name", getValue(client.getColumnmaps(), "husband_name", true));
        map.put("husband_name", getValue(client.getColumnmaps(), "husband_name", true));
        map.put("existing_birth_date", getValue(client.getColumnmaps(), "dob", false));
        int years = 0;
        try {
            years = Years.yearsBetween(new DateTime(getValue(client.getColumnmaps(), "dob", false)), DateTime.now()).getYears();
        } catch (Exception e) {
        }

        map.put("existing_age", years + "");
        map.put("existing_epi_card_number", getValue(client.getColumnmaps(), "epi_card_number", false));
        map.put("marriage", getValue(client.getColumnmaps(), "marriage", false));
        map.put("reminders_approval", getValue(client.getColumnmaps(), "reminders_approval", false));
        map.put("contact_phone_number", getValue(client.getColumnmaps(), "contact_phone_number", false));

        map.putAll(getPreloadVaccineData(client));

        return map;
    }

    private Map<String, String> followupOverrides(Client client) {
        Map<String, String> map = new HashMap<>();
        map.put("existing_full_address", client.getAddress("usual_residence").getAddressField("address1") +
                ", UC: " + client.getAddress("usual_residence").getSubTown() +
                ", Town: " + client.getAddress("usual_residence").getTown() +
                ", City: " + client.getAddress("usual_residence").getCityVillage() +
                " - " + client.getAddress("usual_residence").getAddressField("landmark"));

        map.put("existing_program_client_id", client.getIdentifier("Program Client ID"));
        map.put("program_client_id", client.getIdentifier("Program Client ID"));

        map.put("existing_first_name", client.getFirstName());
        map.put("existing_birth_date", client.getBirthdate().toString("yyyy-MM-dd"));
        int years = 0;
        try {
            years = Years.yearsBetween(client.getBirthdate(), DateTime.now()).getYears();
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("existing_age", years + "");
        Object epi = client.getAttribute("EPI Card Number");
        map.put("existing_epi_card_number", epi == null ? "" : epi.toString());

        try {
            map.put("existing_father_name", getObsValue(getClientEventDb(), client, true, "father_name", "1594AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
            map.put("existing_husband_name", getObsValue(getClientEventDb(), client, true, "husband_name", "5617AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
            map.put("husband_name", getObsValue(getClientEventDb(), client, true, "husband_name", "5617AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
            map.put("marriage", getObsValue(getClientEventDb(), client, false, "marriage"));
            map.put("reminders_approval", getObsValue(getClientEventDb(), client, false, "reminders_approval", "163089AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));
            map.put("contact_phone_number", getObsValue(getClientEventDb(), client, true, "contact_phone_number", "159635AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"));

            map.putAll(getPreloadVaccineData(client));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
