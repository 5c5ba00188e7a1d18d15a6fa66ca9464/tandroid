package org.telegram.ui.Components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.graphics.ColorUtils;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.telegram.messenger.AccountInstance;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ChatObject;
import org.telegram.messenger.ImageLocation;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC$Chat;
import org.telegram.tgnet.TLRPC$TL_groupCallParticipant;
import org.telegram.tgnet.TLRPC$User;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.GroupCallUserCell;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.voip.GroupCallMiniTextureView;
import org.telegram.ui.Components.voip.GroupCallRenderersContainer;
import org.telegram.ui.Components.voip.GroupCallStatusIcon;
import org.telegram.ui.GroupCallActivity;
/* loaded from: classes3.dex */
public class GroupCallFullscreenAdapter extends RecyclerListView.SelectionAdapter {
    private final GroupCallActivity activity;
    private ArrayList<GroupCallMiniTextureView> attachedRenderers;
    private final int currentAccount;
    private ChatObject.Call groupCall;
    private GroupCallRenderersContainer renderersContainer;
    private final ArrayList<ChatObject.VideoParticipant> videoParticipants = new ArrayList<>();
    private final ArrayList<TLRPC$TL_groupCallParticipant> participants = new ArrayList<>();
    private boolean visible = false;

    @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
    public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
        return false;
    }

    public GroupCallFullscreenAdapter(ChatObject.Call call, int i, GroupCallActivity groupCallActivity) {
        this.groupCall = call;
        this.currentAccount = i;
        this.activity = groupCallActivity;
    }

    public void setRenderersPool(ArrayList<GroupCallMiniTextureView> arrayList, GroupCallRenderersContainer groupCallRenderersContainer) {
        this.attachedRenderers = arrayList;
        this.renderersContainer = groupCallRenderersContainer;
    }

    public void setGroupCall(ChatObject.Call call) {
        this.groupCall = call;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RecyclerListView.Holder(new GroupCallUserCell(viewGroup.getContext()));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant;
        ChatObject.VideoParticipant videoParticipant;
        GroupCallUserCell groupCallUserCell = (GroupCallUserCell) viewHolder.itemView;
        ChatObject.VideoParticipant videoParticipant2 = groupCallUserCell.videoParticipant;
        if (i < this.videoParticipants.size()) {
            videoParticipant = this.videoParticipants.get(i);
            tLRPC$TL_groupCallParticipant = this.videoParticipants.get(i).participant;
        } else if (i - this.videoParticipants.size() >= this.participants.size()) {
            return;
        } else {
            videoParticipant = null;
            tLRPC$TL_groupCallParticipant = this.participants.get(i - this.videoParticipants.size());
        }
        groupCallUserCell.setParticipant(videoParticipant, tLRPC$TL_groupCallParticipant);
        if (videoParticipant2 != null && !videoParticipant2.equals(videoParticipant) && groupCallUserCell.attached && groupCallUserCell.getRenderer() != null) {
            groupCallUserCell.attachRenderer(false);
            if (videoParticipant == null) {
                return;
            }
            groupCallUserCell.attachRenderer(true);
        } else if (!groupCallUserCell.attached) {
        } else {
            if (groupCallUserCell.getRenderer() == null && videoParticipant != null && this.visible) {
                groupCallUserCell.attachRenderer(true);
            } else if (groupCallUserCell.getRenderer() == null || videoParticipant != null) {
            } else {
                groupCallUserCell.attachRenderer(false);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.videoParticipants.size() + this.participants.size();
    }

    public void setVisibility(RecyclerListView recyclerListView, boolean z) {
        this.visible = z;
        for (int i = 0; i < recyclerListView.getChildCount(); i++) {
            View childAt = recyclerListView.getChildAt(i);
            if (childAt instanceof GroupCallUserCell) {
                GroupCallUserCell groupCallUserCell = (GroupCallUserCell) childAt;
                if (groupCallUserCell.getVideoParticipant() != null) {
                    groupCallUserCell.attachRenderer(z);
                }
            }
        }
    }

    public void scrollTo(ChatObject.VideoParticipant videoParticipant, RecyclerListView recyclerListView) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerListView.getLayoutManager();
        if (linearLayoutManager == null) {
            return;
        }
        for (int i = 0; i < this.videoParticipants.size(); i++) {
            if (this.videoParticipants.get(i).equals(videoParticipant)) {
                linearLayoutManager.scrollToPositionWithOffset(i, AndroidUtilities.dp(13.0f));
                return;
            }
        }
    }

    /* loaded from: classes3.dex */
    public class GroupCallUserCell extends FrameLayout implements GroupCallStatusIcon.Callback {
        boolean attached;
        private BackupImageView avatarImageView;
        ValueAnimator colorAnimator;
        private TLRPC$Chat currentChat;
        private TLRPC$User currentUser;
        String drawingName;
        int lastColor;
        int lastWavesColor;
        RLottieImageView muteButton;
        String name;
        int nameWidth;
        TLRPC$TL_groupCallParticipant participant;
        long peerId;
        GroupCallMiniTextureView renderer;
        boolean selected;
        float selectionProgress;
        boolean skipInvalidate;
        GroupCallStatusIcon statusIcon;
        ChatObject.VideoParticipant videoParticipant;
        AvatarDrawable avatarDrawable = new AvatarDrawable();
        Paint backgroundPaint = new Paint(1);
        Paint selectionPaint = new Paint(1);
        float progress = 1.0f;
        TextPaint textPaint = new TextPaint(1);
        GroupCallUserCell.AvatarWavesDrawable avatarWavesDrawable = new GroupCallUserCell.AvatarWavesDrawable(AndroidUtilities.dp(26.0f), AndroidUtilities.dp(29.0f));

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public GroupCallUserCell(Context context) {
            super(context);
            GroupCallFullscreenAdapter.this = r9;
            this.avatarDrawable.setTextSize((int) (AndroidUtilities.dp(18.0f) / 1.15f));
            BackupImageView backupImageView = new BackupImageView(context);
            this.avatarImageView = backupImageView;
            backupImageView.setRoundRadius(AndroidUtilities.dp(20.0f));
            addView(this.avatarImageView, LayoutHelper.createFrame(40, 40.0f, 1, 0.0f, 9.0f, 0.0f, 9.0f));
            setWillNotDraw(false);
            this.backgroundPaint.setColor(Theme.getColor("voipgroup_listViewBackground"));
            this.selectionPaint.setColor(Theme.getColor("voipgroup_speakingText"));
            this.selectionPaint.setStyle(Paint.Style.STROKE);
            this.selectionPaint.setStrokeWidth(AndroidUtilities.dp(2.0f));
            this.textPaint.setColor(-1);
            AnonymousClass1 anonymousClass1 = new AnonymousClass1(context, r9);
            this.muteButton = anonymousClass1;
            anonymousClass1.setScaleType(ImageView.ScaleType.CENTER);
            addView(this.muteButton, LayoutHelper.createFrame(24, 24.0f));
        }

        /* renamed from: org.telegram.ui.Components.GroupCallFullscreenAdapter$GroupCallUserCell$1 */
        /* loaded from: classes3.dex */
        public class AnonymousClass1 extends RLottieImageView {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            AnonymousClass1(Context context, GroupCallFullscreenAdapter groupCallFullscreenAdapter) {
                super(context);
                GroupCallUserCell.this = r1;
            }

            @Override // android.view.View
            public void invalidate() {
                super.invalidate();
                GroupCallUserCell.this.invalidate();
            }
        }

        @Override // android.widget.FrameLayout, android.view.View
        protected void onMeasure(int i, int i2) {
            this.textPaint.setTextSize(AndroidUtilities.dp(12.0f));
            if (this.name != null) {
                int min = (int) Math.min(AndroidUtilities.dp(46.0f), this.textPaint.measureText(this.name));
                this.nameWidth = min;
                this.drawingName = TextUtils.ellipsize(this.name, this.textPaint, min, TextUtils.TruncateAt.END).toString();
            }
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), 1073741824), View.MeasureSpec.makeMeasureSpec(AndroidUtilities.dp(80.0f), 1073741824));
        }

        public void setParticipant(ChatObject.VideoParticipant videoParticipant, TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant) {
            this.videoParticipant = videoParticipant;
            this.participant = tLRPC$TL_groupCallParticipant;
            long j = this.peerId;
            long peerId = MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer);
            this.peerId = peerId;
            boolean z = true;
            if (peerId > 0) {
                TLRPC$User user = AccountInstance.getInstance(GroupCallFullscreenAdapter.this.currentAccount).getMessagesController().getUser(Long.valueOf(this.peerId));
                this.currentUser = user;
                this.currentChat = null;
                this.avatarDrawable.setInfo(user);
                this.name = UserObject.getFirstName(this.currentUser);
                this.avatarImageView.getImageReceiver().setCurrentAccount(GroupCallFullscreenAdapter.this.currentAccount);
                this.avatarImageView.setImage(ImageLocation.getForUser(this.currentUser, 1), "50_50", this.avatarDrawable, this.currentUser);
            } else {
                TLRPC$Chat chat = AccountInstance.getInstance(GroupCallFullscreenAdapter.this.currentAccount).getMessagesController().getChat(Long.valueOf(-this.peerId));
                this.currentChat = chat;
                this.currentUser = null;
                this.avatarDrawable.setInfo(chat);
                TLRPC$Chat tLRPC$Chat = this.currentChat;
                if (tLRPC$Chat != null) {
                    this.name = tLRPC$Chat.title;
                    this.avatarImageView.getImageReceiver().setCurrentAccount(GroupCallFullscreenAdapter.this.currentAccount);
                    this.avatarImageView.setImage(ImageLocation.getForChat(this.currentChat, 1), "50_50", this.avatarDrawable, this.currentChat);
                }
            }
            boolean z2 = j == this.peerId;
            if (videoParticipant == null) {
                if (GroupCallFullscreenAdapter.this.renderersContainer.fullscreenPeerId != MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer)) {
                    z = false;
                }
                this.selected = z;
            } else if (GroupCallFullscreenAdapter.this.renderersContainer.fullscreenParticipant != null) {
                this.selected = GroupCallFullscreenAdapter.this.renderersContainer.fullscreenParticipant.equals(videoParticipant);
            } else {
                this.selected = false;
            }
            if (!z2) {
                setSelectedProgress(this.selected ? 1.0f : 0.0f);
            }
            GroupCallStatusIcon groupCallStatusIcon = this.statusIcon;
            if (groupCallStatusIcon != null) {
                groupCallStatusIcon.setParticipant(tLRPC$TL_groupCallParticipant, z2);
                updateState(z2);
            }
        }

        @Override // android.view.View
        public void setAlpha(float f) {
            super.setAlpha(f);
        }

        public void setProgressToFullscreen(float f) {
            if (this.progress == f) {
                return;
            }
            this.progress = f;
            if (f == 1.0f) {
                this.avatarImageView.setTranslationY(0.0f);
                this.avatarImageView.setScaleX(1.0f);
                this.avatarImageView.setScaleY(1.0f);
                this.backgroundPaint.setAlpha(255);
                invalidate();
                GroupCallMiniTextureView groupCallMiniTextureView = this.renderer;
                if (groupCallMiniTextureView == null) {
                    return;
                }
                groupCallMiniTextureView.invalidate();
                return;
            }
            float top = (this.avatarImageView.getTop() + (this.avatarImageView.getMeasuredHeight() / 2.0f)) - (getMeasuredHeight() / 2.0f);
            float f2 = 1.0f - f;
            float dp = ((AndroidUtilities.dp(46.0f) / AndroidUtilities.dp(40.0f)) * f2) + (1.0f * f);
            this.avatarImageView.setTranslationY((-top) * f2);
            this.avatarImageView.setScaleX(dp);
            this.avatarImageView.setScaleY(dp);
            this.backgroundPaint.setAlpha((int) (f * 255.0f));
            invalidate();
            GroupCallMiniTextureView groupCallMiniTextureView2 = this.renderer;
            if (groupCallMiniTextureView2 == null) {
                return;
            }
            groupCallMiniTextureView2.invalidate();
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void dispatchDraw(Canvas canvas) {
            GroupCallMiniTextureView groupCallMiniTextureView = this.renderer;
            if (groupCallMiniTextureView != null && groupCallMiniTextureView.isFullyVisible() && !GroupCallFullscreenAdapter.this.activity.drawingForBlur) {
                drawSelection(canvas);
                return;
            }
            if (this.progress > 0.0f) {
                float measuredWidth = (getMeasuredWidth() / 2.0f) * (1.0f - this.progress);
                RectF rectF = AndroidUtilities.rectTmp;
                rectF.set(measuredWidth, measuredWidth, getMeasuredWidth() - measuredWidth, getMeasuredHeight() - measuredWidth);
                canvas.drawRoundRect(rectF, AndroidUtilities.dp(13.0f), AndroidUtilities.dp(13.0f), this.backgroundPaint);
                drawSelection(canvas);
            }
            float x = this.avatarImageView.getX() + (this.avatarImageView.getMeasuredWidth() / 2);
            float y = this.avatarImageView.getY() + (this.avatarImageView.getMeasuredHeight() / 2);
            this.avatarWavesDrawable.update();
            this.avatarWavesDrawable.draw(canvas, x, y, this);
            float dp = AndroidUtilities.dp(46.0f) / AndroidUtilities.dp(40.0f);
            float f = this.progress;
            float f2 = (dp * (1.0f - f)) + (f * 1.0f);
            this.avatarImageView.setScaleX(this.avatarWavesDrawable.getAvatarScale() * f2);
            this.avatarImageView.setScaleY(this.avatarWavesDrawable.getAvatarScale() * f2);
            super.dispatchDraw(canvas);
        }

        /* JADX WARN: Removed duplicated region for block: B:21:0x003a  */
        /* JADX WARN: Removed duplicated region for block: B:23:? A[RETURN, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        private void drawSelection(Canvas canvas) {
            boolean z = this.selected;
            if (z) {
                float f = this.selectionProgress;
                if (f != 1.0f) {
                    float f2 = f + 0.10666667f;
                    if (f2 > 1.0f) {
                        f2 = 1.0f;
                    } else {
                        invalidate();
                    }
                    setSelectedProgress(f2);
                    if (this.selectionProgress > 0.0f) {
                        return;
                    }
                    float measuredWidth = (getMeasuredWidth() / 2.0f) * (1.0f - this.progress);
                    RectF rectF = AndroidUtilities.rectTmp;
                    rectF.set(measuredWidth, measuredWidth, getMeasuredWidth() - measuredWidth, getMeasuredHeight() - measuredWidth);
                    rectF.inset(this.selectionPaint.getStrokeWidth() / 2.0f, this.selectionPaint.getStrokeWidth() / 2.0f);
                    canvas.drawRoundRect(rectF, AndroidUtilities.dp(12.0f), AndroidUtilities.dp(12.0f), this.selectionPaint);
                    return;
                }
            }
            if (!z) {
                float f3 = this.selectionProgress;
                if (f3 != 0.0f) {
                    float f4 = f3 - 0.10666667f;
                    if (f4 < 0.0f) {
                        f4 = 0.0f;
                    } else {
                        invalidate();
                    }
                    setSelectedProgress(f4);
                }
            }
            if (this.selectionProgress > 0.0f) {
            }
        }

        private void setSelectedProgress(float f) {
            if (this.selectionProgress != f) {
                this.selectionProgress = f;
                this.selectionPaint.setAlpha((int) (f * 255.0f));
            }
        }

        public long getPeerId() {
            return this.peerId;
        }

        public BackupImageView getAvatarImageView() {
            return this.avatarImageView;
        }

        public TLRPC$TL_groupCallParticipant getParticipant() {
            return this.participant;
        }

        public ChatObject.VideoParticipant getVideoParticipant() {
            return this.videoParticipant;
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (GroupCallFullscreenAdapter.this.visible && this.videoParticipant != null) {
                attachRenderer(true);
            }
            this.attached = true;
            if (GroupCallFullscreenAdapter.this.activity.statusIconPool.size() > 0) {
                this.statusIcon = GroupCallFullscreenAdapter.this.activity.statusIconPool.remove(GroupCallFullscreenAdapter.this.activity.statusIconPool.size() - 1);
            } else {
                this.statusIcon = new GroupCallStatusIcon();
            }
            this.statusIcon.setCallback(this);
            this.statusIcon.setImageView(this.muteButton);
            this.statusIcon.setParticipant(this.participant, false);
            updateState(false);
            this.avatarWavesDrawable.setShowWaves(this.statusIcon.isSpeaking(), this);
            if (!this.statusIcon.isSpeaking()) {
                this.avatarWavesDrawable.setAmplitude(0.0d);
            }
        }

        @Override // android.view.ViewGroup, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            attachRenderer(false);
            this.attached = false;
            if (this.statusIcon != null) {
                GroupCallFullscreenAdapter.this.activity.statusIconPool.add(this.statusIcon);
                this.statusIcon.setImageView(null);
                this.statusIcon.setCallback(null);
            }
            this.statusIcon = null;
        }

        public void attachRenderer(boolean z) {
            if (GroupCallFullscreenAdapter.this.activity.isDismissed()) {
                return;
            }
            if (z && this.renderer == null) {
                this.renderer = GroupCallMiniTextureView.getOrCreate(GroupCallFullscreenAdapter.this.attachedRenderers, GroupCallFullscreenAdapter.this.renderersContainer, null, this, null, this.videoParticipant, GroupCallFullscreenAdapter.this.groupCall, GroupCallFullscreenAdapter.this.activity);
            } else if (z) {
            } else {
                GroupCallMiniTextureView groupCallMiniTextureView = this.renderer;
                if (groupCallMiniTextureView != null) {
                    groupCallMiniTextureView.setSecondaryView(null);
                }
                this.renderer = null;
            }
        }

        public void setRenderer(GroupCallMiniTextureView groupCallMiniTextureView) {
            this.renderer = groupCallMiniTextureView;
        }

        public void drawOverlays(Canvas canvas) {
            if (this.drawingName != null) {
                canvas.save();
                int measuredWidth = ((getMeasuredWidth() - this.nameWidth) - AndroidUtilities.dp(24.0f)) / 2;
                this.textPaint.setAlpha((int) (this.progress * 255.0f * getAlpha()));
                canvas.drawText(this.drawingName, AndroidUtilities.dp(22.0f) + measuredWidth, AndroidUtilities.dp(69.0f), this.textPaint);
                canvas.restore();
                canvas.save();
                canvas.translate(measuredWidth, AndroidUtilities.dp(53.0f));
                if (this.muteButton.getDrawable() != null) {
                    this.muteButton.getDrawable().setAlpha((int) (this.progress * 255.0f * getAlpha()));
                    this.muteButton.draw(canvas);
                    this.muteButton.getDrawable().setAlpha(255);
                }
                canvas.restore();
            }
        }

        @Override // android.view.ViewGroup
        protected boolean drawChild(Canvas canvas, View view, long j) {
            if (view == this.muteButton) {
                return true;
            }
            return super.drawChild(canvas, view, j);
        }

        public float getProgressToFullscreen() {
            return this.progress;
        }

        public GroupCallMiniTextureView getRenderer() {
            return this.renderer;
        }

        public void setAmplitude(double d) {
            GroupCallStatusIcon groupCallStatusIcon = this.statusIcon;
            if (groupCallStatusIcon != null) {
                groupCallStatusIcon.setAmplitude(d);
            }
            this.avatarWavesDrawable.setAmplitude(d);
        }

        /* JADX WARN: Removed duplicated region for block: B:14:0x0035  */
        /* JADX WARN: Removed duplicated region for block: B:18:0x006c  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void updateState(boolean z) {
            int i;
            int i2;
            GroupCallStatusIcon groupCallStatusIcon = this.statusIcon;
            if (groupCallStatusIcon == null) {
                return;
            }
            groupCallStatusIcon.updateIcon(z);
            if (this.statusIcon.isMutedByMe()) {
                i2 = Theme.getColor("voipgroup_mutedByAdminIcon");
            } else if (this.statusIcon.isSpeaking()) {
                i2 = Theme.getColor("voipgroup_speakingText");
            } else {
                i2 = Theme.getColor("voipgroup_nameText");
                i = Theme.getColor("voipgroup_listeningText");
                if (z) {
                    ValueAnimator valueAnimator = this.colorAnimator;
                    if (valueAnimator != null) {
                        valueAnimator.removeAllListeners();
                        this.colorAnimator.cancel();
                    }
                    this.lastColor = i2;
                    this.lastWavesColor = i;
                    this.muteButton.setColorFilter(new PorterDuffColorFilter(i2, PorterDuff.Mode.MULTIPLY));
                    this.textPaint.setColor(this.lastColor);
                    this.selectionPaint.setColor(i);
                    this.avatarWavesDrawable.setColor(ColorUtils.setAlphaComponent(i, 38));
                    invalidate();
                    return;
                }
                int i3 = this.lastColor;
                int i4 = this.lastWavesColor;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                this.colorAnimator = ofFloat;
                ofFloat.addUpdateListener(new GroupCallFullscreenAdapter$GroupCallUserCell$$ExternalSyntheticLambda0(this, i3, i2, i4, i));
                this.colorAnimator.addListener(new AnonymousClass2(i2, i));
                this.colorAnimator.start();
                return;
            }
            i = i2;
            if (z) {
            }
        }

        public /* synthetic */ void lambda$updateState$0(int i, int i2, int i3, int i4, ValueAnimator valueAnimator) {
            this.lastColor = ColorUtils.blendARGB(i, i2, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            this.lastWavesColor = ColorUtils.blendARGB(i3, i4, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            this.muteButton.setColorFilter(new PorterDuffColorFilter(this.lastColor, PorterDuff.Mode.MULTIPLY));
            this.textPaint.setColor(this.lastColor);
            this.selectionPaint.setColor(this.lastWavesColor);
            this.avatarWavesDrawable.setColor(ColorUtils.setAlphaComponent(this.lastWavesColor, 38));
            invalidate();
        }

        /* renamed from: org.telegram.ui.Components.GroupCallFullscreenAdapter$GroupCallUserCell$2 */
        /* loaded from: classes3.dex */
        public class AnonymousClass2 extends AnimatorListenerAdapter {
            final /* synthetic */ int val$newColor;
            final /* synthetic */ int val$newWavesColor;

            AnonymousClass2(int i, int i2) {
                GroupCallUserCell.this = r1;
                this.val$newColor = i;
                this.val$newWavesColor = i2;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                GroupCallUserCell groupCallUserCell = GroupCallUserCell.this;
                groupCallUserCell.lastColor = this.val$newColor;
                groupCallUserCell.lastWavesColor = this.val$newWavesColor;
                groupCallUserCell.muteButton.setColorFilter(new PorterDuffColorFilter(GroupCallUserCell.this.lastColor, PorterDuff.Mode.MULTIPLY));
                GroupCallUserCell groupCallUserCell2 = GroupCallUserCell.this;
                groupCallUserCell2.textPaint.setColor(groupCallUserCell2.lastColor);
                GroupCallUserCell groupCallUserCell3 = GroupCallUserCell.this;
                groupCallUserCell3.selectionPaint.setColor(groupCallUserCell3.lastWavesColor);
                GroupCallUserCell groupCallUserCell4 = GroupCallUserCell.this;
                groupCallUserCell4.avatarWavesDrawable.setColor(ColorUtils.setAlphaComponent(groupCallUserCell4.lastWavesColor, 38));
            }
        }

        @Override // android.view.View
        public void invalidate() {
            if (this.skipInvalidate) {
                return;
            }
            this.skipInvalidate = true;
            super.invalidate();
            GroupCallMiniTextureView groupCallMiniTextureView = this.renderer;
            if (groupCallMiniTextureView == null) {
                GroupCallFullscreenAdapter.this.renderersContainer.invalidate();
            } else {
                groupCallMiniTextureView.invalidate();
            }
            this.skipInvalidate = false;
        }

        @Override // org.telegram.ui.Components.voip.GroupCallStatusIcon.Callback
        public void onStatusChanged() {
            this.avatarWavesDrawable.setShowWaves(this.statusIcon.isSpeaking(), this);
            updateState(true);
        }

        public boolean isRemoving(RecyclerListView recyclerListView) {
            return recyclerListView.getChildAdapterPosition(this) == -1;
        }
    }

    public void update(boolean z, RecyclerListView recyclerListView) {
        if (this.groupCall == null) {
            return;
        }
        if (z) {
            ArrayList arrayList = new ArrayList(this.participants);
            ArrayList arrayList2 = new ArrayList(this.videoParticipants);
            this.participants.clear();
            ChatObject.Call call = this.groupCall;
            if (!call.call.rtmp_stream) {
                this.participants.addAll(call.visibleParticipants);
            }
            this.videoParticipants.clear();
            ChatObject.Call call2 = this.groupCall;
            if (!call2.call.rtmp_stream) {
                this.videoParticipants.addAll(call2.visibleVideoParticipants);
            }
            DiffUtil.calculateDiff(new AnonymousClass1(arrayList2, arrayList)).dispatchUpdatesTo(this);
            AndroidUtilities.updateVisibleRows(recyclerListView);
            return;
        }
        this.participants.clear();
        ChatObject.Call call3 = this.groupCall;
        if (!call3.call.rtmp_stream) {
            this.participants.addAll(call3.visibleParticipants);
        }
        this.videoParticipants.clear();
        ChatObject.Call call4 = this.groupCall;
        if (!call4.call.rtmp_stream) {
            this.videoParticipants.addAll(call4.visibleVideoParticipants);
        }
        notifyDataSetChanged();
    }

    /* renamed from: org.telegram.ui.Components.GroupCallFullscreenAdapter$1 */
    /* loaded from: classes3.dex */
    public class AnonymousClass1 extends DiffUtil.Callback {
        final /* synthetic */ ArrayList val$oldParticipants;
        final /* synthetic */ ArrayList val$oldVideoParticipants;

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            return true;
        }

        AnonymousClass1(ArrayList arrayList, ArrayList arrayList2) {
            GroupCallFullscreenAdapter.this = r1;
            this.val$oldVideoParticipants = arrayList;
            this.val$oldParticipants = arrayList2;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return this.val$oldVideoParticipants.size() + this.val$oldParticipants.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return GroupCallFullscreenAdapter.this.videoParticipants.size() + GroupCallFullscreenAdapter.this.participants.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            TLRPC$TL_groupCallParticipant tLRPC$TL_groupCallParticipant;
            if (i < this.val$oldVideoParticipants.size() && i2 < GroupCallFullscreenAdapter.this.videoParticipants.size()) {
                return ((ChatObject.VideoParticipant) this.val$oldVideoParticipants.get(i)).equals(GroupCallFullscreenAdapter.this.videoParticipants.get(i2));
            }
            int size = i - this.val$oldVideoParticipants.size();
            int size2 = i2 - GroupCallFullscreenAdapter.this.videoParticipants.size();
            if (size2 >= 0 && size2 < GroupCallFullscreenAdapter.this.participants.size() && size >= 0 && size < this.val$oldParticipants.size()) {
                return MessageObject.getPeerId(((TLRPC$TL_groupCallParticipant) this.val$oldParticipants.get(size)).peer) == MessageObject.getPeerId(((TLRPC$TL_groupCallParticipant) GroupCallFullscreenAdapter.this.participants.get(size2)).peer);
            }
            if (i < this.val$oldVideoParticipants.size()) {
                tLRPC$TL_groupCallParticipant = ((ChatObject.VideoParticipant) this.val$oldVideoParticipants.get(i)).participant;
            } else {
                tLRPC$TL_groupCallParticipant = (TLRPC$TL_groupCallParticipant) this.val$oldParticipants.get(size);
            }
            return MessageObject.getPeerId(tLRPC$TL_groupCallParticipant.peer) == MessageObject.getPeerId((i2 < GroupCallFullscreenAdapter.this.videoParticipants.size() ? ((ChatObject.VideoParticipant) GroupCallFullscreenAdapter.this.videoParticipants.get(i2)).participant : (TLRPC$TL_groupCallParticipant) GroupCallFullscreenAdapter.this.participants.get(size2)).peer);
        }
    }
}
