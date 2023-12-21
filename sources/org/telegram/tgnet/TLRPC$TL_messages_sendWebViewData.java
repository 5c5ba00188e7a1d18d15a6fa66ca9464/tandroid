package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_sendWebViewData extends TLObject {
    public TLRPC$InputUser bot;
    public String button_text;
    public String data;
    public long random_id;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-603831608);
        this.bot.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt64(this.random_id);
        abstractSerializedData.writeString(this.button_text);
        abstractSerializedData.writeString(this.data);
    }
}
