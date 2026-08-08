package com.eurobuddha.nftwallet;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONObject;
import org.minimarex.minimaapi.MinimaAPI;
import org.minimarex.minimaapi.MinimaAPIMessages;

/**
 * Keeps a State NFT collection minting when the app is closed.
 *
 * A collection needs roughly 18 ticks at best and 25–45 in practice, one per block — 25–40 minutes.
 * Before this existed the engine's only clock was a live Activity, so swiping the app away (or even
 * rotating it) parked the mint silently until the user came back.
 *
 * Modelled on apks/limit's LimitService, with AtomiX's change-guarded notification text.
 * Deliberately self-stopping: the moment every collection is DONE or BURIED this service goes away,
 * because a wallet has no business holding a permanent notification.
 */
public class MintService extends Service {

    private static final int FG_ID = 3101;
    public static final String ACTION_HEARTBEAT = "com.eurobuddha.nftwallet.HEARTBEAT";

    private NodeApi node;
    private BroadcastReceiver receiver;
    private String fgText = "";

    @Override public IBinder onBind(Intent intent) { return null; }

    @Override public void onCreate() {
        super.onCreate();
        Notifier.ensureChannels(this);
        // If the OS refuses foreground promotion, bail gracefully rather than crashing — the mint
        // resumes when the app is next opened.
        if (!startForegroundCompat()) { stopSelf(); return; }

        // Nothing to do? Don't linger. (Boot and the worker both start us speculatively.)
        if (!MintDriver.hasWork(this)) { stopGracefully(); return; }

        // applicationContext: NodeApi's dead-view guard only applies to an Activity host, so a
        // service instance always receives its callbacks.
        node = new NodeApi(getApplicationContext(), enabled -> {});

        receiver = new BroadcastReceiver() {
            @Override public void onReceive(Context c, Intent intent) {
                if (!MinimaAPI.checkMinimaID(MintService.this, intent)) return;
                String data = intent.getStringExtra(MinimaAPIMessages.MINIMA_API_NOTIFY_DATA);
                if (data == null) return;
                try {
                    String event = new JSONObject(data).optString("event", "");
                    if ("NEWBLOCK".equals(event) || "NEWBALANCE".equals(event)) tick();
                } catch (Exception ignored) {}
            }
        };
        ContextCompat.registerReceiver(this, receiver,
                new IntentFilter(MinimaAPIMessages.MINIMA_API_NOTIFY), ContextCompat.RECEIVER_EXPORTED);

        HeartbeatReceiver.schedule(this);
        updateFg(stateText());
        tick();
    }

    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && ACTION_HEARTBEAT.equals(intent.getAction())) tick();
        return START_STICKY;
    }

    /**
     * One step of the pipeline.
     *
     * Stands down while the Activity is in front: both hosts share the same LocalStore, and two
     * ticks racing would try to spend the same coin twice.
     */
    private void tick() {
        if (MainActivity.FOREGROUND) return;
        if (!MintDriver.hasWork(this)) { finishUp(); return; }

        MintDriver.Result r = MintDriver.tick(this, node, message -> {
            updateFg(stateText());
            if (!MintDriver.hasWork(MintService.this)) finishUp();
        });
        if (r == MintDriver.Result.NOTHING_TO_DO) finishUp();
        else if (r == MintDriver.Result.NEEDS_IMAGES) {
            updateFg("Paused — an item still needs its image");
        }
    }

    /** Everything is minted (or parked for the user): say so once, then get out of the way. */
    private void finishUp() {
        // A stuck collection makes hasWork() false, exactly like a finished one — so this used to
        // congratulate the user on a collection that had just died against the 64KB cap. Report
        // what actually happened; the Mint screen shows the full explanation.
        if (MintDriver.hasStuck(this)) {
            Notifier.alert(this, "Minting stopped",
                    "A collection can't be completed — its transactions exceed the chain's 64KB "
                            + "limit. Open the wallet for details.");
        } else if (!MintDriver.needsImages(this)) {
            Notifier.alert(this, "Minting finished", "Your collection is fully stamped.");
        }
        stopGracefully();
    }

    private String stateText() {
        String name = MintDriver.activeName(this);
        int[] p = MintDriver.activeProgress(this);
        if (name.isEmpty() || p == null) return "Finishing up…";
        return name + " · stamped " + p[0] + " of " + p[1];
    }

    /** Re-render the ongoing notification ONLY when the text actually changes (AtomiX's rule). */
    private void updateFg(String text) {
        if (text == null || text.equals(fgText)) return;
        fgText = text;
        android.app.NotificationManager nm = getSystemService(android.app.NotificationManager.class);
        if (nm != null) nm.notify(FG_ID, buildFg(text));
    }

    private Notification buildFg(String text) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(this, Notifier.CH_FG)
                .setContentTitle("Minting your collection")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(Notifier.openApp(this))
                .setOngoing(true);
        int[] p = MintDriver.activeProgress(this);
        if (p != null && p[1] > 0) b.setProgress(p[1], p[0], false);
        return b.build();
    }

    private boolean startForegroundCompat() {
        Notification n = buildFg("Working…");
        try {
            if (Build.VERSION.SDK_INT >= 34) {
                // specialUse, not dataSync: Android 14 caps dataSync at ~6h/day and then CRASHES
                // the service. A mint can legitimately run for a long stretch.
                startForeground(FG_ID, n, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);
            } else if (Build.VERSION.SDK_INT >= 29) {
                startForeground(FG_ID, n, ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC);
            } else {
                startForeground(FG_ID, n);
            }
            return true;
        } catch (Exception e) {
            return false;   // ForegroundServiceStartNotAllowedException etc. — never crash
        }
    }

    /** The user swiped the app away. stopWithTask=false keeps us alive; some OEMs kill anyway. */
    @Override public void onTaskRemoved(Intent rootIntent) {
        if (MintDriver.hasWork(this)) {
            try { MintWorker.schedule(getApplicationContext()); } catch (Exception ignored) {}
            try {
                android.app.AlarmManager am = (android.app.AlarmManager) getSystemService(ALARM_SERVICE);
                android.app.PendingIntent pi = android.app.PendingIntent.getForegroundService(
                        getApplicationContext(), 7, new Intent(getApplicationContext(), MintService.class),
                        android.app.PendingIntent.FLAG_ONE_SHOT | android.app.PendingIntent.FLAG_IMMUTABLE);
                // allow-while-idle: a plain RTC alarm gets deferred to a maintenance window, which
                // could be hours, and the mint would sit dark the whole time.
                if (am != null) am.setAndAllowWhileIdle(android.app.AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + 2000, pi);
            } catch (Exception ignored) {}
        }
        super.onTaskRemoved(rootIntent);
    }

    /** Android 14+ can tell a time-limited FGS to stop; do it cleanly rather than being killed. */
    @Override public void onTimeout(int startId) { stopGracefully(); }
    @Override public void onTimeout(int startId, int fgsType) { stopGracefully(); }

    private void stopGracefully() {
        // Tear down the two wake sources with us. Both are self-perpetuating — the heartbeat
        // re-arms itself and the worker is periodic — so leaving them running meant one finished
        // mint bought a permanent 15-minute wakeup for the life of the install.
        HeartbeatReceiver.cancel(this);
        MintWorker.cancel(this);
        try { stopForeground(STOP_FOREGROUND_REMOVE); } catch (Exception ignored) {}
        stopSelf();
    }

    @Override public void onDestroy() {
        super.onDestroy();
        if (node != null) node.onDestroy();
        if (receiver != null) { try { unregisterReceiver(receiver); } catch (Exception ignored) {} }
    }

    /** Start it only when there is something to do. Safe to call repeatedly. */
    public static void startIfWork(Context ctx) {
        if (!MintDriver.hasWork(ctx)) return;
        try { ContextCompat.startForegroundService(ctx, new Intent(ctx, MintService.class)); }
        catch (Exception ignored) {}
        try { MintWorker.schedule(ctx.getApplicationContext()); } catch (Exception ignored) {}
    }
}
