package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_bots_canSendMessage extends TLObject {
    public TLRPC$InputUser bot;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(324662502);
        this.bot.serializeToStream(abstractSerializedData);
    }
}
