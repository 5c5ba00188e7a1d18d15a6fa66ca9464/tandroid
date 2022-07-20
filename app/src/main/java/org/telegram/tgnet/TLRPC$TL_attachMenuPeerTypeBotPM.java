package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_attachMenuPeerTypeBotPM extends TLRPC$AttachMenuPeerType {
    public static int constructor = -1020528102;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
