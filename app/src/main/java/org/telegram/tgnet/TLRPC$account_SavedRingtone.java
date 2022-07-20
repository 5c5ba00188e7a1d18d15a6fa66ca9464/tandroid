package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$account_SavedRingtone extends TLObject {
    public static TLRPC$account_SavedRingtone TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$account_SavedRingtone tLRPC$account_SavedRingtone;
        if (i != -1222230163) {
            tLRPC$account_SavedRingtone = i != 523271863 ? null : new TLRPC$TL_account_savedRingtoneConverted();
        } else {
            tLRPC$account_SavedRingtone = new TLRPC$TL_account_savedRingtone();
        }
        if (tLRPC$account_SavedRingtone != null || !z) {
            if (tLRPC$account_SavedRingtone != null) {
                tLRPC$account_SavedRingtone.readParams(abstractSerializedData, z);
            }
            return tLRPC$account_SavedRingtone;
        }
        throw new RuntimeException(String.format("can't parse magic %x in account_SavedRingtone", Integer.valueOf(i)));
    }
}
