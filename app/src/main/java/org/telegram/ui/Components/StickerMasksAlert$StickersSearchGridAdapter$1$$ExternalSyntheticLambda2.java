package org.telegram.ui.Components;

import android.util.LongSparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC$TL_error;
import org.telegram.tgnet.TLRPC$TL_messages_getStickers;
import org.telegram.ui.Components.StickerMasksAlert;
/* loaded from: classes3.dex */
public final /* synthetic */ class StickerMasksAlert$StickersSearchGridAdapter$1$$ExternalSyntheticLambda2 implements RequestDelegate {
    public final /* synthetic */ StickerMasksAlert.StickersSearchGridAdapter.AnonymousClass1 f$0;
    public final /* synthetic */ TLRPC$TL_messages_getStickers f$1;
    public final /* synthetic */ ArrayList f$2;
    public final /* synthetic */ LongSparseArray f$3;

    public /* synthetic */ StickerMasksAlert$StickersSearchGridAdapter$1$$ExternalSyntheticLambda2(StickerMasksAlert.StickersSearchGridAdapter.AnonymousClass1 anonymousClass1, TLRPC$TL_messages_getStickers tLRPC$TL_messages_getStickers, ArrayList arrayList, LongSparseArray longSparseArray) {
        this.f$0 = anonymousClass1;
        this.f$1 = tLRPC$TL_messages_getStickers;
        this.f$2 = arrayList;
        this.f$3 = longSparseArray;
    }

    @Override // org.telegram.tgnet.RequestDelegate
    public final void run(TLObject tLObject, TLRPC$TL_error tLRPC$TL_error) {
        this.f$0.lambda$run$2(this.f$1, this.f$2, this.f$3, tLObject, tLRPC$TL_error);
    }
}
