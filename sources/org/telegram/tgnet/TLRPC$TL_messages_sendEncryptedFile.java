package org.telegram.tgnet;
/* loaded from: classes3.dex */
public class TLRPC$TL_messages_sendEncryptedFile extends TLObject {
    public NativeByteBuffer data;
    public TLRPC$InputEncryptedFile file;
    public int flags;
    public TLRPC$TL_inputEncryptedChat peer;
    public long random_id;
    public boolean silent;

    @Override // org.telegram.tgnet.TLObject
    public TLObject deserializeResponse(AbstractSerializedData abstractSerializedData, int i, boolean z) {
        return TLRPC$messages_SentEncryptedMessage.TLdeserialize(abstractSerializedData, i, z);
    }

    @Override // org.telegram.tgnet.TLObject
    public void freeResources() {
        NativeByteBuffer nativeByteBuffer = this.data;
        if (nativeByteBuffer != null) {
            nativeByteBuffer.reuse();
            this.data = null;
        }
    }

    @Override // org.telegram.tgnet.TLObject
    public void serializeToStream(AbstractSerializedData abstractSerializedData) {
        abstractSerializedData.writeInt32(1431914525);
        int i = this.silent ? this.flags | 1 : this.flags & (-2);
        this.flags = i;
        abstractSerializedData.writeInt32(i);
        this.peer.serializeToStream(abstractSerializedData);
        abstractSerializedData.writeInt64(this.random_id);
        abstractSerializedData.writeByteBuffer(this.data);
        this.file.serializeToStream(abstractSerializedData);
    }
}
