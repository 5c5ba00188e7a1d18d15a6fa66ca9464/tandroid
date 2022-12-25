package com.google.android.exoplayer2.extractor.flv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.DummyTrackOutput;
import com.google.android.exoplayer2.util.ParsableByteArray;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
/* loaded from: classes.dex */
final class ScriptTagPayloadReader extends TagPayloadReader {
    private long durationUs;

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean parseHeader(ParsableByteArray parsableByteArray) {
        return true;
    }

    public ScriptTagPayloadReader() {
        super(new DummyTrackOutput());
        this.durationUs = -9223372036854775807L;
    }

    public long getDurationUs() {
        return this.durationUs;
    }

    @Override // com.google.android.exoplayer2.extractor.flv.TagPayloadReader
    protected boolean parsePayload(ParsableByteArray parsableByteArray, long j) throws ParserException {
        if (readAmfType(parsableByteArray) != 2) {
            throw new ParserException();
        }
        if ("onMetaData".equals(readAmfString(parsableByteArray)) && readAmfType(parsableByteArray) == 8) {
            HashMap<String, Object> readAmfEcmaArray = readAmfEcmaArray(parsableByteArray);
            if (readAmfEcmaArray.containsKey("duration")) {
                double doubleValue = ((Double) readAmfEcmaArray.get("duration")).doubleValue();
                if (doubleValue > 0.0d) {
                    this.durationUs = (long) (doubleValue * 1000000.0d);
                }
            }
            return false;
        }
        return false;
    }

    private static int readAmfType(ParsableByteArray parsableByteArray) {
        return parsableByteArray.readUnsignedByte();
    }

    private static Boolean readAmfBoolean(ParsableByteArray parsableByteArray) {
        return Boolean.valueOf(parsableByteArray.readUnsignedByte() == 1);
    }

    private static Double readAmfDouble(ParsableByteArray parsableByteArray) {
        return Double.valueOf(Double.longBitsToDouble(parsableByteArray.readLong()));
    }

    private static String readAmfString(ParsableByteArray parsableByteArray) {
        int readUnsignedShort = parsableByteArray.readUnsignedShort();
        int position = parsableByteArray.getPosition();
        parsableByteArray.skipBytes(readUnsignedShort);
        return new String(parsableByteArray.data, position, readUnsignedShort);
    }

    private static ArrayList<Object> readAmfStrictArray(ParsableByteArray parsableByteArray) {
        int readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
        ArrayList<Object> arrayList = new ArrayList<>(readUnsignedIntToInt);
        for (int i = 0; i < readUnsignedIntToInt; i++) {
            Object readAmfData = readAmfData(parsableByteArray, readAmfType(parsableByteArray));
            if (readAmfData != null) {
                arrayList.add(readAmfData);
            }
        }
        return arrayList;
    }

    private static HashMap<String, Object> readAmfObject(ParsableByteArray parsableByteArray) {
        HashMap<String, Object> hashMap = new HashMap<>();
        while (true) {
            String readAmfString = readAmfString(parsableByteArray);
            int readAmfType = readAmfType(parsableByteArray);
            if (readAmfType == 9) {
                return hashMap;
            }
            Object readAmfData = readAmfData(parsableByteArray, readAmfType);
            if (readAmfData != null) {
                hashMap.put(readAmfString, readAmfData);
            }
        }
    }

    private static HashMap<String, Object> readAmfEcmaArray(ParsableByteArray parsableByteArray) {
        int readUnsignedIntToInt = parsableByteArray.readUnsignedIntToInt();
        HashMap<String, Object> hashMap = new HashMap<>(readUnsignedIntToInt);
        for (int i = 0; i < readUnsignedIntToInt; i++) {
            String readAmfString = readAmfString(parsableByteArray);
            Object readAmfData = readAmfData(parsableByteArray, readAmfType(parsableByteArray));
            if (readAmfData != null) {
                hashMap.put(readAmfString, readAmfData);
            }
        }
        return hashMap;
    }

    private static Date readAmfDate(ParsableByteArray parsableByteArray) {
        Date date = new Date((long) readAmfDouble(parsableByteArray).doubleValue());
        parsableByteArray.skipBytes(2);
        return date;
    }

    private static Object readAmfData(ParsableByteArray parsableByteArray, int i) {
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        if (i != 8) {
                            if (i != 10) {
                                if (i != 11) {
                                    return null;
                                }
                                return readAmfDate(parsableByteArray);
                            }
                            return readAmfStrictArray(parsableByteArray);
                        }
                        return readAmfEcmaArray(parsableByteArray);
                    }
                    return readAmfObject(parsableByteArray);
                }
                return readAmfString(parsableByteArray);
            }
            return readAmfBoolean(parsableByteArray);
        }
        return readAmfDouble(parsableByteArray);
    }
}
