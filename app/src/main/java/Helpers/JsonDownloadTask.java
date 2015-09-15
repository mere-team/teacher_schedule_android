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

    private String getContent(String url) throws IOException{
        BufferedReader reader = null;

        URL content_url = new URL(url);
        HttpURLConnection c = (HttpURLConnection) content_url.openConnection();
        c.setRequestMethod("GET");
        c.setReadTimeout(4000);

        String result = "";

        for (int i = 0; i < 2; i++) {
            if (!result.isEmpty())
                return result;
            try {
                c.connect();
                reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    buf.append(line);
                }
                result = buf.toString();
            } catch (Exception ex) {
                continue;
            } finally {
                c.disconnect();
                if (reader != null)
                    reader.close();
            }
        }
        return result;
    }
}
