package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_stickers_removeStickerFromSet extends TLObject {
    public static int constructor = -143257775;
    public TLRPC$InputDocument sticker;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_StickerSet.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        this.sticker.serializeToStream(abstractSerializedData);
    }
}
