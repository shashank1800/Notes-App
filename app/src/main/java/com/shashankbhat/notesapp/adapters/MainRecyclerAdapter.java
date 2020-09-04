package com.shashankbhat.notesapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.shashankbhat.notesapp.R;
import com.shashankbhat.notesapp.room.Note;
import com.shashankbhat.notesapp.view.CustomPriorityView;

/**
 * Created by SHASHANK BHAT on 19-Jul-20.
 */
public class MainRecyclerAdapter extends PagedListAdapter<Note, MainRecyclerAdapter.MyViewHolder> {

    private Context context;

    public MainRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {
        this.context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.layout_main_card,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Note note = getItem(position);
        if(note!=null)
            holder.bindData(note);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title,desc;
        ImageButton imageButton;

        //Custom component
        CustomPriorityView priorityView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            imageButton = itemView.findViewById(R.id.edit);
            imageButton.setOnClickListener(this);

            //Custom component
            priorityView = itemView.findViewById(R.id.customPriorityView);

        }

        private void bindData(Note note) {
            title.setText(note.getTitle());
            desc.setText(note.getDescription());

            //Custom component
            priorityView.setPriority(note.getPriority());
        }

        @Override
        public void onClick(View v) {

        }
    }

    public static DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem==newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.equals(newItem);
        }
    };
}
