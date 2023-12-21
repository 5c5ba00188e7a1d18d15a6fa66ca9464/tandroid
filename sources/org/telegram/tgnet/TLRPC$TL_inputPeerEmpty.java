package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_inputPeerEmpty extends TLRPC$InputPeer {
    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(2134579434);
    }
}
