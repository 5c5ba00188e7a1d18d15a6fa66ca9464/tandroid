package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_updateSavedReactionTag extends TLObject {
    public int flags;
    public TLRPC$Reaction reaction;
    public String title;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1613331948);
        abstractSerializedData.writeInt32(this.flags);
        this.reaction.serializeToStream(abstractSerializedData);
        if ((this.flags & 1) != 0) {
            abstractSerializedData.writeString(this.title);
        }
    }
}
