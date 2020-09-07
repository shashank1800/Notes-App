package com.shashankbhat.notesapp.ui;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.shashankbhat.notesapp.R;
import com.shashankbhat.notesapp.adapters.MainRecyclerAdapter;
import com.shashankbhat.notesapp.room.Note;
import com.shashankbhat.notesapp.ui.settings.SettingsActivity;
import com.shashankbhat.notesapp.viewmodel.MainActivityViewModel;
import com.shashankbhat.notesapp.viewmodel.MainViewModelFactory;

import java.util.Objects;

public class ShowAllNotes extends Fragment {

    private FloatingActionButton fab;
    private MainActivityViewModel viewModel;
    private Context context;
    private RecyclerView recycler;
    private DrawerLayout drawerLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_show_all_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = view.getContext();
        fab = view.findViewById(R.id.floating_action_button);
        recycler = view.findViewById(R.id.main_rv);

        fab.setOnClickListener(this::moveToAddNotes);





//        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        MainViewModelFactory factory = new MainViewModelFactory(getActivity().getApplication(), 10);
        viewModel = new ViewModelProvider(this, factory).get(MainActivityViewModel.class);

        setupRecyclerView(recycler);
    }

    private void moveToAddNotes(View view) {
        Intent intent = new Intent(view.getContext(), AddNotes.class);
        startActivity(intent);
    }

    private boolean isTabletModeEnabled() {

        PreferenceManager.setDefaultValues(context, R.xml.root_preferences, false);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPref.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
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
            recycler.setLayoutManager(new LinearLayoutManager(context));

        MainRecyclerAdapter adapter = new MainRecyclerAdapter();
        recycler.setAdapter(adapter);

        setItemTouchHelper(recycler, adapter);
        viewModel.getAllNotes().observe(requireActivity(), adapter::submitList);
    }


    private void setItemTouchHelper(RecyclerView recyclerView, MainRecyclerAdapter adapter) {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder v, @NonNull RecyclerView.ViewHolder v1) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                Note note = Objects.requireNonNull(adapter.getCurrentList()).get(viewHolder.getAdapterPosition());
                viewModel.vmDelete(note);
//                deleteAlertDialog(note, false);
            }
        }).attachToRecyclerView(recyclerView);
    }


}