package com.eurobuddha.nftwallet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.content.ContextCompat;

/**
 * Doze-proof clock for {@link MintService}.
 *
 * A foreground service keeps the process resident but does NOT exempt Handler.postDelayed or the
 * node's broadcasts from Doze — a phone left on a desk overnight can stop delivering NEWBLOCK for
 * long stretches. An exact allow-while-idle alarm is the one thing that reliably wakes us.
 */
public class HeartbeatReceiver extends BroadcastReceiver {

    private static final long INTERVAL_MS = 15 * 60_000;
    /** Fixed request code + FLAG_UPDATE_CURRENT, so rescheduling is idempotent. */
    private static final int REQUEST_CODE = 31;

    @Override public void onReceive(Context ctx, Intent intent) {
        // Chain the next one FIRST: a crash below must never end the heartbeat.
        schedule(ctx);
        if (!MintDriver.hasWork(ctx)) return;   // nothing to mint — let the alarm lapse
        try {
            ContextCompat.startForegroundService(ctx,
                    new Intent(ctx, MintService.class).setAction(MintService.ACTION_HEARTBEAT));
        } catch (Exception ignored) {}
    }

    public static void schedule(Context ctx) {
        try {
            AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
            if (am == null) return;
            PendingIntent pi = PendingIntent.getBroadcast(ctx, REQUEST_CODE,
                    new Intent(ctx, HeartbeatReceiver.class),
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            long at = System.currentTimeMillis() + INTERVAL_MS;
            if (Build.VERSION.SDK_INT >= 31 && !am.canScheduleExactAlarms()) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi);
            } else {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at, pi);
            }
        } catch (Exception ignored) {}
    }
}
