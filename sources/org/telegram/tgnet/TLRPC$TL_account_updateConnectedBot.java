package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_updateConnectedBot extends TLObject {
    public static int constructor = 1138250269;
    public TLRPC$InputUser bot;
    public boolean can_reply;
    public boolean deleted;
    public int flags;
    public TLRPC$TL_inputBusinessBotRecipients recipients;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        int i = this.can_reply ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        int i2 = this.deleted ? i | 2 : i & (-3);
        this.flags = i2;
        abstractSerializedData.writeInt32(i2);
        this.bot.serializeToStream(abstractSerializedData);
        this.recipients.serializeToStream(abstractSerializedData);
    }
}
