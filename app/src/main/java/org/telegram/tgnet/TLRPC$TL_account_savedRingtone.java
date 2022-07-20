package org.telegram.tgnet;
/* loaded from: classes.dex */
public class TLRPC$TL_account_savedRingtone extends TLRPC$account_SavedRingtone {
    public static int constructor = -1222230163;

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(constructor);
    }
}
