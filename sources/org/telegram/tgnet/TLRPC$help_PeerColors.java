package org.telegram.tgnet;
/* loaded from: classes3.dex */
public abstract class TLRPC$help_PeerColors extends TLObject {
    public static TLRPC$help_PeerColors TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$help_PeerColors tLRPC$TL_help_peerColors = i != 16313608 ? i != 732034510 ? null : new TLRPC$help_PeerColors() { // from class: org.telegram.tgnet.TLRPC$TL_help_peerColorsNotModified
            @Override // org.telegram.tgnet.TLObject
            public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(732034510);
            }
        } : new TLRPC$TL_help_peerColors();
        if (tLRPC$TL_help_peerColors == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in help_PeerColors", Integer.valueOf(i)));
        }
        if (tLRPC$TL_help_peerColors != null) {
            tLRPC$TL_help_peerColors.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_help_peerColors;
    }
}
