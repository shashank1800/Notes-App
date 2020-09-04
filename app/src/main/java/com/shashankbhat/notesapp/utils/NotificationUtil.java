package com.shashankbhat.notesapp.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.shashankbhat.notesapp.MainActivity;
import com.shashankbhat.notesapp.R;
import com.shashankbhat.notesapp.room.Note;

import static com.shashankbhat.notesapp.utils.Constants.PRIORITY_HIGH;
import static com.shashankbhat.notesapp.utils.Constants.PRIORITY_LOW;
import static com.shashankbhat.notesapp.utils.Constants.PRIORITY_MED;

/**
 * Created by SHASHANK BHAT on 22-Jul-20.
 */
public class NotificationUtil {

    public static String NOTIFICATION_CHANNEL_ID = "CHANNEL ID";
    public static int PENDING_INTENT_CHANNEL_ID = 1001;
    public static final int NOTIFICATION_ID = 1002;

    public static void notify(Context context, Note note){

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_star_yellow)
                .setContentTitle(note.getTitle())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(note.getDescription()))
                .addAction(R.drawable.ic_snooze, context.getString(R.string.see), snoozePendingIntent(context))
                .setAutoCancel(true);

        if(note.getPriority()==PRIORITY_LOW){
            notificationBuilder
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setColor(ContextCompat.getColor(context, R.color.low));
        }else if(note.getPriority()==PRIORITY_MED){
            notificationBuilder
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(ContextCompat.getColor(context, R.color.medium));
        }else if(note.getPriority()==PRIORITY_HIGH){
            notificationBuilder
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setColor(ContextCompat.getColor(context, R.color.high));
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channel_name = context.getString(R.string.channel_name);
            String channel_desc = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channel_name, importance);
            channel.setDescription(channel_desc);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if(notificationManager!=null)
                notificationManager.createNotificationChannel(channel);
        }

        notificationBuilder.build().flags |= Notification.FLAG_AUTO_CANCEL;


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        notificationManagerCompat.notify(NOTIFICATION_ID, notificationBuilder.build());

    }

    private static PendingIntent snoozePendingIntent(Context context) {
        Intent startTodoListActivityIntent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context, PENDING_INTENT_CHANNEL_ID, startTodoListActivityIntent, PendingIntent.FLAG_IMMUTABLE);
    }
}
