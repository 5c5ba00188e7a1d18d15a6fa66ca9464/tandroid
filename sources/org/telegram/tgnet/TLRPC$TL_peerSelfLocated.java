package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_peerSelfLocated extends TLRPC$PeerLocated {
    public int expires;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.expires = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-118740917);
        abstractSerializedData.writeInt32(this.expires);
    }
}
