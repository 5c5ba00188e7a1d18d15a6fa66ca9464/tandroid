package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$account_EmailVerified extends TLObject {
    public static TLRPC$account_EmailVerified TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$account_EmailVerified tLRPC$TL_account_emailVerifiedLogin;
        if (i != -507835039) {
            tLRPC$TL_account_emailVerifiedLogin = i != 731303195 ? null : new TLRPC$account_EmailVerified() { // from class: org.telegram.tgnet.TLRPC$TL_account_emailVerified
                public String email;

                @Override // org.telegram.tgnet.TLObject
                public void readParams(AbstractSerializedData abstractSerializedData2, boolean z2) {
                    this.email = abstractSerializedData2.readString(z2);
                }

                @Override // org.telegram.tgnet.TLObject
                public void serializeToStream(AbstractSerializedData abstractSerializedData2) {
                    abstractSerializedData2.writeInt32(731303195);
                    abstractSerializedData2.writeString(this.email);
                }
            };
        } else {
            tLRPC$TL_account_emailVerifiedLogin = new TLRPC$TL_account_emailVerifiedLogin();
        }
        if (tLRPC$TL_account_emailVerifiedLogin == null && z) {
            throw new RuntimeException(String.format("can't parse magic %x in account_EmailVerified", Integer.valueOf(i)));
        }
        if (tLRPC$TL_account_emailVerifiedLogin != null) {
            tLRPC$TL_account_emailVerifiedLogin.readParams(abstractSerializedData, z);
        }
        return tLRPC$TL_account_emailVerifiedLogin;
    }
}
