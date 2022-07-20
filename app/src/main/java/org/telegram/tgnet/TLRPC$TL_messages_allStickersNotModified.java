package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_allStickersNotModified extends TLRPC$messages_AllStickers {
    public static int constructor = -395967805;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
