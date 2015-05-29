package de.tum.in.securebitcoinwallet.common;

import android.content.Context;
import de.tum.in.securebitcoinwallet.R;
import de.tum.in.securebitcoinwallet.model.exception.IncorrectPinException;
import de.tum.in.securebitcoinwallet.model.exception.NotFoundException;

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

    return context.getString(R.string.error_unknown);
  }
}
