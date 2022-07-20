package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_account_savedRingtonesNotModified extends TLRPC$account_SavedRingtones {
    public static int constructor = -67704655;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
