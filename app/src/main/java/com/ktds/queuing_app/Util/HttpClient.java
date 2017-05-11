package com.ktds.queuing_app.Util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 206-013 on 2016-07-08.
 */
public class HttpClient {

    private static final String WWW_FORM = "application/x-www-form-urlencoded";

    public static String SESSION_ID = "";

    private int httpStatusCode;
    private String body;

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getBody() {
        return body;
    }

    private Builder builder;

    private void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public void request() {
        HttpURLConnection conn = getConnection(false);
        setHeader(conn);
        setBody(conn);
        Log.d("TestVO", "=====================11");
        connect(conn);
        Log.d("TestVO", "=====================12");
        getSession(conn);
        Log.d("TestVO", "=====================13");
        httpStatusCode = getStatusCode(conn);
        Log.d("TestVO", "=====================14   " + httpStatusCode);
        body = readStream(conn);
        Log.d("TestVO", "=====================15   " + body);
        conn.disconnect();
    }

    private HttpURLConnection getConnection(boolean redirectable) {
        try {
            URL url = new URL(builder.getUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(redirectable);
            return connection;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setHeader(HttpURLConnection connection) {
        setContentType(connection);
        setRequestMethod(connection);
        // Login 후 Session 유지를 위한 코드
        setSessionInfo(connection);

        connection.setConnectTimeout(5000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
    }

    private void setContentType(HttpURLConnection connection) {
        connection.setRequestProperty("Content-Type", WWW_FORM);
    }

    private void setRequestMethod(HttpURLConnection connection) {
        try {
            connection.setRequestMethod(builder.getMethod());
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
    }

    private void setSessionInfo(HttpURLConnection connection) {
        Log.d("SESSION", SESSION_ID);
        if ( SESSION_ID.length() > 0 ) {
            connection.setRequestProperty("cookie", SESSION_ID);
        }
    }

    private void setBody(HttpURLConnection connection) {

        String parameter = builder.getParameters();
        if ( parameter != null && parameter.length() > 0 ) {
            OutputStream outputStream = null;
            try {
                outputStream = connection.getOutputStream();
                outputStream.write(parameter.getBytes("UTF-8"));
                outputStream.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if ( outputStream != null )
                        outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void connect(HttpURLConnection conn) {
        try {
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getSession(HttpURLConnection conn) {

        Log.d("TestVO", "SESSION_0 : " + SESSION_ID.length());
        Log.d("TestVO", "SESSION_0 : " + SESSION_ID);

        if ( SESSION_ID.length() == 0 ) {

            Log.d("TestVO", "SESSION_01");

            Map m = conn.getHeaderFields();

            Log.d("TestVO", "SESSION_1");

            if(m.containsKey("Set-Cookie")) {
                Log.d("TestVO", "SESSION_2");

                Collection c =(Collection)m.get("Set-Cookie");

                Log.d("TestVO", "SESSION_3");
                for(Iterator i = c.iterator(); i.hasNext(); ) {
                    Log.d("TestVO", "SESSION_4");
                    SESSION_ID += (String)i.next() + ", ";
                }
            }
            Log.d("RESULT-SESSION", SESSION_ID);
        }

    }

    private int getStatusCode(HttpURLConnection connection) {
        try {
            return connection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return -10;
    }

    private String readStream(HttpURLConnection connection) {
        String result = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = null;
            while ( (line = reader.readLine()) != null ) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {}
        }

        return result;
    }

    public static class Builder {

        private Map<String, String> parameters;
        private String method;
        private String url;

        public String getMethod() {
            return method;
        }

        public String getUrl() {
            return url;
        }

        public Builder(String method, String url) {
            if(method == null) {
                method = "GET";
            }
            this.method = method;
            this.url = url;
            this.parameters = new HashMap();
        }

        public void addOrReplaceParameter(String key, String value) {
            this.parameters.put(key, value);
        }

        public void addAllParameters(Map<String, String> param) {
            this.parameters.putAll(param);
        }

        public String getParameters() {
            return generateParameters();
        }

        public String getParameter(String key) {
            return this.parameters.get(key);
        }

        private String generateParameters() {
            StringBuffer parameters = new StringBuffer();

            Iterator<String> keys = getKeys();

            String key = "";
            while ( keys.hasNext() ) {
                key = keys.next();
                parameters.append(String.format("%s=%s", key, this.parameters.get(key)));
                parameters.append("&");
            }

            String params = parameters.toString();
            if ( params.length() > 0 ) {
                params = params.substring( 0, params.length() - 1 );
            }

            return params;
        }

        private Iterator<String> getKeys() {
            return this.parameters.keySet().iterator();
        }

        public HttpClient create() {
            HttpClient client = new HttpClient();
            client.setBuilder(this);
            return client;
        }

    }

}