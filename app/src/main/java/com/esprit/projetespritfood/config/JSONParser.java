package com.esprit.projetespritfood.config;

import android.os.AsyncTask;

import com.esprit.projetespritfood.config.IP;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class JSONParser extends AsyncTask<String, Void, String> {

    static InputStream is = null;
    static String json = "";
    //ADRESSE DE BASE
    String adresse = IP.localhost;

    @Override
    protected void onPreExecute() {
    }

    //CONSTRUCTEUR QUI CONTIENT L'ADRESSE URL
    public JSONParser(String adresse) {
        this.adresse += adresse;
    }

    @Override
    protected String doInBackground(String... params) {
        String url = adresse;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse getResponse = httpClient.execute(httpGet);
            // ^ ACTIVER CNX ^


            final int statusCode = getResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity getResponseEntity = getResponse.getEntity();
            is = getResponseEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8), 8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            // ^ RECUP2RATION DU CONTENU DE L'URL ^
            is.close();
            json = sb.toString();
        } catch (Exception e) {
        }

        return json;
    }

    protected void onPostExecute(String page) {
    }
}