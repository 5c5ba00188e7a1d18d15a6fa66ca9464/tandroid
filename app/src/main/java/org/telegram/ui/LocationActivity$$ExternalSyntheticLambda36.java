package org.telegram.ui;

import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.Components.ProximitySheet;
/* loaded from: classes3.dex */
public final /* synthetic */ class LocationActivity$$ExternalSyntheticLambda36 implements ProximitySheet.onRadiusPickerChange {
    public final /* synthetic */ LocationActivity f$0;
    public final /* synthetic */ TLRPC$User f$1;

    public /* synthetic */ LocationActivity$$ExternalSyntheticLambda36(LocationActivity locationActivity, TLRPC$User tLRPC$User) {
        this.f$0 = locationActivity;
        this.f$1 = tLRPC$User;
    }

    @Override // org.telegram.ui.Components.ProximitySheet.onRadiusPickerChange
    public final boolean run(boolean z, int i) {
        boolean lambda$openProximityAlert$23;
        lambda$openProximityAlert$23 = this.f$0.lambda$openProximityAlert$23(this.f$1, z, i);
        return lambda$openProximityAlert$23;
    }
}
