package Helpers;

import android.app.Activity;
import android.app.ProgressDialog;

import ScheduleDatabase.ScheduleDatabaseHelper;

public class LoadingIndicator {
    private ProgressDialog _progress;

    public LoadingIndicator(Activity activity, String message){
        _progress = new ProgressDialog(activity);
        _progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _progress.setMessage(message);
        _progress.setIndeterminate(true);
        _progress.setCancelable(true);
    }

    public void show(){
        _progress.show();
    }

    public void close(){
        _progress.cancel();
    }
}
