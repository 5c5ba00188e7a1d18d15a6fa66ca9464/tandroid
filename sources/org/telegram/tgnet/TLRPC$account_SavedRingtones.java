package org.telegram.tgnet;
/* loaded from: classes3.dex */
public abstract class TLRPC$account_SavedRingtones extends TLObject {
    public static TLRPC$account_SavedRingtones TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$account_SavedRingtones tLRPC$TL_account_savedRingtones = i != -1041683259 ? i != -67704655 ? null : new TLRPC$account_SavedRingtones() { // from class: org.telegram.tgnet.TLRPC$TL_account_savedRingtonesNotModified
            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(-67704655);
            }
        } : new TLRPC$TL_account_savedRingtones();
        if (tLRPC$TL_account_savedRingtones == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in account_SavedRingtones", Integer.valueOf(i)));
        }
        if (tLRPC$TL_account_savedRingtones != null) {
            tLRPC$TL_account_savedRingtones.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_account_savedRingtones;
    }
}
