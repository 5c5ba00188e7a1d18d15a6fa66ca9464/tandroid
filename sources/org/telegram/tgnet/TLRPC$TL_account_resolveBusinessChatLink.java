package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_account_resolveBusinessChatLink extends TLObject {
    public String slug;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_account_resolvedBusinessChatLinks.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1418913262);
        abstractSerializedData.writeString(this.slug);
    }
}
