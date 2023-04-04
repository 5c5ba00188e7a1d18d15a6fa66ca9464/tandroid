package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$account_SavedRingtone extends TLObject {
    public static TLRPC$account_SavedRingtone TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$account_SavedRingtone tLRPC$TL_account_savedRingtoneConverted = i != -1222230163 ? i != 523271863 ? null : new TLRPC$TL_account_savedRingtoneConverted() : new TLRPC$account_SavedRingtone() { // from class: org.telegram.tgnet.TLRPC$TL_account_savedRingtone
            public static int constructor = -1222230163;

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(constructor);
            }
        };
        if (tLRPC$TL_account_savedRingtoneConverted == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in account_SavedRingtone", Integer.valueOf(i)));
        }
        if (tLRPC$TL_account_savedRingtoneConverted != null) {
            tLRPC$TL_account_savedRingtoneConverted.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_account_savedRingtoneConverted;
    }
}
