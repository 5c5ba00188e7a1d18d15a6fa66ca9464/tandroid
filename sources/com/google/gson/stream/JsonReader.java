package com.google.gson.stream;

import com.google.gson.Strictness;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.TroubleshootingGuide;
import com.google.gson.internal.bind.JsonTreeReader;
import java.io.Closeable;
import java.io.EOFException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes.dex */
public class JsonReader implements Closeable {
    private final Reader in;
    private int[] pathIndices;
    private String[] pathNames;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int[] stack;
    private Strictness strictness = Strictness.LEGACY_STRICT;
    private final char[] buffer = new char[1024];
    private int pos = 0;
    private int limit = 0;
    private int lineNumber = 0;
    private int lineStart = 0;
    int peeked = 0;
    private int stackSize = 1;

    static {
        JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() { // from class: com.google.gson.stream.JsonReader.1
            @Override // com.google.gson.internal.JsonReaderInternalAccess
            public void promoteNameToValue(JsonReader jsonReader) {
                int i;
                if (jsonReader instanceof JsonTreeReader) {
                    ((JsonTreeReader) jsonReader).promoteNameToValue();
                    return;
                }
                int i2 = jsonReader.peeked;
                if (i2 == 0) {
                    i2 = jsonReader.doPeek();
                }
                if (i2 == 13) {
                    i = 9;
                } else if (i2 == 12) {
                    i = 8;
                } else {
                    if (i2 != 14) {
                        throw jsonReader.unexpectedTokenError("a name");
                    }
                    i = 10;
                }
                jsonReader.peeked = i;
            }
        };
    }

    public JsonReader(Reader reader) {
        int[] iArr = new int[32];
        this.stack = iArr;
        iArr[0] = 6;
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
        Objects.requireNonNull(reader, "in == null");
        this.in = reader;
    }

    private void checkLenient() {
        if (this.strictness != Strictness.LENIENT) {
            throw syntaxError("Use JsonReader.setStrictness(Strictness.LENIENT) to accept malformed JSON");
        }
    }

    private void consumeNonExecutePrefix() {
        nextNonWhitespace(true);
        int i = this.pos;
        this.pos = i - 1;
        if (i + 4 <= this.limit || fillBuffer(5)) {
            int i2 = this.pos;
            char[] cArr = this.buffer;
            if (cArr[i2] == ')' && cArr[i2 + 1] == ']' && cArr[i2 + 2] == '}' && cArr[i2 + 3] == '\'' && cArr[i2 + 4] == '\n') {
                this.pos = i2 + 5;
            }
        }
    }

    private boolean fillBuffer(int i) {
        int i2;
        int i3;
        char[] cArr = this.buffer;
        int i4 = this.lineStart;
        int i5 = this.pos;
        this.lineStart = i4 - i5;
        int i6 = this.limit;
        if (i6 != i5) {
            int i7 = i6 - i5;
            this.limit = i7;
            System.arraycopy(cArr, i5, cArr, 0, i7);
        } else {
            this.limit = 0;
        }
        this.pos = 0;
        do {
            Reader reader = this.in;
            int i8 = this.limit;
            int read = reader.read(cArr, i8, cArr.length - i8);
            if (read == -1) {
                return false;
            }
            i2 = this.limit + read;
            this.limit = i2;
            if (this.lineNumber == 0 && (i3 = this.lineStart) == 0 && i2 > 0 && cArr[0] == 65279) {
                this.pos++;
                this.lineStart = i3 + 1;
                i++;
            }
        } while (i2 < i);
        return true;
    }

    private String getPath(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append('$');
        int i = 0;
        while (true) {
            int i2 = this.stackSize;
            if (i >= i2) {
                return sb.toString();
            }
            int i3 = this.stack[i];
            switch (i3) {
                case 1:
                case 2:
                    int i4 = this.pathIndices[i];
                    if (z && i4 > 0 && i == i2 - 1) {
                        i4--;
                    }
                    sb.append('[');
                    sb.append(i4);
                    sb.append(']');
                    break;
                case 3:
                case 4:
                case 5:
                    sb.append('.');
                    String str = this.pathNames[i];
                    if (str == null) {
                        break;
                    } else {
                        sb.append(str);
                        break;
                    }
                case 6:
                case 7:
                case 8:
                    break;
                default:
                    throw new AssertionError("Unknown scope value: " + i3);
            }
            i++;
        }
    }

    private boolean isLiteral(char c) {
        if (c == '\t' || c == '\n' || c == '\f' || c == '\r' || c == ' ') {
            return false;
        }
        if (c != '#') {
            if (c == ',') {
                return false;
            }
            if (c != '/' && c != '=') {
                if (c == '{' || c == '}' || c == ':') {
                    return false;
                }
                if (c != ';') {
                    switch (c) {
                        case '[':
                        case ']':
                            return false;
                        case '\\':
                            break;
                        default:
                            return true;
                    }
                }
            }
        }
        checkLenient();
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x0053, code lost:
    
        if (r5 != '/') goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0055, code lost:
    
        r8.pos = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0058, code lost:
    
        if (r4 != r2) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x005a, code lost:
    
        r8.pos = r1;
        r1 = fillBuffer(2);
        r8.pos++;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0065, code lost:
    
        if (r1 != false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0067, code lost:
    
        return r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0068, code lost:
    
        checkLenient();
        r1 = r8.pos;
        r2 = r0[r1];
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0071, code lost:
    
        if (r2 == '*') goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x007e, code lost:
    
        r8.pos = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0088, code lost:
    
        if (skipTo("*\/") == false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0095, code lost:
    
        throw syntaxError("Unterminated comment");
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0073, code lost:
    
        if (r2 == '/') goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x0076, code lost:
    
        r8.pos = r1 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0075, code lost:
    
        return r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0096, code lost:
    
        r8.pos = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x009a, code lost:
    
        if (r5 != '#') goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x009c, code lost:
    
        checkLenient();
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x00a0, code lost:
    
        return r5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int nextNonWhitespace(boolean z) {
        char[] cArr = this.buffer;
        while (true) {
            int i = this.pos;
            while (true) {
                int i2 = this.limit;
                while (true) {
                    if (i == i2) {
                        this.pos = i;
                        if (!fillBuffer(1)) {
                            if (!z) {
                                return -1;
                            }
                            throw new EOFException("End of input" + locationString());
                        }
                        i = this.pos;
                        i2 = this.limit;
                    }
                    int i3 = i + 1;
                    char c = cArr[i];
                    if (c != '\n') {
                        if (c != ' ' && c != '\r' && c != '\t') {
                            break;
                        }
                    } else {
                        this.lineNumber++;
                        this.lineStart = i3;
                    }
                    i = i3;
                }
                i = this.pos + 2;
            }
            skipToEndOfLine();
        }
    }

    private String nextQuotedValue(char c) {
        char[] cArr = this.buffer;
        StringBuilder sb = null;
        while (true) {
            int i = this.pos;
            int i2 = this.limit;
            while (true) {
                if (i < i2) {
                    int i3 = i + 1;
                    char c2 = cArr[i];
                    if (this.strictness == Strictness.STRICT && c2 < ' ') {
                        throw syntaxError("Unescaped control characters (\\u0000-\\u001F) are not allowed in strict mode");
                    }
                    if (c2 == c) {
                        this.pos = i3;
                        int i4 = (i3 - i) - 1;
                        if (sb == null) {
                            return new String(cArr, i, i4);
                        }
                        sb.append(cArr, i, i4);
                        return sb.toString();
                    }
                    if (c2 == '\\') {
                        this.pos = i3;
                        int i5 = i3 - i;
                        int i6 = i5 - 1;
                        if (sb == null) {
                            sb = new StringBuilder(Math.max(i5 * 2, 16));
                        }
                        sb.append(cArr, i, i6);
                        sb.append(readEscapeCharacter());
                    } else {
                        if (c2 == '\n') {
                            this.lineNumber++;
                            this.lineStart = i3;
                        }
                        i = i3;
                    }
                } else {
                    if (sb == null) {
                        sb = new StringBuilder(Math.max((i - i) * 2, 16));
                    }
                    sb.append(cArr, i, i - i);
                    this.pos = i;
                    if (!fillBuffer(1)) {
                        throw syntaxError("Unterminated string");
                    }
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:58:0x004a, code lost:
    
        checkLenient();
     */
    /* JADX WARN: Failed to find 'out' block for switch in B:54:0x0044. Please report as an issue. */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x008a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private String nextUnquotedValue() {
        String sb;
        StringBuilder sb2 = null;
        int i = 0;
        do {
            int i2 = 0;
            while (true) {
                int i3 = this.pos;
                if (i3 + i2 < this.limit) {
                    char c = this.buffer[i3 + i2];
                    if (c != '\t' && c != '\n' && c != '\f' && c != '\r' && c != ' ') {
                        if (c != '#') {
                            if (c != ',') {
                                if (c != '/' && c != '=') {
                                    if (c != '{' && c != '}' && c != ':') {
                                        if (c != ';') {
                                            switch (c) {
                                                case '[':
                                                case ']':
                                                    break;
                                                case '\\':
                                                    break;
                                                default:
                                                    i2++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else if (i2 >= this.buffer.length) {
                    if (sb2 == null) {
                        sb2 = new StringBuilder(Math.max(i2, 16));
                    }
                    sb2.append(this.buffer, this.pos, i2);
                    this.pos += i2;
                } else if (fillBuffer(i2 + 1)) {
                }
            }
            i = i2;
            if (sb2 != null) {
                sb = new String(this.buffer, this.pos, i);
            } else {
                sb2.append(this.buffer, this.pos, i);
                sb = sb2.toString();
            }
            this.pos += i;
            return sb;
        } while (fillBuffer(1));
        if (sb2 != null) {
        }
        this.pos += i;
        return sb;
    }

    private int peekKeyword() {
        String str;
        String str2;
        int i;
        char c = this.buffer[this.pos];
        if (c == 't' || c == 'T') {
            str = "true";
            str2 = "TRUE";
            i = 5;
        } else if (c == 'f' || c == 'F') {
            str = "false";
            str2 = "FALSE";
            i = 6;
        } else {
            if (c != 'n' && c != 'N') {
                return 0;
            }
            str = "null";
            str2 = "NULL";
            i = 7;
        }
        boolean z = this.strictness != Strictness.STRICT;
        int length = str.length();
        for (int i2 = 0; i2 < length; i2++) {
            if (this.pos + i2 >= this.limit && !fillBuffer(i2 + 1)) {
                return 0;
            }
            char c2 = this.buffer[this.pos + i2];
            if (c2 != str.charAt(i2) && (!z || c2 != str2.charAt(i2))) {
                return 0;
            }
        }
        if ((this.pos + length < this.limit || fillBuffer(length + 1)) && isLiteral(this.buffer[this.pos + length])) {
            return 0;
        }
        this.pos += length;
        this.peeked = i;
        return i;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0097, code lost:
    
        if (r9 != 2) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0099, code lost:
    
        if (r10 == false) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x009f, code lost:
    
        if (r11 != Long.MIN_VALUE) goto L61;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x00a1, code lost:
    
        if (r13 == false) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x00a7, code lost:
    
        if (r11 != 0) goto L64;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00a9, code lost:
    
        if (r13 != false) goto L70;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00ab, code lost:
    
        if (r13 == false) goto L66;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x00ae, code lost:
    
        r11 = -r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x00af, code lost:
    
        r18.peekedLong = r11;
        r18.pos += r8;
        r1 = 15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x00b8, code lost:
    
        r18.peeked = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00ba, code lost:
    
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00bb, code lost:
    
        if (r9 == 2) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00be, code lost:
    
        if (r9 == 4) goto L78;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00c1, code lost:
    
        if (r9 != 7) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00c4, code lost:
    
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x00c6, code lost:
    
        r18.peekedNumberLength = r8;
        r1 = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x0095, code lost:
    
        if (isLiteral(r14) != false) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x00cb, code lost:
    
        return 0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int peekNumber() {
        char[] cArr = this.buffer;
        int i = this.pos;
        int i2 = this.limit;
        int i3 = 0;
        int i4 = 0;
        char c = 0;
        boolean z = true;
        long j = 0;
        boolean z2 = false;
        while (true) {
            if (i + i4 == i2) {
                if (i4 == cArr.length) {
                    return i3;
                }
                if (!fillBuffer(i4 + 1)) {
                    break;
                }
                i = this.pos;
                i2 = this.limit;
            }
            char c2 = cArr[i + i4];
            if (c2 != '+') {
                if (c2 == 'E' || c2 == 'e') {
                    i3 = 0;
                    if (c != 2 && c != 4) {
                        return 0;
                    }
                    c = 5;
                } else if (c2 == '-') {
                    i3 = 0;
                    if (c == 0) {
                        c = 1;
                        z2 = true;
                    } else if (c != 5) {
                        return 0;
                    }
                } else if (c2 == '.') {
                    i3 = 0;
                    if (c != 2) {
                        return 0;
                    }
                    c = 3;
                } else {
                    if (c2 < '0' || c2 > '9') {
                        break;
                    }
                    if (c == 1 || c == 0) {
                        j = -(c2 - '0');
                        i3 = 0;
                        c = 2;
                    } else {
                        if (c == 2) {
                            if (j == 0) {
                                return 0;
                            }
                            long j2 = (10 * j) - (c2 - '0');
                            z &= j > -922337203685477580L || (j == -922337203685477580L && j2 < j);
                            j = j2;
                        } else if (c == 3) {
                            i3 = 0;
                            c = 4;
                        } else if (c == 5 || c == 6) {
                            i3 = 0;
                            c = 7;
                        }
                        i3 = 0;
                    }
                }
                i4++;
            } else {
                i3 = 0;
                if (c != 5) {
                    return 0;
                }
            }
            c = 6;
            i4++;
        }
    }

    private void push(int i) {
        int i2 = this.stackSize;
        int[] iArr = this.stack;
        if (i2 == iArr.length) {
            int i3 = i2 * 2;
            this.stack = Arrays.copyOf(iArr, i3);
            this.pathIndices = Arrays.copyOf(this.pathIndices, i3);
            this.pathNames = (String[]) Arrays.copyOf(this.pathNames, i3);
        }
        int[] iArr2 = this.stack;
        int i4 = this.stackSize;
        this.stackSize = i4 + 1;
        iArr2[i4] = i;
    }

    private char readEscapeCharacter() {
        int i;
        if (this.pos == this.limit && !fillBuffer(1)) {
            throw syntaxError("Unterminated escape sequence");
        }
        char[] cArr = this.buffer;
        int i2 = this.pos;
        int i3 = i2 + 1;
        this.pos = i3;
        char c = cArr[i2];
        if (c != '\n') {
            if (c != '\"') {
                if (c != '\'') {
                    if (c != '/' && c != '\\') {
                        if (c == 'b') {
                            return '\b';
                        }
                        if (c == 'f') {
                            return '\f';
                        }
                        if (c == 'n') {
                            return '\n';
                        }
                        if (c == 'r') {
                            return '\r';
                        }
                        if (c == 't') {
                            return '\t';
                        }
                        if (c != 'u') {
                            throw syntaxError("Invalid escape sequence");
                        }
                        if (i2 + 5 > this.limit && !fillBuffer(4)) {
                            throw syntaxError("Unterminated escape sequence");
                        }
                        int i4 = this.pos;
                        int i5 = i4 + 4;
                        int i6 = 0;
                        while (i4 < i5) {
                            char c2 = this.buffer[i4];
                            int i7 = i6 << 4;
                            if (c2 >= '0' && c2 <= '9') {
                                i = c2 - '0';
                            } else if (c2 >= 'a' && c2 <= 'f') {
                                i = c2 - 'W';
                            } else {
                                if (c2 < 'A' || c2 > 'F') {
                                    throw syntaxError("Malformed Unicode escape \\u" + new String(this.buffer, this.pos, 4));
                                }
                                i = c2 - '7';
                            }
                            i6 = i7 + i;
                            i4++;
                        }
                        this.pos += 4;
                        return (char) i6;
                    }
                }
            }
            return c;
        }
        if (this.strictness == Strictness.STRICT) {
            throw syntaxError("Cannot escape a newline character in strict mode");
        }
        this.lineNumber++;
        this.lineStart = i3;
        if (this.strictness == Strictness.STRICT) {
            throw syntaxError("Invalid escaped character \"'\" in strict mode");
        }
        return c;
    }

    private void skipQuotedValue(char c) {
        char[] cArr = this.buffer;
        while (true) {
            int i = this.pos;
            int i2 = this.limit;
            while (true) {
                if (i < i2) {
                    int i3 = i + 1;
                    char c2 = cArr[i];
                    if (c2 == c) {
                        this.pos = i3;
                        return;
                    }
                    if (c2 == '\\') {
                        this.pos = i3;
                        readEscapeCharacter();
                        break;
                    } else {
                        if (c2 == '\n') {
                            this.lineNumber++;
                            this.lineStart = i3;
                        }
                        i = i3;
                    }
                } else {
                    this.pos = i;
                    if (!fillBuffer(1)) {
                        throw syntaxError("Unterminated string");
                    }
                }
            }
        }
    }

    private boolean skipTo(String str) {
        int length = str.length();
        while (true) {
            if (this.pos + length > this.limit && !fillBuffer(length)) {
                return false;
            }
            char[] cArr = this.buffer;
            int i = this.pos;
            if (cArr[i] != '\n') {
                for (int i2 = 0; i2 < length; i2++) {
                    if (this.buffer[this.pos + i2] != str.charAt(i2)) {
                        break;
                    }
                }
                return true;
            }
            this.lineNumber++;
            this.lineStart = i + 1;
            this.pos++;
        }
    }

    private void skipToEndOfLine() {
        char c;
        do {
            if (this.pos >= this.limit && !fillBuffer(1)) {
                return;
            }
            char[] cArr = this.buffer;
            int i = this.pos;
            int i2 = i + 1;
            this.pos = i2;
            c = cArr[i];
            if (c == '\n') {
                this.lineNumber++;
                this.lineStart = i2;
                return;
            }
        } while (c != '\r');
    }

    /*  JADX ERROR: JadxRuntimeException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Failed to find switch 'out' block (already processed)
        	at jadx.core.dex.visitors.regions.RegionMaker.calcSwitchOut(RegionMaker.java:923)
        	at jadx.core.dex.visitors.regions.RegionMaker.processSwitch(RegionMaker.java:797)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:157)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:735)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:152)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:735)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:152)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:735)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:152)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:740)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:152)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:740)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:152)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:735)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:152)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processIf(RegionMaker.java:735)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:152)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeEndlessLoop(RegionMaker.java:411)
        	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:201)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:135)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMaker.processLoop(RegionMaker.java:242)
        	at jadx.core.dex.visitors.regions.RegionMaker.traverse(RegionMaker.java:135)
        	at jadx.core.dex.visitors.regions.RegionMaker.makeRegion(RegionMaker.java:91)
        	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
        */
    private void skipUnquotedValue() {
        /*
            r4 = this;
        L0:
            r0 = 0
        L1:
            int r1 = r4.pos
            int r2 = r1 + r0
            int r3 = r4.limit
            if (r2 >= r3) goto L51
            char[] r2 = r4.buffer
            int r1 = r1 + r0
            char r1 = r2[r1]
            r2 = 9
            if (r1 == r2) goto L4b
            r2 = 10
            if (r1 == r2) goto L4b
            r2 = 12
            if (r1 == r2) goto L4b
            r2 = 13
            if (r1 == r2) goto L4b
            r2 = 32
            if (r1 == r2) goto L4b
            r2 = 35
            if (r1 == r2) goto L48
            r2 = 44
            if (r1 == r2) goto L4b
            r2 = 47
            if (r1 == r2) goto L48
            r2 = 61
            if (r1 == r2) goto L48
            r2 = 123(0x7b, float:1.72E-43)
            if (r1 == r2) goto L4b
            r2 = 125(0x7d, float:1.75E-43)
            if (r1 == r2) goto L4b
            r2 = 58
            if (r1 == r2) goto L4b
            r2 = 59
            if (r1 == r2) goto L48
            switch(r1) {
                case 91: goto L4b;
                case 92: goto L48;
                case 93: goto L4b;
                default: goto L45;
            }
        L45:
            int r0 = r0 + 1
            goto L1
        L48:
            r4.checkLenient()
        L4b:
            int r1 = r4.pos
            int r1 = r1 + r0
            r4.pos = r1
            return
        L51:
            int r1 = r1 + r0
            r4.pos = r1
            r0 = 1
            boolean r0 = r4.fillBuffer(r0)
            if (r0 != 0) goto L0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.gson.stream.JsonReader.skipUnquotedValue():void");
    }

    private MalformedJsonException syntaxError(String str) {
        throw new MalformedJsonException(str + locationString() + "\nSee " + TroubleshootingGuide.createUrl("malformed-json"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IllegalStateException unexpectedTokenError(String str) {
        return new IllegalStateException("Expected " + str + " but was " + peek() + locationString() + "\nSee " + TroubleshootingGuide.createUrl(peek() == JsonToken.NULL ? "adapter-not-null-safe" : "unexpected-json-structure"));
    }

    public void beginArray() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i != 3) {
            throw unexpectedTokenError("BEGIN_ARRAY");
        }
        push(1);
        this.pathIndices[this.stackSize - 1] = 0;
        this.peeked = 0;
    }

    public void beginObject() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i != 1) {
            throw unexpectedTokenError("BEGIN_OBJECT");
        }
        push(3);
        this.peeked = 0;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.peeked = 0;
        this.stack[0] = 8;
        this.stackSize = 1;
        this.in.close();
    }

    int doPeek() {
        int i;
        int nextNonWhitespace;
        int[] iArr = this.stack;
        int i2 = this.stackSize;
        int i3 = iArr[i2 - 1];
        if (i3 == 1) {
            iArr[i2 - 1] = 2;
        } else {
            if (i3 != 2) {
                if (i3 == 3 || i3 == 5) {
                    iArr[i2 - 1] = 4;
                    if (i3 == 5 && (nextNonWhitespace = nextNonWhitespace(true)) != 44) {
                        if (nextNonWhitespace != 59) {
                            if (nextNonWhitespace != 125) {
                                throw syntaxError("Unterminated object");
                            }
                            this.peeked = 2;
                            return 2;
                        }
                        checkLenient();
                    }
                    int nextNonWhitespace2 = nextNonWhitespace(true);
                    if (nextNonWhitespace2 == 34) {
                        i = 13;
                    } else if (nextNonWhitespace2 == 39) {
                        checkLenient();
                        i = 12;
                    } else {
                        if (nextNonWhitespace2 == 125) {
                            if (i3 == 5) {
                                throw syntaxError("Expected name");
                            }
                            this.peeked = 2;
                            return 2;
                        }
                        checkLenient();
                        this.pos--;
                        if (!isLiteral((char) nextNonWhitespace2)) {
                            throw syntaxError("Expected name");
                        }
                        i = 14;
                    }
                } else if (i3 == 4) {
                    iArr[i2 - 1] = 5;
                    int nextNonWhitespace3 = nextNonWhitespace(true);
                    if (nextNonWhitespace3 != 58) {
                        if (nextNonWhitespace3 != 61) {
                            throw syntaxError("Expected ':'");
                        }
                        checkLenient();
                        if (this.pos < this.limit || fillBuffer(1)) {
                            char[] cArr = this.buffer;
                            int i4 = this.pos;
                            if (cArr[i4] == '>') {
                                this.pos = i4 + 1;
                            }
                        }
                    }
                } else if (i3 == 6) {
                    if (this.strictness == Strictness.LENIENT) {
                        consumeNonExecutePrefix();
                    }
                    this.stack[this.stackSize - 1] = 7;
                } else if (i3 == 7) {
                    if (nextNonWhitespace(false) == -1) {
                        i = 17;
                    } else {
                        checkLenient();
                        this.pos--;
                    }
                } else if (i3 == 8) {
                    throw new IllegalStateException("JsonReader is closed");
                }
                this.peeked = i;
                return i;
            }
            int nextNonWhitespace4 = nextNonWhitespace(true);
            if (nextNonWhitespace4 != 44) {
                if (nextNonWhitespace4 != 59) {
                    if (nextNonWhitespace4 != 93) {
                        throw syntaxError("Unterminated array");
                    }
                    this.peeked = 4;
                    return 4;
                }
                checkLenient();
            }
        }
        int nextNonWhitespace5 = nextNonWhitespace(true);
        if (nextNonWhitespace5 != 34) {
            if (nextNonWhitespace5 == 39) {
                checkLenient();
                this.peeked = 8;
                return 8;
            }
            if (nextNonWhitespace5 != 44 && nextNonWhitespace5 != 59) {
                if (nextNonWhitespace5 == 91) {
                    this.peeked = 3;
                    return 3;
                }
                if (nextNonWhitespace5 != 93) {
                    if (nextNonWhitespace5 == 123) {
                        this.peeked = 1;
                        return 1;
                    }
                    this.pos--;
                    int peekKeyword = peekKeyword();
                    if (peekKeyword != 0) {
                        return peekKeyword;
                    }
                    int peekNumber = peekNumber();
                    if (peekNumber != 0) {
                        return peekNumber;
                    }
                    if (!isLiteral(this.buffer[this.pos])) {
                        throw syntaxError("Expected value");
                    }
                    checkLenient();
                    i = 10;
                } else if (i3 == 1) {
                    this.peeked = 4;
                    return 4;
                }
            }
            if (i3 != 1 && i3 != 2) {
                throw syntaxError("Unexpected value");
            }
            checkLenient();
            this.pos--;
            this.peeked = 7;
            return 7;
        }
        i = 9;
        this.peeked = i;
        return i;
    }

    public void endArray() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i != 4) {
            throw unexpectedTokenError("END_ARRAY");
        }
        int i2 = this.stackSize;
        this.stackSize = i2 - 1;
        int[] iArr = this.pathIndices;
        int i3 = i2 - 2;
        iArr[i3] = iArr[i3] + 1;
        this.peeked = 0;
    }

    public void endObject() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i != 2) {
            throw unexpectedTokenError("END_OBJECT");
        }
        int i2 = this.stackSize;
        int i3 = i2 - 1;
        this.stackSize = i3;
        this.pathNames[i3] = null;
        int[] iArr = this.pathIndices;
        int i4 = i2 - 2;
        iArr[i4] = iArr[i4] + 1;
        this.peeked = 0;
    }

    public String getPath() {
        return getPath(false);
    }

    public String getPreviousPath() {
        return getPath(true);
    }

    public boolean hasNext() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        return (i == 2 || i == 4 || i == 17) ? false : true;
    }

    public final boolean isLenient() {
        return this.strictness == Strictness.LENIENT;
    }

    String locationString() {
        return " at line " + (this.lineNumber + 1) + " column " + ((this.pos - this.lineStart) + 1) + " path " + getPath();
    }

    public boolean nextBoolean() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 5) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return true;
        }
        if (i != 6) {
            throw unexpectedTokenError("a boolean");
        }
        this.peeked = 0;
        int[] iArr2 = this.pathIndices;
        int i3 = this.stackSize - 1;
        iArr2[i3] = iArr2[i3] + 1;
        return false;
    }

    public double nextDouble() {
        String nextQuotedValue;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 15) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return this.peekedLong;
        }
        if (i == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            if (i == 8 || i == 9) {
                nextQuotedValue = nextQuotedValue(i == 8 ? '\'' : '\"');
            } else if (i == 10) {
                nextQuotedValue = nextUnquotedValue();
            } else if (i != 11) {
                throw unexpectedTokenError("a double");
            }
            this.peekedString = nextQuotedValue;
        }
        this.peeked = 11;
        double parseDouble = Double.parseDouble(this.peekedString);
        if (this.strictness != Strictness.LENIENT && (Double.isNaN(parseDouble) || Double.isInfinite(parseDouble))) {
            throw syntaxError("JSON forbids NaN and infinities: " + parseDouble);
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] iArr2 = this.pathIndices;
        int i3 = this.stackSize - 1;
        iArr2[i3] = iArr2[i3] + 1;
        return parseDouble;
    }

    public int nextInt() {
        String nextQuotedValue;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 15) {
            long j = this.peekedLong;
            int i2 = (int) j;
            if (j == i2) {
                this.peeked = 0;
                int[] iArr = this.pathIndices;
                int i3 = this.stackSize - 1;
                iArr[i3] = iArr[i3] + 1;
                return i2;
            }
            throw new NumberFormatException("Expected an int but was " + this.peekedLong + locationString());
        }
        if (i == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            if (i != 8 && i != 9 && i != 10) {
                throw unexpectedTokenError("an int");
            }
            if (i == 10) {
                nextQuotedValue = nextUnquotedValue();
            } else {
                nextQuotedValue = nextQuotedValue(i == 8 ? '\'' : '\"');
            }
            this.peekedString = nextQuotedValue;
            try {
                int parseInt = Integer.parseInt(this.peekedString);
                this.peeked = 0;
                int[] iArr2 = this.pathIndices;
                int i4 = this.stackSize - 1;
                iArr2[i4] = iArr2[i4] + 1;
                return parseInt;
            } catch (NumberFormatException unused) {
            }
        }
        this.peeked = 11;
        double parseDouble = Double.parseDouble(this.peekedString);
        int i5 = (int) parseDouble;
        if (i5 != parseDouble) {
            throw new NumberFormatException("Expected an int but was " + this.peekedString + locationString());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] iArr3 = this.pathIndices;
        int i6 = this.stackSize - 1;
        iArr3[i6] = iArr3[i6] + 1;
        return i5;
    }

    public long nextLong() {
        String nextQuotedValue;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 15) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return this.peekedLong;
        }
        if (i == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            if (i != 8 && i != 9 && i != 10) {
                throw unexpectedTokenError("a long");
            }
            if (i == 10) {
                nextQuotedValue = nextUnquotedValue();
            } else {
                nextQuotedValue = nextQuotedValue(i == 8 ? '\'' : '\"');
            }
            this.peekedString = nextQuotedValue;
            try {
                long parseLong = Long.parseLong(this.peekedString);
                this.peeked = 0;
                int[] iArr2 = this.pathIndices;
                int i3 = this.stackSize - 1;
                iArr2[i3] = iArr2[i3] + 1;
                return parseLong;
            } catch (NumberFormatException unused) {
            }
        }
        this.peeked = 11;
        double parseDouble = Double.parseDouble(this.peekedString);
        long j = (long) parseDouble;
        if (j != parseDouble) {
            throw new NumberFormatException("Expected a long but was " + this.peekedString + locationString());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] iArr3 = this.pathIndices;
        int i4 = this.stackSize - 1;
        iArr3[i4] = iArr3[i4] + 1;
        return j;
    }

    public String nextName() {
        char c;
        String nextQuotedValue;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 14) {
            nextQuotedValue = nextUnquotedValue();
        } else {
            if (i == 12) {
                c = '\'';
            } else {
                if (i != 13) {
                    throw unexpectedTokenError("a name");
                }
                c = '\"';
            }
            nextQuotedValue = nextQuotedValue(c);
        }
        this.peeked = 0;
        this.pathNames[this.stackSize - 1] = nextQuotedValue;
        return nextQuotedValue;
    }

    public void nextNull() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i != 7) {
            throw unexpectedTokenError("null");
        }
        this.peeked = 0;
        int[] iArr = this.pathIndices;
        int i2 = this.stackSize - 1;
        iArr[i2] = iArr[i2] + 1;
    }

    public String nextString() {
        String str;
        char c;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 10) {
            str = nextUnquotedValue();
        } else {
            if (i == 8) {
                c = '\'';
            } else if (i == 9) {
                c = '\"';
            } else if (i == 11) {
                str = this.peekedString;
                this.peekedString = null;
            } else if (i == 15) {
                str = Long.toString(this.peekedLong);
            } else {
                if (i != 16) {
                    throw unexpectedTokenError("a string");
                }
                str = new String(this.buffer, this.pos, this.peekedNumberLength);
                this.pos += this.peekedNumberLength;
            }
            str = nextQuotedValue(c);
        }
        this.peeked = 0;
        int[] iArr = this.pathIndices;
        int i2 = this.stackSize - 1;
        iArr[i2] = iArr[i2] + 1;
        return str;
    }

    public JsonToken peek() {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        switch (i) {
            case 1:
                return JsonToken.BEGIN_OBJECT;
            case 2:
                return JsonToken.END_OBJECT;
            case 3:
                return JsonToken.BEGIN_ARRAY;
            case 4:
                return JsonToken.END_ARRAY;
            case 5:
            case 6:
                return JsonToken.BOOLEAN;
            case 7:
                return JsonToken.NULL;
            case 8:
            case 9:
            case 10:
            case 11:
                return JsonToken.STRING;
            case 12:
            case 13:
            case 14:
                return JsonToken.NAME;
            case 15:
            case 16:
                return JsonToken.NUMBER;
            case 17:
                return JsonToken.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Failed to find 'out' block for switch in B:6:0x0011. Please report as an issue. */
    public void skipValue() {
        int i = 0;
        do {
            int i2 = this.peeked;
            if (i2 == 0) {
                i2 = doPeek();
            }
            switch (i2) {
                case 1:
                    push(3);
                    i++;
                    this.peeked = 0;
                    break;
                case 2:
                    if (i == 0) {
                        this.pathNames[this.stackSize - 1] = null;
                    }
                    this.stackSize--;
                    i--;
                    this.peeked = 0;
                    break;
                case 3:
                    push(1);
                    i++;
                    this.peeked = 0;
                    break;
                case 4:
                    this.stackSize--;
                    i--;
                    this.peeked = 0;
                    break;
                case 5:
                case 6:
                case 7:
                case 11:
                case 15:
                default:
                    this.peeked = 0;
                    break;
                case 8:
                    skipQuotedValue('\'');
                    this.peeked = 0;
                    break;
                case 9:
                    skipQuotedValue('\"');
                    this.peeked = 0;
                    break;
                case 10:
                    skipUnquotedValue();
                    this.peeked = 0;
                    break;
                case 12:
                    skipQuotedValue('\'');
                    if (i == 0) {
                        this.pathNames[this.stackSize - 1] = "<skipped>";
                    }
                    this.peeked = 0;
                    break;
                case 13:
                    skipQuotedValue('\"');
                    if (i == 0) {
                        this.pathNames[this.stackSize - 1] = "<skipped>";
                    }
                    this.peeked = 0;
                    break;
                case 14:
                    skipUnquotedValue();
                    if (i == 0) {
                        this.pathNames[this.stackSize - 1] = "<skipped>";
                    }
                    this.peeked = 0;
                    break;
                case 16:
                    this.pos += this.peekedNumberLength;
                    this.peeked = 0;
                    break;
                case 17:
                    return;
            }
        } while (i > 0);
        int[] iArr = this.pathIndices;
        int i3 = this.stackSize - 1;
        iArr[i3] = iArr[i3] + 1;
    }

    public String toString() {
        return getClass().getSimpleName() + locationString();
    }
}
