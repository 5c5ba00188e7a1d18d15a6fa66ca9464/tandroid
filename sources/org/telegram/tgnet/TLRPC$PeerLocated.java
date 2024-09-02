package org.telegram.tgnet;
/* loaded from: classes3.dex */
public abstract class TLRPC$PeerLocated extends TLObject {
    public static TLRPC$PeerLocated TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$PeerLocated tLRPC$PeerLocated = i != -901375139 ? i != -118740917 ? null : new TLRPC$PeerLocated() { // from class: org.telegram.tgnet.TLRPC$TL_peerSelfLocated
            public int expires;

            @Override // org.telegram.tgnet.TLObject
            public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                this.expires = abstractSerializedData2.readInt32(z2);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(-118740917);
                abstractSerializedData2.writeInt32(this.expires);
            }
        } : new TLRPC$PeerLocated() { // from class: org.telegram.tgnet.TLRPC$TL_peerLocated
            public int distance;
            public int expires;
            public TLRPC$Peer peer;

            @Override // org.telegram.tgnet.TLObject
            public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                this.peer = TLRPC$Peer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                this.expires = abstractSerializedData2.readInt32(z2);
                this.distance = abstractSerializedData2.readInt32(z2);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(-901375139);
                this.peer.serializeToStream(abstractSerializedData2);
                abstractSerializedData2.writeInt32(this.expires);
                abstractSerializedData2.writeInt32(this.distance);
            }
        };
        if (tLRPC$PeerLocated == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in PeerLocated", Integer.valueOf(i)));
        }
        if (tLRPC$PeerLocated != null) {
            tLRPC$PeerLocated.readParams(abstractSerializedData, z);
        }
        return tLRPC$PeerLocated;
    }
}
