package de.tum.in.securebitcoinwallet.common;

import android.content.Context;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.model.exception.IncorrectPinException;
import de.tum.in.securebitcoinwallet.model.exception.NotFoundException;
import de.tum.in.securebitcoinwallet.smartcard.exception.AppletNotInitializedException;
import de.tum.in.securebitcoinwallet.smartcard.exception.AuthenticationFailedExeption;
import de.tum.in.securebitcoinwallet.smartcard.exception.CardLockedException;
import de.tum.in.securebitcoinwallet.smartcard.exception.KeyStoreFullException;
import de.tum.in.securebitcoinwallet.smartcard.exception.SmartCardNotConnectedException;

/**
 * @author Hannes Dorfmann
 */
public class ErrorMessageDeterminer {

  private Context context;

  public ErrorMessageDeterminer(Context context) {
    this.context = context;
  }

  public String getString(Throwable error, boolean pullToRefresh) {
    if (error instanceof IncorrectPinException) {
      return context.getString(R.string.error_pin_incorrect);
    }

    if (error instanceof NotFoundException) {
      return context.getString(R.string.error_not_found);
    }

    if (error instanceof AuthenticationFailedExeption) {
      return context.getString(R.string.error_pin_incorrect);
    }

    if (error instanceof CardLockedException) {
      return context.getString(R.string.error_card_locked);
    }

    if (error instanceof AppletNotInitializedException) {
      return context.getString(R.string.error_applet_not_initialized);
    }

    if (error instanceof KeyStoreFullException) {
      return context.getString(R.string.error_keystore_full);
    }

    if (error instanceof SmartCardNotConnectedException) {
      return context.getString(R.string.error_smartcard_not_connected);
    }

    return context.getString(R.string.error_unknown);
  }
}
