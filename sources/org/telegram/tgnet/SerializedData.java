package org.telegram.tgnet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.NotificationCenter;
import org.telegram.tgnet.TLRPC;

/* loaded from: classes3.dex */
public class SerializedData extends AbstractSerializedData {
    private DataInputStream in;
    private ByteArrayInputStream inbuf;
    protected boolean isOut;
    private boolean justCalc;
    private int len;
    private DataOutputStream out;
    private ByteArrayOutputStream outbuf;

    public SerializedData() {
        this.isOut = true;
        this.justCalc = false;
        this.outbuf = new ByteArrayOutputStream();
        this.out = new DataOutputStream(this.outbuf);
    }

    public SerializedData(int i) {
        this.isOut = true;
        this.justCalc = false;
        this.outbuf = new ByteArrayOutputStream(i);
        this.out = new DataOutputStream(this.outbuf);
    }

    public SerializedData(File file) {
        this.isOut = true;
        this.justCalc = false;
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bArr = new byte[(int) file.length()];
        new DataInputStream(fileInputStream).readFully(bArr);
        fileInputStream.close();
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(bArr);
        this.in = new DataInputStream(this.inbuf);
    }

    public SerializedData(boolean z) {
        this.isOut = true;
        this.justCalc = false;
        if (!z) {
            this.outbuf = new ByteArrayOutputStream();
            this.out = new DataOutputStream(this.outbuf);
        }
        this.justCalc = z;
        this.len = 0;
    }

    public SerializedData(byte[] bArr) {
        this.justCalc = false;
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(bArr);
        this.in = new DataInputStream(this.inbuf);
        this.len = 0;
    }

    private void writeInt32(int i, DataOutputStream dataOutputStream) {
        for (int i2 = 0; i2 < 4; i2++) {
            try {
                dataOutputStream.write(i >> (i2 * 8));
            } catch (Exception e) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("write int32 error");
                    FileLog.e(e);
                    return;
                }
                return;
            }
        }
    }

    private void writeInt64(long j, DataOutputStream dataOutputStream) {
        for (int i = 0; i < 8; i++) {
            try {
                dataOutputStream.write((int) (j >> (i * 8)));
            } catch (Exception e) {
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("write int64 error");
                    FileLog.e(e);
                    return;
                }
                return;
            }
        }
    }

    public void cleanup() {
        try {
            ByteArrayInputStream byteArrayInputStream = this.inbuf;
            if (byteArrayInputStream != null) {
                byteArrayInputStream.close();
                this.inbuf = null;
            }
        } catch (Exception e) {
            FileLog.e(e);
        }
        try {
            DataInputStream dataInputStream = this.in;
            if (dataInputStream != null) {
                dataInputStream.close();
                this.in = null;
            }
        } catch (Exception e2) {
            FileLog.e(e2);
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = this.outbuf;
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
                this.outbuf = null;
            }
        } catch (Exception e3) {
            FileLog.e(e3);
        }
        try {
            DataOutputStream dataOutputStream = this.out;
            if (dataOutputStream != null) {
                dataOutputStream.close();
                this.out = null;
            }
        } catch (Exception e4) {
            FileLog.e(e4);
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public int getPosition() {
        return this.len;
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public int length() {
        return !this.justCalc ? this.isOut ? this.outbuf.size() : this.inbuf.available() : this.len;
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public boolean readBool(boolean z) {
        int readInt32 = readInt32(z);
        if (readInt32 == -1720552011) {
            return true;
        }
        if (readInt32 == -1132882121) {
            return false;
        }
        if (z) {
            throw new RuntimeException("Not bool value!");
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.e("Not bool value!");
        }
        return false;
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public byte readByte(boolean z) {
        try {
            byte readByte = this.in.readByte();
            this.len++;
            return readByte;
        } catch (Exception e) {
            if (z) {
                throw new RuntimeException("read byte error", e);
            }
            if (!BuildVars.LOGS_ENABLED) {
                return (byte) 0;
            }
            FileLog.e("read byte error");
            FileLog.e(e);
            return (byte) 0;
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public byte[] readByteArray(boolean z) {
        int i;
        try {
            int read = this.in.read();
            this.len++;
            if (read >= 254) {
                read = this.in.read() | (this.in.read() << 8) | (this.in.read() << 16);
                this.len += 3;
                i = 4;
            } else {
                i = 1;
            }
            byte[] bArr = new byte[read];
            this.in.read(bArr);
            this.len++;
            while ((read + i) % 4 != 0) {
                this.in.read();
                this.len++;
                i++;
            }
            return bArr;
        } catch (Exception e) {
            if (z) {
                throw new RuntimeException("read byte array error", e);
            }
            if (!BuildVars.LOGS_ENABLED) {
                return null;
            }
            FileLog.e("read byte array error");
            FileLog.e(e);
            return null;
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public NativeByteBuffer readByteBuffer(boolean z) {
        return null;
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void readBytes(byte[] bArr, boolean z) {
        try {
            this.in.read(bArr);
            this.len += bArr.length;
        } catch (Exception e) {
            if (z) {
                throw new RuntimeException("read bytes error", e);
            }
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("read bytes error");
                FileLog.e(e);
            }
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public byte[] readData(int i, boolean z) {
        byte[] bArr = new byte[i];
        readBytes(bArr, z);
        return bArr;
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public double readDouble(boolean z) {
        try {
            return Double.longBitsToDouble(readInt64(z));
        } catch (Exception e) {
            if (z) {
                throw new RuntimeException("read double error", e);
            }
            if (!BuildVars.LOGS_ENABLED) {
                return 0.0d;
            }
            FileLog.e("read double error");
            FileLog.e(e);
            return 0.0d;
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public float readFloat(boolean z) {
        try {
            return Float.intBitsToFloat(readInt32(z));
        } catch (Exception e) {
            if (z) {
                throw new RuntimeException("read float error", e);
            }
            if (!BuildVars.LOGS_ENABLED) {
                return 0.0f;
            }
            FileLog.e("read float error");
            FileLog.e(e);
            return 0.0f;
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public int readInt32(boolean z) {
        int i = 0;
        for (int i2 = 0; i2 < 4; i2++) {
            try {
                i |= this.in.read() << (i2 * 8);
                this.len++;
            } catch (Exception e) {
                if (z) {
                    throw new RuntimeException("read int32 error", e);
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("read int32 error");
                    FileLog.e(e);
                }
                return 0;
            }
        }
        return i;
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public long readInt64(boolean z) {
        long j = 0;
        for (int i = 0; i < 8; i++) {
            try {
                j |= this.in.read() << (i * 8);
                this.len++;
            } catch (Exception e) {
                if (z) {
                    throw new RuntimeException("read int64 error", e);
                }
                if (BuildVars.LOGS_ENABLED) {
                    FileLog.e("read int64 error");
                    FileLog.e(e);
                }
                return 0L;
            }
        }
        return j;
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public String readString(boolean z) {
        int i;
        try {
            int read = this.in.read();
            this.len++;
            if (read >= 254) {
                read = this.in.read() | (this.in.read() << 8) | (this.in.read() << 16);
                this.len += 3;
                i = 4;
            } else {
                i = 1;
            }
            byte[] bArr = new byte[read];
            this.in.read(bArr);
            this.len++;
            while ((read + i) % 4 != 0) {
                this.in.read();
                this.len++;
                i++;
            }
            return new String(bArr, "UTF-8");
        } catch (Exception e) {
            if (z) {
                throw new RuntimeException("read string error", e);
            }
            if (!BuildVars.LOGS_ENABLED) {
                return null;
            }
            FileLog.e("read string error");
            FileLog.e(e);
            return null;
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public int remaining() {
        try {
            return this.in.available();
        } catch (Exception unused) {
            return ConnectionsManager.DEFAULT_DATACENTER_ID;
        }
    }

    protected void set(byte[] bArr) {
        this.isOut = false;
        this.inbuf = new ByteArrayInputStream(bArr);
        this.in = new DataInputStream(this.inbuf);
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void skip(int i) {
        if (i == 0) {
            return;
        }
        if (this.justCalc) {
            this.len += i;
            return;
        }
        DataInputStream dataInputStream = this.in;
        if (dataInputStream != null) {
            try {
                dataInputStream.skipBytes(i);
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    public byte[] toByteArray() {
        return this.outbuf.toByteArray();
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeBool(boolean z) {
        if (this.justCalc) {
            this.len += 4;
        } else {
            writeInt32(z ? TLRPC.TL_boolTrue.constructor : TLRPC.TL_boolFalse.constructor);
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeByte(byte b) {
        try {
            if (this.justCalc) {
                this.len++;
            } else {
                this.out.writeByte(b);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write byte error");
                FileLog.e(e);
            }
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeByte(int i) {
        try {
            if (this.justCalc) {
                this.len++;
            } else {
                this.out.writeByte((byte) i);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write byte error");
                FileLog.e(e);
            }
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeByteArray(byte[] bArr) {
        int i;
        DataOutputStream dataOutputStream;
        int length;
        try {
            if (bArr.length <= 253) {
                if (this.justCalc) {
                    i = this.len + 1;
                    this.len = i;
                } else {
                    dataOutputStream = this.out;
                    length = bArr.length;
                    dataOutputStream.write(length);
                }
            } else if (this.justCalc) {
                i = this.len + 4;
                this.len = i;
            } else {
                this.out.write(NotificationCenter.notificationsCountUpdated);
                this.out.write(bArr.length);
                this.out.write(bArr.length >> 8);
                dataOutputStream = this.out;
                length = bArr.length >> 16;
                dataOutputStream.write(length);
            }
            if (this.justCalc) {
                this.len += bArr.length;
            } else {
                this.out.write(bArr);
            }
            for (int i2 = bArr.length <= 253 ? 1 : 4; (bArr.length + i2) % 4 != 0; i2++) {
                if (this.justCalc) {
                    this.len++;
                } else {
                    this.out.write(0);
                }
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write byte array error");
                FileLog.e(e);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x004b  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0053 A[Catch: Exception -> 0x0010, TryCatch #0 {Exception -> 0x0010, blocks: (B:4:0x0006, B:6:0x000a, B:7:0x003a, B:9:0x003e, B:13:0x004e, B:15:0x0053, B:17:0x0057, B:20:0x005e, B:27:0x0044, B:28:0x0012, B:29:0x0038, B:30:0x0016, B:32:0x001a, B:33:0x0035), top: B:2:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0044 A[Catch: Exception -> 0x0010, TryCatch #0 {Exception -> 0x0010, blocks: (B:4:0x0006, B:6:0x000a, B:7:0x003a, B:9:0x003e, B:13:0x004e, B:15:0x0053, B:17:0x0057, B:20:0x005e, B:27:0x0044, B:28:0x0012, B:29:0x0038, B:30:0x0016, B:32:0x001a, B:33:0x0035), top: B:2:0x0004 }] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x003e A[Catch: Exception -> 0x0010, TryCatch #0 {Exception -> 0x0010, blocks: (B:4:0x0006, B:6:0x000a, B:7:0x003a, B:9:0x003e, B:13:0x004e, B:15:0x0053, B:17:0x0057, B:20:0x005e, B:27:0x0044, B:28:0x0012, B:29:0x0038, B:30:0x0016, B:32:0x001a, B:33:0x0035), top: B:2:0x0004 }] */
    @Override // org.telegram.tgnet.AbstractSerializedData
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void writeByteArray(byte[] bArr, int i, int i2) {
        int i3;
        int i4;
        try {
            if (i2 > 253) {
                if (this.justCalc) {
                    i3 = this.len + 4;
                    this.len = i3;
                    if (this.justCalc) {
                    }
                    if (i2 > 253) {
                    }
                    while ((i2 + i4) % 4 != 0) {
                    }
                }
                this.out.write(NotificationCenter.notificationsCountUpdated);
                this.out.write(i2);
                this.out.write(i2 >> 8);
                this.out.write(i2 >> 16);
                if (this.justCalc) {
                }
                if (i2 > 253) {
                }
                while ((i2 + i4) % 4 != 0) {
                }
            }
            if (this.justCalc) {
                i3 = this.len + 1;
                this.len = i3;
                if (this.justCalc) {
                }
                if (i2 > 253) {
                }
                while ((i2 + i4) % 4 != 0) {
                }
            }
            this.out.write(i2);
            if (this.justCalc) {
                this.out.write(bArr, i, i2);
            } else {
                this.len += i2;
            }
            for (i4 = i2 > 253 ? 1 : 4; (i2 + i4) % 4 != 0; i4++) {
                if (this.justCalc) {
                    this.len++;
                } else {
                    this.out.write(0);
                }
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write byte array error");
                FileLog.e(e);
            }
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeByteBuffer(NativeByteBuffer nativeByteBuffer) {
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeBytes(byte[] bArr) {
        try {
            if (this.justCalc) {
                this.len += bArr.length;
            } else {
                this.out.write(bArr);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write raw error");
                FileLog.e(e);
            }
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeBytes(byte[] bArr, int i, int i2) {
        try {
            if (this.justCalc) {
                this.len += i2;
            } else {
                this.out.write(bArr, i, i2);
            }
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write bytes error");
                FileLog.e(e);
            }
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeDouble(double d) {
        try {
            writeInt64(Double.doubleToRawLongBits(d));
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write double error");
                FileLog.e(e);
            }
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeFloat(float f) {
        try {
            writeInt32(Float.floatToIntBits(f));
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write float error");
                FileLog.e(e);
            }
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeInt32(int i) {
        if (this.justCalc) {
            this.len += 4;
        } else {
            writeInt32(i, this.out);
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeInt64(long j) {
        if (this.justCalc) {
            this.len += 8;
        } else {
            writeInt64(j, this.out);
        }
    }

    @Override // org.telegram.tgnet.AbstractSerializedData
    public void writeString(String str) {
        try {
            writeByteArray(str.getBytes("UTF-8"));
        } catch (Exception e) {
            if (BuildVars.LOGS_ENABLED) {
                FileLog.e("write string error");
                FileLog.e(e);
            }
        }
    }
}
