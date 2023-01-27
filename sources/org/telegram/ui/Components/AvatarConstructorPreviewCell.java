package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC$Document;
import org.telegram.tgnet.TLRPC$StickerSetCovered;
import org.telegram.tgnet.TLRPC$TL_emojiList;
import org.telegram.tgnet.TLRPC$TL_messages_stickerSet;
import org.telegram.tgnet.TLRPC$TL_stickerSetFullCovered;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.AvatarConstructorFragment;
/* loaded from: classes3.dex */
public class AvatarConstructorPreviewCell extends FrameLayout {
    private AnimatedEmojiDrawable animatedEmojiDrawable;
    int backgroundIndex;
    private final int currentAccount;
    Drawable currentBackgroundDrawable;
    BackupImageView currentImage;
    int emojiIndex;
    TLRPC$TL_emojiList emojiList;
    public final boolean forUser;
    Drawable nextBackgroundDrawable;
    BackupImageView nextImage;
    float progressToNext;
    Runnable scheduleSwitchToNextRunnable;
    TextView textView;

    public AvatarConstructorPreviewCell(Context context, boolean z) {
        super(context);
        int i = UserConfig.selectedAccount;
        this.currentAccount = i;
        this.backgroundIndex = 0;
        this.emojiIndex = 0;
        this.progressToNext = 1.0f;
        this.scheduleSwitchToNextRunnable = new Runnable() { // from class: org.telegram.ui.Components.AvatarConstructorPreviewCell.1
            @Override // java.lang.Runnable
            public void run() {
                AndroidUtilities.runOnUIThread(AvatarConstructorPreviewCell.this.scheduleSwitchToNextRunnable, 1000L);
                TLRPC$TL_emojiList tLRPC$TL_emojiList = AvatarConstructorPreviewCell.this.emojiList;
                if (tLRPC$TL_emojiList == null || tLRPC$TL_emojiList.document_id.isEmpty()) {
                    return;
                }
                AvatarConstructorPreviewCell avatarConstructorPreviewCell = AvatarConstructorPreviewCell.this;
                if (avatarConstructorPreviewCell.progressToNext != 1.0f) {
                    return;
                }
                int i2 = avatarConstructorPreviewCell.emojiIndex + 1;
                avatarConstructorPreviewCell.emojiIndex = i2;
                avatarConstructorPreviewCell.backgroundIndex++;
                if (i2 > avatarConstructorPreviewCell.emojiList.document_id.size() - 1) {
                    AvatarConstructorPreviewCell.this.emojiIndex = 0;
                }
                AvatarConstructorPreviewCell avatarConstructorPreviewCell2 = AvatarConstructorPreviewCell.this;
                if (avatarConstructorPreviewCell2.backgroundIndex > Theme.keys_avatar_background.length - 1) {
                    avatarConstructorPreviewCell2.backgroundIndex = 0;
                }
                int i3 = AvatarConstructorPreviewCell.this.currentAccount;
                AvatarConstructorPreviewCell avatarConstructorPreviewCell3 = AvatarConstructorPreviewCell.this;
                avatarConstructorPreviewCell2.animatedEmojiDrawable = new AnimatedEmojiDrawable(4, i3, avatarConstructorPreviewCell3.emojiList.document_id.get(avatarConstructorPreviewCell3.emojiIndex).longValue());
                AvatarConstructorPreviewCell avatarConstructorPreviewCell4 = AvatarConstructorPreviewCell.this;
                avatarConstructorPreviewCell4.nextImage.setAnimatedEmojiDrawable(avatarConstructorPreviewCell4.animatedEmojiDrawable);
                AvatarConstructorPreviewCell.this.nextBackgroundDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Theme.getColor(Theme.keys_avatar_background[AvatarConstructorPreviewCell.this.backgroundIndex]), Theme.getColor(Theme.keys_avatar_background2[AvatarConstructorPreviewCell.this.backgroundIndex])});
                AvatarConstructorPreviewCell avatarConstructorPreviewCell5 = AvatarConstructorPreviewCell.this;
                avatarConstructorPreviewCell5.progressToNext = 0.0f;
                avatarConstructorPreviewCell5.invalidate();
            }
        };
        this.forUser = z;
        if (z) {
            this.emojiList = MediaDataController.getInstance(i).profileAvatarConstructorDefault;
        } else {
            this.emojiList = MediaDataController.getInstance(i).groupAvatarConstructorDefault;
        }
        TLRPC$TL_emojiList tLRPC$TL_emojiList = this.emojiList;
        if (tLRPC$TL_emojiList == null || tLRPC$TL_emojiList.document_id.isEmpty()) {
            ArrayList<TLRPC$TL_messages_stickerSet> stickerSets = MediaDataController.getInstance(i).getStickerSets(5);
            this.emojiList = new TLRPC$TL_emojiList();
            if (stickerSets.isEmpty()) {
                ArrayList<TLRPC$StickerSetCovered> featuredEmojiSets = MediaDataController.getInstance(i).getFeaturedEmojiSets();
                for (int i2 = 0; i2 < featuredEmojiSets.size(); i2++) {
                    TLRPC$StickerSetCovered tLRPC$StickerSetCovered = featuredEmojiSets.get(i2);
                    TLRPC$Document tLRPC$Document = tLRPC$StickerSetCovered.cover;
                    if (tLRPC$Document != null) {
                        this.emojiList.document_id.add(Long.valueOf(tLRPC$Document.id));
                    } else if (tLRPC$StickerSetCovered instanceof TLRPC$TL_stickerSetFullCovered) {
                        TLRPC$TL_stickerSetFullCovered tLRPC$TL_stickerSetFullCovered = (TLRPC$TL_stickerSetFullCovered) tLRPC$StickerSetCovered;
                        if (!tLRPC$TL_stickerSetFullCovered.documents.isEmpty()) {
                            this.emojiList.document_id.add(Long.valueOf(tLRPC$TL_stickerSetFullCovered.documents.get(0).id));
                        }
                    }
                }
            } else {
                for (int i3 = 0; i3 < stickerSets.size(); i3++) {
                    TLRPC$TL_messages_stickerSet tLRPC$TL_messages_stickerSet = stickerSets.get(i3);
                    if (!tLRPC$TL_messages_stickerSet.documents.isEmpty()) {
                        this.emojiList.document_id.add(Long.valueOf(tLRPC$TL_messages_stickerSet.documents.get(Math.abs(Utilities.fastRandom.nextInt() % tLRPC$TL_messages_stickerSet.documents.size())).id));
                    }
                }
            }
        }
        this.currentImage = new BackupImageView(context);
        this.nextImage = new BackupImageView(context);
        addView(this.currentImage, LayoutHelper.createFrame(50, 50, 1));
        addView(this.nextImage, LayoutHelper.createFrame(50, 50, 1));
        TLRPC$TL_emojiList tLRPC$TL_emojiList2 = this.emojiList;
        if (tLRPC$TL_emojiList2 != null && !tLRPC$TL_emojiList2.document_id.isEmpty()) {
            AnimatedEmojiDrawable animatedEmojiDrawable = new AnimatedEmojiDrawable(4, this.currentAccount, this.emojiList.document_id.get(0).longValue());
            this.animatedEmojiDrawable = animatedEmojiDrawable;
            this.currentImage.setAnimatedEmojiDrawable(animatedEmojiDrawable);
        }
        this.currentBackgroundDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{Theme.getColor(Theme.keys_avatar_background[this.backgroundIndex]), Theme.getColor(Theme.keys_avatar_background2[this.backgroundIndex])});
        TextView textView = new TextView(context);
        this.textView = textView;
        textView.setTextSize(1, 12.0f);
        this.textView.setTextColor(Theme.getColor("avatar_text"));
        this.textView.setTypeface(AndroidUtilities.getTypeface(AndroidUtilities.TYPEFACE_ROBOTO_MEDIUM));
        this.textView.setGravity(17);
        this.textView.setText(LocaleController.getString("UseEmoji", R.string.UseEmoji));
        addView(this.textView, LayoutHelper.createFrame(-1, 28.0f, 80, 10.0f, 10.0f, 10.0f, 10.0f));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int top = this.textView.getTop();
        int i3 = (int) (top * 0.7f);
        int i4 = (int) ((top - i3) * 0.7f);
        ViewGroup.LayoutParams layoutParams = this.currentImage.getLayoutParams();
        this.currentImage.getLayoutParams().height = i3;
        layoutParams.width = i3;
        ViewGroup.LayoutParams layoutParams2 = this.nextImage.getLayoutParams();
        this.nextImage.getLayoutParams().height = i3;
        layoutParams2.width = i3;
        ((FrameLayout.LayoutParams) this.currentImage.getLayoutParams()).topMargin = i4;
        ((FrameLayout.LayoutParams) this.nextImage.getLayoutParams()).topMargin = i4;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void dispatchDraw(Canvas canvas) {
        BackupImageView backupImageView;
        Drawable drawable = this.currentBackgroundDrawable;
        if (drawable != null) {
            drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }
        Drawable drawable2 = this.nextBackgroundDrawable;
        if (drawable2 != null) {
            drawable2.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
        }
        float f = this.progressToNext;
        if (f == 1.0f) {
            this.currentBackgroundDrawable.setAlpha(255);
            this.currentBackgroundDrawable.draw(canvas);
            this.currentImage.setAlpha(1.0f);
            this.currentImage.setScaleX(1.0f);
            this.currentImage.setScaleY(1.0f);
            this.nextImage.setAlpha(0.0f);
        } else {
            float interpolation = CubicBezierInterpolator.DEFAULT.getInterpolation(f);
            this.currentBackgroundDrawable.setAlpha(255);
            this.currentBackgroundDrawable.draw(canvas);
            this.nextBackgroundDrawable.setAlpha((int) (255.0f * interpolation));
            this.nextBackgroundDrawable.draw(canvas);
            this.progressToNext += 0.064f;
            float f2 = 1.0f - interpolation;
            this.currentImage.setAlpha(f2);
            this.currentImage.setScaleX(f2);
            this.currentImage.setScaleY(f2);
            this.currentImage.setPivotY(0.0f);
            this.nextImage.setAlpha(interpolation);
            this.nextImage.setScaleX(interpolation);
            this.nextImage.setScaleY(interpolation);
            this.nextImage.setPivotY(backupImageView.getMeasuredHeight());
            if (this.progressToNext > 1.0f) {
                this.progressToNext = 1.0f;
                this.currentBackgroundDrawable = this.nextBackgroundDrawable;
                BackupImageView backupImageView2 = this.currentImage;
                this.currentImage = this.nextImage;
                this.nextImage = backupImageView2;
            }
            invalidate();
        }
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AndroidUtilities.runOnUIThread(this.scheduleSwitchToNextRunnable, 1000L);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AndroidUtilities.cancelRunOnUIThread(this.scheduleSwitchToNextRunnable);
    }

    public AvatarConstructorFragment.BackgroundGradient getBackgroundGradient() {
        AvatarConstructorFragment.BackgroundGradient backgroundGradient = new AvatarConstructorFragment.BackgroundGradient();
        backgroundGradient.color1 = Theme.getColor(Theme.keys_avatar_background[this.backgroundIndex]);
        backgroundGradient.color2 = Theme.getColor(Theme.keys_avatar_background2[this.backgroundIndex]);
        return backgroundGradient;
    }

    public AnimatedEmojiDrawable getAnimatedEmoji() {
        return this.animatedEmojiDrawable;
    }
}
