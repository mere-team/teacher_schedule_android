package Helpers;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        String result = "";

        URL url = new URL(path);
        try {
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setReadTimeout(4000);
            c.connect();
            reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder buf = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                buf.append(line);
            }
            result = buf.toString();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            if (reader != null)
                reader.close();
        }
        return result;
    }
}
