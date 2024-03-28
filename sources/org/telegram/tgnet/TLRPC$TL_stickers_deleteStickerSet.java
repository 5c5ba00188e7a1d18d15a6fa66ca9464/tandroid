package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_stickers_deleteStickerSet extends TLObject {
    public TLRPC$InputStickerSet stickerset;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-2022685804);
        this.stickerset.serializeToStream(abstractSerializedData);
    }
}
