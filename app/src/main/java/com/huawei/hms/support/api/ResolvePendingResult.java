package com.huawei.hms.support.api;

import com.huawei.hms.core.aidl.IMessageEntity;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.client.Result;
import com.huawei.hms.support.api.client.Status;
/* loaded from: classes.dex */
public class ResolvePendingResult<T extends IMessageEntity> extends PendingResultImpl<ResolveResult<T>, T> {
    protected ResolvePendingResult(ApiClient apiClient, String str, IMessageEntity iMessageEntity, Class<T> cls) {
        super(apiClient, str, iMessageEntity, cls);
    }

    public static <R extends IMessageEntity> ResolvePendingResult<R> build(ApiClient apiClient, String str, IMessageEntity iMessageEntity, Class<R> cls) {
        return new ResolvePendingResult<>(apiClient, str, iMessageEntity, cls);
    }

    public T get() {
        return (T) await().getValue();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.huawei.hms.support.api.PendingResultImpl
    /* renamed from: onComplete  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ Result mo234onComplete(IMessageEntity iMessageEntity) {
        return mo234onComplete((ResolvePendingResult<T>) iMessageEntity);
    }

    @Override // com.huawei.hms.support.api.PendingResultImpl
    /* renamed from: onComplete */
    public ResolveResult<T> mo234onComplete(T t) {
        ResolveResult<T> resolveResult = new ResolveResult<>(t);
        resolveResult.setStatus(new Status(0));
        return resolveResult;
    }
}
