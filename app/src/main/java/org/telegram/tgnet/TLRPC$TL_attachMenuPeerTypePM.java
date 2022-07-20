package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_attachMenuPeerTypePM extends TLRPC$AttachMenuPeerType {
    public static int constructor = -247016673;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
