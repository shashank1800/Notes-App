package com.shashankbhat.notesapp.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import com.shashankbhat.notesapp.R;
import com.shashankbhat.notesapp.databinding.ActivityAddNotesBinding;
import com.shashankbhat.notesapp.room.Note;
import com.shashankbhat.notesapp.viewmodel.AddNotesViewModel;

import java.util.Calendar;

import static com.shashankbhat.notesapp.utils.Constants.PRIORITY_HIGH;
import static com.shashankbhat.notesapp.utils.Constants.PRIORITY_LOW;
import static com.shashankbhat.notesapp.utils.Constants.PRIORITY_MED;

public class AddNotes extends AppCompatActivity {

    private int priority = 1;
    private Calendar finishBefore = Calendar.getInstance();
    private Note note;
    private DatePicker.OnDateChangedListener onDateChangedListener;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAddNotesBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_notes);
        binding.setLifecycleOwner(this);

        AddNotesViewModel addNotesViewModel = ViewModelProviders.of(this).get(AddNotesViewModel.class);

        binding.datePicker.setMinDate(finishBefore.getTimeInMillis());
        finishBefore.set(Calendar.YEAR, finishBefore.getWeekYear() + 1900);

        note = (Note) getIntent().getSerializableExtra("Note");
        System.out.println("Notes "+note);
        if(note !=null){
            binding.setTitle(note.getTitle());
            binding.setDescription(note.getDescription());
            priority = note.getPriority();
            finishBefore.setTime(note.getFinishBefore());
            switch (priority){
                case 2: binding.priority2.setChecked(true);
                        break;
                case 3: binding.priority3.setChecked(true);
                        break;
                default: binding.priority1.setChecked(true);
            }
        }else{
            note = new Note(Calendar.getInstance().getTime(), finishBefore.getTime(), "","", priority);
            binding.priority1.setChecked(true);
        }

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

        binding.datePicker.setOnDateChangedListener(getDateChangeListener());

        binding.setSaveListener(view -> {
            String title = binding.getTitle();
            String description = binding.getDescription();
            if(title!=null && description!=null && !title.isEmpty() && !description.isEmpty()){
                note.setTitle(title);
                note.setDescription(description);
                note.setFinishBefore(finishBefore.getTime());
                note.setPriority(priority);
                note.setUpdatedDate(Calendar.getInstance().getTime());

                addNotesViewModel.saveNote(note);
                Toast.makeText(getApplicationContext(), "Notes saved", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "Some fields is empty", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public DatePicker.OnDateChangedListener getDateChangeListener(){

        if(onDateChangedListener==null) {
            onDateChangedListener = (view, year, monthOfYear, dayOfMonth) -> {
                finishBefore.set(Calendar.YEAR, year+1900);
                finishBefore.set(Calendar.MONTH, monthOfYear);
                finishBefore.set(Calendar.DATE, dayOfMonth);
            };
        }
        return onDateChangedListener;
    }

}