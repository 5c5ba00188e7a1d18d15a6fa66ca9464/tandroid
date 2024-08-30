package com.google.android.exoplayer2.extractor.avi;

import com.google.android.exoplayer2.util.ParsableByteArray;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
/* loaded from: classes.dex */
final class ListChunk implements AviChunk {
    public final ImmutableList children;
    private final int type;

    private ListChunk(int i, ImmutableList immutableList) {
        this.type = i;
        this.children = immutableList;
    }

    private static AviChunk createBox(int i, int i2, ParsableByteArray parsableByteArray) {
        if (i != 1718776947) {
            if (i != 1751742049) {
                if (i != 1752331379) {
                    if (i != 1852994675) {
                        return null;
                    }
                    return StreamNameChunk.parseFrom(parsableByteArray);
                }
                return AviStreamHeaderChunk.parseFrom(parsableByteArray);
            }
            return AviMainHeaderChunk.parseFrom(parsableByteArray);
        }
        return StreamFormatChunk.parseFrom(i2, parsableByteArray);
    }

    public static ListChunk parseFrom(int i, ParsableByteArray parsableByteArray) {
        ImmutableList.Builder builder = new ImmutableList.Builder();
        int limit = parsableByteArray.limit();
        int i2 = -2;
        while (parsableByteArray.bytesLeft() > 8) {
            int readLittleEndianInt = parsableByteArray.readLittleEndianInt();
            int position = parsableByteArray.getPosition() + parsableByteArray.readLittleEndianInt();
            parsableByteArray.setLimit(position);
            AviChunk parseFrom = readLittleEndianInt == 1414744396 ? parseFrom(parsableByteArray.readLittleEndianInt(), parsableByteArray) : createBox(readLittleEndianInt, i2, parsableByteArray);
            if (parseFrom != null) {
                if (parseFrom.getType() == 1752331379) {
                    i2 = ((AviStreamHeaderChunk) parseFrom).getTrackType();
                }
                builder.add((Object) parseFrom);
            }
            parsableByteArray.setPosition(position);
            parsableByteArray.setLimit(limit);
        }
        return new ListChunk(i, builder.build());
    }

    public AviChunk getChild(Class cls) {
        UnmodifiableIterator it = this.children.iterator();
        while (it.hasNext()) {
            AviChunk aviChunk = (AviChunk) it.next();
            if (aviChunk.getClass() == cls) {
                return aviChunk;
            }
        }
        return null;
    }

    @Override // com.google.android.exoplayer2.extractor.avi.AviChunk
    public int getType() {
        return this.type;
    }
}
