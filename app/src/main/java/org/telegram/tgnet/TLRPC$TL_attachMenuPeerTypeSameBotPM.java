package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_attachMenuPeerTypeSameBotPM extends TLRPC$AttachMenuPeerType {
    public static int constructor = 2104224014;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
