package androidx.core.view.inputmethod;

import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputContentInfo;
/* loaded from: classes.dex */
public final class InputConnectionCompat {

    /* loaded from: classes.dex */
    public interface OnCommitContentListener {
        boolean onCommitContent(InputContentInfoCompat inputContentInfo, int flags, Bundle opts);
    }

    static boolean handlePerformPrivateCommand(String action, Bundle data, OnCommitContentListener onCommitContentListener) {
        boolean z;
        Throwable th;
        ResultReceiver resultReceiver;
        boolean z2 = false;
        z2 = false;
        if (data == null) {
            return false;
        }
        if (TextUtils.equals("androidx.core.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", action)) {
            z = false;
        } else if (!TextUtils.equals("android.support.v13.view.inputmethod.InputConnectionCompat.COMMIT_CONTENT", action)) {
            return false;
        } else {
            z = true;
        }
        try {
            resultReceiver = (ResultReceiver) data.getParcelable(z ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_RESULT_RECEIVER");
            try {
                Uri uri = (Uri) data.getParcelable(z ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_URI" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_URI");
                ClipDescription clipDescription = (ClipDescription) data.getParcelable(z ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_DESCRIPTION");
                Uri uri2 = (Uri) data.getParcelable(z ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_LINK_URI");
                int i = data.getInt(z ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_FLAGS");
                Bundle bundle = (Bundle) data.getParcelable(z ? "android.support.v13.view.inputmethod.InputConnectionCompat.CONTENT_OPTS" : "androidx.core.view.inputmethod.InputConnectionCompat.CONTENT_OPTS");
                if (uri != null && clipDescription != null) {
                    z2 = onCommitContentListener.onCommitContent(new InputContentInfoCompat(uri, clipDescription, uri2), i, bundle);
                }
                if (resultReceiver != null) {
                    int i2 = z2 ? 1 : 0;
                    int i3 = z2 ? 1 : 0;
                    int i4 = z2 ? 1 : 0;
                    resultReceiver.send(i2, null);
                }
                return z2;
            } catch (Throwable th2) {
                th = th2;
                if (resultReceiver != null) {
                    resultReceiver.send(0, null);
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            resultReceiver = null;
        }
    }

    public static InputConnection createWrapper(InputConnection inputConnection, EditorInfo editorInfo, OnCommitContentListener onCommitContentListener) {
        if (inputConnection != null) {
            if (editorInfo == null) {
                throw new IllegalArgumentException("editorInfo must be non-null");
            }
            if (onCommitContentListener == null) {
                throw new IllegalArgumentException("onCommitContentListener must be non-null");
            }
            if (Build.VERSION.SDK_INT >= 25) {
                return new AnonymousClass1(inputConnection, false, onCommitContentListener);
            }
            return EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0 ? inputConnection : new AnonymousClass2(inputConnection, false, onCommitContentListener);
        }
        throw new IllegalArgumentException("inputConnection must be non-null");
    }

    /* renamed from: androidx.core.view.inputmethod.InputConnectionCompat$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends InputConnectionWrapper {
        final /* synthetic */ OnCommitContentListener val$listener;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass1(InputConnection target, boolean mutable, final OnCommitContentListener val$listener) {
            super(target, mutable);
            this.val$listener = val$listener;
        }

        @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
        public boolean commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts) {
            if (this.val$listener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), flags, opts)) {
                return true;
            }
            return super.commitContent(inputContentInfo, flags, opts);
        }
    }

    /* renamed from: androidx.core.view.inputmethod.InputConnectionCompat$2 */
    /* loaded from: classes.dex */
    public class AnonymousClass2 extends InputConnectionWrapper {
        final /* synthetic */ OnCommitContentListener val$listener;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        AnonymousClass2(InputConnection target, boolean mutable, final OnCommitContentListener val$listener) {
            super(target, mutable);
            this.val$listener = val$listener;
        }

        @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
        public boolean performPrivateCommand(String action, Bundle data) {
            if (InputConnectionCompat.handlePerformPrivateCommand(action, data, this.val$listener)) {
                return true;
            }
            return super.performPrivateCommand(action, data);
        }
    }
}
