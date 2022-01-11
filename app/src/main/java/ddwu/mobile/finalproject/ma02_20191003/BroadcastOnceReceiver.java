package ddwu.mobile.finalproject.ma02_20191003;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class BroadcastOnceReceiver extends BroadcastReceiver {
	MedicDBManager medicDBManager;

	public void onReceive(Context context, Intent intent) {
		// Notification 출력
		String name = intent.getStringExtra("name");
		int notificationId = (int) intent.getLongExtra("memoId", 0);

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