package org.telegram.tgnet;
/* loaded from: classes3.dex */
public abstract class TLRPC$DialogPeer extends TLObject {
    public static TLRPC$DialogPeer TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$DialogPeer tLRPC$TL_dialogPeer = i != -445792507 ? i != 1363483106 ? null : new TLRPC$DialogPeer() { // from class: org.telegram.tgnet.TLRPC$TL_dialogPeerFolder
            public int folder_id;

            @Override // org.telegram.tgnet.TLObject
            public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                this.folder_id = abstractSerializedData2.readInt32(z2);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(1363483106);
                abstractSerializedData2.writeInt32(this.folder_id);
            }
        } : new TLRPC$TL_dialogPeer();
        if (tLRPC$TL_dialogPeer == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in DialogPeer", Integer.valueOf(i)));
        }
        if (tLRPC$TL_dialogPeer != null) {
            tLRPC$TL_dialogPeer.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_dialogPeer;
    }
}
