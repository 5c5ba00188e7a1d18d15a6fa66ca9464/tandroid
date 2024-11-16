package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.IBinder;
import android.os.Parcel;

/* loaded from: classes.dex */
public final class zzv extends com.google.android.gms.internal.common.zza implements IAccountAccessor {
    zzv(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IAccountAccessor");
    }

    @Override // com.google.android.gms.common.internal.IAccountAccessor
    public final Account zzb() {
        Parcel zzB = zzB(2, zza());
        Account account = (Account) com.google.android.gms.internal.common.zzc.zza(zzB, Account.CREATOR);
        zzB.recycle();
        return account;
    }
}
