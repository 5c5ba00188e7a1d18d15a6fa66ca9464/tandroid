package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$stories_CanApplyBoostResult extends TLObject {
    public static TLRPC$stories_CanApplyBoostResult TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$stories_CanApplyBoostResult tLRPC$TL_stories_canApplyBoostReplace = i != -1021889145 ? i != 1898726997 ? null : new TLRPC$TL_stories_canApplyBoostReplace() : new TLRPC$stories_CanApplyBoostResult() { // from class: org.telegram.tgnet.TLRPC$TL_stories_canApplyBoostOk
            public static int constructor = -1021889145;

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(constructor);
            }
        };
        if (tLRPC$TL_stories_canApplyBoostReplace == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in stories_CanApplyBoostResult", Integer.valueOf(i)));
        }
        if (tLRPC$TL_stories_canApplyBoostReplace != null) {
            tLRPC$TL_stories_canApplyBoostReplace.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_stories_canApplyBoostReplace;
    }
}
