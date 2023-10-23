package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_setChatTheme extends TLObject {
    public String emoticon;
    public TLRPC$InputPeer peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-432283329);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.emoticon);
    }
}
