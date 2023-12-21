package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_editChatAbout extends TLObject {
    public String about;
    public TLRPC$InputPeer peer;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-554301545);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeString(this.about);
    }
}
