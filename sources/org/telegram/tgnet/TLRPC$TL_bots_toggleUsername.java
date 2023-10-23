package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_bots_toggleUsername extends TLObject {
    public boolean active;
    public TLRPC$InputUser bot;
    public String username;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(87861619);
        this.bot.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.username);
        abstractSerializedData.writeBool(this.active);
    }
}
