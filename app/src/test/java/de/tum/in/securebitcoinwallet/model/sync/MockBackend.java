package de.tum.in.securebitcoinwallet.model.sync;

import de.tum.in.securebitcoinwallet.model.BackendApi;
import de.tum.in.securebitcoinwallet.model.dto.AddressDto;
import de.tum.in.securebitcoinwallet.model.dto.TransactionDto;
import de.tum.in.securebitcoinwallet.model.exception.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * @author Hannes Dorfmann
 */
public class MockBackend implements BackendApi {

  Map<String, AddressDto> addressDtoMap = new HashMap<>();

  @Override public Observable<AddressDto> getAddress(@Path("address") String address,
      @Query("offset") int offset, @Query("limit") int limit) {

    AddressDto dto = addressDtoMap.get(address);
    if (dto == null) {
      return Observable.error(new NotFoundException());
    }
    return Observable.just(copyDto(dto, offset, limit));
  }

  @Override public Observable<TransactionDto> getTranasction(@Path("txHash") String txHash) {
    return null;
  }

  @Override public void postTransaction(TransactionDto t) {

  }

  private AddressDto copyDto(AddressDto dto, int offset, int limit) {
    AddressDto copy = new AddressDto();
    copy.setAddress(dto.getAddress());
    copy.setAmount(dto.getAmount());
    copy.setAddress(dto.getAddress());
    copy.setTotalReceived(dto.getTotalReceived());
    copy.setTotalSent(dto.getTotalSent());
    copy.setTransactions(dto.getTransactions()
        .subList(offset, Math.min(offset + limit, dto.getTransactions().size() - 1)));
    copy.setTransactionsCount(copy.getTransactions().size());
    return copy;
  }

  public void put(AddressDto dto) {
    addressDtoMap.put(dto.getAddress(), dto);
  }
}
