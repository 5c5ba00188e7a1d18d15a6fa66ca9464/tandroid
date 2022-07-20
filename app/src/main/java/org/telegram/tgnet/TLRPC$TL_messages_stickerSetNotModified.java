package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_stickerSetNotModified extends TLRPC$TL_messages_stickerSet {
    public static int constructor = -738646805;

    @Override // org.telegram.tgnet.TLRPC$TL_messages_stickerSet, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
