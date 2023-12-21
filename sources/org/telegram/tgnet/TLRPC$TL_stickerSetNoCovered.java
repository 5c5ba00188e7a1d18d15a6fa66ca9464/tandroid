package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_stickerSetNoCovered extends TLRPC$StickerSetCovered {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.set = TLRPC$StickerSet.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(2008112412);
        this.set.serializeToStream(abstractSerializedData);
    }
}
