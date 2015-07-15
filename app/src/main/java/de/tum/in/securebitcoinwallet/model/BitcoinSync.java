package de.tum.in.securebitcoinwallet.model;

import de.tum.in.securebitcoinwallet.model.database.AddressDao;
import de.tum.in.securebitcoinwallet.model.database.TransactionDao;
import javax.inject.Inject;

/**
 * This class is responsible to sync the local database with the backend.
 *
 * @author Hannes Dorfmann
 */
public class BitcoinSync {

  private BackendApi backendApi;
  private AddressDao addressDao;
  private TransactionDao transactionDao;

  @Inject
  public BitcoinSync(BackendApi backendApi, AddressDao addressDao, TransactionDao transactionDao) {
    this.backendApi = backendApi;
    this.addressDao = addressDao;
    this.transactionDao = transactionDao;
  }


  public void statSync(){

  }

}
