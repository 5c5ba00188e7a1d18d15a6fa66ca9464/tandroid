package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_notifyChats extends TLRPC$NotifyPeer {
    public static int constructor = -1073230141;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
