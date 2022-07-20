package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_attachMenuPeerTypeBroadcast extends TLRPC$AttachMenuPeerType {
    public static int constructor = 2080104188;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
