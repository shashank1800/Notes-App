package com.shashankbhat.notesapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.shashankbhat.notesapp.adapters.MainRecyclerAdapter;
import com.shashankbhat.notesapp.room.Note;
import com.shashankbhat.notesapp.service.NotifyNotes;
import com.shashankbhat.notesapp.ui.add_notes.AddNotes;
import com.shashankbhat.notesapp.ui.settings.SettingsActivity;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private FloatingActionButton fab;
    private CoordinatorLayout coordinator_layout;
    private MainActivityViewModel viewModel;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");

        context = this;

        fab = findViewById(R.id.floating_action_button);
        coordinator_layout = findViewById(R.id.coordinator_layout);

        fab.setOnClickListener(v -> Snackbar.make(coordinator_layout, getString(R.string.popup_regarding_notes), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.yes), moveToAddNotes()).show());

        RecyclerView recycler = findViewById(R.id.main_rv);

        if(getResources().getBoolean(R.bool.isTablet))
            recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        else
            recycler.setLayoutManager(new LinearLayoutManager(this));
        MainRecyclerAdapter adapter = new MainRecyclerAdapter();
        recycler.setAdapter(adapter);

        setItemTouchHelper(recycler, adapter);

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        viewModel.getAllNotes().observe(this, adapter::submitList);

        initSettingFragment();
    }

    private void initSettingFragment() {
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        SharedPreferences sharedPref = PreferenceManager
                        .getDefaultSharedPreferences(this);
        boolean switchPref = sharedPref.getBoolean
                (SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
        Toast.makeText(this, Boolean.toString(switchPref),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "OnStart");
        scheduleNotificationWork();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    private void scheduleNotificationWork() {
//        viewModel.scheduleWork();

        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotifyNotes.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        assert alarmMgr != null;
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);

    }

    private void setItemTouchHelper(RecyclerView recyclerView, MainRecyclerAdapter adapter) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(1, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder v, @NonNull RecyclerView.ViewHolder v1) {

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Note note = Objects.requireNonNull(adapter.getCurrentList()).get(viewHolder.getAdapterPosition());
                deleteAlert(note);
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void deleteAlert(Note note) {
        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(MainActivity.this);

        myAlertBuilder.setTitle("Delete");
        myAlertBuilder.setMessage("Do yo want to delete this note ?");
        myAlertBuilder.setPositiveButton(R.string.yes, (dialog, which) -> viewModel.vmDelete(note));
        myAlertBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        myAlertBuilder.show();
    }


    private View.OnClickListener moveToAddNotes() {
        return view -> startActivity(new Intent(getApplicationContext(), AddNotes.class));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.menu_settings:
                startActivity(new Intent(context, SettingsActivity.class));
                break;

            case R.id.menu_delete_all:
                viewModel.vmDeleteAllNotes();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.nav_page1:
                Snackbar.make(coordinator_layout, "Page 1 selected", Snackbar.LENGTH_LONG).show();
                break;

            case R.id.nav_page2:
                Snackbar.make(coordinator_layout, "Page 2 selected", Snackbar.LENGTH_LONG).show();
                break;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart");
        super.onRestart();
    }
}