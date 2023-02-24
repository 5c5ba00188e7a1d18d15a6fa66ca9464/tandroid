package com.google.android.exoplayer2;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.android.exoplayer2.audio.AudioCapabilities;
import com.google.android.exoplayer2.audio.AudioRendererEventListener;
import com.google.android.exoplayer2.audio.AudioSink;
import com.google.android.exoplayer2.audio.DefaultAudioSink;
import com.google.android.exoplayer2.audio.MediaCodecAudioRenderer;
import com.google.android.exoplayer2.ext.ffmpeg.FfmpegAudioRenderer;
import com.google.android.exoplayer2.ext.flac.LibflacAudioRenderer;
import com.google.android.exoplayer2.ext.opus.LibopusAudioRenderer;
import com.google.android.exoplayer2.mediacodec.DefaultMediaCodecAdapterFactory;
import com.google.android.exoplayer2.mediacodec.MediaCodecAdapter;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.MetadataRenderer;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.text.TextRenderer;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.exoplayer2.video.spherical.CameraMotionRenderer;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class DefaultRenderersFactory implements RenderersFactory {
    private final Context context;
    private boolean enableAudioTrackPlaybackParams;
    private boolean enableDecoderFallback;
    private boolean enableFloatOutput;
    private boolean enableOffload;
    private final DefaultMediaCodecAdapterFactory codecAdapterFactory = new DefaultMediaCodecAdapterFactory();
    private int extensionRendererMode = 0;
    private long allowedVideoJoiningTimeMs = 5000;
    private MediaCodecSelector mediaCodecSelector = MediaCodecSelector.DEFAULT;

    protected void buildMiscellaneousRenderers(Context context, Handler handler, int i, ArrayList<Renderer> arrayList) {
    }

    public DefaultRenderersFactory(Context context) {
        this.context = context;
    }

    public DefaultRenderersFactory setExtensionRendererMode(int i) {
        this.extensionRendererMode = i;
        return this;
    }

    @Override // com.google.android.exoplayer2.RenderersFactory
    public Renderer[] createRenderers(Handler handler, VideoRendererEventListener videoRendererEventListener, AudioRendererEventListener audioRendererEventListener, TextOutput textOutput, MetadataOutput metadataOutput) {
        ArrayList<Renderer> arrayList = new ArrayList<>();
        buildVideoRenderers(this.context, this.extensionRendererMode, this.mediaCodecSelector, this.enableDecoderFallback, handler, videoRendererEventListener, this.allowedVideoJoiningTimeMs, arrayList);
        AudioSink buildAudioSink = buildAudioSink(this.context, this.enableFloatOutput, this.enableAudioTrackPlaybackParams, this.enableOffload);
        if (buildAudioSink != null) {
            buildAudioRenderers(this.context, this.extensionRendererMode, this.mediaCodecSelector, this.enableDecoderFallback, buildAudioSink, handler, audioRendererEventListener, arrayList);
        }
        buildTextRenderers(this.context, textOutput, handler.getLooper(), this.extensionRendererMode, arrayList);
        buildMetadataRenderers(this.context, metadataOutput, handler.getLooper(), this.extensionRendererMode, arrayList);
        buildCameraMotionRenderers(this.context, this.extensionRendererMode, arrayList);
        buildMiscellaneousRenderers(this.context, handler, this.extensionRendererMode, arrayList);
        return (Renderer[]) arrayList.toArray(new Renderer[0]);
    }

    protected void buildVideoRenderers(Context context, int i, MediaCodecSelector mediaCodecSelector, boolean z, Handler handler, VideoRendererEventListener videoRendererEventListener, long j, ArrayList<Renderer> arrayList) {
        int i2;
        arrayList.add(new MediaCodecVideoRenderer(context, getCodecAdapterFactory(), mediaCodecSelector, j, z, handler, videoRendererEventListener, 50));
        if (i == 0) {
            return;
        }
        int size = arrayList.size();
        if (i == 2) {
            size--;
        }
        try {
            try {
                i2 = size + 1;
                try {
                    arrayList.add(size, (Renderer) Class.forName("com.google.android.exoplayer2.ext.vp9.LibvpxVideoRenderer").getConstructor(Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE).newInstance(Long.valueOf(j), handler, videoRendererEventListener, 50));
                    Log.i("DefaultRenderersFactory", "Loaded LibvpxVideoRenderer.");
                } catch (ClassNotFoundException unused) {
                    size = i2;
                    i2 = size;
                    arrayList.add(i2, (Renderer) Class.forName("com.google.android.exoplayer2.ext.av1.Libgav1VideoRenderer").getConstructor(Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE).newInstance(Long.valueOf(j), handler, videoRendererEventListener, 50));
                    Log.i("DefaultRenderersFactory", "Loaded Libgav1VideoRenderer.");
                }
            } catch (Exception e) {
                throw new RuntimeException("Error instantiating VP9 extension", e);
            }
        } catch (ClassNotFoundException unused2) {
        }
        try {
            arrayList.add(i2, (Renderer) Class.forName("com.google.android.exoplayer2.ext.av1.Libgav1VideoRenderer").getConstructor(Long.TYPE, Handler.class, VideoRendererEventListener.class, Integer.TYPE).newInstance(Long.valueOf(j), handler, videoRendererEventListener, 50));
            Log.i("DefaultRenderersFactory", "Loaded Libgav1VideoRenderer.");
        } catch (ClassNotFoundException unused3) {
        } catch (Exception e2) {
            throw new RuntimeException("Error instantiating AV1 extension", e2);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(14:5|(1:7)|8|9|10|11|(2:12|13)|14|(5:15|16|17|18|19)|20|21|22|(2:23|24)|(2:26|27)) */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void buildAudioRenderers(Context context, int i, MediaCodecSelector mediaCodecSelector, boolean z, AudioSink audioSink, Handler handler, AudioRendererEventListener audioRendererEventListener, ArrayList<Renderer> arrayList) {
        int i2;
        int i3;
        int i4;
        arrayList.add(new MediaCodecAudioRenderer(context, getCodecAdapterFactory(), mediaCodecSelector, z, handler, audioRendererEventListener, audioSink));
        if (i == 0) {
            return;
        }
        int size = arrayList.size();
        if (i == 2) {
            size--;
        }
        try {
            try {
                i2 = size + 1;
            } catch (Exception e) {
                throw new RuntimeException("Error instantiating MIDI extension", e);
            }
        } catch (ClassNotFoundException unused) {
        }
        try {
            try {
                arrayList.add(size, (Renderer) Class.forName("com.google.android.exoplayer2.decoder.midi.MidiRenderer").getConstructor(new Class[0]).newInstance(new Object[0]));
                Log.i("DefaultRenderersFactory", "Loaded MidiRenderer.");
            } catch (ClassNotFoundException unused2) {
                size = i2;
                i2 = size;
                try {
                    i3 = i2 + 1;
                    try {
                        arrayList.add(i2, (Renderer) LibopusAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                        Log.i("DefaultRenderersFactory", "Loaded LibopusAudioRenderer.");
                    } catch (ClassNotFoundException unused3) {
                        i2 = i3;
                        i3 = i2;
                        i4 = i3 + 1;
                        arrayList.add(i3, (Renderer) LibflacAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                        Log.i("DefaultRenderersFactory", "Loaded LibflacAudioRenderer.");
                        arrayList.add(i4, (Renderer) FfmpegAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                        Log.i("DefaultRenderersFactory", "Loaded FfmpegAudioRenderer.");
                    }
                    i4 = i3 + 1;
                    arrayList.add(i3, (Renderer) LibflacAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                    Log.i("DefaultRenderersFactory", "Loaded LibflacAudioRenderer.");
                    arrayList.add(i4, (Renderer) FfmpegAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                    Log.i("DefaultRenderersFactory", "Loaded FfmpegAudioRenderer.");
                } catch (Exception e2) {
                    throw new RuntimeException("Error instantiating Opus extension", e2);
                }
            }
            i4 = i3 + 1;
            try {
                arrayList.add(i3, (Renderer) LibflacAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                Log.i("DefaultRenderersFactory", "Loaded LibflacAudioRenderer.");
            } catch (ClassNotFoundException unused4) {
                i3 = i4;
                i4 = i3;
                arrayList.add(i4, (Renderer) FfmpegAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                Log.i("DefaultRenderersFactory", "Loaded FfmpegAudioRenderer.");
            }
            try {
                arrayList.add(i4, (Renderer) FfmpegAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
                Log.i("DefaultRenderersFactory", "Loaded FfmpegAudioRenderer.");
            } catch (ClassNotFoundException unused5) {
                return;
            } catch (Exception e3) {
                throw new RuntimeException("Error instantiating FFmpeg extension", e3);
            }
        } catch (Exception e4) {
            throw new RuntimeException("Error instantiating FLAC extension", e4);
        }
        try {
            i3 = i2 + 1;
            arrayList.add(i2, (Renderer) LibopusAudioRenderer.class.getConstructor(Handler.class, AudioRendererEventListener.class, AudioSink.class).newInstance(handler, audioRendererEventListener, audioSink));
            Log.i("DefaultRenderersFactory", "Loaded LibopusAudioRenderer.");
        } catch (ClassNotFoundException unused6) {
        }
    }

    protected void buildTextRenderers(Context context, TextOutput textOutput, Looper looper, int i, ArrayList<Renderer> arrayList) {
        arrayList.add(new TextRenderer(textOutput, looper));
    }

    protected void buildMetadataRenderers(Context context, MetadataOutput metadataOutput, Looper looper, int i, ArrayList<Renderer> arrayList) {
        arrayList.add(new MetadataRenderer(metadataOutput, looper));
    }

    protected void buildCameraMotionRenderers(Context context, int i, ArrayList<Renderer> arrayList) {
        arrayList.add(new CameraMotionRenderer());
    }

    protected AudioSink buildAudioSink(Context context, boolean z, boolean z2, boolean z3) {
        return new DefaultAudioSink.Builder().setAudioCapabilities(AudioCapabilities.getCapabilities(context)).setEnableFloatOutput(z).setEnableAudioTrackPlaybackParams(z2).setOffloadMode(z3 ? 1 : 0).build();
    }

    protected MediaCodecAdapter.Factory getCodecAdapterFactory() {
        return this.codecAdapterFactory;
    }
}
