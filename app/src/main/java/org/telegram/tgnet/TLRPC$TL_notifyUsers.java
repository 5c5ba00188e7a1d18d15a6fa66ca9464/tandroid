package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_notifyUsers extends TLRPC$NotifyPeer {
    public static int constructor = -1261946036;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
