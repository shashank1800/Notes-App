package com.shashankbhat.notesapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
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
import com.shashankbhat.notesapp.viewmodel.MainActivityViewModel;
import com.shashankbhat.notesapp.viewmodel.MainViewModelFactory;

import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private FloatingActionButton fab;
    private CoordinatorLayout coordinator_layout;
    private MainActivityViewModel viewModel;
    private Context context;
    private static final int NOTIFICATION_ID = 10;
    private RecyclerView recycler;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate");

        context = this;
        fab = findViewById(R.id.floating_action_button);
        coordinator_layout = findViewById(R.id.coordinator_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        fab.setOnClickListener(v -> Snackbar.make(coordinator_layout, getString(R.string.popup_regarding_notes), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.yes), moveToAddNotes()).show());

        recycler = findViewById(R.id.main_rv);

//        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        viewModel = new ViewModelProvider(this, new MainViewModelFactory(getApplication(), 10)).get(MainActivityViewModel.class);

    }

    private boolean isTabletModeEnabled() {

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        return sharedPref.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "OnStart");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
        scheduleNotificationWork();

        setupRecyclerView(recycler);
    }

    private void setupRecyclerView(RecyclerView recycler){

        /**
         * Checks whether it's tablet or standard mobile device
         * if table 2 column grid view
         * if mobile just a leaner layout
         */
        if(getResources().getBoolean(R.bool.isTablet) || isTabletModeEnabled())
            recycler.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        else
            recycler.setLayoutManager(new LinearLayoutManager(this));

        MainRecyclerAdapter adapter = new MainRecyclerAdapter();
        recycler.setAdapter(adapter);

        setItemTouchHelper(recycler, adapter);
        viewModel.getAllNotes().observe(this, adapter::submitList);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume");
        super.onResume();
    }

    private void scheduleNotificationWork() {

        /**
         * Keep eye on pending intent notification id in case of clearing the notification in broadcast
         * or clear it when app start ie. in onStart()
         */

        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotifyNotes.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent, PendingIntent.FLAG_ONE_SHOT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if(Calendar.getInstance().getTimeInMillis() > calendar.getTimeInMillis()){
            calendar.add(Calendar.DATE, 1);
        }

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
                deleteAlert(note, false);
            }
        }).attachToRecyclerView(recyclerView);
    }

    public void deleteAlert(Note note, boolean isDeleteAll) {

        AlertDialog.Builder myAlertBuilder = new AlertDialog.Builder(MainActivity.this);

        myAlertBuilder.setTitle("Delete");
        if(isDeleteAll) {
            myAlertBuilder.setMessage("Do yo want to delete all note ?");
            myAlertBuilder.setPositiveButton(R.string.yes, (dialog, which) -> viewModel.vmDeleteAllNotes());
        } else {
            myAlertBuilder.setMessage("Do yo want to delete this note ?");
            myAlertBuilder.setPositiveButton(R.string.yes, (dialog, which) -> viewModel.vmDelete(note));
        }

        myAlertBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        myAlertBuilder.show();
    }


    private View.OnClickListener moveToAddNotes() {
        return view -> startActivity(new Intent(getApplicationContext(), AddNotes.class));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        Log.i("option", "option");

        int id = item.getItemId();
        switch (id) {
            case R.id.menu_settings:
                startActivity(new Intent(context, SettingsActivity.class));
                break;

            case R.id.menu_delete_all:
                deleteAlert(null, true);
                break;

        }
        return true;
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

}