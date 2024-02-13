package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messageActionBoostApply extends TLRPC$MessageAction {
    public int boosts;

    @Override // org.telegram.tgnet.TLObject
    public void readParams(AbstractSerializedData abstractSerializedData, boolean z) {
        this.boosts = abstractSerializedData.readInt32(z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-872240531);
        abstractSerializedData.writeInt32(this.boosts);
    }
}
