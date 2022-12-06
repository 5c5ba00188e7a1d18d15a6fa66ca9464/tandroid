package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_setDefaultHistoryTTL extends TLObject {
    public static int constructor = -1632299963;
    public int period;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
        abstractSerializedData.writeInt32(this.period);
    }
}
