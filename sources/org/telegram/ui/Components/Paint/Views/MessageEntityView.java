package org.telegram.ui.Components.Paint.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManagerFixed;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MediaDataController;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.UserConfig;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.ChatActionCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.Components.BlurringShader;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.MessageBackgroundDrawable;
import org.telegram.ui.Components.Paint.Views.EntityView;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Stories.recorder.PreviewView;
import org.telegram.ui.Stories.recorder.StoryEntry;

/* loaded from: classes3.dex */
public abstract class MessageEntityView extends EntityView {
    private final BlurringShader.BlurManager blurManager;
    private boolean clipVideoMessageForBitmap;
    public final FrameLayout container;
    private final SparseIntArray currentColors;
    public boolean firstMeasure;
    private MessageObject.GroupedMessages groupedMessages;
    private boolean isDark;
    public final RecyclerListView listView;
    public final ArrayList messageObjects;
    private Theme.MessageDrawable msgInDrawable;
    private Theme.MessageDrawable msgInDrawableSelected;
    private Theme.MessageDrawable msgMediaInDrawable;
    private Theme.MessageDrawable msgMediaInDrawableSelected;
    private Theme.MessageDrawable msgMediaOutDrawable;
    private Theme.MessageDrawable msgMediaOutDrawableSelected;
    private Theme.MessageDrawable msgOutDrawable;
    private Theme.MessageDrawable msgOutDrawableSelected;
    public final Theme.ResourcesProvider resourcesProvider;
    private TextureView textureView;
    private boolean textureViewActive;
    private boolean usesBackgroundPaint;
    private int videoHeight;
    private int videoWidth;

    public class MessageEntityViewSelectionView extends EntityView.SelectionView {
        private final Paint clearPaint;
        private Path path;

        public MessageEntityViewSelectionView(Context context) {
            super(context);
            Paint paint = new Paint(1);
            this.clearPaint = paint;
            this.path = new Path();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int saveCount = canvas.getSaveCount();
            float showAlpha = getShowAlpha();
            if (showAlpha <= 0.0f) {
                return;
            }
            if (showAlpha < 1.0f) {
                canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (showAlpha * 255.0f), 31);
            }
            float dp = AndroidUtilities.dp(2.0f);
            float dpf2 = AndroidUtilities.dpf2(5.66f);
            float dp2 = dp + dpf2 + AndroidUtilities.dp(15.0f);
            float f = dp2 * 2.0f;
            float measuredWidth = getMeasuredWidth() - f;
            float measuredHeight = getMeasuredHeight() - f;
            RectF rectF = AndroidUtilities.rectTmp;
            float f2 = dp2 + measuredWidth;
            float f3 = dp2 + measuredHeight;
            rectF.set(dp2, dp2, f2, f3);
            float dp3 = AndroidUtilities.dp(12.0f);
            float min = Math.min(dp3, measuredWidth / 2.0f);
            float f4 = measuredHeight / 2.0f;
            float min2 = Math.min(dp3, f4);
            this.path.rewind();
            float f5 = min * 2.0f;
            float f6 = dp2 + f5;
            float f7 = 2.0f * min2;
            float f8 = dp2 + f7;
            rectF.set(dp2, dp2, f6, f8);
            this.path.arcTo(rectF, 180.0f, 90.0f);
            float f9 = f2 - f5;
            rectF.set(f9, dp2, f2, f8);
            this.path.arcTo(rectF, 270.0f, 90.0f);
            canvas.drawPath(this.path, this.paint);
            this.path.rewind();
            float f10 = f3 - f7;
            rectF.set(dp2, f10, f6, f3);
            this.path.arcTo(rectF, 180.0f, -90.0f);
            rectF.set(f9, f10, f2, f3);
            this.path.arcTo(rectF, 90.0f, -90.0f);
            canvas.drawPath(this.path, this.paint);
            float f11 = dp2 + f4;
            canvas.drawCircle(dp2, f11, dpf2, this.dotStrokePaint);
            canvas.drawCircle(dp2, f11, (dpf2 - AndroidUtilities.dp(1.0f)) + 1.0f, this.dotPaint);
            canvas.drawCircle(f2, f11, dpf2, this.dotStrokePaint);
            canvas.drawCircle(f2, f11, (dpf2 - AndroidUtilities.dp(1.0f)) + 1.0f, this.dotPaint);
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), NotificationCenter.liveLocationsChanged, 31);
            float f12 = dp2 + min2;
            float f13 = f3 - min2;
            canvas.drawLine(dp2, f12, dp2, f13, this.paint);
            canvas.drawLine(f2, f12, f2, f13, this.paint);
            canvas.drawCircle(f2, f11, (AndroidUtilities.dp(1.0f) + dpf2) - 1.0f, this.clearPaint);
            canvas.drawCircle(dp2, f11, (dpf2 + AndroidUtilities.dp(1.0f)) - 1.0f, this.clearPaint);
            canvas.restoreToCount(saveCount);
        }

        @Override // org.telegram.ui.Components.Paint.Views.EntityView.SelectionView
        protected int pointInsideHandle(float f, float f2) {
            float dp = AndroidUtilities.dp(1.0f);
            float dp2 = AndroidUtilities.dp(19.5f);
            float f3 = dp + dp2;
            float f4 = f3 * 2.0f;
            float measuredWidth = getMeasuredWidth() - f4;
            float measuredHeight = getMeasuredHeight() - f4;
            float f5 = (measuredHeight / 2.0f) + f3;
            if (f > f3 - dp2 && f2 > f5 - dp2 && f < f3 + dp2 && f2 < f5 + dp2) {
                return 1;
            }
            float f6 = f3 + measuredWidth;
            if (f <= f6 - dp2 || f2 <= f5 - dp2 || f >= f6 + dp2 || f2 >= f5 + dp2) {
                return (f <= f3 || f >= measuredWidth || f2 <= f3 || f2 >= measuredHeight) ? 0 : 3;
            }
            return 2;
        }
    }

    public MessageEntityView(final Context context, Point point, float f, float f2, ArrayList arrayList, final BlurringShader.BlurManager blurManager, final boolean z, final PreviewView.TextureViewHolder textureViewHolder) {
        super(context, point);
        TLRPC.MessageFwdHeader messageFwdHeader;
        TLRPC.Peer peer;
        this.messageObjects = new ArrayList();
        this.videoWidth = 1;
        this.videoHeight = 1;
        this.firstMeasure = true;
        this.isDark = Theme.isCurrentThemeDark();
        this.currentColors = new SparseIntArray();
        this.resourcesProvider = new Theme.ResourcesProvider() { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView.7
            public final Paint chat_actionBackgroundGradientDarkenPaint;
            public final Paint chat_actionBackgroundPaint;
            public final Paint chat_actionBackgroundSelectedPaint;
            public final TextPaint chat_actionTextPaint;
            public final TextPaint chat_actionTextPaint2;
            public final TextPaint chat_botButtonPaint;

            {
                TextPaint textPaint = new TextPaint();
                this.chat_actionTextPaint = textPaint;
                TextPaint textPaint2 = new TextPaint();
                this.chat_actionTextPaint2 = textPaint2;
                TextPaint textPaint3 = new TextPaint();
                this.chat_botButtonPaint = textPaint3;
                this.chat_actionBackgroundPaint = new Paint(3);
                this.chat_actionBackgroundSelectedPaint = new Paint(3);
                Paint paint = new Paint(3);
                this.chat_actionBackgroundGradientDarkenPaint = paint;
                textPaint.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
                textPaint2.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
                textPaint3.setTextSize(AndroidUtilities.dp(15.0f));
                textPaint3.setTypeface(AndroidUtilities.bold());
                paint.setColor(352321536);
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ void applyServiceShaderMatrix(int i, int i2, float f3, float f4) {
                Theme.applyServiceShaderMatrix(i, i2, f3, f4);
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ ColorFilter getAnimatedEmojiColorFilter() {
                ColorFilter colorFilter;
                colorFilter = Theme.chat_animatedEmojiTextColorFilter;
                return colorFilter;
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public int getColor(int i) {
                return MessageEntityView.this.currentColors.get(i, Theme.getColor(i));
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ int getColorOrDefault(int i) {
                int color;
                color = getColor(i);
                return color;
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ int getCurrentColor(int i) {
                int color;
                color = getColor(i);
                return color;
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public Drawable getDrawable(String str) {
                if (str.equals("drawableMsgIn")) {
                    if (MessageEntityView.this.msgInDrawable == null) {
                        MessageEntityView.this.msgInDrawable = new Theme.MessageDrawable(0, false, false, MessageEntityView.this.resourcesProvider);
                    }
                    return MessageEntityView.this.msgInDrawable;
                }
                if (str.equals("drawableMsgInSelected")) {
                    if (MessageEntityView.this.msgInDrawableSelected == null) {
                        MessageEntityView.this.msgInDrawableSelected = new Theme.MessageDrawable(0, false, true, MessageEntityView.this.resourcesProvider);
                    }
                    return MessageEntityView.this.msgInDrawableSelected;
                }
                if (str.equals("drawableMsgOut")) {
                    if (MessageEntityView.this.msgOutDrawable == null) {
                        MessageEntityView.this.msgOutDrawable = new Theme.MessageDrawable(0, true, false, MessageEntityView.this.resourcesProvider);
                    }
                    return MessageEntityView.this.msgOutDrawable;
                }
                if (str.equals("drawableMsgOutSelected")) {
                    if (MessageEntityView.this.msgOutDrawableSelected == null) {
                        MessageEntityView.this.msgOutDrawableSelected = new Theme.MessageDrawable(0, true, true, MessageEntityView.this.resourcesProvider);
                    }
                    return MessageEntityView.this.msgOutDrawableSelected;
                }
                if (str.equals("drawableMsgInMedia")) {
                    if (MessageEntityView.this.msgMediaInDrawable == null) {
                        MessageEntityView.this.msgMediaInDrawable = new Theme.MessageDrawable(1, false, false, MessageEntityView.this.resourcesProvider);
                    }
                    MessageEntityView.this.msgMediaInDrawable.invalidateSelf();
                    return MessageEntityView.this.msgMediaInDrawable;
                }
                if (str.equals("drawableMsgInMediaSelected")) {
                    if (MessageEntityView.this.msgMediaInDrawableSelected == null) {
                        MessageEntityView.this.msgMediaInDrawableSelected = new Theme.MessageDrawable(1, false, true, MessageEntityView.this.resourcesProvider);
                    }
                    return MessageEntityView.this.msgMediaInDrawableSelected;
                }
                if (str.equals("drawableMsgOutMedia")) {
                    if (MessageEntityView.this.msgMediaOutDrawable == null) {
                        MessageEntityView.this.msgMediaOutDrawable = new Theme.MessageDrawable(1, true, false, MessageEntityView.this.resourcesProvider);
                    }
                    return MessageEntityView.this.msgMediaOutDrawable;
                }
                if (!str.equals("drawableMsgOutMediaSelected")) {
                    return Theme.getThemeDrawable(str);
                }
                if (MessageEntityView.this.msgMediaOutDrawableSelected == null) {
                    MessageEntityView.this.msgMediaOutDrawableSelected = new Theme.MessageDrawable(1, true, true, MessageEntityView.this.resourcesProvider);
                }
                return MessageEntityView.this.msgMediaOutDrawableSelected;
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public Paint getPaint(String str) {
                Paint themePaint;
                str.hashCode();
                switch (str) {
                    case "paintChatActionText2":
                        return this.chat_actionTextPaint2;
                    case "paintChatBotButton":
                        return this.chat_botButtonPaint;
                    case "paintChatActionBackgroundDarken":
                        return this.chat_actionBackgroundGradientDarkenPaint;
                    case "paintChatActionBackgroundSelected":
                        return this.chat_actionBackgroundSelectedPaint;
                    case "paintChatActionText":
                        return this.chat_actionTextPaint;
                    default:
                        themePaint = Theme.getThemePaint(str);
                        return themePaint;
                }
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ boolean hasGradientService() {
                return Theme.ResourcesProvider.-CC.$default$hasGradientService(this);
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public boolean isDark() {
                return MessageEntityView.this.isDark;
            }

            @Override // org.telegram.ui.ActionBar.Theme.ResourcesProvider
            public /* synthetic */ void setAnimatedColor(int i, int i2) {
                Theme.ResourcesProvider.-CC.$default$setAnimatedColor(this, i, i2);
            }
        };
        this.blurManager = blurManager;
        setRotation(f);
        setScale(f2);
        for (int i = 0; i < arrayList.size(); i++) {
            MessageObject messageObject = (MessageObject) arrayList.get(i);
            TLRPC.Message message = messageObject.messageOwner;
            int i2 = message.date;
            TLRPC.Message copyMessage = copyMessage(message);
            Boolean useForwardForRepost = StoryEntry.useForwardForRepost(messageObject);
            if (useForwardForRepost != null && useForwardForRepost.booleanValue() && (messageFwdHeader = copyMessage.fwd_from) != null && (peer = messageFwdHeader.from_id) != null) {
                copyMessage.from_id = peer;
                copyMessage.peer_id = peer;
                copyMessage.flags &= -5;
                copyMessage.fwd_from = null;
            }
            copyMessage.voiceTranscriptionOpen = false;
            int i3 = messageObject.currentAccount;
            MessageObject messageObject2 = new MessageObject(i3, copyMessage, messageObject.replyMessageObject, MessagesController.getInstance(i3).getUsers(), MessagesController.getInstance(messageObject.currentAccount).getChats(), null, null, true, true, 0L, true, z, false);
            messageObject2.setType();
            this.messageObjects.add(messageObject2);
        }
        this.groupedMessages = null;
        if (this.messageObjects.size() > 1) {
            MessageObject.GroupedMessages groupedMessages = new MessageObject.GroupedMessages();
            this.groupedMessages = groupedMessages;
            groupedMessages.messages.addAll(this.messageObjects);
            this.groupedMessages.groupId = ((MessageObject) this.messageObjects.get(0)).getGroupId();
            this.groupedMessages.calculate();
        }
        FrameLayout frameLayout = new FrameLayout(context) { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView.1
            private final Matrix videoMatrix = new Matrix();
            private final float[] radii = new float[8];
            private final Path clipPath = new Path();

            @Override // android.view.ViewGroup
            protected boolean drawChild(Canvas canvas, View view, long j) {
                ImageReceiver photoImage;
                if (view != MessageEntityView.this.textureView) {
                    return super.drawChild(canvas, view, j);
                }
                ChatMessageCell cell = MessageEntityView.this.getCell();
                if (cell == null || (photoImage = cell.getPhotoImage()) == null) {
                    return false;
                }
                this.videoMatrix.reset();
                float max = Math.max(photoImage.getImageWidth() / MessageEntityView.this.videoWidth, photoImage.getImageHeight() / MessageEntityView.this.videoHeight);
                this.videoMatrix.postScale((MessageEntityView.this.videoWidth / MessageEntityView.this.textureView.getWidth()) * max, (MessageEntityView.this.videoHeight / MessageEntityView.this.textureView.getHeight()) * max);
                this.videoMatrix.postTranslate(((MessageEntityView.this.listView.getX() + cell.getX()) + photoImage.getCenterX()) - ((MessageEntityView.this.videoWidth * max) / 2.0f), ((MessageEntityView.this.listView.getY() + cell.getY()) + photoImage.getCenterY()) - ((MessageEntityView.this.videoHeight * max) / 2.0f));
                MessageEntityView.this.textureView.setTransform(this.videoMatrix);
                canvas.save();
                this.clipPath.rewind();
                AndroidUtilities.rectTmp.set(MessageEntityView.this.listView.getX() + cell.getX() + photoImage.getImageX(), MessageEntityView.this.listView.getY() + cell.getY() + photoImage.getImageY(), MessageEntityView.this.listView.getX() + cell.getX() + photoImage.getImageX2(), MessageEntityView.this.listView.getY() + cell.getY() + photoImage.getImageY2());
                for (int i4 = 0; i4 < photoImage.getRoundRadius().length; i4++) {
                    int i5 = i4 * 2;
                    this.radii[i5] = photoImage.getRoundRadius()[i4];
                    this.radii[i5 + 1] = photoImage.getRoundRadius()[i4];
                }
                this.clipPath.addRoundRect(AndroidUtilities.rectTmp, this.radii, Path.Direction.CW);
                canvas.clipPath(this.clipPath);
                boolean drawChild = super.drawChild(canvas, view, j);
                canvas.restore();
                return drawChild;
            }

            @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
            protected void onLayout(boolean z2, int i4, int i5, int i6, int i7) {
                int left;
                int boundsRight;
                int measuredWidth = MessageEntityView.this.listView.getMeasuredWidth();
                int i8 = 0;
                for (int i9 = 0; i9 < MessageEntityView.this.listView.getChildCount(); i9++) {
                    View childAt = MessageEntityView.this.listView.getChildAt(i9);
                    int left2 = childAt.getLeft();
                    int right = childAt.getRight();
                    if (childAt instanceof ChatMessageCell) {
                        ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                        left2 = childAt.getLeft() + chatMessageCell.getBoundsLeft();
                        left = childAt.getLeft();
                        boundsRight = chatMessageCell.getBoundsRight();
                    } else if (childAt instanceof ChatActionCell) {
                        ChatActionCell chatActionCell = (ChatActionCell) childAt;
                        left2 = childAt.getLeft() + chatActionCell.getBoundsLeft();
                        left = childAt.getLeft();
                        boundsRight = chatActionCell.getBoundsRight();
                    } else {
                        measuredWidth = Math.min(left2, measuredWidth);
                        i8 = Math.max(right, i8);
                    }
                    right = boundsRight + left;
                    measuredWidth = Math.min(left2, measuredWidth);
                    i8 = Math.max(right, i8);
                }
                RecyclerListView recyclerListView = MessageEntityView.this.listView;
                recyclerListView.layout(-measuredWidth, 0, recyclerListView.getMeasuredWidth() - measuredWidth, MessageEntityView.this.listView.getMeasuredHeight());
                if (MessageEntityView.this.textureView != null) {
                    MessageEntityView.this.textureView.layout(0, 0, getMeasuredWidth(), MessageEntityView.this.listView.getMeasuredHeight());
                }
            }

            @Override // android.widget.FrameLayout, android.view.View
            protected void onMeasure(int i4, int i5) {
                int left;
                int boundsRight;
                MessageEntityView.this.listView.measure(i4, View.MeasureSpec.makeMeasureSpec(0, 0));
                if (MessageEntityView.this.textureView != null) {
                    MessageEntityView.this.textureView.measure(View.MeasureSpec.makeMeasureSpec(MessageEntityView.this.listView.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(MessageEntityView.this.listView.getMeasuredHeight(), 1073741824));
                }
                int measuredWidth = MessageEntityView.this.listView.getMeasuredWidth();
                int i6 = 0;
                for (int i7 = 0; i7 < MessageEntityView.this.listView.getChildCount(); i7++) {
                    View childAt = MessageEntityView.this.listView.getChildAt(i7);
                    int left2 = childAt.getLeft();
                    int right = childAt.getRight();
                    if (childAt instanceof ChatMessageCell) {
                        ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                        left2 = childAt.getLeft() + chatMessageCell.getBoundsLeft();
                        left = childAt.getLeft();
                        boundsRight = chatMessageCell.getBoundsRight();
                    } else if (childAt instanceof ChatActionCell) {
                        ChatActionCell chatActionCell = (ChatActionCell) childAt;
                        left2 = childAt.getLeft() + chatActionCell.getBoundsLeft();
                        left = childAt.getLeft();
                        boundsRight = chatActionCell.getBoundsRight();
                    } else {
                        measuredWidth = Math.min(left2, measuredWidth);
                        i6 = Math.max(right, i6);
                    }
                    right = boundsRight + left;
                    measuredWidth = Math.min(left2, measuredWidth);
                    i6 = Math.max(right, i6);
                }
                setMeasuredDimension(i6 - measuredWidth, MessageEntityView.this.listView.getMeasuredHeight());
            }
        };
        this.container = frameLayout;
        addView(frameLayout, LayoutHelper.createFrame(-1, -1.0f));
        RecyclerListView recyclerListView = new RecyclerListView(context, this.resourcesProvider) { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView.2
            private final ArrayList drawTimeAfter = new ArrayList();
            private final ArrayList drawNamesAfter = new ArrayList();
            private final ArrayList drawCaptionAfter = new ArrayList();
            private final ArrayList drawReactionsAfter = new ArrayList();
            private final ArrayList drawingGroups = new ArrayList(10);

            private void drawChatBackgroundElements(Canvas canvas) {
                float f3;
                int i4;
                int i5;
                boolean z2;
                MessageObject.GroupedMessages currentMessagesGroup;
                int i6;
                int childCount = getChildCount();
                int i7 = 0;
                MessageObject.GroupedMessages groupedMessages2 = null;
                while (true) {
                    f3 = 0.0f;
                    i4 = 4;
                    i5 = 2;
                    z2 = true;
                    if (i7 >= childCount) {
                        break;
                    }
                    View childAt = getChildAt(i7);
                    if (childAt.getVisibility() != 4) {
                        if (childAt instanceof ChatMessageCell) {
                            ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                            MessageObject.GroupedMessages currentMessagesGroup2 = chatMessageCell.getCurrentMessagesGroup();
                            if (currentMessagesGroup2 == null || currentMessagesGroup2 != groupedMessages2) {
                                MessageObject.GroupedMessagePosition currentPosition = chatMessageCell.getCurrentPosition();
                                MessageBackgroundDrawable backgroundDrawable = chatMessageCell.getBackgroundDrawable();
                                if ((backgroundDrawable.isAnimationInProgress() || chatMessageCell.isDrawingSelectionBackground()) && (currentPosition == null || (currentPosition.flags & 2) != 0)) {
                                    int y = (int) chatMessageCell.getY();
                                    canvas.save();
                                    if (currentPosition == null) {
                                        i6 = chatMessageCell.getMeasuredHeight();
                                    } else {
                                        int measuredHeight = chatMessageCell.getMeasuredHeight() + y;
                                        long j = 0;
                                        float f4 = 0.0f;
                                        for (int i8 = 0; i8 < childCount; i8++) {
                                            View childAt2 = getChildAt(i8);
                                            if (childAt2 instanceof ChatMessageCell) {
                                                ChatMessageCell chatMessageCell2 = (ChatMessageCell) childAt2;
                                                if (chatMessageCell2.getCurrentMessagesGroup() == currentMessagesGroup2) {
                                                    MessageBackgroundDrawable backgroundDrawable2 = chatMessageCell2.getBackgroundDrawable();
                                                    y = Math.min(y, (int) chatMessageCell2.getY());
                                                    measuredHeight = Math.max(measuredHeight, ((int) chatMessageCell2.getY()) + chatMessageCell2.getMeasuredHeight());
                                                    long lastTouchTime = backgroundDrawable2.getLastTouchTime();
                                                    if (lastTouchTime > j) {
                                                        f3 = backgroundDrawable2.getTouchX() + chatMessageCell2.getX();
                                                        f4 = backgroundDrawable2.getTouchY() + chatMessageCell2.getY();
                                                        j = lastTouchTime;
                                                    }
                                                }
                                            }
                                        }
                                        backgroundDrawable.setTouchCoordsOverride(f3, f4 - y);
                                        i6 = measuredHeight - y;
                                    }
                                    int i9 = i6 + y;
                                    canvas.clipRect(0, y, getMeasuredWidth(), i9);
                                    backgroundDrawable.setCustomPaint(null);
                                    backgroundDrawable.setColor(getThemedColor(Theme.key_chat_selectedBackground));
                                    backgroundDrawable.setBounds(0, y, getMeasuredWidth(), i9);
                                    backgroundDrawable.draw(canvas);
                                    canvas.restore();
                                }
                                groupedMessages2 = currentMessagesGroup2;
                            }
                        } else if (childAt instanceof ChatActionCell) {
                            ChatActionCell chatActionCell = (ChatActionCell) childAt;
                            if (chatActionCell.hasGradientService()) {
                                canvas.save();
                                canvas.translate(chatActionCell.getX(), chatActionCell.getY());
                                canvas.scale(chatActionCell.getScaleX(), chatActionCell.getScaleY(), chatActionCell.getMeasuredWidth() / 2.0f, chatActionCell.getMeasuredHeight() / 2.0f);
                                chatActionCell.drawBackground(canvas, true);
                                chatActionCell.drawReactions(canvas, true, null);
                                canvas.restore();
                            }
                        }
                    }
                    i7++;
                }
                int i10 = 0;
                while (i10 < 3) {
                    this.drawingGroups.clear();
                    if (i10 != i5 || isFastScrollAnimationRunning()) {
                        int i11 = 0;
                        while (i11 < childCount) {
                            View childAt3 = getChildAt(i11);
                            if (childAt3 instanceof ChatMessageCell) {
                                ChatMessageCell chatMessageCell3 = (ChatMessageCell) childAt3;
                                if (childAt3.getY() <= getHeight() && childAt3.getY() + childAt3.getHeight() >= f3 && chatMessageCell3.getVisibility() != i4 && chatMessageCell3.getVisibility() != 8 && (currentMessagesGroup = chatMessageCell3.getCurrentMessagesGroup()) != null && ((i10 != 0 || currentMessagesGroup.messages.size() != z2) && ((i10 != z2 || currentMessagesGroup.transitionParams.drawBackgroundForDeletedItems) && ((i10 != 0 || !chatMessageCell3.getMessageObject().deleted) && ((i10 != z2 || chatMessageCell3.getMessageObject().deleted) && ((i10 != i5 || chatMessageCell3.willRemovedAfterAnimation()) && (i10 == i5 || !chatMessageCell3.willRemovedAfterAnimation()))))))) {
                                    if (!this.drawingGroups.contains(currentMessagesGroup)) {
                                        MessageObject.GroupedMessages.TransitionParams transitionParams = currentMessagesGroup.transitionParams;
                                        transitionParams.left = 0;
                                        transitionParams.top = 0;
                                        transitionParams.right = 0;
                                        transitionParams.bottom = 0;
                                        transitionParams.pinnedBotton = false;
                                        transitionParams.pinnedTop = false;
                                        transitionParams.cell = chatMessageCell3;
                                        this.drawingGroups.add(currentMessagesGroup);
                                    }
                                    currentMessagesGroup.transitionParams.pinnedTop = chatMessageCell3.isPinnedTop();
                                    currentMessagesGroup.transitionParams.pinnedBotton = chatMessageCell3.isPinnedBottom();
                                    int left = chatMessageCell3.getLeft() + chatMessageCell3.getBackgroundDrawableLeft();
                                    int left2 = chatMessageCell3.getLeft() + chatMessageCell3.getBackgroundDrawableRight();
                                    int top = chatMessageCell3.getTop() + chatMessageCell3.getBackgroundDrawableTop();
                                    int top2 = chatMessageCell3.getTop() + chatMessageCell3.getBackgroundDrawableBottom();
                                    if ((chatMessageCell3.getCurrentPosition().flags & i4) == 0) {
                                        top -= AndroidUtilities.dp(10.0f);
                                    }
                                    if ((8 & chatMessageCell3.getCurrentPosition().flags) == 0) {
                                        top2 += AndroidUtilities.dp(10.0f);
                                    }
                                    int i12 = top2;
                                    if (chatMessageCell3.willRemovedAfterAnimation()) {
                                        currentMessagesGroup.transitionParams.cell = chatMessageCell3;
                                    }
                                    MessageObject.GroupedMessages.TransitionParams transitionParams2 = currentMessagesGroup.transitionParams;
                                    int i13 = transitionParams2.top;
                                    if (i13 == 0 || top < i13) {
                                        transitionParams2.top = top;
                                    }
                                    int i14 = transitionParams2.bottom;
                                    if (i14 == 0 || i12 > i14) {
                                        transitionParams2.bottom = i12;
                                    }
                                    int i15 = transitionParams2.left;
                                    if (i15 == 0 || left < i15) {
                                        transitionParams2.left = left;
                                    }
                                    int i16 = transitionParams2.right;
                                    if (i16 == 0 || left2 > i16) {
                                        transitionParams2.right = left2;
                                    }
                                    i11++;
                                    i5 = 2;
                                }
                            }
                            i11++;
                            i5 = 2;
                        }
                        int i17 = 0;
                        while (i17 < this.drawingGroups.size()) {
                            MessageObject.GroupedMessages groupedMessages3 = (MessageObject.GroupedMessages) this.drawingGroups.get(i17);
                            float nonAnimationTranslationX = groupedMessages3.transitionParams.cell.getNonAnimationTranslationX(z2);
                            MessageObject.GroupedMessages.TransitionParams transitionParams3 = groupedMessages3.transitionParams;
                            float f5 = transitionParams3.left + nonAnimationTranslationX + transitionParams3.offsetLeft;
                            float f6 = transitionParams3.top + transitionParams3.offsetTop;
                            float f7 = transitionParams3.right + nonAnimationTranslationX + transitionParams3.offsetRight;
                            float f8 = transitionParams3.bottom + transitionParams3.offsetBottom;
                            if (!transitionParams3.backgroundChangeBounds) {
                                f6 += transitionParams3.cell.getTranslationY();
                                f8 += groupedMessages3.transitionParams.cell.getTranslationY();
                            }
                            float f9 = f8;
                            boolean z3 = (groupedMessages3.transitionParams.cell.getScaleX() == 1.0f && groupedMessages3.transitionParams.cell.getScaleY() == 1.0f) ? false : true;
                            if (z3) {
                                canvas.save();
                                canvas.scale(groupedMessages3.transitionParams.cell.getScaleX(), groupedMessages3.transitionParams.cell.getScaleY(), f5 + ((f7 - f5) / 2.0f), f6 + ((f9 - f6) / 2.0f));
                            }
                            MessageObject.GroupedMessages.TransitionParams transitionParams4 = groupedMessages3.transitionParams;
                            float f10 = f6;
                            int i18 = i17;
                            transitionParams4.cell.drawBackground(canvas, (int) f5, (int) f6, (int) f7, (int) f9, transitionParams4.pinnedTop, transitionParams4.pinnedBotton, false, 0);
                            MessageObject.GroupedMessages.TransitionParams transitionParams5 = groupedMessages3.transitionParams;
                            transitionParams5.cell = null;
                            transitionParams5.drawCaptionLayout = groupedMessages3.hasCaption;
                            if (z3) {
                                canvas.restore();
                                for (int i19 = 0; i19 < childCount; i19++) {
                                    View childAt4 = getChildAt(i19);
                                    if (childAt4 instanceof ChatMessageCell) {
                                        ChatMessageCell chatMessageCell4 = (ChatMessageCell) childAt4;
                                        if (chatMessageCell4.getCurrentMessagesGroup() == groupedMessages3) {
                                            int left3 = chatMessageCell4.getLeft();
                                            int top3 = chatMessageCell4.getTop();
                                            childAt4.setPivotX((f5 - left3) + ((f7 - f5) / 2.0f));
                                            childAt4.setPivotY((f10 - top3) + ((f9 - f10) / 2.0f));
                                        }
                                    }
                                }
                            }
                            i17 = i18 + 1;
                            z2 = true;
                        }
                    }
                    i10++;
                    z2 = true;
                    i5 = 2;
                    i4 = 4;
                    f3 = 0.0f;
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            private void drawChatForegroundElements(Canvas canvas) {
                int size = this.drawTimeAfter.size();
                boolean z2 = 1;
                boolean z3 = false;
                if (size > 0) {
                    for (int i4 = 0; i4 < size; i4++) {
                        ChatMessageCell chatMessageCell = (ChatMessageCell) this.drawTimeAfter.get(i4);
                        canvas.save();
                        canvas.translate(chatMessageCell.getLeft() + chatMessageCell.getNonAnimationTranslationX(false), chatMessageCell.getY());
                        chatMessageCell.drawTime(canvas, chatMessageCell.shouldDrawAlphaLayer() ? chatMessageCell.getAlpha() : 1.0f, true);
                        canvas.restore();
                    }
                    this.drawTimeAfter.clear();
                }
                int size2 = this.drawNamesAfter.size();
                if (size2 > 0) {
                    for (int i5 = 0; i5 < size2; i5++) {
                        ChatMessageCell chatMessageCell2 = (ChatMessageCell) this.drawNamesAfter.get(i5);
                        float left = chatMessageCell2.getLeft() + chatMessageCell2.getNonAnimationTranslationX(false);
                        float y = chatMessageCell2.getY();
                        float alpha = chatMessageCell2.shouldDrawAlphaLayer() ? chatMessageCell2.getAlpha() : 1.0f;
                        canvas.save();
                        canvas.translate(left, y);
                        chatMessageCell2.setInvalidatesParent(true);
                        chatMessageCell2.drawNamesLayout(canvas, alpha);
                        chatMessageCell2.setInvalidatesParent(false);
                        canvas.restore();
                    }
                    this.drawNamesAfter.clear();
                }
                int size3 = this.drawCaptionAfter.size();
                if (size3 > 0) {
                    int i6 = 0;
                    while (i6 < size3) {
                        ChatMessageCell chatMessageCell3 = (ChatMessageCell) this.drawCaptionAfter.get(i6);
                        boolean z4 = chatMessageCell3.getCurrentPosition() != null && (chatMessageCell3.getCurrentPosition().flags & z2) == 0;
                        float alpha2 = chatMessageCell3.shouldDrawAlphaLayer() ? chatMessageCell3.getAlpha() : 1.0f;
                        float left2 = chatMessageCell3.getLeft() + chatMessageCell3.getNonAnimationTranslationX(z3);
                        float y2 = chatMessageCell3.getY();
                        canvas.save();
                        MessageObject.GroupedMessages currentMessagesGroup = chatMessageCell3.getCurrentMessagesGroup();
                        if (currentMessagesGroup != null && currentMessagesGroup.transitionParams.backgroundChangeBounds) {
                            float nonAnimationTranslationX = chatMessageCell3.getNonAnimationTranslationX(z2);
                            MessageObject.GroupedMessages.TransitionParams transitionParams = currentMessagesGroup.transitionParams;
                            float f3 = transitionParams.left + nonAnimationTranslationX + transitionParams.offsetLeft;
                            float f4 = transitionParams.top + transitionParams.offsetTop;
                            float f5 = transitionParams.right + nonAnimationTranslationX + transitionParams.offsetRight;
                            float f6 = transitionParams.bottom + transitionParams.offsetBottom;
                            if (!transitionParams.backgroundChangeBounds) {
                                f4 += chatMessageCell3.getTranslationY();
                                f6 += chatMessageCell3.getTranslationY();
                            }
                            canvas.clipRect(f3 + AndroidUtilities.dp(8.0f), f4 + AndroidUtilities.dp(8.0f), f5 - AndroidUtilities.dp(8.0f), f6 - AndroidUtilities.dp(8.0f));
                        }
                        if (chatMessageCell3.getTransitionParams().wasDraw) {
                            canvas.translate(left2, y2);
                            chatMessageCell3.setInvalidatesParent(true);
                            chatMessageCell3.drawCaptionLayout(canvas, z4, alpha2);
                            chatMessageCell3.setInvalidatesParent(false);
                            canvas.restore();
                        }
                        i6++;
                        z2 = 1;
                        z3 = false;
                    }
                    this.drawCaptionAfter.clear();
                }
                int size4 = this.drawReactionsAfter.size();
                if (size4 > 0) {
                    for (int i7 = 0; i7 < size4; i7++) {
                        ChatMessageCell chatMessageCell4 = (ChatMessageCell) this.drawReactionsAfter.get(i7);
                        if (chatMessageCell4.getCurrentPosition() == null || (chatMessageCell4.getCurrentPosition().flags & 1) != 0) {
                            float alpha3 = chatMessageCell4.shouldDrawAlphaLayer() ? chatMessageCell4.getAlpha() : 1.0f;
                            float left3 = chatMessageCell4.getLeft() + chatMessageCell4.getNonAnimationTranslationX(false);
                            float y3 = chatMessageCell4.getY();
                            canvas.save();
                            MessageObject.GroupedMessages currentMessagesGroup2 = chatMessageCell4.getCurrentMessagesGroup();
                            if (currentMessagesGroup2 != null && currentMessagesGroup2.transitionParams.backgroundChangeBounds) {
                                float nonAnimationTranslationX2 = chatMessageCell4.getNonAnimationTranslationX(true);
                                MessageObject.GroupedMessages.TransitionParams transitionParams2 = currentMessagesGroup2.transitionParams;
                                float f7 = transitionParams2.left + nonAnimationTranslationX2 + transitionParams2.offsetLeft;
                                float f8 = transitionParams2.top + transitionParams2.offsetTop;
                                float f9 = transitionParams2.right + nonAnimationTranslationX2 + transitionParams2.offsetRight;
                                float f10 = transitionParams2.bottom + transitionParams2.offsetBottom;
                                if (!transitionParams2.backgroundChangeBounds) {
                                    f8 += chatMessageCell4.getTranslationY();
                                    f10 += chatMessageCell4.getTranslationY();
                                }
                                canvas.clipRect(f7 + AndroidUtilities.dp(8.0f), f8 + AndroidUtilities.dp(8.0f), f9 - AndroidUtilities.dp(8.0f), f10 - AndroidUtilities.dp(8.0f));
                            }
                            if (chatMessageCell4.getTransitionParams().wasDraw) {
                                canvas.translate(left3, y3);
                                chatMessageCell4.setInvalidatesParent(true);
                                chatMessageCell4.drawReactionsLayout(canvas, alpha3, null);
                                chatMessageCell4.drawCommentLayout(canvas, alpha3);
                                chatMessageCell4.setInvalidatesParent(false);
                                canvas.restore();
                            }
                        }
                    }
                    this.drawReactionsAfter.clear();
                }
            }

            @Override // org.telegram.ui.Components.RecyclerListView, android.view.ViewGroup, android.view.View
            protected void dispatchDraw(Canvas canvas) {
                canvas.save();
                this.selectorRect.setEmpty();
                drawChatBackgroundElements(canvas);
                super.dispatchDraw(canvas);
                drawChatForegroundElements(canvas);
                canvas.restore();
            }

            /* JADX WARN: Code restructure failed: missing block: B:183:0x00f4, code lost:
            
                if ((r8 & 1) != 0) goto L66;
             */
            /* JADX WARN: Removed duplicated region for block: B:12:0x005b  */
            /* JADX WARN: Removed duplicated region for block: B:14:0x0067  */
            /* JADX WARN: Removed duplicated region for block: B:151:0x0378  */
            /* JADX WARN: Removed duplicated region for block: B:17:0x0072  */
            /* JADX WARN: Removed duplicated region for block: B:20:0x007d  */
            /* JADX WARN: Removed duplicated region for block: B:22:0x0089  */
            @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
            /*
                Code decompiled incorrectly, please refer to instructions dump.
            */
            public boolean drawChild(Canvas canvas, View view, long j) {
                ChatActionCell chatActionCell;
                float f3;
                boolean z2;
                int adapterPosition;
                int adapterPosition2;
                int i4;
                ChatMessageCell chatMessageCell = null;
                if (view instanceof ChatMessageCell) {
                    chatMessageCell = (ChatMessageCell) view;
                    chatActionCell = null;
                } else {
                    chatActionCell = view instanceof ChatActionCell ? (ChatActionCell) view : null;
                }
                boolean drawChild = super.drawChild(canvas, view, j);
                if (chatMessageCell == null || !chatMessageCell.hasOutboundsContent()) {
                    if (chatActionCell != null) {
                        canvas.save();
                        canvas.translate(chatActionCell.getX(), chatActionCell.getY());
                        chatActionCell.drawOutboundsContent(canvas);
                    }
                    if (view.getTranslationY() != 0.0f) {
                        canvas.save();
                        canvas.translate(0.0f, view.getTranslationY());
                    }
                    if (chatMessageCell != null) {
                        chatMessageCell.drawCheckBox(canvas);
                    }
                    if (view.getTranslationY() != 0.0f) {
                        canvas.restore();
                    }
                    if (view.getTranslationY() != 0.0f) {
                        canvas.save();
                        canvas.translate(0.0f, view.getTranslationY());
                    }
                    if (chatMessageCell != null) {
                        chatMessageCell.getMessageObject();
                        MessageObject.GroupedMessagePosition currentPosition = chatMessageCell.getCurrentPosition();
                        if (currentPosition != null || chatMessageCell.getTransitionParams().animateBackgroundBoundsInner) {
                            if (currentPosition == null || currentPosition.last || (currentPosition.minX == 0 && currentPosition.minY == 0)) {
                                if (currentPosition == null || currentPosition.last) {
                                    this.drawTimeAfter.add(chatMessageCell);
                                }
                                if ((currentPosition == null || (currentPosition.minX == 0 && currentPosition.minY == 0)) && chatMessageCell.hasNameLayout()) {
                                    this.drawNamesAfter.add(chatMessageCell);
                                }
                            }
                            if (currentPosition != null || chatMessageCell.getTransitionParams().transformGroupToSingleMessage || chatMessageCell.getTransitionParams().animateBackgroundBoundsInner) {
                                if (currentPosition == null || (currentPosition.flags & chatMessageCell.captionFlag()) != 0) {
                                    this.drawCaptionAfter.add(chatMessageCell);
                                }
                                if (currentPosition != null) {
                                    int i5 = currentPosition.flags;
                                    if ((i5 & 8) != 0) {
                                    }
                                }
                                this.drawReactionsAfter.add(chatMessageCell);
                            }
                        }
                        ImageReceiver avatarImage = chatMessageCell.getAvatarImage();
                        if (avatarImage != null) {
                            boolean z3 = isFastScrollAnimationRunning() || (MessageEntityView.this.groupedMessages != null && MessageEntityView.this.groupedMessages.transitionParams.backgroundChangeBounds);
                            int top = z3 ? view.getTop() : (int) view.getY();
                            if (chatMessageCell.drawPinnedBottom() && (adapterPosition2 = MessageEntityView.this.listView.getChildViewHolder(view).getAdapterPosition()) >= 0) {
                                if (MessageEntityView.this.groupedMessages == null || currentPosition == null) {
                                    i4 = adapterPosition2 - 1;
                                } else {
                                    int indexOf = MessageEntityView.this.groupedMessages.posArray.indexOf(currentPosition);
                                    int size = MessageEntityView.this.groupedMessages.posArray.size();
                                    if ((currentPosition.flags & 8) != 0) {
                                        i4 = (adapterPosition2 - size) + indexOf;
                                    } else {
                                        i4 = adapterPosition2 - 1;
                                        for (int i6 = indexOf + 1; i6 < size && MessageEntityView.this.groupedMessages.posArray.get(i6).minY <= currentPosition.maxY; i6++) {
                                            i4--;
                                        }
                                    }
                                }
                                if (findViewHolderForAdapterPosition(i4) != null) {
                                    if (view.getTranslationY() != 0.0f) {
                                        canvas.restore();
                                    }
                                    avatarImage.setVisible(false, false);
                                    return drawChild;
                                }
                            }
                            float slidingOffsetX = chatMessageCell.getSlidingOffsetX() + chatMessageCell.getCheckBoxTranslation();
                            int top2 = (int) ((z3 ? view.getTop() : view.getY()) + chatMessageCell.getLayoutHeight() + chatMessageCell.getTransitionParams().deltaBottom);
                            int measuredHeight = getMeasuredHeight() - getPaddingBottom();
                            boolean z4 = chatMessageCell.isCheckBoxVisible() && slidingOffsetX == 0.0f;
                            if (chatMessageCell.isPlayingRound() || chatMessageCell.getTransitionParams().animatePlayingRound) {
                                if (chatMessageCell.getTransitionParams().animatePlayingRound) {
                                    float f4 = chatMessageCell.getTransitionParams().animateChangeProgress;
                                    if (!chatMessageCell.isPlayingRound()) {
                                        f4 = 1.0f - f4;
                                    }
                                    top2 = (int) ((top2 * f4) + (Math.min(top2, measuredHeight) * (1.0f - f4)));
                                }
                            } else if (top2 > measuredHeight) {
                                top2 = measuredHeight;
                            }
                            if (!z3 && view.getTranslationY() != 0.0f) {
                                canvas.restore();
                            }
                            if (chatMessageCell.drawPinnedTop() && (adapterPosition = getChildViewHolder(view).getAdapterPosition()) >= 0) {
                                int i7 = adapterPosition;
                                int i8 = top;
                                float f5 = slidingOffsetX;
                                ChatMessageCell chatMessageCell2 = chatMessageCell;
                                int i9 = 0;
                                while (i9 < 20) {
                                    i9++;
                                    if (MessageEntityView.this.groupedMessages == null || currentPosition == null) {
                                        i7++;
                                    } else {
                                        int indexOf2 = MessageEntityView.this.groupedMessages.posArray.indexOf(currentPosition);
                                        if (indexOf2 < 0) {
                                            break;
                                        }
                                        MessageEntityView.this.groupedMessages.posArray.size();
                                        if ((currentPosition.flags & 4) != 0) {
                                            i7 = i7 + indexOf2 + 1;
                                        } else {
                                            i7++;
                                            for (int i10 = indexOf2 - 1; i10 >= 0 && MessageEntityView.this.groupedMessages.posArray.get(i10).maxY >= currentPosition.minY; i10--) {
                                                i7++;
                                            }
                                        }
                                    }
                                    RecyclerView.ViewHolder findViewHolderForAdapterPosition = findViewHolderForAdapterPosition(i7);
                                    if (findViewHolderForAdapterPosition == null) {
                                        break;
                                    }
                                    i8 = findViewHolderForAdapterPosition.itemView.getTop();
                                    View view2 = findViewHolderForAdapterPosition.itemView;
                                    if (!(view2 instanceof ChatMessageCell)) {
                                        break;
                                    }
                                    chatMessageCell2 = (ChatMessageCell) view2;
                                    float slidingOffsetX2 = chatMessageCell2.getSlidingOffsetX() + chatMessageCell2.getCheckBoxTranslation();
                                    if (z4 && slidingOffsetX2 > 0.0f) {
                                        f5 = slidingOffsetX2;
                                    }
                                    if (!chatMessageCell2.drawPinnedTop()) {
                                        break;
                                    }
                                }
                                chatMessageCell = chatMessageCell2;
                                slidingOffsetX = f5;
                                top = i8;
                            }
                            if (top2 - AndroidUtilities.dp(42.0f) < top) {
                                top2 = top + AndroidUtilities.dp(42.0f);
                            }
                            if (!chatMessageCell.drawPinnedBottom()) {
                                int bottom = z3 ? chatMessageCell.getBottom() : (int) (chatMessageCell.getY() + chatMessageCell.getMeasuredHeight() + chatMessageCell.getTransitionParams().deltaBottom);
                                if (top2 > bottom) {
                                    top2 = bottom;
                                }
                            }
                            canvas.save();
                            if (slidingOffsetX != 0.0f) {
                                canvas.translate(slidingOffsetX, 0.0f);
                            }
                            if (chatMessageCell.getCurrentMessagesGroup() != null && chatMessageCell.getCurrentMessagesGroup().transitionParams.backgroundChangeBounds) {
                                top2 = (int) (top2 - chatMessageCell.getTranslationY());
                            }
                            avatarImage.setImageY(top2 - AndroidUtilities.dp(40.0f));
                            if (chatMessageCell.shouldDrawAlphaLayer()) {
                                avatarImage.setAlpha(chatMessageCell.getAlpha());
                                z2 = true;
                                canvas.scale(chatMessageCell.getScaleX(), chatMessageCell.getScaleY(), chatMessageCell.getX() + chatMessageCell.getPivotX(), chatMessageCell.getY() + (chatMessageCell.getHeight() >> 1));
                            } else {
                                z2 = true;
                                avatarImage.setAlpha(1.0f);
                            }
                            avatarImage.setVisible(z2, false);
                            avatarImage.draw(canvas);
                            canvas.restore();
                            if (!z3) {
                                f3 = 0.0f;
                                if (view.getTranslationY() != 0.0f) {
                                    canvas.save();
                                }
                                if (view.getTranslationY() != f3) {
                                    canvas.restore();
                                }
                                return drawChild;
                            }
                        }
                    }
                    f3 = 0.0f;
                    if (view.getTranslationY() != f3) {
                    }
                    return drawChild;
                }
                canvas.save();
                canvas.translate(chatMessageCell.getX(), chatMessageCell.getY());
                chatMessageCell.drawOutboundsContent(canvas);
                canvas.restore();
                if (view.getTranslationY() != 0.0f) {
                }
                if (chatMessageCell != null) {
                }
                if (view.getTranslationY() != 0.0f) {
                }
                if (view.getTranslationY() != 0.0f) {
                }
                if (chatMessageCell != null) {
                }
                f3 = 0.0f;
                if (view.getTranslationY() != f3) {
                }
                return drawChild;
            }
        };
        this.listView = recyclerListView;
        recyclerListView.setAdapter(new RecyclerListView.SelectionAdapter() { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView.3
            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemCount() {
                return MessageEntityView.this.messageObjects.size();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public int getItemViewType(int i4) {
                return ((MessageObject) MessageEntityView.this.messageObjects.get((MessageEntityView.this.messageObjects.size() - 1) - i4)).contentType;
            }

            @Override // org.telegram.ui.Components.RecyclerListView.SelectionAdapter
            public boolean isEnabled(RecyclerView.ViewHolder viewHolder) {
                return true;
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i4) {
                MessageObject.GroupedMessagePosition position;
                MessageObject messageObject3 = (MessageObject) MessageEntityView.this.messageObjects.get((MessageEntityView.this.messageObjects.size() - 1) - i4);
                View view = viewHolder.itemView;
                if (view instanceof ChatMessageCell) {
                    ((ChatMessageCell) view).setMessageObject(messageObject3, MessageEntityView.this.groupedMessages, MessageEntityView.this.groupedMessages != null, (MessageEntityView.this.groupedMessages == null || (position = MessageEntityView.this.groupedMessages.getPosition(messageObject3)) == null || position.minY == 0) ? false : true);
                } else if (view instanceof ChatActionCell) {
                    ((ChatActionCell) view).setMessageObject(messageObject3);
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.Adapter
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i4) {
                if (i4 == 1) {
                    return new RecyclerListView.Holder(new ChatActionCell(context, false, MessageEntityView.this.resourcesProvider) { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView.3.1
                        public final BlurringShader.StoryBlurDrawer blurDrawer;
                        private final TextPaint textPaint;

                        {
                            this.blurDrawer = new BlurringShader.StoryBlurDrawer(blurManager, this, 10);
                            TextPaint textPaint = new TextPaint(1);
                            this.textPaint = textPaint;
                            textPaint.setTypeface(AndroidUtilities.bold());
                            textPaint.setTextSize(AndroidUtilities.dp(Math.max(16, SharedConfig.fontSize) - 2));
                            textPaint.setColor(-1);
                        }

                        @Override // org.telegram.ui.Cells.ChatActionCell
                        protected Paint getThemedPaint(String str) {
                            if ("paintChatActionText".equals(str) || "paintChatActionText2".equals(str)) {
                                return this.textPaint;
                            }
                            if ("paintChatActionBackground".equals(str)) {
                                MessageEntityView.this.usesBackgroundPaint = true;
                                Paint paint = this.blurDrawer.adapt(MessageEntityView.this.isDark).getPaint(1.0f);
                                if (paint != null) {
                                    return paint;
                                }
                            }
                            return super.getThemedPaint(str);
                        }
                    });
                }
                ChatMessageCell chatMessageCell = new ChatMessageCell(context, UserConfig.selectedAccount, false, null, MessageEntityView.this.resourcesProvider) { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView.3.2
                    public BlurringShader.StoryBlurDrawer blurDrawer;
                    private final Paint clearPaint;
                    private final RectF dst;
                    private final Rect src;
                    private final float[] radii = new float[8];
                    private final Path clipPath = new Path();

                    {
                        this.blurDrawer = new BlurringShader.StoryBlurDrawer(blurManager, this, 10);
                        Paint paint = new Paint();
                        this.clearPaint = paint;
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        this.src = new Rect();
                        this.dst = new RectF();
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell
                    protected boolean drawPhotoImage(Canvas canvas) {
                        PreviewView.TextureViewHolder textureViewHolder2;
                        ImageReceiver photoImage = getPhotoImage();
                        3 r1 = 3.this;
                        if (!z || photoImage == null || (((textureViewHolder2 = textureViewHolder) == null || !textureViewHolder2.active || !textureViewHolder2.textureViewActive || !MessageEntityView.this.textureViewActive) && !MessageEntityView.this.clipVideoMessageForBitmap && (MessageEntityView.this.textureView == null || !MessageEntityView.this.drawForBitmap()))) {
                            return super.drawPhotoImage(canvas);
                        }
                        for (int i5 = 0; i5 < photoImage.getRoundRadius().length; i5++) {
                            int i6 = i5 * 2;
                            this.radii[i6] = photoImage.getRoundRadius()[i5];
                            this.radii[i6 + 1] = photoImage.getRoundRadius()[i5];
                        }
                        RectF rectF = AndroidUtilities.rectTmp;
                        rectF.set(photoImage.getImageX(), photoImage.getImageY(), photoImage.getImageX2(), photoImage.getImageY2());
                        this.clipPath.rewind();
                        this.clipPath.addRoundRect(rectF, this.radii, Path.Direction.CW);
                        if (MessageEntityView.this.textureView == null || !MessageEntityView.this.drawForBitmap()) {
                            canvas.drawPath(this.clipPath, this.clearPaint);
                        } else {
                            Bitmap bitmap = MessageEntityView.this.textureView.getBitmap();
                            if (bitmap == null) {
                                return super.drawPhotoImage(canvas);
                            }
                            canvas.save();
                            canvas.clipPath(this.clipPath);
                            canvas.translate(-getX(), -getY());
                            float max = Math.max(photoImage.getImageWidth() / MessageEntityView.this.videoWidth, photoImage.getImageHeight() / MessageEntityView.this.videoHeight);
                            canvas.translate(photoImage.getCenterX() - ((MessageEntityView.this.videoWidth * max) / 2.0f), photoImage.getCenterY() - ((MessageEntityView.this.videoHeight * max) / 2.0f));
                            canvas.scale((MessageEntityView.this.videoWidth / MessageEntityView.this.textureView.getWidth()) * max, (MessageEntityView.this.videoHeight / MessageEntityView.this.textureView.getHeight()) * max);
                            this.src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
                            this.dst.set(0.0f, 0.0f, MessageEntityView.this.textureView.getWidth(), MessageEntityView.this.textureView.getHeight());
                            canvas.drawBitmap(bitmap, this.src, this.dst, (Paint) null);
                            canvas.restore();
                        }
                        return true;
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell
                    public Paint getThemedPaint(String str) {
                        if ("paintChatActionBackground".equals(str)) {
                            MessageEntityView.this.usesBackgroundPaint = true;
                            Paint paint = this.blurDrawer.getPaint(1.0f);
                            if (paint != null) {
                                return paint;
                            }
                        }
                        return super.getThemedPaint(str);
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell, android.view.View
                    protected void onDraw(Canvas canvas) {
                        3 r0 = 3.this;
                        PreviewView.TextureViewHolder textureViewHolder2 = textureViewHolder;
                        if ((textureViewHolder2 != null && textureViewHolder2.active && textureViewHolder2.textureViewActive) || MessageEntityView.this.clipVideoMessageForBitmap) {
                            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), NotificationCenter.liveLocationsChanged, 31);
                        } else {
                            canvas.save();
                        }
                        super.onDraw(canvas);
                        canvas.restore();
                    }

                    @Override // org.telegram.ui.Cells.ChatMessageCell, android.view.View
                    public boolean onTouchEvent(MotionEvent motionEvent) {
                        return false;
                    }
                };
                chatMessageCell.isChat = true;
                return new RecyclerListView.Holder(chatMessageCell);
            }
        });
        GridLayoutManagerFixed gridLayoutManagerFixed = new GridLayoutManagerFixed(context, MediaDataController.MAX_STYLE_RUNS_COUNT, 1, true) { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView.4
            @Override // androidx.recyclerview.widget.GridLayoutManagerFixed
            protected boolean hasSiblingChild(int i4) {
                byte b;
                int size = (MessageEntityView.this.messageObjects.size() - 1) - i4;
                if (MessageEntityView.this.groupedMessages != null && size >= 0 && size < MessageEntityView.this.messageObjects.size()) {
                    MessageObject.GroupedMessagePosition position = MessageEntityView.this.groupedMessages.getPosition((MessageObject) MessageEntityView.this.messageObjects.get(size));
                    if (position != null && position.minX != position.maxX && (b = position.minY) == position.maxY && b != 0) {
                        int size2 = MessageEntityView.this.groupedMessages.posArray.size();
                        for (int i5 = 0; i5 < size2; i5++) {
                            MessageObject.GroupedMessagePosition groupedMessagePosition = MessageEntityView.this.groupedMessages.posArray.get(i5);
                            if (groupedMessagePosition != position) {
                                byte b2 = groupedMessagePosition.minY;
                                byte b3 = position.minY;
                                if (b2 <= b3 && groupedMessagePosition.maxY >= b3) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }

            @Override // androidx.recyclerview.widget.GridLayoutManagerFixed
            public boolean shouldLayoutChildFromOpositeSide(View view) {
                if (view instanceof ChatMessageCell) {
                    return !((ChatMessageCell) view).getMessageObject().isOutOwner();
                }
                return false;
            }

            @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        gridLayoutManagerFixed.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView.5
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i4) {
                int size = (MessageEntityView.this.messageObjects.size() - 1) - i4;
                if (MessageEntityView.this.groupedMessages == null || size < 0 || size >= MessageEntityView.this.groupedMessages.messages.size()) {
                    return MediaDataController.MAX_STYLE_RUNS_COUNT;
                }
                MessageObject.GroupedMessagePosition position = MessageEntityView.this.groupedMessages.getPosition(MessageEntityView.this.groupedMessages.messages.get(size));
                return position != null ? position.spanSize : MediaDataController.MAX_STYLE_RUNS_COUNT;
            }
        });
        recyclerListView.setLayoutManager(gridLayoutManagerFixed);
        recyclerListView.addItemDecoration(new RecyclerView.ItemDecoration() { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView.6
            @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
            public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
                ChatMessageCell chatMessageCell;
                MessageObject.GroupedMessages currentMessagesGroup;
                MessageObject.GroupedMessagePosition currentPosition;
                int i4 = 0;
                rect.bottom = 0;
                if (!(view instanceof ChatMessageCell) || (currentMessagesGroup = (chatMessageCell = (ChatMessageCell) view).getCurrentMessagesGroup()) == null || (currentPosition = chatMessageCell.getCurrentPosition()) == null || currentPosition.siblingHeights == null) {
                    return;
                }
                android.graphics.Point point2 = AndroidUtilities.displaySize;
                float max = Math.max(point2.x, point2.y) * 0.5f;
                int extraInsetHeight = chatMessageCell.getExtraInsetHeight();
                int i5 = 0;
                while (true) {
                    if (i5 >= currentPosition.siblingHeights.length) {
                        break;
                    }
                    extraInsetHeight += (int) Math.ceil(r3[i5] * max);
                    i5++;
                }
                int round = extraInsetHeight + ((currentPosition.maxY - currentPosition.minY) * Math.round(AndroidUtilities.density * 7.0f));
                int size = currentMessagesGroup.posArray.size();
                while (true) {
                    if (i4 < size) {
                        MessageObject.GroupedMessagePosition groupedMessagePosition = currentMessagesGroup.posArray.get(i4);
                        byte b = groupedMessagePosition.minY;
                        byte b2 = currentPosition.minY;
                        if (b == b2 && ((groupedMessagePosition.minX != currentPosition.minX || groupedMessagePosition.maxX != currentPosition.maxX || b != b2 || groupedMessagePosition.maxY != currentPosition.maxY) && b == b2)) {
                            round -= ((int) Math.ceil(max * groupedMessagePosition.ph)) - AndroidUtilities.dp(4.0f);
                            break;
                        }
                        i4++;
                    } else {
                        break;
                    }
                }
                rect.bottom = -round;
            }
        });
        frameLayout.addView(recyclerListView, LayoutHelper.createFrame(-1, -1.0f));
        if (textureViewHolder != null && textureViewHolder.active) {
            textureViewHolder.takeTextureView(new Utilities.Callback() { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView$$ExternalSyntheticLambda0
                @Override // org.telegram.messenger.Utilities.Callback
                public final void run(Object obj) {
                    MessageEntityView.this.lambda$new$0((TextureView) obj);
                }
            }, new Utilities.Callback2() { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView$$ExternalSyntheticLambda1
                @Override // org.telegram.messenger.Utilities.Callback2
                public final void run(Object obj, Object obj2) {
                    MessageEntityView.this.lambda$new$2((Integer) obj, (Integer) obj2);
                }
            });
        }
        updatePosition();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ChatMessageCell getCell() {
        if (this.listView == null) {
            return null;
        }
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            if (this.listView.getChildAt(i) instanceof ChatMessageCell) {
                return (ChatMessageCell) this.listView.getChildAt(i);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(TextureView textureView) {
        this.textureView = textureView;
        if (textureView != null) {
            this.container.addView(textureView, 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1() {
        this.textureViewActive = true;
        invalidateAll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Integer num, Integer num2) {
        this.videoWidth = num.intValue();
        this.videoHeight = num2.intValue();
        AndroidUtilities.runOnUIThread(new Runnable() { // from class: org.telegram.ui.Components.Paint.Views.MessageEntityView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MessageEntityView.this.lambda$new$1();
            }
        }, 60L);
    }

    public TLRPC.Message copyMessage(TLRPC.Message message) {
        TLRPC.Message tL_messageService;
        if (message instanceof TLRPC.TL_message) {
            tL_messageService = new TLRPC.TL_message();
        } else {
            if (!(message instanceof TLRPC.TL_messageService)) {
                return message;
            }
            tL_messageService = new TLRPC.TL_messageService();
        }
        tL_messageService.id = message.id;
        tL_messageService.from_id = message.from_id;
        tL_messageService.peer_id = message.peer_id;
        tL_messageService.date = message.date;
        tL_messageService.expire_date = message.expire_date;
        tL_messageService.action = message.action;
        tL_messageService.message = message.message;
        tL_messageService.media = message.media;
        tL_messageService.flags = message.flags;
        tL_messageService.mentioned = message.mentioned;
        tL_messageService.media_unread = message.media_unread;
        tL_messageService.out = message.out;
        tL_messageService.unread = message.unread;
        tL_messageService.entities = message.entities;
        tL_messageService.via_bot_name = message.via_bot_name;
        tL_messageService.reply_markup = message.reply_markup;
        tL_messageService.views = message.views;
        tL_messageService.forwards = message.forwards;
        tL_messageService.replies = message.replies;
        tL_messageService.edit_date = message.edit_date;
        tL_messageService.silent = message.silent;
        tL_messageService.post = message.post;
        tL_messageService.from_scheduled = message.from_scheduled;
        tL_messageService.legacy = message.legacy;
        tL_messageService.edit_hide = message.edit_hide;
        tL_messageService.pinned = message.pinned;
        tL_messageService.fwd_from = message.fwd_from;
        tL_messageService.via_bot_id = message.via_bot_id;
        tL_messageService.reply_to = message.reply_to;
        tL_messageService.post_author = message.post_author;
        tL_messageService.grouped_id = message.grouped_id;
        tL_messageService.reactions = message.reactions;
        tL_messageService.restriction_reason = message.restriction_reason;
        tL_messageService.ttl_period = message.ttl_period;
        tL_messageService.noforwards = message.noforwards;
        tL_messageService.invert_media = message.invert_media;
        tL_messageService.send_state = message.send_state;
        tL_messageService.fwd_msg_id = message.fwd_msg_id;
        tL_messageService.attachPath = message.attachPath;
        tL_messageService.params = message.params;
        tL_messageService.random_id = message.random_id;
        tL_messageService.local_id = message.local_id;
        tL_messageService.dialog_id = message.dialog_id;
        tL_messageService.ttl = message.ttl;
        tL_messageService.destroyTime = message.destroyTime;
        tL_messageService.destroyTimeMillis = message.destroyTimeMillis;
        tL_messageService.layer = message.layer;
        tL_messageService.seq_in = message.seq_in;
        tL_messageService.seq_out = message.seq_out;
        tL_messageService.with_my_score = message.with_my_score;
        tL_messageService.replyMessage = message.replyMessage;
        tL_messageService.reqId = message.reqId;
        tL_messageService.realId = message.realId;
        tL_messageService.stickerVerified = message.stickerVerified;
        tL_messageService.isThreadMessage = message.isThreadMessage;
        tL_messageService.voiceTranscription = message.voiceTranscription;
        tL_messageService.voiceTranscriptionOpen = message.voiceTranscriptionOpen;
        tL_messageService.voiceTranscriptionRated = message.voiceTranscriptionRated;
        tL_messageService.voiceTranscriptionFinal = message.voiceTranscriptionFinal;
        tL_messageService.voiceTranscriptionForce = message.voiceTranscriptionForce;
        tL_messageService.voiceTranscriptionId = message.voiceTranscriptionId;
        tL_messageService.premiumEffectWasPlayed = message.premiumEffectWasPlayed;
        tL_messageService.originalLanguage = message.originalLanguage;
        tL_messageService.translatedToLanguage = message.translatedToLanguage;
        tL_messageService.translatedText = message.translatedText;
        tL_messageService.replyStory = message.replyStory;
        return tL_messageService;
    }

    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    protected EntityView.SelectionView createSelectionView() {
        return new MessageEntityViewSelectionView(getContext());
    }

    public abstract boolean drawForBitmap();

    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    protected float getBounceScale() {
        return 0.02f;
    }

    public float getBubbleBounds(RectF rectF) {
        float x;
        float y;
        float y2;
        float max;
        float f;
        float f2 = 2.14748365E9f;
        float f3 = 2.14748365E9f;
        float f4 = -2.14748365E9f;
        float f5 = -2.14748365E9f;
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            View childAt = this.listView.getChildAt(i);
            if (childAt instanceof ChatMessageCell) {
                ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                if (chatMessageCell.getMessageObject() == null || !chatMessageCell.getMessageObject().isRoundVideo() || chatMessageCell.getPhotoImage() == null) {
                    float x2 = this.container.getX() + childAt.getX() + chatMessageCell.getBackgroundDrawableLeft() + AndroidUtilities.dp(1.0f);
                    if (this.groupedMessages == null) {
                        x2 += AndroidUtilities.dp(8.0f);
                    }
                    float x3 = ((this.container.getX() + childAt.getX()) + chatMessageCell.getBackgroundDrawableRight()) - AndroidUtilities.dp(1.66f);
                    y = this.container.getY() + childAt.getY() + chatMessageCell.getBackgroundDrawableTop() + AndroidUtilities.dp(2.0f);
                    y2 = ((this.container.getY() + childAt.getY()) + chatMessageCell.getBackgroundDrawableBottom()) - AndroidUtilities.dp(1.0f);
                    f = x2;
                    x = x3;
                } else {
                    f = this.container.getX() + chatMessageCell.getX() + chatMessageCell.getPhotoImage().getImageX();
                    x = this.container.getX() + chatMessageCell.getX() + chatMessageCell.getPhotoImage().getImageX2();
                    y = this.container.getY() + chatMessageCell.getY() + chatMessageCell.getPhotoImage().getImageY();
                    y2 = this.container.getY() + chatMessageCell.getY() + chatMessageCell.getPhotoImage().getImageY2();
                }
                f2 = Math.min(Math.min(f2, f), x);
                max = Math.max(f4, f);
            } else {
                if (childAt instanceof ChatActionCell) {
                    ChatActionCell chatActionCell = (ChatActionCell) childAt;
                    if (chatActionCell.starGiftLayout.has()) {
                        float x4 = this.container.getX() + chatActionCell.getX() + chatActionCell.getBoundsLeft();
                        x = this.container.getX() + chatActionCell.getX() + chatActionCell.getBoundsRight();
                        y = this.container.getY() + chatActionCell.getY();
                        y2 = this.container.getY() + chatActionCell.getY() + chatActionCell.getMeasuredHeight();
                        f2 = Math.min(Math.min(f2, x4), x);
                        max = Math.max(f4, x4);
                    }
                }
            }
            f4 = Math.max(max, x);
            f3 = Math.min(Math.min(f3, y), y2);
            f5 = Math.max(Math.max(f5, y), y2);
        }
        rectF.set(f2, f3, f4, f5);
        return AndroidUtilities.dp(SharedConfig.bubbleRadius);
    }

    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    public org.telegram.ui.Components.Rect getSelectionBounds() {
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup == null) {
            return new org.telegram.ui.Components.Rect();
        }
        float scaleX = viewGroup.getScaleX();
        return new org.telegram.ui.Components.Rect(((getPositionX() * scaleX) - (((getMeasuredWidth() * getScale()) / 2.0f) * scaleX)) - AndroidUtilities.dp(35.5f), ((getPositionY() * scaleX) - (((getMeasuredHeight() * getScale()) / 2.0f) * scaleX)) - AndroidUtilities.dp(35.5f), (getMeasuredWidth() * getScale() * scaleX) + AndroidUtilities.dp(71.0f), (getMeasuredHeight() * getScale() * scaleX) + AndroidUtilities.dp(71.0f));
    }

    public void invalidateAll() {
        this.listView.invalidate();
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            this.listView.getChildAt(i).invalidate();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0031, code lost:
    
        if (((org.telegram.messenger.MessageObject) r4.messageObjects.get(0)).contentType == 1) goto L12;
     */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void onMeasure(int i, int i2) {
        boolean z;
        this.container.measure(i, i2);
        setMeasuredDimension(this.container.getMeasuredWidth(), this.container.getMeasuredHeight());
        updatePosition();
        if (this.firstMeasure) {
            ArrayList arrayList = this.messageObjects;
            if (arrayList != null) {
                z = true;
                if (arrayList.size() == 1) {
                }
            }
            z = false;
            float min = Math.min((View.MeasureSpec.getSize(i) - AndroidUtilities.dp(z ? 0.0f : 44.0f)) / getMeasuredWidth(), (View.MeasureSpec.getSize(i2) - AndroidUtilities.dp(z ? 0.0f : 192.0f)) / getMeasuredHeight());
            if (min < 1.0f) {
                setScale(min);
            }
            Point position = getPosition();
            if (!z) {
                position.x -= AndroidUtilities.dp(19.0f) * Math.min(1.0f, min);
            }
            setPosition(position);
            this.firstMeasure = false;
        }
    }

    public void prepareToDraw(boolean z) {
        this.clipVideoMessageForBitmap = z;
        for (int i = 0; i < this.listView.getChildCount(); i++) {
            View childAt = this.listView.getChildAt(i);
            if (childAt instanceof ChatMessageCell) {
                ((ChatMessageCell) childAt).drawingToBitmap = z;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x009b  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0074  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void setupTheme(StoryEntry storyEntry) {
        SparseIntArray themeFileValues;
        int[] defaultColors;
        if (storyEntry == null) {
            this.currentColors.clear();
            return;
        }
        SharedPreferences sharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("themeconfig", 0);
        String str = "Blue";
        String string = sharedPreferences.getString("lastDayTheme", "Blue");
        if (Theme.getTheme(string) == null || Theme.getTheme(string).isDark()) {
            string = "Blue";
        }
        String str2 = "Dark Blue";
        String string2 = sharedPreferences.getString("lastDarkTheme", "Dark Blue");
        if (Theme.getTheme(string2) == null || !Theme.getTheme(string2).isDark()) {
            string2 = "Dark Blue";
        }
        Theme.ThemeInfo activeTheme = Theme.getActiveTheme();
        if (!string.equals(string2)) {
            str2 = string2;
        } else if (activeTheme.isDark() || string.equals("Dark Blue") || string.equals("Night")) {
            str2 = string2;
            boolean z = storyEntry.isDark;
            this.isDark = z;
            Theme.ThemeInfo theme = !z ? Theme.getTheme(str2) : Theme.getTheme(str);
            String[] strArr = new String[1];
            String str3 = theme.assetName;
            themeFileValues = str3 == null ? Theme.getThemeFileValues(null, str3, strArr) : Theme.getThemeFileValues(new File(theme.pathToFile), null, strArr);
            this.currentColors.clear();
            defaultColors = Theme.getDefaultColors();
            if (defaultColors != null) {
                for (int i = 0; i < defaultColors.length; i++) {
                    this.currentColors.put(i, defaultColors[i]);
                }
            }
            if (themeFileValues != null) {
                for (int i2 = 0; i2 < themeFileValues.size(); i2++) {
                    this.currentColors.put(themeFileValues.keyAt(i2), themeFileValues.valueAt(i2));
                }
                Theme.ThemeAccent accent = theme.getAccent(false);
                if (accent != null) {
                    accent.fillAccentColors(themeFileValues, this.currentColors);
                }
            }
            invalidateAll();
        }
        str = string;
        boolean z2 = storyEntry.isDark;
        this.isDark = z2;
        if (!z2) {
        }
        String[] strArr2 = new String[1];
        String str32 = theme.assetName;
        if (str32 == null) {
        }
        this.currentColors.clear();
        defaultColors = Theme.getDefaultColors();
        if (defaultColors != null) {
        }
        if (themeFileValues != null) {
        }
        invalidateAll();
    }

    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    protected void updatePosition() {
        setX(getPositionX() - (getMeasuredWidth() / 2.0f));
        setY(getPositionY() - (getMeasuredHeight() / 2.0f));
        updateSelectionView();
        if (this.usesBackgroundPaint) {
            invalidateAll();
        }
    }
}
