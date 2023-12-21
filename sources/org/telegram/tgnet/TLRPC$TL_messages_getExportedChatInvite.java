package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_getExportedChatInvite extends TLObject {
    public String link;
    public TLRPC$InputPeer peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_ExportedChatInvite.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1937010524);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.link);
    }
}
