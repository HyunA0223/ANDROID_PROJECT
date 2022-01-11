package ddwu.mobile.finalproject.ma02_20191003;

import android.app.PendingIntent;
import android.content.*;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class BroadcastRepeatReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean[] week = intent.getBooleanArrayExtra("weekday");
        String name = intent.getStringExtra("name");
        Calendar cal = Calendar.getInstance();

        int notificationId = (int) intent.getLongExtra("memoId", 0);

        if (!week[cal.get(Calendar.DAY_OF_WEEK) - 1]) { // 반복 지정한 날짜가 아니면 return
//            Log.d("BroadcastRepeatReceiver", "another day of week");
            return;
        }

        intent = new Intent(context, MedicActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.CHANNEL_ID))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("SMARTIC")
                .setContentText(name + " 먹을 시간이에요!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(notificationId, builder.build());
    }
}
