package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_stickers_replaceSticker extends TLObject {
    public TLRPC$TL_inputStickerSetItem new_sticker;
    public TLRPC$InputDocument sticker;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_StickerSet.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1184253338);
        this.sticker.serializeToStream(abstractSerializedData);
        this.new_sticker.serializeToStream(abstractSerializedData);
    }
}
