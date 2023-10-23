package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_payments_launchPrepaidGiveaway extends TLObject {
    public static int constructor = 1609928480;
    public long giveaway_id;
    public TLRPC$InputPeer peer;
    public TLRPC$InputStorePaymentPurpose purpose;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Updates.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt64(this.giveaway_id);
        this.purpose.serializeToStream(abstractSerializedData);
    }
}
