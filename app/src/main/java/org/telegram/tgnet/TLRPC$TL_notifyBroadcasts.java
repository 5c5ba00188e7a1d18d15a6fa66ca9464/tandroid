package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_notifyBroadcasts extends TLRPC$NotifyPeer {
    public static int constructor = -703403793;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
