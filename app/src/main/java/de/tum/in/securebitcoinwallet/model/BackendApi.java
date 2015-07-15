package de.tum.in.securebitcoinwallet.model;

import de.tum.in.securebitcoinwallet.model.dto.AddressDto;
import de.tum.in.securebitcoinwallet.model.dto.TransactionDto;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * The API to talk to blockchain.info backend API
 *
 * @author Hannes Dorfmann
 */
public interface BackendApi {

  /**
   * Get the details of an address and the tranasactions associated with the address. Pagination is
   * required for that API call.
   *
   * @param address The addres as plaintext
   * @param offset The offset for pagination
   * @param limit The limit for pagination
   * @return {@link AddressDto}
   */
  @GET("/address/{address}?format=json") public Observable<AddressDto> getAddress(
      @Path("address") String address, @Query("offset") int offset, @Query("limit") int limit);

  /**
   * Get detail information about a transaction
   *
   * @param txHash The transaction hash
   * @return {@link TransactionDto}
   */
  @GET("/rawtx/{txHash}") public Observable<TransactionDto> getTranasction(
      @Path("txHash") String txHash);

  /**
   * Posts a transaction to the bitcoin network
   *
   * @param t the transaction
   */
  public void postTransaction(TransactionDto t);
}
