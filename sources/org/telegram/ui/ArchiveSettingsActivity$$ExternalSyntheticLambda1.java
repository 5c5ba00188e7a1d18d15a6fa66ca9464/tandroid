package org.telegram.ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
/* loaded from: classes3.dex */
public final /* synthetic */ class ArchiveSettingsActivity$$ExternalSyntheticLambda1 implements RequestDelegate {
    public static final /* synthetic */ ArchiveSettingsActivity$$ExternalSyntheticLambda1 INSTANCE = new ArchiveSettingsActivity$$ExternalSyntheticLambda1();

    private /* synthetic */ ArchiveSettingsActivity$$ExternalSyntheticLambda1() {
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        ArchiveSettingsActivity.lambda$onFragmentDestroy$2(tLObject, tLRPC$TL_error);
    }
}
