package de.tum.in.securebitcoinwallet.model;

import de.tum.in.securebitcoinwallet.model.database.AddressDao;
import de.tum.in.securebitcoinwallet.model.database.TransactionDao;
import javax.inject.Inject;

/**
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




}
