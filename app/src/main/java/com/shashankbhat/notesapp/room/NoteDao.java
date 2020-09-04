package com.shashankbhat.notesapp.room;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;

/**
 * Created by SHASHANK BHAT on 19-Jul-20.
 */

@Dao
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Note note);

    @Update
    void update(Note note);

    @Delete
    void delete(Note delete);

    @Query("DELETE FROM note_table")
    void deleteAllNotes();

    @Query("SELECT * FROM note_table ORDER BY updatedDate ASC")
    DataSource.Factory<Integer, Note> getAllNotes();

    @Query("SELECT * FROM NOTE_TABLE WHERE finishBefore >= :today ORDER BY finishBefore ASC, priority DESC LIMIT 1")
    Note getNearestDatedNotes(Date today);
}
