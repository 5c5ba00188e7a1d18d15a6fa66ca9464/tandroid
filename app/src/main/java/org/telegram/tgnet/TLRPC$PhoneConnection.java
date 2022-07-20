package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$PhoneConnection extends TLObject {
    public int flags;
    public long id;
    public String ip;
    public String ipv6;
    public String password;
    public byte[] peer_tag;
    public int port;
    public boolean stun;
    public boolean tcp;
    public boolean turn;
    public String username;

    public static TLRPC$PhoneConnection TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$PhoneConnection tLRPC$PhoneConnection;
        if (i == -1665063993) {
            tLRPC$PhoneConnection = new TLRPC$TL_phoneConnection();
        } else {
            tLRPC$PhoneConnection = i != 1667228533 ? null : new TLRPC$TL_phoneConnectionWebrtc();
        }
        if (tLRPC$PhoneConnection != null || !z) {
            if (tLRPC$PhoneConnection != null) {
                tLRPC$PhoneConnection.readParams(abstractSerializedData, z);
            }
            return tLRPC$PhoneConnection;
        }
        throw new RuntimeException(String.format("can't parse magic %x in PhoneConnection", Integer.valueOf(i)));
    }
}
