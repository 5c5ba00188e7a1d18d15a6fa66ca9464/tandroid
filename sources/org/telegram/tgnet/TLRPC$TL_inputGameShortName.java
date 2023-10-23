package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_inputGameShortName extends TLRPC$InputGame {
    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.bot_id = TLRPC$InputUser.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
        this.short_name = abstractSerializedData.readString(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1020139510);
        this.bot_id.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.short_name);
    }
}
