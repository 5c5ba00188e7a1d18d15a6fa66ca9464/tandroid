package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_toggleDialogFilterTags extends TLObject {
    public boolean enabled;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-47326647);
        abstractSerializedData.writeBool(this.enabled);
    }
}
