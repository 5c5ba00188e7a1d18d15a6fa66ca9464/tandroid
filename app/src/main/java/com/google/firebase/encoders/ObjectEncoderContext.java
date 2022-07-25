package com.google.firebase.encoders;

import java.io.IOException;
/* loaded from: classes.dex */
public interface ObjectEncoderContext {
    /* renamed from: add */
    ObjectEncoderContext mo195add(FieldDescriptor fieldDescriptor, int i) throws IOException;

    /* renamed from: add */
    ObjectEncoderContext mo196add(FieldDescriptor fieldDescriptor, long j) throws IOException;

    ObjectEncoderContext add(FieldDescriptor fieldDescriptor, Object obj) throws IOException;
}
