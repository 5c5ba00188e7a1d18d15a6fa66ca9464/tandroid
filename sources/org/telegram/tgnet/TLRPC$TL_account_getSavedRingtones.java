package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_account_getSavedRingtones extends TLObject {
    public long hash;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$account_SavedRingtones.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-510647672);
        abstractSerializedData.writeInt64(this.hash);
    }
}
