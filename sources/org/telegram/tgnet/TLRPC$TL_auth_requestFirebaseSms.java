package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_auth_requestFirebaseSms extends TLObject {
    public int flags;
    public String ios_push_secret;
    public String phone_code_hash;
    public String phone_number;
    public String safety_net_token;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$Bool.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1991881904);
        abstractSerializedData.writeInt32(this.flags);
        abstractSerializedData.writeString(this.phone_number);
        abstractSerializedData.writeString(this.phone_code_hash);
        if ((this.flags & 1) != 0) {
            abstractSerializedData.writeString(this.safety_net_token);
        }
        if ((this.flags & 2) != 0) {
            abstractSerializedData.writeString(this.ios_push_secret);
        }
    }
}
