package org.telegram.messenger;

import java.util.ArrayList;
import java.util.Comparator;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaDataController$$ExternalSyntheticLambda124 implements Comparator {
    public final /* synthetic */ ArrayList f$0;

    public /* synthetic */ MediaDataController$$ExternalSyntheticLambda124(ArrayList arrayList) {
        this.f$0 = arrayList;
    }

    @Override // java.util.Comparator
    public final int compare(Object obj, Object obj2) {
        int lambda$reorderStickers$37;
        lambda$reorderStickers$37 = MediaDataController.lambda$reorderStickers$37(this.f$0, (TLRPC$TL_messages_stickerSet) obj, (TLRPC$TL_messages_stickerSet) obj2);
        return lambda$reorderStickers$37;
    }
}
