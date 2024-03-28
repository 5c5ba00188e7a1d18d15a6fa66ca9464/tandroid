package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_stickers_renameStickerSet extends TLObject {
    public TLRPC$InputStickerSet stickerset;
    public String title;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_StickerSet.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(306912256);
        this.stickerset.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.title);
    }
}
