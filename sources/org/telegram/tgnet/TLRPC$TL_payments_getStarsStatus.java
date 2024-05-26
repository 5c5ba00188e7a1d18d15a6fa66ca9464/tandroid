package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_payments_getStarsStatus extends TLObject {
    public TLRPC$InputPeer peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_payments_starsStatus.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(273665959);
        this.peer.serializeToStream(abstractSerializedData);
    }
}
