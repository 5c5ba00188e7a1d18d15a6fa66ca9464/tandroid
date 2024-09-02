package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_stickers_addStickerToSet extends TLObject {
    public TLRPC$TL_inputStickerSetItem sticker;
    public TLRPC$InputStickerSet stickerset;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_StickerSet.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-2041315650);
        this.stickerset.serializeToStream(abstractSerializedData);
        this.sticker.serializeToStream(abstractSerializedData);
    }
}
