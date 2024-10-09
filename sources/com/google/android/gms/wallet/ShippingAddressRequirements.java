package com.google.android.gms.wallet;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.ArrayList;

/* loaded from: classes.dex */
public final class ShippingAddressRequirements extends AbstractSafeParcelable {
    public static final Parcelable.Creator<ShippingAddressRequirements> CREATOR = new zzam();
    ArrayList zza;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ShippingAddressRequirements(ArrayList arrayList) {
        this.zza = arrayList;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int beginObjectHeader = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeStringList(parcel, 1, this.zza, false);
        SafeParcelWriter.finishObjectHeader(parcel, beginObjectHeader);
    }
}
