package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputChatPhotoEmpty extends TLRPC$InputChatPhoto {
    public static int constructor = 480546647;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
