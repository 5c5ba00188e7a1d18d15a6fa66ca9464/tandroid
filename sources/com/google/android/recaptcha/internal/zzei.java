package com.google.android.recaptcha.internal;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/* loaded from: classes.dex */
public final class zzei implements zzen {
    private final Context zzb;

    public zzei(Context context) {
        this.zzb = context;
    }

    @Override // com.google.android.recaptcha.internal.zzen
    public final /* synthetic */ Object cs(Object[] objArr) {
        return zzel.zza(this, objArr);
    }

    @Override // com.google.android.recaptcha.internal.zzen
    public final Object zza(Object... objArr) {
        Intent registerReceiver = this.zzb.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        if (registerReceiver == null) {
            throw new zzae(7, 19, null);
        }
        int intExtra = registerReceiver.getIntExtra("health", -1);
        int intExtra2 = registerReceiver.getIntExtra("level", -1);
        int intExtra3 = registerReceiver.getIntExtra("plugged", -1);
        boolean booleanExtra = registerReceiver.getBooleanExtra("present", false);
        int intExtra4 = registerReceiver.getIntExtra("scale", -1);
        int intExtra5 = registerReceiver.getIntExtra("status", -1);
        String stringExtra = registerReceiver.getStringExtra("technology");
        if (stringExtra == null) {
            stringExtra = "";
        }
        return new Object[]{Integer.valueOf(intExtra), Integer.valueOf(intExtra2), Integer.valueOf(intExtra3), Boolean.valueOf(booleanExtra), Integer.valueOf(intExtra4), Integer.valueOf(intExtra5), stringExtra, Integer.valueOf(registerReceiver.getIntExtra("temperature", -1)), Integer.valueOf(registerReceiver.getIntExtra("voltage", -1))};
    }
}
