package com.tutran.aaogpa.data.web.http.httpclient;

import com.tutran.aaogpa.services.WebDataRepository;
import com.tutran.aaogpa.data.web.AaoWeb;
import com.tutran.aaogpa.data.web.exceptions.EncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class AaoWebHttpClient implements AaoWeb {

    @Override
    public String getResultsBlocking(String id) {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(URL);

        // add request params
        ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("mssv", id));
        params.add(new BasicNameValuePair("HOC_KY", "d.hk_nh is not NULL"));
        try {
            request.setEntity(new UrlEncodedFormEntity(params));
        } catch (UnsupportedEncodingException e) {
            throw new EncodingException();
        }

        // making request and fetch result
        StringBuilder result = new StringBuilder();
        try {
            HttpResponse response = client.execute(request);
            BufferedReader rd = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
        } catch (ClientProtocolException e) {
            throw new com.tutran.aaogpa.data.web.exceptions.ProtocolException();
        } catch (IOException e) {
            throw new com.tutran.aaogpa.data.web.exceptions.IOException();
        }

        return result.toString();
    }

    @Override
    public void getResultsNonBlocking(WebDataRepository.Listener callback) {

    }
}
