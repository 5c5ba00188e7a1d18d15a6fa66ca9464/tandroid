package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_help_getPeerProfileColors extends TLObject {
    public int hash;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$help_PeerColors.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1412453891);
        abstractSerializedData.writeInt32(this.hash);
    }
}
