package org.telegram.tgnet;

import org.telegram.tgnet.TLRPC;
import org.telegram.tgnet.Vector;

/* loaded from: classes3.dex */
public final /* synthetic */ class TLRPC$TL_attachMenuBot$$ExternalSyntheticLambda1 implements Vector.TLDeserializer {
    @Override // org.telegram.tgnet.Vector.TLDeserializer
    public final TLObject deserialize(InputSerializedData inputSerializedData, int i, boolean z) {
        return TLRPC.TL_attachMenuBotIcon.TLdeserialize(inputSerializedData, i, z);
    }
}
