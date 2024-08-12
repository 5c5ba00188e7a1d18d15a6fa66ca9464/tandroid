package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_setChatAvailableReactions extends TLObject {
    public TLRPC$ChatReactions available_reactions;
    public int flags;
    public boolean paid_enabled;
    public TLRPC$InputPeer peer;
    public int reactions_limit;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-2041895551);
        abstractSerializedData.writeInt32(this.flags);
        this.peer.serializeToStream(abstractSerializedData);
        this.available_reactions.serializeToStream(abstractSerializedData);
        if ((this.flags & 1) != 0) {
            abstractSerializedData.writeInt32(this.reactions_limit);
        }
        if ((this.flags & 2) != 0) {
            abstractSerializedData.writeBool(this.paid_enabled);
        }
    }
}
