package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_updatePaidReactionPrivacy extends TLRPC$Update {
    public boolean isPrivate;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.isPrivate = abstractSerializedData.readBool(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1372224236);
        abstractSerializedData.writeBool(this.isPrivate);
    }
}
