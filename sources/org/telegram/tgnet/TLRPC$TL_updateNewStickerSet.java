package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_updateNewStickerSet extends TLRPC$Update {
    public TLRPC$TL_messages_stickerSet stickerset;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.stickerset = TLRPC$messages_StickerSet.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1753886890);
        this.stickerset.serializeToStream(abstractSerializedData);
    }
}
