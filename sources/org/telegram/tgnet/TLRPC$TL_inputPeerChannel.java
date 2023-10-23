package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputPeerChannel extends TLRPC$InputPeer {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.channel_id = abstractSerializedData.readInt64(z);
        this.access_hash = abstractSerializedData.readInt64(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(666680316);
        abstractSerializedData.writeInt64(this.channel_id);
        abstractSerializedData.writeInt64(this.access_hash);
    }
}
