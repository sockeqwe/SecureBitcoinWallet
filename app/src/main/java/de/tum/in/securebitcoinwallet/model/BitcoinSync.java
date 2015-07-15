package de.tum.in.securebitcoinwallet.model;

import de.tum.in.securebitcoinwallet.model.database.AddressDao;
import de.tum.in.securebitcoinwallet.model.database.TransactionDao;
import de.tum.in.securebitcoinwallet.model.dto.AddressDto;
import java.util.List;
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

  /**
   * Syncs the local addresses and transactions with the one from bitcoin network
   * return true, if transaction has been submitted and therefore a a sync should rerun in the
   * future (10 minutes) to check if the transactions has been confirmed
   */
  public boolean statSync() {

    int submittedTransactions = syncNotSubmittedTransactions();

    syncAddresses();
    return submittedTransactions > 0;
  }

  /**
   * Submits any missing transactions into
   * return the number of transaction that has been submitted
   */
  int syncNotSubmittedTransactions() {
    List<Transaction> transactionList =
        transactionDao.getNotSubmittedTransactions().toBlocking().first();

    for (Transaction t : transactionList) {
      // TODO implement that
      // backendApi.postTransaction(t);

      t.setSyncState(Transaction.SYNC_WAITING_CONFIRM);
      transactionDao.insertOrUpdate(t);
    }

    return transactionList.size();
  }

  void syncAddresses() {

    List<Address> addresses = addressDao.getAddresses().toBlocking().first();
    for (Address a : addresses) {
      AddressDto addressDto = backendApi.getAddress(a.getAddress(), 0, 50).toBlocking().first();

      if (addressDto != null){

        // Check transactions


        // Update Dato
        addressDao.updateBalance(addressDto);
      }
    }
  }
}
