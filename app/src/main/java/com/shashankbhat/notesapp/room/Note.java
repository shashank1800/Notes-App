package com.shashankbhat.notesapp.room;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Created by SHASHANK BHAT on 19-Jul-20.
 */

@Entity(tableName = "note_table")
public class Note implements Serializable {

    @NonNull
    @PrimaryKey(autoGenerate = false)
    private Date updatedDate;
    private Date finishBefore;

    private String title;
    private String description;
    private int priority;

    public Note(@NonNull Date updatedDate, Date finishBefore, String title, String description, int priority) {
        this.updatedDate = updatedDate;
        this.finishBefore = finishBefore;
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Note{" +
                "updatedDate=" + updatedDate +
                ", finishBefore=" + finishBefore +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                '}';
    }

    @NonNull
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public Date getFinishBefore() {
        return finishBefore;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note)) return false;
        Note note = (Note) o;
        return priority == note.priority &&
                updatedDate.equals(note.updatedDate) &&
                Objects.equals(finishBefore, note.finishBefore) &&
                Objects.equals(title, note.title) &&
                Objects.equals(description, note.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(updatedDate, finishBefore, title, description, priority);
    }
}
