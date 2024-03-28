package org.telegram.tgnet.tl;

import org.telegram.tgnet.AbstractSerializedData;
import org.telegram.tgnet.TLObject;
/* loaded from: classes3.dex */
public class TL_fragment$TL_getCollectibleInfo extends TLObject {
    public TL_fragment$InputCollectible collectible;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TL_fragment$TL_collectibleInfo.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1105295942);
        this.collectible.serializeToStream(abstractSerializedData);
    }
}
