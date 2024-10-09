package com.google.firebase.encoders;

/* loaded from: classes.dex */
public interface ObjectEncoderContext {
    ObjectEncoderContext add(FieldDescriptor fieldDescriptor, int i);

    ObjectEncoderContext add(FieldDescriptor fieldDescriptor, long j);

    ObjectEncoderContext add(FieldDescriptor fieldDescriptor, Object obj);
}
