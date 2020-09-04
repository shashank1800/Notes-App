package com.shashankbhat.notesapp.room;

import android.content.Context;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.shashankbhat.notesapp.utils.Converters;
import java.util.Date;

import static com.shashankbhat.notesapp.utils.Constants.TABLE_NAME;

/**
 * Created by SHASHANK BHAT on 19-Jul-20.
 */
@Database(entities = {Note.class}, exportSchema = false, version = 1)
@TypeConverters({Converters.class})
public abstract class NotesDatabase extends RoomDatabase {

    public abstract NoteDao getNoteDao();
    public static NotesDatabase instance;

    public static synchronized NotesDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context, NotesDatabase.class, TABLE_NAME)
                    .addCallback(roomCallback)
                    .build();
        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDatabase(instance).execute();
        }
    };

    private static class PopulateDatabase extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;

        public PopulateDatabase(NotesDatabase database) {
            this.noteDao = database.getNoteDao();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Void doInBackground(Void... voids) {

            Date now = Calendar.getInstance().getTime();
            noteDao.insert(new Note(now,now,"This is test title","Description is related to title",2));
            
            return null;
        }
    }

}
