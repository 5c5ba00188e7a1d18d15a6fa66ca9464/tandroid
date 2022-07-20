package org.telegram.tgnet;
/* loaded from: classes.dex */
public abstract class TLRPC$MessageUserVote extends TLObject {
    public int date;
    public long user_id;

    public static TLRPC$MessageUserVote TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$MessageUserVote tLRPC$MessageUserVote;
        if (i == -1973033641) {
            tLRPC$MessageUserVote = new TLRPC$TL_messageUserVoteMultiple();
        } else if (i == 886196148) {
            tLRPC$MessageUserVote = new TLRPC$TL_messageUserVote();
        } else {
            tLRPC$MessageUserVote = i != 1017491692 ? null : new TLRPC$TL_messageUserVoteInputOption();
        }
        if (tLRPC$MessageUserVote != null || !z) {
            if (tLRPC$MessageUserVote != null) {
                tLRPC$MessageUserVote.readParams(abstractSerializedData, z);
            }
            return tLRPC$MessageUserVote;
        }
        throw new RuntimeException(String.format("can't parse magic %x in MessageUserVote", Integer.valueOf(i)));
    }
}
