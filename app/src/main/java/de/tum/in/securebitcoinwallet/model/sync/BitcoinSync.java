package de.tum.in.securebitcoinwallet.model.sync;

import de.tum.in.securebitcoinwallet.model.Address;
import de.tum.in.securebitcoinwallet.model.BackendApi;
import de.tum.in.securebitcoinwallet.model.Transaction;
import de.tum.in.securebitcoinwallet.model.database.AddressDao;
import de.tum.in.securebitcoinwallet.model.database.TransactionDao;
import de.tum.in.securebitcoinwallet.model.dto.AddressDto;
import de.tum.in.securebitcoinwallet.model.dto.TransactionDto;
import java.util.ArrayList;
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
   * Submits any missing transactions into
   *
   * @return true if at least one transaction has been submitted. That means that the sync should be
   * redone after a time period of roughly 10 minutes (usually it tooks that long until bitcoin
   * network confirms a transaction).
   */
  public boolean syncNotSubmittedTransactions() {
    List<Transaction> transactionList =
        transactionDao.getNotSubmittedTransactions().toBlocking().first();

    for (Transaction t : transactionList) {
      // TODO implement that
      // backendApi.postTransaction(t);

      t.setSyncState(Transaction.SYNC_WAITING_CONFIRM);
      transactionDao.insertOrUpdate(t);
    }

    return !transactionList.isEmpty();
  }

  /**
   * Compares the local database (addresses and transactions) with the one retrieved from backend.
   *
   * @return A list of new detected transactions
   */
  public List<Transaction> syncAddresses() {

    List<Transaction> newDetectedTransactions = new ArrayList<>();

    List<Address> addresses = addressDao.getAddresses().toBlocking().first();
    for (Address a : addresses) {
      int offset = 0;
      int limit = 50;

      AddressDto addressDto;
      while (true) {
        addressDto = backendApi.getAddress(a.getAddress(), offset, limit).toBlocking().first();

        // Check transactions

        for (TransactionDto dto : addressDto.getTransactions()) {

          Transaction transaction =
              transactionDao.getTransactionById(dto.getHash()).toBlocking().first();

          // New transaction detected
          if (transaction == null) {
            Transaction newTransaction = dto.toTransaction();
            newTransaction.setAddress(addressDto.getAddress());
            transaction.setName("Unknown");
            newDetectedTransactions.add(newTransaction);
            transactionDao.insertOrUpdate(newTransaction);
          } else {

            // check if the old needs an update
            if (transaction.getSyncState() != Transaction.SYNC_OK) {
              transaction.setSyncState(Transaction.SYNC_OK);
              transactionDao.update(transaction);
            }
          }
        }

        // Check if offset should be moved and address requeried
        if (addressDto.getTransactionsCount() < offset + limit) {
          break;
        }

        offset += limit; // for next loop
      }

      // Update Dato
      addressDao.updateBalance(addressDto);
    }

    return newDetectedTransactions;
  }
}
