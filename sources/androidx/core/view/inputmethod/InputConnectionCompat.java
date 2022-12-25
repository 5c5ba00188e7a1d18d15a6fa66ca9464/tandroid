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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v3, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v6 */
    static boolean handlePerformPrivateCommand(String action, Bundle data, OnCommitContentListener onCommitContentListener) {
        boolean z;
        ResultReceiver resultReceiver;
        ?? r0 = 0;
        r0 = 0;
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
                    r0 = onCommitContentListener.onCommitContent(new InputContentInfoCompat(uri, clipDescription, uri2), i, bundle);
                }
                if (resultReceiver != 0) {
                    resultReceiver.send(r0, null);
                }
                return r0;
            } catch (Throwable th) {
                th = th;
                if (resultReceiver != 0) {
                    resultReceiver.send(0, null);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            resultReceiver = 0;
        }
    }

    public static InputConnection createWrapper(InputConnection inputConnection, EditorInfo editorInfo, final OnCommitContentListener onCommitContentListener) {
        if (inputConnection != null) {
            if (editorInfo != null) {
                if (onCommitContentListener == null) {
                    throw new IllegalArgumentException("onCommitContentListener must be non-null");
                }
                if (Build.VERSION.SDK_INT >= 25) {
                    return new InputConnectionWrapper(inputConnection, false) { // from class: androidx.core.view.inputmethod.InputConnectionCompat.1
                        @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
                        public boolean commitContent(InputContentInfo inputContentInfo, int flags, Bundle opts) {
                            if (onCommitContentListener.onCommitContent(InputContentInfoCompat.wrap(inputContentInfo), flags, opts)) {
                                return true;
                            }
                            return super.commitContent(inputContentInfo, flags, opts);
                        }
                    };
                }
                return EditorInfoCompat.getContentMimeTypes(editorInfo).length == 0 ? inputConnection : new InputConnectionWrapper(inputConnection, false) { // from class: androidx.core.view.inputmethod.InputConnectionCompat.2
                    @Override // android.view.inputmethod.InputConnectionWrapper, android.view.inputmethod.InputConnection
                    public boolean performPrivateCommand(String action, Bundle data) {
                        if (InputConnectionCompat.handlePerformPrivateCommand(action, data, onCommitContentListener)) {
                            return true;
                        }
                        return super.performPrivateCommand(action, data);
                    }
                };
            }
            throw new IllegalArgumentException("editorInfo must be non-null");
        }
        throw new IllegalArgumentException("inputConnection must be non-null");
    }
}
