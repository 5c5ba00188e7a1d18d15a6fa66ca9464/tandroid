package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_createBusinessChatLink extends TLObject {
    public TLRPC$TL_inputBusinessChatLink link;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_businessChatLink.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-2007898482);
        this.link.serializeToStream(abstractSerializedData);
    }
}
