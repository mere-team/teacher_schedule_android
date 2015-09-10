package Helpers;


import android.content.Context;
import android.content.Loader;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class AsyncTaskJsonLoader extends Loader<JSONArray> {

    final String LOG_TAG = "AsyncTaskJsonLoader";
    JsonDownloadTask jsonDownloadTask;
    String _url;
    Context _context;

    public AsyncTaskJsonLoader(Context context, String url) {
        super(context);

        _url = url;
        _context = context;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(LOG_TAG, hashCode() + " onStartLoading");
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(LOG_TAG, hashCode() + " onStopLoading");
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        Log.d(LOG_TAG, hashCode() + " onAbandon");
    }

    @Override
    protected void onReset() {
        super.onReset();
        Log.d(LOG_TAG, hashCode() + " onReset");
    }

    void getResultFromTask(String json) {
        try {
            JSONArray array = new JSONArray(json);
            deliverResult(array);
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.getMessage());
            Log.d(LOG_TAG, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }
    }

    @Override
    protected void onForceLoad(){
        if (jsonDownloadTask != null)
            jsonDownloadTask.cancel(true);
        jsonDownloadTask = new JsonDownloadTask(_url);
        jsonDownloadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    class JsonDownloadTask extends AsyncTask<String, Void, String> {

        private String _url;

        public JsonDownloadTask(String url){
            _url = url;
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
        protected void onPostExecute(String json){
            Log.d(LOG_TAG, "JsonDownloadTask completed");
            getResultFromTask(json);
        }

        private String getContent(String path) throws IOException{
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
    }
}
