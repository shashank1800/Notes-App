package com.shashankbhat.notesapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import com.shashankbhat.notesapp.room.Note;
import com.shashankbhat.notesapp.room.NoteRepository;
import com.shashankbhat.notesapp.task.ShowNotesWorker;
import com.shashankbhat.notesapp.utils.Constants;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by SHASHANK BHAT on 19-Jul-20.
 */
public class MainActivityViewModel extends AndroidViewModel {

    private NoteRepository noteRepository;
    private WorkManager workManager;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        workManager = WorkManager.getInstance(application.getApplicationContext());
    }

    public void vmUpdate(Note note) {
        noteRepository.repoUpdate(note);
    }

    public void vmDelete(Note note) {
        noteRepository.repoDelete(note);
    }

    public void vmDeleteAllNotes() {
        noteRepository.repoDeleteAllNotes();
    }

    public LiveData<PagedList<Note>> getAllNotes() {
        return noteRepository.getAllNotes();
    }
}
