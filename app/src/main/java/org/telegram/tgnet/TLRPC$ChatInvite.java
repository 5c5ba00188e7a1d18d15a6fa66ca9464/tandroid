package org.telegram.tgnet;

import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class TLRPC$ChatInvite extends TLObject {
    public String about;
    public boolean broadcast;
    public boolean channel;
    public TLRPC$Chat chat;
    public int expires;
    public int flags;
    public boolean isPublic;
    public boolean megagroup;
    public ArrayList<TLRPC$User> participants = new ArrayList<>();
    public int participants_count;
    public TLRPC$Photo photo;
    public boolean request_needed;
    public String title;

    public static TLRPC$ChatInvite TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$ChatInvite tLRPC$ChatInvite;
        if (i == 806110401) {
            tLRPC$ChatInvite = new TLRPC$TL_chatInvite();
        } else if (i != 1516793212) {
            tLRPC$ChatInvite = i != 1634294960 ? null : new TLRPC$TL_chatInvitePeek();
        } else {
            tLRPC$ChatInvite = new TLRPC$TL_chatInviteAlready();
        }
        if (tLRPC$ChatInvite != null || !z) {
            if (tLRPC$ChatInvite != null) {
                tLRPC$ChatInvite.readParams(abstractSerializedData, z);
            }
            return tLRPC$ChatInvite;
        }
        throw new RuntimeException(String.format("can't parse magic %x in ChatInvite", Integer.valueOf(i)));
    }
}
