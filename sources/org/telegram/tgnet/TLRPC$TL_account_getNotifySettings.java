package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_getNotifySettings extends TLObject {
    public TLRPC$InputNotifyPeer peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$PeerNotifySettings.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(313765169);
        this.peer.serializeToStream(abstractSerializedData);
    }
}
