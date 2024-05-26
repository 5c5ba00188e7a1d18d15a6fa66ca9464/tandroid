package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
/* loaded from: classes.dex */
public class Timeout {
    private long deadlineNanoTime;
    private boolean hasDeadline;

    static {
        new Timeout() { // from class: okio.Timeout.1
            @Override // okio.Timeout
            public void throwIfReached() throws IOException {
            }
        };
    }

    public void throwIfReached() throws IOException {
        if (Thread.interrupted()) {
            Thread.currentThread().interrupt();
            throw new InterruptedIOException("interrupted");
        } else if (this.hasDeadline && this.deadlineNanoTime - System.nanoTime() <= 0) {
            throw new InterruptedIOException("deadline reached");
        }
    }
}
