package com.example.lizheng.querypvapp;

/**
 * Created by Lizheng on 2015/7/3.
 */
public class DayQuery {
    private String date;
    private String query;
    private String pv;
    private String uv;
    private String[] idDlts;
    private float[] pvRates;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }


    public String showIDs() {
        StringBuffer sb = new StringBuffer();
        if (idDlts != null) {
            for (int i = 0; i < idDlts.length - 1; i++) {
                sb.append(idDlts[i] + "    ");
            }
            sb.append(idDlts[idDlts.length - 1]);
        }
        return sb.toString();
    }

    public String showPVs() {
        StringBuffer sb = new StringBuffer();
        if (pvRates != null) {
            for (int i = 0; i < pvRates.length - 1; i++) {
                sb.append(pvRates[i] + "    ");
            }
            sb.append(pvRates[pvRates.length - 1]);
        }
        return sb.toString();
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String[] getIdDlts() {
        return idDlts;
    }

    public void setIdDlts(String[] idDlts) {
        this.idDlts = idDlts;
    }

    public float[] getPvRates() {
        return pvRates;
    }

    public void setPvRates(float[] pvRates) {
        this.pvRates = pvRates;
    }


}
