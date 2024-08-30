package org.telegram.tgnet;
/* loaded from: classes3.dex */
public abstract class TLRPC$InputUser extends TLObject {
    public long access_hash;
    public long user_id;

    public static TLRPC$InputUser TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$InputUser tLRPC$TL_inputUserSelf = i != -1182234929 ? i != -233744186 ? i != -138301121 ? i != 497305826 ? null : new TLRPC$InputUser() { // from class: org.telegram.tgnet.TLRPC$TL_inputUserFromMessage
            public int msg_id;
            public TLRPC$InputPeer peer;

            @Override // org.telegram.tgnet.TLObject
            public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                this.peer = TLRPC$InputPeer.TLdeserialize(abstractSerializedData2, abstractSerializedData2.readInt32(z2), z2);
                this.msg_id = abstractSerializedData2.readInt32(z2);
                this.user_id = abstractSerializedData2.readInt64(z2);
            }

            @Override // org.telegram.tgnet.TLObject
            public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                abstractSerializedData2.writeInt32(497305826);
                this.peer.serializeToStream(abstractSerializedData2);
                abstractSerializedData2.writeInt32(this.msg_id);
                abstractSerializedData2.writeInt64(this.user_id);
            }
        } : new TLRPC$TL_inputUserSelf() : new TLRPC$TL_inputUser() : new TLRPC$TL_inputUserEmpty();
        if (tLRPC$TL_inputUserSelf == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in InputUser", Integer.valueOf(i)));
        }
        if (tLRPC$TL_inputUserSelf != null) {
            tLRPC$TL_inputUserSelf.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_inputUserSelf;
    }
}
