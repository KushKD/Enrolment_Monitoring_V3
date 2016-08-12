package com.dithp.aadhaar.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.dithp.aadhaar.Enum.TaskType;
import com.dithp.aadhaar.HTTP.HttpManager;
import com.dithp.aadhaar.Interfaces.AsyncTaskListener;

/**
 * Created by kuush on 8/11/2016.
 */
public class Generic_Async_Post extends AsyncTask<Object,Void ,String> {


    String outputStr;
    ProgressDialog dialog;
    Context context;
    AsyncTaskListener taskListener;
    TaskType taskType;

    public Generic_Async_Post(Context context, AsyncTaskListener taskListener, TaskType taskType){
        this.context = context;
        this.taskListener = taskListener;
        this.taskType = taskType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "Loading", "Connecting to Server .. Please Wait", true);
        dialog.setCancelable(false);
    }

    @Override
    protected String doInBackground(Object... params) {
        String Data_From_Server = null;
        HttpManager http_manager = null;

            http_manager = new HttpManager();

                Data_From_Server = http_manager.PostData_SaveData(params[0]);
                return Data_From_Server;



    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        taskListener.onTaskCompleted(result, taskType);
        dialog.dismiss();
    }



}
