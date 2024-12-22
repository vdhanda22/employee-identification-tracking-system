package com.karzansoft.fastvmi.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.karzansoft.fastvmi.Activities.LoginActivity;

import org.chat21.android.core.ChatManager;
import org.chat21.android.core.users.models.ChatUser;
import org.chat21.android.notifications.ChatFirebaseMessagingService;
import org.chat21.android.ui.ChatUI;
import org.chat21.android.utils.Utils;

import static org.chat21.android.ui.ChatUI.BUNDLE_CHANNEL_TYPE;
import static org.chat21.android.utils.DebugConstants.DEBUG_NOTIFICATION;

/**
 * Created by Yasir on 8/1/2018.
 */

public class FirebasePushMessagingService extends ChatFirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);

        Log.d(DEBUG_NOTIFICATION, "From: " + remoteMessage.getFrom());
        Log.e("FIREBase MSG",""+remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(DEBUG_NOTIFICATION, "Message data payload: " + remoteMessage.getData());

            String sender = remoteMessage.getData().get("sender");
            String senderFullName = remoteMessage.getData().get("sender_fullname");
            String channelType = remoteMessage.getData().get("channel_type");
            String text = remoteMessage.getData().get("text");
            String timestamp = remoteMessage.getData().get("timestamp");
            String recipientFullName = remoteMessage.getData().get("recipient_fullname");
            String recipient = remoteMessage.getData().get("recipient");

            if (Utils.getUser(this) != null) {
                if(!Utils.getUser(this).getId().equals(recipient))
                    return;
                try {
                    if (ChatManager.getInstance() != null) {
                        super.onMessageReceived(remoteMessage);
                    }else {
                        sendDirectNotification(sender, senderFullName, text, channelType);
                    }
                } catch (RuntimeException ex) {
                    sendDirectNotification(sender, senderFullName, text, channelType);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }else {

                sendDirectNotification(sender, senderFullName, text, channelType);
            }

        }
    }

    private void sendDirectNotification(String sender, String senderFullName, String text, String channel) {

        Utils.setUnreadChat(true,this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(ChatUI.BUNDLE_RECIPIENT, new ChatUser(sender, senderFullName));
        intent.putExtra(BUNDLE_CHANNEL_TYPE, channel);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channel)
                        .setSmallIcon(org.chat21.android.R.drawable.ic_stat_ac_unit)
                        .setContentTitle(senderFullName)
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setGroup(sender)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Oreo fix
        String channelId = channel;
        String channelName = channel;
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        int notificationId = NOTIFICATION_ID;
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}
