package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_editBusinessChatLink extends TLObject {
    public TLRPC$TL_inputBusinessChatLink link;
    public String slug;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_businessChatLink.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1942744913);
        abstractSerializedData.writeString(this.slug);
        this.link.serializeToStream(abstractSerializedData);
    }
}
