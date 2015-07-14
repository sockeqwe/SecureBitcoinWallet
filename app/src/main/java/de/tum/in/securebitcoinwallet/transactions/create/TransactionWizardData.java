package de.tum.in.securebitcoinwallet.transactions.create;

import android.os.Parcel;
import android.os.Parcelable;
import com.hannesdorfmann.parcelableplease.annotation.ParcelablePlease;

/**
 * @author Hannes Dorfmann
 */
@ParcelablePlease
public class TransactionWizardData implements Parcelable {

  String receiverAddress;
  long satoshi;
  String reference;

  public String getReceiverAddress() {
    return receiverAddress;
  }

  public void setReceiverAddress(String receiverAddress) {
    this.receiverAddress = receiverAddress;
  }

  public long getSatoshi() {
    return satoshi;
  }

  public void setSatoshi(long satoshi) {
    this.satoshi = satoshi;
  }

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    TransactionWizardDataParcelablePlease.writeToParcel(this, dest, flags);
  }

  public static final Creator<TransactionWizardData> CREATOR = new Creator<TransactionWizardData>() {
    public TransactionWizardData createFromParcel(Parcel source) {
      TransactionWizardData target = new TransactionWizardData();
      TransactionWizardDataParcelablePlease.readFromParcel(target, source);
      return target;
    }

    public TransactionWizardData[] newArray(int size) {
      return new TransactionWizardData[size];
    }
  };
}
