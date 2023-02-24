package com.google.android.exoplayer2.extractor.mkv;

import com.google.android.exoplayer2.ParserException;
import com.google.android.exoplayer2.extractor.ExtractorInput;
import com.google.android.exoplayer2.util.Assertions;
import java.io.IOException;
import java.util.ArrayDeque;
/* loaded from: classes.dex */
final class DefaultEbmlReader implements EbmlReader {
    private long elementContentSize;
    private int elementId;
    private int elementState;
    private EbmlProcessor processor;
    private final byte[] scratch = new byte[8];
    private final ArrayDeque<MasterElement> masterElementsStack = new ArrayDeque<>();
    private final VarintReader varintReader = new VarintReader();

    @Override // com.google.android.exoplayer2.extractor.mkv.EbmlReader
    public void init(EbmlProcessor ebmlProcessor) {
        this.processor = ebmlProcessor;
    }

    @Override // com.google.android.exoplayer2.extractor.mkv.EbmlReader
    public void reset() {
        this.elementState = 0;
        this.masterElementsStack.clear();
        this.varintReader.reset();
    }

    @Override // com.google.android.exoplayer2.extractor.mkv.EbmlReader
    public boolean read(ExtractorInput extractorInput) throws IOException {
        Assertions.checkStateNotNull(this.processor);
        while (true) {
            MasterElement peek = this.masterElementsStack.peek();
            if (peek == null || extractorInput.getPosition() < peek.elementEndPosition) {
                if (this.elementState == 0) {
                    long readUnsignedVarint = this.varintReader.readUnsignedVarint(extractorInput, true, false, 4);
                    if (readUnsignedVarint == -2) {
                        readUnsignedVarint = maybeResyncToNextLevel1Element(extractorInput);
                    }
                    if (readUnsignedVarint == -1) {
                        return false;
                    }
                    this.elementId = (int) readUnsignedVarint;
                    this.elementState = 1;
                }
                if (this.elementState == 1) {
                    this.elementContentSize = this.varintReader.readUnsignedVarint(extractorInput, false, true, 8);
                    this.elementState = 2;
                }
                int elementType = this.processor.getElementType(this.elementId);
                if (elementType != 0) {
                    if (elementType == 1) {
                        long position = extractorInput.getPosition();
                        this.masterElementsStack.push(new MasterElement(this.elementId, this.elementContentSize + position));
                        this.processor.startMasterElement(this.elementId, position, this.elementContentSize);
                        this.elementState = 0;
                        return true;
                    } else if (elementType == 2) {
                        long j = this.elementContentSize;
                        if (j > 8) {
                            throw ParserException.createForMalformedContainer("Invalid integer size: " + this.elementContentSize, null);
                        }
                        this.processor.integerElement(this.elementId, readInteger(extractorInput, (int) j));
                        this.elementState = 0;
                        return true;
                    } else if (elementType == 3) {
                        long j2 = this.elementContentSize;
                        if (j2 > 2147483647L) {
                            throw ParserException.createForMalformedContainer("String element size: " + this.elementContentSize, null);
                        }
                        this.processor.stringElement(this.elementId, readString(extractorInput, (int) j2));
                        this.elementState = 0;
                        return true;
                    } else if (elementType == 4) {
                        this.processor.binaryElement(this.elementId, (int) this.elementContentSize, extractorInput);
                        this.elementState = 0;
                        return true;
                    } else if (elementType == 5) {
                        long j3 = this.elementContentSize;
                        if (j3 != 4 && j3 != 8) {
                            throw ParserException.createForMalformedContainer("Invalid float size: " + this.elementContentSize, null);
                        }
                        this.processor.floatElement(this.elementId, readFloat(extractorInput, (int) j3));
                        this.elementState = 0;
                        return true;
                    } else {
                        throw ParserException.createForMalformedContainer("Invalid element type " + elementType, null);
                    }
                }
                extractorInput.skipFully((int) this.elementContentSize);
                this.elementState = 0;
            } else {
                this.processor.endMasterElement(this.masterElementsStack.pop().elementId);
                return true;
            }
        }
    }

    private long maybeResyncToNextLevel1Element(ExtractorInput extractorInput) throws IOException {
        extractorInput.resetPeekPosition();
        while (true) {
            extractorInput.peekFully(this.scratch, 0, 4);
            int parseUnsignedVarintLength = VarintReader.parseUnsignedVarintLength(this.scratch[0]);
            if (parseUnsignedVarintLength != -1 && parseUnsignedVarintLength <= 4) {
                int assembleVarint = (int) VarintReader.assembleVarint(this.scratch, parseUnsignedVarintLength, false);
                if (this.processor.isLevel1Element(assembleVarint)) {
                    extractorInput.skipFully(parseUnsignedVarintLength);
                    return assembleVarint;
                }
            }
            extractorInput.skipFully(1);
        }
    }

    private long readInteger(ExtractorInput extractorInput, int i) throws IOException {
        extractorInput.readFully(this.scratch, 0, i);
        long j = 0;
        for (int i2 = 0; i2 < i; i2++) {
            j = (j << 8) | (this.scratch[i2] & 255);
        }
        return j;
    }

    private double readFloat(ExtractorInput extractorInput, int i) throws IOException {
        long readInteger = readInteger(extractorInput, i);
        if (i == 4) {
            return Float.intBitsToFloat((int) readInteger);
        }
        return Double.longBitsToDouble(readInteger);
    }

    private static String readString(ExtractorInput extractorInput, int i) throws IOException {
        if (i == 0) {
            return "";
        }
        byte[] bArr = new byte[i];
        extractorInput.readFully(bArr, 0, i);
        while (i > 0 && bArr[i - 1] == 0) {
            i--;
        }
        return new String(bArr, 0, i);
    }

    /* loaded from: classes.dex */
    private static final class MasterElement {
        private final long elementEndPosition;
        private final int elementId;

        private MasterElement(int i, long j) {
            this.elementId = i;
            this.elementEndPosition = j;
        }
    }
}
