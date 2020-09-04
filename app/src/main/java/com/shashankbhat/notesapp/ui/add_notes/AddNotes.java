package com.shashankbhat.notesapp.ui.add_notes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.shashankbhat.notesapp.R;
import com.shashankbhat.notesapp.databinding.ActivityAddNotesBinding;
import com.shashankbhat.notesapp.room.Note;

import java.util.Calendar;
import java.util.Date;

import static com.shashankbhat.notesapp.utils.Constants.PRIORITY_HIGH;
import static com.shashankbhat.notesapp.utils.Constants.PRIORITY_LOW;
import static com.shashankbhat.notesapp.utils.Constants.PRIORITY_MED;

public class AddNotes extends AppCompatActivity {

    private int priority;
    private Date date;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddNotesBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_notes);
        binding.setLifecycleOwner(this);

        AddNotesViewModel addNotesViewModel = ViewModelProviders.of(this).get(AddNotesViewModel.class);

        binding.setPriorityListener(view -> {

            switch (view.getId()){
                case R.id.priority1:
                    priority = PRIORITY_LOW;
                    break;
                case R.id.priority2:
                    priority = PRIORITY_MED;
                    break;
                case R.id.priority3:
                    priority = PRIORITY_HIGH;
                    break;
            }
        });

        binding.setSaveListener(view -> {
            Note newNote;
            String title = binding.getTitle();
            String description = binding.getDescription();

            if(title!=null && description!=null && priority!=0 && date!=null){

                Date now = Calendar.getInstance().getTime();
                newNote = new Note(now, date, title,description,priority);

                addNotesViewModel.saveNote(newNote);

                Toast.makeText(getApplicationContext(), "Notes saved", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "You have missed something", Toast.LENGTH_SHORT).show();
            }
        });

        binding.datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> date = new Date(year-1900, monthOfYear,dayOfMonth,0,0,0));
    }


}