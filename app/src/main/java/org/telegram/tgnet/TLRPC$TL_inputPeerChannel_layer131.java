package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputPeerChannel_layer131 extends TLRPC$TL_inputPeerChannel {
    public static int constructor = 548253432;

    @Override // org.telegram.tgnet.TLRPC$TL_inputPeerChannel, org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.channel_id = abstractSerializedData.readInt32(z);
        this.access_hash = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLRPC$TL_inputPeerChannel, org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32((int) this.channel_id);
        abstractSerializedData.writeInt64(this.access_hash);
    }
}
