package org.telegram.tgnet;

import java.util.ArrayList;
/* loaded from: classes3.dex */
public class TLRPC$TL_channels_getForumTopicsByID extends TLObject {
    public TLRPC$InputChannel channel;
    public ArrayList topics = new ArrayList();

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$TL_messages_forumTopics.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(-1333584199);
        this.channel.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt32(481674261);
        int size = this.topics.size();
        abstractSerializedData.writeInt32(size);
        for (int i = 0; i < size; i++) {
            abstractSerializedData.writeInt32(((Integer) this.topics.get(i)).intValue());
        }
    }
}
