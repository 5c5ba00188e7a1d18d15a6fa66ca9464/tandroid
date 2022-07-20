package org.telegram.tgnet;

import java.util.ArrayList;
/* loaded from: classes.dex */
public abstract class TLRPC$MessageReactions extends TLObject {
    public boolean can_see_list;
    public int flags;
    public boolean min;
    public ArrayList<TLRPC$TL_reactionCount> results = new ArrayList<>();
    public ArrayList<TLRPC$TL_messagePeerReaction> recent_reactions = new ArrayList<>();

    public static TLRPC$TL_messageReactions TLdeserialize(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        TLRPC$TL_messageReactions tLRPC$TL_messageReactions;
        if (i == -1199954735) {
            tLRPC$TL_messageReactions = new TLRPC$TL_messageReactions_layer137();
        } else if (i == 142306870) {
            tLRPC$TL_messageReactions = new TLRPC$TL_messageReactionsOld();
        } else {
            tLRPC$TL_messageReactions = i != 1328256121 ? null : new TLRPC$TL_messageReactions();
        }
        if (tLRPC$TL_messageReactions != null || !z) {
            if (tLRPC$TL_messageReactions != null) {
                tLRPC$TL_messageReactions.readParams(abstractSerializedData, z);
            }
            return tLRPC$TL_messageReactions;
        }
        throw new RuntimeException(String.format("can't parse magic %x in MessageReactions", Integer.valueOf(i)));
    }
}
