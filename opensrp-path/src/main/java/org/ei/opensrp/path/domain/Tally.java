package org.ei.opensrp.path.domain;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.codehaus.jackson.annotate.JsonProperty;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by Jason Rogena - jrogena@ona.io on 15/06/2017.
 */

public class Tally implements Serializable {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    protected Hia2Indicator indicator;
    @JsonProperty
    protected long id;
    @JsonProperty
    protected String value;
    @JsonProperty
    protected String providerId;
    @JsonProperty
    protected Date updatedAt;


    @JsonProperty
    protected Date createdAt;

    public Tally() {
    }

    public Hia2Indicator getIndicator() {
        return indicator;
    }

    public void setIndicator(Hia2Indicator indicator) {
        this.indicator = indicator;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public JSONObject getJsonObject() throws JsonProcessingException, JSONException {
        JSONObject tally = new JSONObject();
        tally.put("id", id);
        tally.put("value", value);
        tally.put("providerId", providerId);
        tally.put("updatedAt", DATE_FORMAT.format(updatedAt));

        JSONObject hia2Indicator = indicator.getJsonObject();

        JSONObject combined = new JSONObject();
        Iterator hia2Iterator = hia2Indicator.keys();
        while (hia2Iterator.hasNext()) {
            String curKey = (String) hia2Iterator.next();
            combined.put(curKey, hia2Indicator.get(curKey));
        }

        Iterator tallyIterator = tally.keys();
        while (tallyIterator.hasNext()) {
            String curKey = (String) tallyIterator.next();
            combined.put(curKey, tally.get(curKey));
        }

        return combined;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Tally) {
            Tally tally = (Tally) o;
            if (getIndicator().getDhisId().equals(tally.getIndicator().getDhisId())) {
                return true;
            }
        }
        return false;
    }
}
