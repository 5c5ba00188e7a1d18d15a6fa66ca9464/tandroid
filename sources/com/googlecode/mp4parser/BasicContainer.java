package com.googlecode.mp4parser;

import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.Container;
import com.googlecode.mp4parser.util.Logger;
import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/* loaded from: classes.dex */
public abstract class BasicContainer implements Container, Iterator, Closeable {
    private static final Box EOF = new AbstractBox("eof ") { // from class: com.googlecode.mp4parser.BasicContainer.1
        @Override // com.googlecode.mp4parser.AbstractBox
        protected void _parseDetails(ByteBuffer byteBuffer) {
        }

        @Override // com.googlecode.mp4parser.AbstractBox
        protected void getContent(ByteBuffer byteBuffer) {
        }

        @Override // com.googlecode.mp4parser.AbstractBox
        protected long getContentSize() {
            return 0L;
        }
    };
    private static Logger LOG = Logger.getLogger(BasicContainer.class);
    Box lookahead = null;
    long parsePosition = 0;
    long startPosition = 0;
    long endPosition = 0;
    private List boxes = new ArrayList();

    public void addBox(Box box) {
        if (box != null) {
            this.boxes = new ArrayList(getBoxes());
            box.setParent(this);
            this.boxes.add(box);
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        throw null;
    }

    @Override // com.coremedia.iso.boxes.Container
    public List getBoxes() {
        return this.boxes;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public long getContainerSize() {
        long j = 0;
        for (int i = 0; i < getBoxes().size(); i++) {
            j += ((Box) this.boxes.get(i)).getSize();
        }
        return j;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        Box box = this.lookahead;
        if (box == EOF) {
            return false;
        }
        if (box != null) {
            return true;
        }
        try {
            this.lookahead = next();
            return true;
        } catch (NoSuchElementException unused) {
            this.lookahead = EOF;
            return false;
        }
    }

    @Override // java.util.Iterator
    public Box next() {
        Box box = this.lookahead;
        if (box == null || box == EOF) {
            this.lookahead = EOF;
            throw new NoSuchElementException();
        }
        this.lookahead = null;
        return box;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("[");
        for (int i = 0; i < this.boxes.size(); i++) {
            if (i > 0) {
                sb.append(";");
            }
            sb.append(((Box) this.boxes.get(i)).toString());
        }
        sb.append("]");
        return sb.toString();
    }

    public final void writeContainer(WritableByteChannel writableByteChannel) {
        Iterator it = getBoxes().iterator();
        while (it.hasNext()) {
            ((Box) it.next()).getBox(writableByteChannel);
        }
    }
}
