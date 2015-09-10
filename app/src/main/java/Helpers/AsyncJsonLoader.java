package Helpers;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AsyncJsonLoader extends AsyncTaskLoader<JSONArray> {

    private String _url;

    public AsyncJsonLoader(Context context, String url){
        super(context);
        _url = url;
    }

    @Override
    public JSONArray loadInBackground() {
        String content;
        try {
            content = getContent(_url);
            return new JSONArray(content);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    @Override
    public void deliverResult(JSONArray data){
        if (isStarted()){
            super.deliverResult(data);
        }
    }

    private String getContent(String path) throws IOException {
        BufferedReader reader = null;
        try{
            URL url = new URL(path);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(4000);
            c.connect();
            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                buf.append(line);
            }
            return buf.toString();
        }
        finally {
            if (reader != null)
                reader.close();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

}
