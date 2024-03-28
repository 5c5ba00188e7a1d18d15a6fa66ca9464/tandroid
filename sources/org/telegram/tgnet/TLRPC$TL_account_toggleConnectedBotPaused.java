package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_toggleConnectedBotPaused extends TLObject {
    public boolean paused;
    public TLRPC$InputPeer peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1684934807);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeBool(this.paused);
    }
}
