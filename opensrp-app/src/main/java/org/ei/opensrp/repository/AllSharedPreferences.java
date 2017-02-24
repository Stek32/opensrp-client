package org.ei.opensrp.repository;

import android.content.SharedPreferences;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import static org.ei.opensrp.AllConstants.CURRENT_LOCALITY;
import static org.ei.opensrp.AllConstants.DEFAULT_LOCALE;
import static org.ei.opensrp.AllConstants.DEFAULT_LOCALITY_ID_PREFIX;
import static org.ei.opensrp.AllConstants.DRISHTI_BASE_URL;
import static org.ei.opensrp.AllConstants.ENCRYPTED_GROUP_ID_PREFIX;
import static org.ei.opensrp.AllConstants.ENCRYPTED_PASSWORD_PREFIX;
import static org.ei.opensrp.AllConstants.IS_SYNC_IN_PROGRESS_PREFERENCE_KEY;
import static org.ei.opensrp.AllConstants.LANGUAGE_PREFERENCE_KEY;
import static org.ei.opensrp.AllConstants.PIONEER_USER;
import static org.ei.opensrp.util.Log.logError;
import static org.ei.opensrp.util.Log.logInfo;

public class AllSharedPreferences {
    public static final String ANM_IDENTIFIER_PREFERENCE_KEY = "anmIdentifier";
    private static final String HOST = "HOST";
    private static final String PORT = "PORT";
    private static final String LAST_SYNC_DATE = "LAST_SYNC_DATE";
    private SharedPreferences preferences;

    public AllSharedPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void updateANMUserName(String userName) {
        preferences.edit().putString(ANM_IDENTIFIER_PREFERENCE_KEY, userName).commit();
    }

    public String fetchRegisteredANM() {
        return preferences.getString(ANM_IDENTIFIER_PREFERENCE_KEY, "").trim();
    }

    public String fetchEncryptedPassword(String username) {
        if (username != null) {
            return preferences.getString(ENCRYPTED_PASSWORD_PREFIX + username, null);
        }
        return null;
    }

    public void saveEncryptedPassword(String username, String password) {
        if (username != null) {
            preferences.edit().putString(ENCRYPTED_PASSWORD_PREFIX + username, password).commit();
        }
    }

    public String fetchPioneerUser() {
        return preferences.getString(PIONEER_USER, null);
    }

    public void savePioneerUser(String username) {
        preferences.edit().putString(PIONEER_USER, username).commit();
    }

    public void saveDefaultLocalityId(String username, String localityId) {
        if (username != null) {
            preferences.edit().putString(DEFAULT_LOCALITY_ID_PREFIX + username, localityId).commit();
        }
    }

    public String fetchDefaultLocalityId(String username) {
        if (username != null) {
            return preferences.getString(DEFAULT_LOCALITY_ID_PREFIX + username, null);
        }
        return null;
    }

    public String fetchCurrentLocality() {
        return preferences.getString(CURRENT_LOCALITY, null);
    }

    public void saveCurrentLocality(String currentLocality) {
        preferences.edit().putString(CURRENT_LOCALITY, currentLocality).commit();
    }

    public String fetchEncryptedGroupId(String username) {
        if (username != null) {
            return preferences.getString(ENCRYPTED_GROUP_ID_PREFIX + username, null);
        }
        return null;
    }

    public void saveEncryptedGroupId(String username, String groupId) {
        if (username != null) {
            preferences.edit().putString(ENCRYPTED_GROUP_ID_PREFIX + username, groupId).commit();
        }
    }

    public String fetchLanguagePreference() {
        return preferences.getString(LANGUAGE_PREFERENCE_KEY, DEFAULT_LOCALE).trim();
    }

    public void saveLanguagePreference(String languagePreference) {
        preferences.edit().putString(LANGUAGE_PREFERENCE_KEY, languagePreference).commit();
    }

    public Boolean fetchIsSyncInProgress() {
        return preferences.getBoolean(IS_SYNC_IN_PROGRESS_PREFERENCE_KEY, false);
    }

    public void saveIsSyncInProgress(Boolean isSyncInProgress) {
        preferences.edit().putBoolean(IS_SYNC_IN_PROGRESS_PREFERENCE_KEY, isSyncInProgress).commit();
    }

    public String fetchBaseURL(String baseurl){

      return   preferences.getString(DRISHTI_BASE_URL,baseurl);
    }

    public String fetchHost(String host){
        if((host == null || host.isEmpty()) && preferences.getString(HOST,host).equals(host)){
            updateUrl(fetchBaseURL(""));
        }
        return  preferences.getString(HOST,host);
    }

    public void saveHost(String host){
        preferences.edit().putString(HOST,host).commit();
    }

    public Integer fetchPort(Integer port){

        return  Integer.parseInt( preferences.getString(PORT,""+port));
    }

    public Long fetchLastSyncDate(long lastSyncDate){

        return  preferences.getLong(LAST_SYNC_DATE, lastSyncDate);
    }

    public void saveLastSyncDate(long lastSyncDate){
        preferences.edit().putLong(LAST_SYNC_DATE, lastSyncDate).commit();
    }

    public void savePort(Integer port){
        preferences.edit().putString(PORT, String.valueOf(port)).commit();
    }
    public void updateUrl(String baseUrl){
        try {

            URL url = new URL(baseUrl);

            String base = url.getProtocol() + "://" + url.getHost();
            int port = url.getPort();

            logInfo("Base URL: " + base);
            logInfo("Port: " + port);

            saveHost(base);
            savePort(port);

        }catch (MalformedURLException e){
            logError("Malformed Url: " + baseUrl);
        }
    }
    public void updateANMPreferredName(String userName,String preferredName) {
        preferences.edit().putString(userName, preferredName).commit();
    }

    public String getANMPreferredName(String userName){
        return  preferences.getString(userName, "");
    }

}
