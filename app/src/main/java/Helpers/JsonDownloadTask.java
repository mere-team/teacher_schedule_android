package Helpers;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class JsonDownloadTask extends AsyncTask<String, Void, String> {

    private String _url;
    private OnJsonDownloadedListener _listener;

    public JsonDownloadTask(String url, OnJsonDownloadedListener listener){
        _url = url;
        _listener = listener;
    }

    public interface OnJsonDownloadedListener {
        void onJsonDownloaded(JSONArray data);
    }

    @Override
    protected String doInBackground(String... params) {
        String content;
        try{
            content = getContent(_url);
            if (content == "")
                return "Data not downloaded";
        } catch (IOException ex){
            content = ex.getMessage();
        }
        return  content;
    }

    @Override
    protected void onProgressUpdate(Void... items){

    }

    @Override
    protected void onPostExecute(String content){
        JSONArray array = null;
        try {
            array = new JSONArray(content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        _listener.onJsonDownloaded(array);
    }

    private String getContent(String path) throws IOException{
        BufferedReader reader = null;
        try{
            URL url = new URL(path);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(10000);
            c.connect();
            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line = null;
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
}
