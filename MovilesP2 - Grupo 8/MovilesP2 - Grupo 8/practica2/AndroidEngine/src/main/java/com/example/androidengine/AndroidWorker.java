package com.example.androidengine;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class AndroidWorker extends Worker {

    public static String channelIdInput = "notifications_channel_id";
    public static String titleInput = "title";
    public static String descriptionInput = "description";
    public static String iconInput = "notifications_icon";
    public static String classInput = "class";

    public AndroidWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String title = getInputData().getString(titleInput);
        String description = getInputData().getString(descriptionInput);
        String CHANNEL_ID = getInputData().getString(channelIdInput);
        int icon = getInputData().getInt(iconInput,0);
        String nameClass=getInputData().getString(classInput);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

        Intent intent = getApplicationContext().getPackageManager().getLaunchIntentForPackage(nameClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent. getActivity(getApplicationContext(), 0, intent,
                PendingIntent. FLAG_IMMUTABLE | PendingIntent. FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder( getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(icon)
                .setContentTitle( title )
                .setContentText( description )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setChannelId(CHANNEL_ID);

        getApplicationContext().checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS);
        notificationManager.notify(1, builder.build());

        return Result.success();
    }
}
