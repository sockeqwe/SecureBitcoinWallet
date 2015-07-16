package de.tum.in.securebitcoinwallet.model.sync;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import com.hannesdorfmann.mosby.dagger1.Injector;
import de.tum.in.securebitcoinwallet.IntentStarter;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.model.Transaction;
import java.util.List;
import javax.inject.Inject;

/**
 * @author Hannes Dorfmann
 */
public class BitcoinSyncService extends IntentService {

  private final long RESCHEDULE_SUBMITTING_AFTER = 11 * 60 * 1000;

  @Inject BitcoinSync sync;
  @Inject IntentStarter intentStarter;

  public BitcoinSyncService() {
    super("BitcoinSyncService");
  }

  @Override public int onStartCommand(Intent intent, int flags, int startId) {
    ((Injector) getApplication()).getObjectGraph().inject(this);
    return super.onStartCommand(intent, flags, startId);
  }

  @Override protected void onHandleIntent(Intent intent) {

    boolean anotherSyncNeeded = false;
    try {
      // submit unsynchronized transactions
      anotherSyncNeeded = sync.syncNotSubmittedTransactions();
    } catch (Exception e) {
      anotherSyncNeeded = true;
    }

    if (anotherSyncNeeded) {
      scheduleRerunForSubmittedTransactions();
    }

    try {
      // syncs the addresses of local database
      List<Transaction> newTransactions = sync.syncAddresses();
      if (!newTransactions.isEmpty()) {
        buildNotifications(newTransactions);
      }
    } catch (Exception e) {
      // Do nothing, the next scheduled sync will restart again
    }
  }

  /**
   * Reschedules to run this service again. This is neede to determine whether a transcation has
   * been confirmed or not
   */
  private void scheduleRerunForSubmittedTransactions() {
    AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

    PendingIntent pendingIntent = PendingIntent.getService(getApplication(), 0,
        new Intent(getApplication(), BitcoinSyncService.class), 0);

    alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
        SystemClock.elapsedRealtime() + RESCHEDULE_SUBMITTING_AFTER, pendingIntent);
  }

  /**
   * Creates a notification for new incomming notifications
   *
   * @param transactions new transactions
   */
  private void buildNotifications(List<Transaction> transactions) {

    for (Transaction t : transactions) {

      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
          intentStarter.getIntentToShowTransactions(getApplication(), t.getAddress()),
          PendingIntent.FLAG_UPDATE_CURRENT);

      NotificationCompat.Builder builder =
          new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_lock)
              .setContentTitle(getApplication().getString(R.string.notification_received))
              .setContentText(getApplication().getString(R.string.noticiations_details))
              .setContentIntent(pendingIntent);

      NotificationManager notificationManager =
          (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
      notificationManager.notify(t.getId().hashCode(), builder.build());
    }
  }
}
