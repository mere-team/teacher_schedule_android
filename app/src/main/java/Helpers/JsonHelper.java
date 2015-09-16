package Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import Helpers.JsonDownloadTask.OnJsonDownloadedListener;
import Models.Faculty;
import Models.IJsonInterface;

public class JsonHelper<T extends IJsonInterface<T>> {

    private String _path;
    private Context _context;
    private boolean IsExist = false;

    public JsonHelper(String path, Context context)
    {
        _path = path;
        _context = context;
    }

    public void SaveJsonToFile(JSONArray data)
    {
        try {
            FileOutputStream fOut = _context.openFileOutput(_path, Context.MODE_WORLD_READABLE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write(data.toString());
            osw.flush();
            osw.close();
            fOut.close();

            //IsExist = true;
        }
        catch (Exception ex)
        {
            Log.d("Error = ", ex.toString());
        }
    }

    public JSONArray GetDataFromFile()
    {
        String readString;
        JSONArray array = null;

        try
        {
            FileInputStream fIn = _context.openFileInput(_path);
            InputStreamReader isr = new InputStreamReader(fIn);

            char[] inputBuffer = new char[fIn.available()];
            isr.read(inputBuffer);
            readString = new String(inputBuffer);
            array = new JSONArray(readString);

            isr.close();
            fIn.close();
        }
        catch (Exception ex)
        {
            Log.d("Error = ", ex.toString());
        }

        return array;
    }

    public boolean FileIsExist()
    {
        try
        {
            FileInputStream fIn = _context.openFileInput(_path);
            InputStreamReader isr = new InputStreamReader(fIn);

            isr.close();
            fIn.close();

            return true;
        }
        catch (Exception ex)
        {
            return  false;
        }
    }

    public ArrayList<T> GetListOfModels(JSONArray data, T obj) throws JsonDownloadException {
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
//            if (FileIsExist()) {
//                listener.onJsonDownloaded(GetDataFromFile());
//
//            } else
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
