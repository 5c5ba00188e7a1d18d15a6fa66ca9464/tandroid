package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$help_AppUpdate extends TLObject {
    public static TLRPC$help_AppUpdate TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$help_AppUpdate tLRPC$TL_help_appUpdate = i != -1000708810 ? i != -860107216 ? null : new TLRPC$TL_help_appUpdate() : new TLRPC$help_AppUpdate() { // from class: org.telegram.tgnet.TLRPC$TL_help_noAppUpdate
            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(-1000708810);
            }
        };
        if (tLRPC$TL_help_appUpdate == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in help_AppUpdate", Integer.valueOf(i)));
        }
        if (tLRPC$TL_help_appUpdate != null) {
            tLRPC$TL_help_appUpdate.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_help_appUpdate;
    }
}
