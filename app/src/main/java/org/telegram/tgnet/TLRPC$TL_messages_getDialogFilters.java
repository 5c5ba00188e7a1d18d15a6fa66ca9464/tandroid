package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_messages_getDialogFilters extends TLObject {
    public static int constructor = -241247891;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$Vector tLRPC$Vector = new TLRPC$Vector();
        int readInt32 = abstractSerializedData.readInt32(z);
        for (int i2 = 0; i2 < readInt32; i2++) {
            TLRPC$DialogFilter TLdeserialize = TLRPC$DialogFilter.TLdeserialize(abstractSerializedData, abstractSerializedData.readInt32(z), z);
            if (TLdeserialize == null) {
                return tLRPC$Vector;
            }
            tLRPC$Vector.objects.add(TLdeserialize);
        }
        return tLRPC$Vector;
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
