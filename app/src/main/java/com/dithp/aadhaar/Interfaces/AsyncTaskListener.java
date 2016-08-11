package com.dithp.aadhaar.Interfaces;

import com.dithp.aadhaar.Enum.TaskType;

/**
 * Created by kuush on 8/11/2016.
 */
public interface AsyncTaskListener {

    public void onTaskCompleted(String result, TaskType taskType);
}
