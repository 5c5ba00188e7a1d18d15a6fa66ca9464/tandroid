package com.microsoft.appcenter.crashes;

import com.microsoft.appcenter.crashes.model.ErrorReport;

/* loaded from: classes.dex */
public interface CrashesListener {
    Iterable getErrorAttachments(ErrorReport errorReport);

    void onBeforeSending(ErrorReport errorReport);

    void onSendingFailed(ErrorReport errorReport, Exception exc);

    void onSendingSucceeded(ErrorReport errorReport);

    boolean shouldAwaitUserConfirmation();

    boolean shouldProcess(ErrorReport errorReport);
}
