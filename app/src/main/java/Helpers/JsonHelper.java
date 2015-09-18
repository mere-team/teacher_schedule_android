package Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import Helpers.JsonDownloadTask.OnJsonDownloadedListener;
import Models.IJsonInterface;

public class JsonHelper<T extends IJsonInterface<T>> {

    private Context _context;

    public JsonHelper(Context context)
    {
        _context = context;
    }

    public List<T> GetListOfModels(JSONArray data, T obj) throws JsonDownloadException {
        if (data == null)
            throw new JsonDownloadException();
        ArrayList<T> array = new ArrayList<>(data.length());

        for (int i = 0; i < data.length(); i++){
            T item = obj.getFromJson(data.optJSONObject(i));
            Log.d("Add = ", item.toString());
            array.add(item);
        }

        return array;
    }

    public boolean IsNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null;
    }

    public void DownloadJson(String url, OnJsonDownloadedListener listener){
        try {
            if(IsNetworkConnected()) {
                new JsonDownloadTask(url, listener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else {
                Toast toast = Toast.makeText(_context, "Not connected to net", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        catch (Exception ex)
        {
            Toast toast = Toast.makeText(_context, ex.toString(), Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
