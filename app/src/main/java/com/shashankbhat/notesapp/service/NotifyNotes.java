package com.shashankbhat.notesapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import com.shashankbhat.notesapp.task.ShowNotesWorker;


public class NotifyNotes extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        WorkManager workManager = WorkManager.getInstance(context);
        WorkRequest workRequest = new OneTimeWorkRequest.Builder(ShowNotesWorker.class).build();
        workManager.enqueue(workRequest);

    }
}
