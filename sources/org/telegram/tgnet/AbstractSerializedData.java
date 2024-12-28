package org.telegram.tgnet;

/* loaded from: classes3.dex */
public abstract class AbstractSerializedData implements InputSerializedData, OutputSerializedData {
    @Override // org.telegram.tgnet.InputSerializedData, org.telegram.tgnet.OutputSerializedData
    public abstract int getPosition();

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract int length();

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract boolean readBool(boolean z);

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract byte readByte(boolean z);

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract byte[] readByteArray(boolean z);

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract NativeByteBuffer readByteBuffer(boolean z);

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract void readBytes(byte[] bArr, boolean z);

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract byte[] readData(int i, boolean z);

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract double readDouble(boolean z);

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract float readFloat(boolean z);

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract int readInt32(boolean z);

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract long readInt64(boolean z);

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract String readString(boolean z);

    @Override // org.telegram.tgnet.InputSerializedData
    public abstract int remaining();

    @Override // org.telegram.tgnet.InputSerializedData, org.telegram.tgnet.OutputSerializedData
    public abstract void skip(int i);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeBool(boolean z);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeByte(byte b);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeByte(int i);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeByteArray(byte[] bArr);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeByteArray(byte[] bArr, int i, int i2);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeByteBuffer(NativeByteBuffer nativeByteBuffer);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeBytes(byte[] bArr);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeBytes(byte[] bArr, int i, int i2);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeDouble(double d);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeFloat(float f);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeInt32(int i);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeInt64(long j);

    @Override // org.telegram.tgnet.OutputSerializedData
    public abstract void writeString(String str);
}
