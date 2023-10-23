package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$contacts_TopPeers extends TLObject {
    public static TLRPC$contacts_TopPeers TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$contacts_TopPeers tLRPC$contacts_TopPeers;
        if (i == -1255369827) {
            tLRPC$contacts_TopPeers = new TLRPC$contacts_TopPeers() { // from class: org.telegram.tgnet.TLRPC$TL_contacts_topPeersDisabled
                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                    abstractSerializedData2.writeInt32(-1255369827);
                }
            };
        } else if (i != -567906571) {
            tLRPC$contacts_TopPeers = i != 1891070632 ? null : new TLRPC$TL_contacts_topPeers();
        } else {
            tLRPC$contacts_TopPeers = new TLRPC$contacts_TopPeers() { // from class: org.telegram.tgnet.TLRPC$TL_contacts_topPeersNotModified
                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                    abstractSerializedData2.writeInt32(-567906571);
                }
            };
        }
        if (tLRPC$contacts_TopPeers == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in contacts_TopPeers", Integer.valueOf(i)));
        }
        if (tLRPC$contacts_TopPeers != null) {
            tLRPC$contacts_TopPeers.readParams(abstractSerializedData, z);
        }
        return tLRPC$contacts_TopPeers;
    }
}
