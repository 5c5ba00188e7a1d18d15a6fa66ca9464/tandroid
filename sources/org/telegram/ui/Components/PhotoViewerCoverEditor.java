package org.telegram.ui.Components;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import org.telegram.messenger.MediaController;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.BlurringShader;
import org.telegram.ui.PhotoViewer;
import org.telegram.ui.Stories.recorder.ButtonWithCounterView;
import org.telegram.ui.Stories.recorder.GallerySheet;
import org.telegram.ui.Stories.recorder.TimelineView;

/* loaded from: classes3.dex */
public class PhotoViewerCoverEditor extends FrameLayout {
    private float aspectRatio;
    public ButtonWithCounterView button;
    private GallerySheet gallerySheet;
    private Utilities.Callback onGalleryListener;
    public EditCoverButton openGalleryButton;
    private long time;
    public TimelineView timelineView;
    private VideoPlayer videoPlayer;

    public PhotoViewerCoverEditor(final Context context, final Theme.ResourcesProvider resourcesProvider, PhotoViewer photoViewer, BlurringShader.BlurManager blurManager) {
        super(context);
        this.time = -1L;
        this.aspectRatio = 1.39f;
        TimelineView timelineView = new TimelineView(context, null, null, resourcesProvider, blurManager);
        this.timelineView = timelineView;
        timelineView.setCover();
        addView(this.timelineView, LayoutHelper.createFrame(-1, TimelineView.heightDp(), 87, 0.0f, 0.0f, 0.0f, 74.0f));
        ButtonWithCounterView buttonWithCounterView = new ButtonWithCounterView(context, resourcesProvider);
        this.button = buttonWithCounterView;
        buttonWithCounterView.setText("Save Cover", false);
        addView(this.button, LayoutHelper.createFrame(-1, 48.0f, 87, 10.0f, 10.0f, 10.0f, 10.0f));
        EditCoverButton editCoverButton = new EditCoverButton(context, photoViewer, "Choose from Gallery", true);
        this.openGalleryButton = editCoverButton;
        editCoverButton.setOnClickListener(new View.OnClickListener() { // from class: org.telegram.ui.Components.PhotoViewerCoverEditor$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                PhotoViewerCoverEditor.this.lambda$new$1(context, resourcesProvider, view);
            }
        });
        addView(this.openGalleryButton, LayoutHelper.createFrame(-1, 32.0f, 87, 60.0f, 0.0f, 60.0f, 134.0f));
        this.timelineView.setDelegate(new TimelineView.TimelineDelegate() { // from class: org.telegram.ui.Components.PhotoViewerCoverEditor.1
            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onAudioLeftChange(float f) {
                TimelineView.TimelineDelegate.-CC.$default$onAudioLeftChange(this, f);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onAudioOffsetChange(long j) {
                TimelineView.TimelineDelegate.-CC.$default$onAudioOffsetChange(this, j);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onAudioRemove() {
                TimelineView.TimelineDelegate.-CC.$default$onAudioRemove(this);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onAudioRightChange(float f) {
                TimelineView.TimelineDelegate.-CC.$default$onAudioRightChange(this, f);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onAudioVolumeChange(float f) {
                TimelineView.TimelineDelegate.-CC.$default$onAudioVolumeChange(this, f);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onProgressChange(long j, boolean z) {
                TimelineView.TimelineDelegate.-CC.$default$onProgressChange(this, j, z);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onProgressDragChange(boolean z) {
                TimelineView.TimelineDelegate.-CC.$default$onProgressDragChange(this, z);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onRoundLeftChange(float f) {
                TimelineView.TimelineDelegate.-CC.$default$onRoundLeftChange(this, f);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onRoundOffsetChange(long j) {
                TimelineView.TimelineDelegate.-CC.$default$onRoundOffsetChange(this, j);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onRoundRemove() {
                TimelineView.TimelineDelegate.-CC.$default$onRoundRemove(this);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onRoundRightChange(float f) {
                TimelineView.TimelineDelegate.-CC.$default$onRoundRightChange(this, f);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onRoundSelectChange(boolean z) {
                TimelineView.TimelineDelegate.-CC.$default$onRoundSelectChange(this, z);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onRoundVolumeChange(float f) {
                TimelineView.TimelineDelegate.-CC.$default$onRoundVolumeChange(this, f);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onVideoLeftChange(int i, float f) {
                TimelineView.TimelineDelegate.-CC.$default$onVideoLeftChange(this, i, f);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public void onVideoLeftChange(boolean z, float f) {
                if (PhotoViewerCoverEditor.this.videoPlayer == null) {
                    return;
                }
                PhotoViewerCoverEditor.this.time = (long) ((f + ((f / 0.96f) * 0.04f)) * PhotoViewerCoverEditor.this.videoPlayer.getDuration());
                PhotoViewerCoverEditor.this.videoPlayer.seekTo(PhotoViewerCoverEditor.this.time, !z);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onVideoOffsetChange(int i, long j) {
                TimelineView.TimelineDelegate.-CC.$default$onVideoOffsetChange(this, i, j);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onVideoRightChange(int i, float f) {
                TimelineView.TimelineDelegate.-CC.$default$onVideoRightChange(this, i, f);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onVideoRightChange(boolean z, float f) {
                TimelineView.TimelineDelegate.-CC.$default$onVideoRightChange(this, z, f);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onVideoSelected(int i) {
                TimelineView.TimelineDelegate.-CC.$default$onVideoSelected(this, i);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onVideoVolumeChange(float f) {
                TimelineView.TimelineDelegate.-CC.$default$onVideoVolumeChange(this, f);
            }

            @Override // org.telegram.ui.Stories.recorder.TimelineView.TimelineDelegate
            public /* synthetic */ void onVideoVolumeChange(int i, float f) {
                TimelineView.TimelineDelegate.-CC.$default$onVideoVolumeChange(this, i, f);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.gallerySheet = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Context context, Theme.ResourcesProvider resourcesProvider, View view) {
        if (this.gallerySheet == null) {
            GallerySheet gallerySheet = new GallerySheet(context, resourcesProvider, this.aspectRatio);
            this.gallerySheet = gallerySheet;
            gallerySheet.setOnDismissListener(new Runnable() { // from class: org.telegram.ui.Components.PhotoViewerCoverEditor$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PhotoViewerCoverEditor.this.lambda$new$0();
                }
            });
            this.gallerySheet.setOnGalleryImage(this.onGalleryListener);
        }
        this.gallerySheet.show();
    }

    public void closeGallery() {
        GallerySheet gallerySheet = this.gallerySheet;
        if (gallerySheet != null) {
            gallerySheet.lambda$new$0();
            this.gallerySheet = null;
        }
    }

    public void destroy() {
        this.videoPlayer = null;
        this.timelineView.setVideo(false, null, 0L, 0.0f);
    }

    public long getTime() {
        return this.time;
    }

    /* JADX WARN: Code restructure failed: missing block: B:9:0x002b, code lost:
    
        if (r2 >= 0) goto L14;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void set(MediaController.PhotoEntry photoEntry, VideoPlayer videoPlayer, Theme.ResourcesProvider resourcesProvider) {
        long currentPosition;
        int i;
        this.button.updateColors(resourcesProvider);
        int i2 = photoEntry.width;
        if (i2 <= 0 || (i = photoEntry.height) <= 0) {
            this.aspectRatio = 1.39f;
        } else {
            this.aspectRatio = Utilities.clamp(i / i2, 1.39f, 0.85f);
        }
        this.videoPlayer = videoPlayer;
        if (photoEntry.customThumb) {
            currentPosition = photoEntry.customThumbSavedPosition;
        }
        currentPosition = videoPlayer.getCurrentPosition();
        this.time = currentPosition;
        if (photoEntry.customThumb) {
            videoPlayer.seekTo(currentPosition, false);
        }
        this.timelineView.setVideo(false, videoPlayer.getCurrentUri().getPath(), videoPlayer.getDuration(), videoPlayer.player.getVolume());
        float max = (this.time / Math.max(1L, videoPlayer.getDuration())) * 0.96f;
        this.timelineView.setVideoLeft(max);
        this.timelineView.setVideoRight(max + 0.04f);
        this.timelineView.setCoverVideo(0L, videoPlayer.getDuration());
    }

    public void setOnGalleryImage(Utilities.Callback<MediaController.PhotoEntry> callback) {
        this.onGalleryListener = callback;
    }
}
