package com.google.android.datatransport.runtime.scheduling.jobscheduling;

import android.app.job.JobParameters;
/* loaded from: classes.dex */
public final /* synthetic */ class JobInfoSchedulerService$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ JobInfoSchedulerService f$0;
    public final /* synthetic */ JobParameters f$1;

    public /* synthetic */ JobInfoSchedulerService$$ExternalSyntheticLambda0(JobInfoSchedulerService jobInfoSchedulerService, JobParameters jobParameters) {
        this.f$0 = jobInfoSchedulerService;
        this.f$1 = jobParameters;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onStartJob$0(this.f$1);
    }
}
