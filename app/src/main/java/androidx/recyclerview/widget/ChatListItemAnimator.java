package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.BotHelpCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ChatGreetingsView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.TextMessageEnterTransition;
import org.telegram.ui.VoiceMessageEnterTransition;
/* loaded from: classes.dex */
public class ChatListItemAnimator extends DefaultItemAnimator {
    public static final Interpolator DEFAULT_INTERPOLATOR = new CubicBezierInterpolator(0.19919472913616398d, 0.010644531250000006d, 0.27920937042459737d, 0.91025390625d);
    private final ChatActivity activity;
    private ChatGreetingsView chatGreetingsView;
    private RecyclerView.ViewHolder greetingsSticker;
    private final RecyclerListView recyclerListView;
    private final Theme.ResourcesProvider resourcesProvider;
    private boolean reversePositions;
    private boolean shouldAnimateEnterFromBottom;
    private HashMap<Integer, MessageObject.GroupedMessages> willRemovedGroup = new HashMap<>();
    private ArrayList<MessageObject.GroupedMessages> willChangedGroups = new ArrayList<>();
    HashMap<RecyclerView.ViewHolder, Animator> animators = new HashMap<>();
    ArrayList<Runnable> runOnAnimationsEnd = new ArrayList<>();
    HashMap<Long, Long> groupIdToEnterDelay = new HashMap<>();

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public long getChangeDuration() {
        return 250L;
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    protected long getMoveAnimationDelay() {
        return 0L;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public long getMoveDuration() {
        return 250L;
    }

    public void onAnimationStart() {
    }

    public ChatListItemAnimator(ChatActivity chatActivity, RecyclerListView recyclerListView, Theme.ResourcesProvider resourcesProvider) {
        this.resourcesProvider = resourcesProvider;
        this.activity = chatActivity;
        this.recyclerListView = recyclerListView;
        this.translationInterpolator = DEFAULT_INTERPOLATOR;
        this.alwaysCreateMoveAnimationIfPossible = true;
        setSupportsChangeAnimations(false);
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void runPendingAnimations() {
        boolean z = !this.mPendingRemovals.isEmpty();
        boolean z2 = !this.mPendingMoves.isEmpty();
        boolean z3 = !this.mPendingChanges.isEmpty();
        boolean z4 = !this.mPendingAdditions.isEmpty();
        if (z || z2 || z4 || z3) {
            boolean z5 = false;
            if (this.shouldAnimateEnterFromBottom) {
                boolean z6 = false;
                for (int i = 0; i < this.mPendingAdditions.size(); i++) {
                    if (this.reversePositions) {
                        if (this.mPendingAdditions.get(i).getLayoutPosition() != (this.recyclerListView.getAdapter() == null ? 0 : this.recyclerListView.getAdapter().getItemCount()) - 1) {
                        }
                        z6 = true;
                    } else {
                        if (this.mPendingAdditions.get(i).getLayoutPosition() != 0) {
                        }
                        z6 = true;
                    }
                }
                z5 = z6;
            }
            onAnimationStart();
            if (z5) {
                runMessageEnterTransition();
            } else {
                runAlphaEnterTransition();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.addUpdateListener(new ChatListItemAnimator$$ExternalSyntheticLambda2(this));
            ofFloat.setDuration(getRemoveDuration() + getMoveDuration());
            ofFloat.start();
        }
    }

    public /* synthetic */ void lambda$runPendingAnimations$0(ValueAnimator valueAnimator) {
        ChatActivity chatActivity = this.activity;
        if (chatActivity != null) {
            chatActivity.onListItemAnimatorTick();
        } else {
            this.recyclerListView.invalidate();
        }
    }

    private void runAlphaEnterTransition() {
        boolean z = !this.mPendingRemovals.isEmpty();
        boolean z2 = !this.mPendingMoves.isEmpty();
        boolean z3 = !this.mPendingChanges.isEmpty();
        boolean z4 = !this.mPendingAdditions.isEmpty();
        if (z || z2 || z4 || z3) {
            Iterator<RecyclerView.ViewHolder> it = this.mPendingRemovals.iterator();
            while (it.hasNext()) {
                animateRemoveImpl(it.next());
            }
            this.mPendingRemovals.clear();
            if (z2) {
                ArrayList<DefaultItemAnimator.MoveInfo> arrayList = new ArrayList<>();
                arrayList.addAll(this.mPendingMoves);
                this.mMovesList.add(arrayList);
                this.mPendingMoves.clear();
                AnonymousClass1 anonymousClass1 = new AnonymousClass1(arrayList);
                if (this.delayAnimations && z) {
                    ViewCompat.postOnAnimationDelayed(arrayList.get(0).holder.itemView, anonymousClass1, getMoveAnimationDelay());
                } else {
                    anonymousClass1.run();
                }
            }
            if (z3) {
                ArrayList<DefaultItemAnimator.ChangeInfo> arrayList2 = new ArrayList<>();
                arrayList2.addAll(this.mPendingChanges);
                this.mChangesList.add(arrayList2);
                this.mPendingChanges.clear();
                AnonymousClass2 anonymousClass2 = new AnonymousClass2(arrayList2);
                if (this.delayAnimations && z) {
                    ViewCompat.postOnAnimationDelayed(arrayList2.get(0).oldHolder.itemView, anonymousClass2, 0L);
                } else {
                    anonymousClass2.run();
                }
            }
            if (!z4) {
                return;
            }
            ArrayList arrayList3 = new ArrayList();
            arrayList3.addAll(this.mPendingAdditions);
            this.mPendingAdditions.clear();
            Collections.sort(arrayList3, ChatListItemAnimator$$ExternalSyntheticLambda7.INSTANCE);
            Iterator it2 = arrayList3.iterator();
            while (it2.hasNext()) {
                animateAddImpl((RecyclerView.ViewHolder) it2.next());
            }
            arrayList3.clear();
        }
    }

    /* renamed from: androidx.recyclerview.widget.ChatListItemAnimator$1 */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        final /* synthetic */ ArrayList val$moves;

        AnonymousClass1(ArrayList arrayList) {
            ChatListItemAnimator.this = r1;
            this.val$moves = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            Iterator it = this.val$moves.iterator();
            while (it.hasNext()) {
                DefaultItemAnimator.MoveInfo moveInfo = (DefaultItemAnimator.MoveInfo) it.next();
                ChatListItemAnimator.this.animateMoveImpl(moveInfo.holder, moveInfo);
            }
            this.val$moves.clear();
            ChatListItemAnimator.this.mMovesList.remove(this.val$moves);
        }
    }

    /* renamed from: androidx.recyclerview.widget.ChatListItemAnimator$2 */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements Runnable {
        final /* synthetic */ ArrayList val$changes;

        AnonymousClass2(ArrayList arrayList) {
            ChatListItemAnimator.this = r1;
            this.val$changes = arrayList;
        }

        @Override // java.lang.Runnable
        public void run() {
            Iterator it = this.val$changes.iterator();
            while (it.hasNext()) {
                ChatListItemAnimator.this.animateChangeImpl((DefaultItemAnimator.ChangeInfo) it.next());
            }
            this.val$changes.clear();
            ChatListItemAnimator.this.mChangesList.remove(this.val$changes);
        }
    }

    public static /* synthetic */ int lambda$runAlphaEnterTransition$1(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        return viewHolder2.itemView.getTop() - viewHolder.itemView.getTop();
    }

    private void runMessageEnterTransition() {
        boolean z = !this.mPendingRemovals.isEmpty();
        boolean z2 = !this.mPendingMoves.isEmpty();
        boolean z3 = !this.mPendingChanges.isEmpty();
        boolean z4 = !this.mPendingAdditions.isEmpty();
        if (z || z2 || z4 || z3) {
            int i = 0;
            for (int i2 = 0; i2 < this.mPendingAdditions.size(); i2++) {
                View view = this.mPendingAdditions.get(i2).itemView;
                if (view instanceof ChatMessageCell) {
                    ChatMessageCell chatMessageCell = (ChatMessageCell) view;
                    if (chatMessageCell.getCurrentPosition() != null && (chatMessageCell.getCurrentPosition().flags & 1) == 0) {
                    }
                }
                i += this.mPendingAdditions.get(i2).itemView.getHeight();
            }
            Iterator<RecyclerView.ViewHolder> it = this.mPendingRemovals.iterator();
            while (it.hasNext()) {
                animateRemoveImpl(it.next());
            }
            this.mPendingRemovals.clear();
            if (z2) {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(this.mPendingMoves);
                this.mPendingMoves.clear();
                Iterator it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    DefaultItemAnimator.MoveInfo moveInfo = (DefaultItemAnimator.MoveInfo) it2.next();
                    animateMoveImpl(moveInfo.holder, moveInfo);
                }
                arrayList.clear();
            }
            if (!z4) {
                return;
            }
            ArrayList arrayList2 = new ArrayList();
            arrayList2.addAll(this.mPendingAdditions);
            this.mPendingAdditions.clear();
            Iterator it3 = arrayList2.iterator();
            while (it3.hasNext()) {
                animateAddImpl((RecyclerView.ViewHolder) it3.next(), i);
            }
            arrayList2.clear();
        }
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean animateAppearance(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo2) {
        int i;
        boolean animateAppearance = super.animateAppearance(viewHolder, itemHolderInfo, itemHolderInfo2);
        if (animateAppearance && this.shouldAnimateEnterFromBottom) {
            boolean z = false;
            for (int i2 = 0; i2 < this.mPendingAdditions.size(); i2++) {
                if (this.mPendingAdditions.get(i2).getLayoutPosition() == 0) {
                    z = true;
                }
            }
            if (z) {
                i = 0;
                for (int i3 = 0; i3 < this.mPendingAdditions.size(); i3++) {
                    i += this.mPendingAdditions.get(i3).itemView.getHeight();
                }
            } else {
                i = 0;
            }
            for (int i4 = 0; i4 < this.mPendingAdditions.size(); i4++) {
                this.mPendingAdditions.get(i4).itemView.setTranslationY(i);
            }
        }
        return animateAppearance;
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        resetAnimation(viewHolder);
        viewHolder.itemView.setAlpha(0.0f);
        if (!this.shouldAnimateEnterFromBottom) {
            viewHolder.itemView.setScaleX(0.9f);
            viewHolder.itemView.setScaleY(0.9f);
        } else {
            View view = viewHolder.itemView;
            if (view instanceof ChatMessageCell) {
                ((ChatMessageCell) view).getTransitionParams().messageEntering = true;
            }
        }
        this.mPendingAdditions.add(viewHolder);
        return true;
    }

    public void animateAddImpl(RecyclerView.ViewHolder viewHolder, int i) {
        View view = viewHolder.itemView;
        ViewPropertyAnimator animate = view.animate();
        this.mAddAnimations.add(viewHolder);
        view.setTranslationY(i);
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
        View view2 = viewHolder.itemView;
        ChatMessageCell chatMessageCell = view2 instanceof ChatMessageCell ? (ChatMessageCell) view2 : null;
        if (chatMessageCell == null || !chatMessageCell.getTransitionParams().ignoreAlpha) {
            viewHolder.itemView.setAlpha(1.0f);
        }
        if (chatMessageCell != null && this.activity.animatingMessageObjects.contains(chatMessageCell.getMessageObject())) {
            this.activity.animatingMessageObjects.remove(chatMessageCell.getMessageObject());
            if (this.activity.getChatActivityEnterView().canShowMessageTransition()) {
                if (chatMessageCell.getMessageObject().isVoice()) {
                    if (Math.abs(view.getTranslationY()) < view.getMeasuredHeight() * 3.0f) {
                        new VoiceMessageEnterTransition(chatMessageCell, this.activity.getChatActivityEnterView(), this.recyclerListView, this.activity.messageEnterTransitionContainer, this.resourcesProvider).start();
                    }
                } else if (SharedConfig.getDevicePerformanceClass() != 0 && Math.abs(view.getTranslationY()) < this.recyclerListView.getMeasuredHeight()) {
                    ChatActivity chatActivity = this.activity;
                    new TextMessageEnterTransition(chatMessageCell, chatActivity, this.recyclerListView, chatActivity.messageEnterTransitionContainer, this.resourcesProvider).start();
                }
                this.activity.getChatActivityEnterView().startMessageTransition();
            }
        }
        animate.translationY(0.0f).setDuration(getMoveDuration()).setInterpolator(this.translationInterpolator).setListener(new AnonymousClass3(viewHolder, view, animate)).start();
    }

    /* renamed from: androidx.recyclerview.widget.ChatListItemAnimator$3 */
    /* loaded from: classes.dex */
    public class AnonymousClass3 extends AnimatorListenerAdapter {
        final /* synthetic */ ViewPropertyAnimator val$animation;
        final /* synthetic */ RecyclerView.ViewHolder val$holder;
        final /* synthetic */ View val$view;

        AnonymousClass3(RecyclerView.ViewHolder viewHolder, View view, ViewPropertyAnimator viewPropertyAnimator) {
            ChatListItemAnimator.this = r1;
            this.val$holder = viewHolder;
            this.val$view = view;
            this.val$animation = viewPropertyAnimator;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            ChatListItemAnimator.this.dispatchAddStarting(this.val$holder);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.val$view.setTranslationY(0.0f);
            View view = this.val$view;
            if (view instanceof ChatMessageCell) {
                ((ChatMessageCell) view).getTransitionParams().messageEntering = false;
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            View view = this.val$view;
            if (view instanceof ChatMessageCell) {
                ((ChatMessageCell) view).getTransitionParams().messageEntering = false;
            }
            this.val$animation.setListener(null);
            if (ChatListItemAnimator.this.mAddAnimations.remove(this.val$holder)) {
                ChatListItemAnimator.this.dispatchAddFinished(this.val$holder);
                ChatListItemAnimator.this.dispatchFinishedWhenDone();
            }
        }
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate remove");
        }
        boolean animateRemove = super.animateRemove(viewHolder, itemHolderInfo);
        if (animateRemove && itemHolderInfo != null) {
            int i = itemHolderInfo.top;
            int top = viewHolder.itemView.getTop();
            int left = viewHolder.itemView.getLeft() - itemHolderInfo.left;
            int i2 = top - i;
            if (i2 != 0) {
                viewHolder.itemView.setTranslationY(-i2);
            }
            View view = viewHolder.itemView;
            if (view instanceof ChatMessageCell) {
                ChatMessageCell chatMessageCell = (ChatMessageCell) view;
                if (left != 0) {
                    chatMessageCell.setAnimationOffsetX(-left);
                }
                if (itemHolderInfo instanceof ItemHolderInfoExtended) {
                    ItemHolderInfoExtended itemHolderInfoExtended = (ItemHolderInfoExtended) itemHolderInfo;
                    chatMessageCell.setImageCoords(itemHolderInfoExtended.imageX, itemHolderInfoExtended.imageY, itemHolderInfoExtended.imageWidth, itemHolderInfoExtended.imageHeight);
                }
            } else if (left != 0) {
                view.setTranslationX(-left);
            }
        }
        return animateRemove;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:185:0x046c  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0099  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x00ba  */
    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean animateMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        ChatMessageCell chatMessageCell;
        float f;
        float f2;
        float f3;
        float f4;
        int i7;
        int i8;
        int i9;
        boolean z;
        float f5;
        MessageObject.GroupedMessages groupedMessages;
        int i10;
        int i11;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate move");
        }
        View view = viewHolder.itemView;
        ChatMessageCell chatMessageCell2 = null;
        if (view instanceof ChatMessageCell) {
            chatMessageCell2 = (ChatMessageCell) view;
            i6 = i + ((int) chatMessageCell2.getAnimationOffsetX());
            if (chatMessageCell2.getTransitionParams().lastTopOffset != chatMessageCell2.getTopMediaOffset()) {
                i5 = i2 + (chatMessageCell2.getTransitionParams().lastTopOffset - chatMessageCell2.getTopMediaOffset());
                chatMessageCell = chatMessageCell2;
                int i12 = i6;
                int translationY = i5 + ((int) viewHolder.itemView.getTranslationY());
                int[] iArr = new int[4];
                if (chatMessageCell == null) {
                    float imageX = chatMessageCell.getPhotoImage().getImageX();
                    float imageY = chatMessageCell.getPhotoImage().getImageY();
                    f3 = chatMessageCell.getPhotoImage().getImageWidth();
                    float imageHeight = chatMessageCell.getPhotoImage().getImageHeight();
                    for (int i13 = 0; i13 < 4; i13++) {
                        iArr[i13] = chatMessageCell.getPhotoImage().getRoundRadius()[i13];
                    }
                    f = imageX;
                    f2 = imageY;
                    f4 = imageHeight;
                } else {
                    f4 = 0.0f;
                    f3 = 0.0f;
                    f2 = 0.0f;
                    f = 0.0f;
                }
                resetAnimation(viewHolder);
                int i14 = i3 - i12;
                i7 = i4 - translationY;
                if (i7 != 0) {
                    view.setTranslationY(-i7);
                }
                int i15 = i14;
                float f6 = f4;
                float f7 = f3;
                float f8 = f2;
                float f9 = f;
                MoveInfoExtended moveInfoExtended = new MoveInfoExtended(this, viewHolder, i12, translationY, i3, i4);
                if (chatMessageCell == null) {
                    ChatMessageCell.TransitionParams transitionParams = chatMessageCell.getTransitionParams();
                    if (!transitionParams.supportChangeAnimation()) {
                        if (i15 == 0 && i7 == 0) {
                            dispatchMoveFinished(viewHolder);
                            return false;
                        }
                        if (i15 != 0) {
                            view.setTranslationX(-i15);
                        }
                        this.mPendingMoves.add(moveInfoExtended);
                        checkIsRunning();
                        return true;
                    }
                    MessageObject.GroupedMessages currentMessagesGroup = chatMessageCell.getCurrentMessagesGroup();
                    if (i15 != 0) {
                        chatMessageCell.setAnimationOffsetX(-i15);
                    }
                    if (itemHolderInfo instanceof ItemHolderInfoExtended) {
                        ImageReceiver photoImage = chatMessageCell.getPhotoImage();
                        ItemHolderInfoExtended itemHolderInfoExtended = (ItemHolderInfoExtended) itemHolderInfo;
                        boolean z2 = (!transitionParams.wasDraw || itemHolderInfoExtended.imageHeight == 0.0f || itemHolderInfoExtended.imageWidth == 0.0f) ? false : true;
                        moveInfoExtended.animateImage = z2;
                        if (z2) {
                            this.recyclerListView.setClipChildren(false);
                            this.recyclerListView.invalidate();
                            transitionParams.imageChangeBoundsTransition = true;
                            if (chatMessageCell.getMessageObject().isRoundVideo()) {
                                transitionParams.animateToImageX = f9;
                                transitionParams.animateToImageY = f8;
                                transitionParams.animateToImageW = f7;
                                transitionParams.animateToImageH = f6;
                                transitionParams.animateToRadius = iArr;
                            } else {
                                transitionParams.animateToImageX = photoImage.getImageX();
                                transitionParams.animateToImageY = photoImage.getImageY();
                                transitionParams.animateToImageW = photoImage.getImageWidth();
                                transitionParams.animateToImageH = photoImage.getImageHeight();
                                transitionParams.animateToRadius = photoImage.getRoundRadius();
                            }
                            transitionParams.animateRadius = false;
                            int i16 = 0;
                            while (true) {
                                if (i16 >= 4) {
                                    break;
                                } else if (transitionParams.imageRoundRadius[i16] != transitionParams.animateToRadius[i16]) {
                                    transitionParams.animateRadius = true;
                                    break;
                                } else {
                                    i16++;
                                }
                            }
                            float f10 = transitionParams.animateToImageX;
                            float f11 = itemHolderInfoExtended.imageX;
                            if (f10 == f11 && transitionParams.animateToImageY == itemHolderInfoExtended.imageY && transitionParams.animateToImageH == itemHolderInfoExtended.imageHeight && transitionParams.animateToImageW == itemHolderInfoExtended.imageWidth && !transitionParams.animateRadius) {
                                transitionParams.imageChangeBoundsTransition = false;
                                moveInfoExtended.animateImage = false;
                            } else {
                                moveInfoExtended.imageX = f11;
                                moveInfoExtended.imageY = itemHolderInfoExtended.imageY;
                                moveInfoExtended.imageWidth = itemHolderInfoExtended.imageWidth;
                                moveInfoExtended.imageHeight = itemHolderInfoExtended.imageHeight;
                                if (currentMessagesGroup != null) {
                                    boolean z3 = currentMessagesGroup.hasCaption;
                                    MessageObject.GroupedMessages.TransitionParams transitionParams2 = currentMessagesGroup.transitionParams;
                                    boolean z4 = transitionParams2.drawCaptionLayout;
                                    if (z3 != z4) {
                                        transitionParams2.captionEnterProgress = z4 ? 1.0f : 0.0f;
                                    }
                                }
                                if (transitionParams.animateRadius) {
                                    if (transitionParams.animateToRadius == photoImage.getRoundRadius()) {
                                        transitionParams.animateToRadius = new int[4];
                                        for (int i17 = 0; i17 < 4; i17++) {
                                            transitionParams.animateToRadius[i17] = photoImage.getRoundRadius()[i17];
                                        }
                                    }
                                    photoImage.setRoundRadius(transitionParams.imageRoundRadius);
                                }
                                chatMessageCell.setImageCoords(moveInfoExtended.imageX, moveInfoExtended.imageY, moveInfoExtended.imageWidth, moveInfoExtended.imageHeight);
                            }
                        }
                        if (currentMessagesGroup == null && transitionParams.wasDraw) {
                            boolean isOutOwner = chatMessageCell.getMessageObject().isOutOwner();
                            if (((isOutOwner && transitionParams.lastDrawingBackgroundRect.left != chatMessageCell.getBackgroundDrawableLeft()) || (!isOutOwner && transitionParams.lastDrawingBackgroundRect.right != chatMessageCell.getBackgroundDrawableRight())) || transitionParams.lastDrawingBackgroundRect.top != chatMessageCell.getBackgroundDrawableTop() || transitionParams.lastDrawingBackgroundRect.bottom != chatMessageCell.getBackgroundDrawableBottom()) {
                                moveInfoExtended.deltaBottom = chatMessageCell.getBackgroundDrawableBottom() - transitionParams.lastDrawingBackgroundRect.bottom;
                                moveInfoExtended.deltaTop = chatMessageCell.getBackgroundDrawableTop() - transitionParams.lastDrawingBackgroundRect.top;
                                if (isOutOwner) {
                                    moveInfoExtended.deltaLeft = chatMessageCell.getBackgroundDrawableLeft() - transitionParams.lastDrawingBackgroundRect.left;
                                } else {
                                    moveInfoExtended.deltaRight = chatMessageCell.getBackgroundDrawableRight() - transitionParams.lastDrawingBackgroundRect.right;
                                }
                                moveInfoExtended.animateBackgroundOnly = true;
                                transitionParams.animateBackgroundBoundsInner = true;
                                transitionParams.deltaLeft = -moveInfoExtended.deltaLeft;
                                transitionParams.deltaRight = -moveInfoExtended.deltaRight;
                                transitionParams.deltaTop = -moveInfoExtended.deltaTop;
                                transitionParams.deltaBottom = -moveInfoExtended.deltaBottom;
                                this.recyclerListView.setClipChildren(false);
                                this.recyclerListView.invalidate();
                            }
                        }
                    }
                    if (currentMessagesGroup == null || !this.willChangedGroups.contains(currentMessagesGroup)) {
                        i9 = i7;
                        i8 = i15;
                    } else {
                        this.willChangedGroups.remove(currentMessagesGroup);
                        RecyclerListView recyclerListView = (RecyclerListView) viewHolder.itemView.getParent();
                        MessageObject.GroupedMessages.TransitionParams transitionParams3 = currentMessagesGroup.transitionParams;
                        int i18 = 0;
                        int i19 = 0;
                        int i20 = 0;
                        int i21 = 0;
                        int i22 = 0;
                        boolean z5 = true;
                        while (i19 < recyclerListView.getChildCount()) {
                            View childAt = recyclerListView.getChildAt(i19);
                            if (childAt instanceof ChatMessageCell) {
                                ChatMessageCell chatMessageCell3 = (ChatMessageCell) childAt;
                                if (chatMessageCell3.getCurrentMessagesGroup() == currentMessagesGroup && !chatMessageCell3.getMessageObject().deleted) {
                                    int left = chatMessageCell3.getLeft() + chatMessageCell3.getBackgroundDrawableLeft();
                                    groupedMessages = currentMessagesGroup;
                                    int left2 = chatMessageCell3.getLeft() + chatMessageCell3.getBackgroundDrawableRight();
                                    i11 = i7;
                                    int top = chatMessageCell3.getTop() + chatMessageCell3.getBackgroundDrawableTop();
                                    i10 = i15;
                                    int top2 = chatMessageCell3.getTop() + chatMessageCell3.getBackgroundDrawableBottom();
                                    if (i22 == 0 || left < i22) {
                                        i22 = left;
                                    }
                                    if (i18 == 0 || left2 > i18) {
                                        i18 = left2;
                                    }
                                    if (chatMessageCell3.getTransitionParams().wasDraw || transitionParams3.isNewGroup) {
                                        if (i20 == 0 || top < i20) {
                                            i20 = top;
                                        }
                                        if (i21 == 0 || top2 > i21) {
                                            i21 = top2;
                                        }
                                        z5 = false;
                                    }
                                    i19++;
                                    currentMessagesGroup = groupedMessages;
                                    i7 = i11;
                                    i15 = i10;
                                }
                            }
                            groupedMessages = currentMessagesGroup;
                            i11 = i7;
                            i10 = i15;
                            i19++;
                            currentMessagesGroup = groupedMessages;
                            i7 = i11;
                            i15 = i10;
                        }
                        i9 = i7;
                        i8 = i15;
                        transitionParams3.isNewGroup = false;
                        if (i20 == 0 && i21 == 0 && i22 == 0 && i18 == 0) {
                            moveInfoExtended.animateChangeGroupBackground = false;
                            transitionParams3.backgroundChangeBounds = false;
                        } else {
                            int i23 = (-i20) + transitionParams3.top;
                            moveInfoExtended.groupOffsetTop = i23;
                            int i24 = (-i21) + transitionParams3.bottom;
                            moveInfoExtended.groupOffsetBottom = i24;
                            int i25 = (-i22) + transitionParams3.left;
                            moveInfoExtended.groupOffsetLeft = i25;
                            int i26 = (-i18) + transitionParams3.right;
                            moveInfoExtended.groupOffsetRight = i26;
                            moveInfoExtended.animateChangeGroupBackground = true;
                            transitionParams3.backgroundChangeBounds = true;
                            transitionParams3.offsetTop = i23;
                            transitionParams3.offsetBottom = i24;
                            transitionParams3.offsetLeft = i25;
                            transitionParams3.offsetRight = i26;
                            transitionParams3.captionEnterProgress = transitionParams3.drawCaptionLayout ? 1.0f : 0.0f;
                            recyclerListView.setClipChildren(false);
                            recyclerListView.invalidate();
                        }
                        transitionParams3.drawBackgroundForDeletedItems = z5;
                    }
                    MessageObject.GroupedMessages groupedMessages2 = this.willRemovedGroup.get(Integer.valueOf(chatMessageCell.getMessageObject().getId()));
                    if (groupedMessages2 != null) {
                        MessageObject.GroupedMessages.TransitionParams transitionParams4 = groupedMessages2.transitionParams;
                        this.willRemovedGroup.remove(Integer.valueOf(chatMessageCell.getMessageObject().getId()));
                        if (transitionParams.wasDraw) {
                            int left3 = chatMessageCell.getLeft() + chatMessageCell.getBackgroundDrawableLeft();
                            int left4 = chatMessageCell.getLeft() + chatMessageCell.getBackgroundDrawableRight();
                            int top3 = chatMessageCell.getTop() + chatMessageCell.getBackgroundDrawableTop();
                            int top4 = chatMessageCell.getTop() + chatMessageCell.getBackgroundDrawableBottom();
                            moveInfoExtended.animateRemoveGroup = true;
                            transitionParams.animateBackgroundBoundsInner = true;
                            int i27 = left3 - transitionParams4.left;
                            moveInfoExtended.deltaLeft = i27;
                            moveInfoExtended.deltaRight = left4 - transitionParams4.right;
                            moveInfoExtended.deltaTop = top3 - transitionParams4.top;
                            moveInfoExtended.deltaBottom = top4 - transitionParams4.bottom;
                            moveInfoExtended.animateBackgroundOnly = false;
                            transitionParams.deltaLeft = (int) ((-i27) - chatMessageCell.getAnimationOffsetX());
                            transitionParams.deltaRight = (int) ((-moveInfoExtended.deltaRight) - chatMessageCell.getAnimationOffsetX());
                            transitionParams.deltaTop = (int) ((-moveInfoExtended.deltaTop) - chatMessageCell.getTranslationY());
                            transitionParams.deltaBottom = (int) ((-moveInfoExtended.deltaBottom) - chatMessageCell.getTranslationY());
                            z = true;
                            transitionParams.transformGroupToSingleMessage = true;
                            this.recyclerListView.setClipChildren(false);
                            this.recyclerListView.invalidate();
                        } else {
                            z = true;
                            transitionParams4.drawBackgroundForDeletedItems = true;
                        }
                    } else {
                        z = true;
                    }
                    if (transitionParams.drawPinnedBottomBackground != chatMessageCell.isDrawPinnedBottom()) {
                        moveInfoExtended.animatePinnedBottom = z;
                        f5 = 0.0f;
                        transitionParams.changePinnedBottomProgress = 0.0f;
                    } else {
                        f5 = 0.0f;
                    }
                    boolean animateChange = chatMessageCell.getTransitionParams().animateChange();
                    moveInfoExtended.animateChangeInternal = animateChange;
                    if (animateChange) {
                        chatMessageCell.getTransitionParams().animateChange = z;
                        chatMessageCell.getTransitionParams().animateChangeProgress = f5;
                    }
                    if (i8 == 0 && i9 == 0 && !moveInfoExtended.animateImage && !moveInfoExtended.animateRemoveGroup && !moveInfoExtended.animateChangeGroupBackground && !moveInfoExtended.animatePinnedBottom && !moveInfoExtended.animateBackgroundOnly && !moveInfoExtended.animateChangeInternal) {
                        dispatchMoveFinished(viewHolder);
                        return false;
                    }
                } else {
                    View view2 = viewHolder.itemView;
                    if (view2 instanceof BotHelpCell) {
                        ((BotHelpCell) view2).setAnimating(true);
                    } else if (i15 == 0 && i7 == 0) {
                        dispatchMoveFinished(viewHolder);
                        return false;
                    } else if (i15 != 0) {
                        view.setTranslationX(-i15);
                    }
                }
                this.mPendingMoves.add(moveInfoExtended);
                checkIsRunning();
                return true;
            }
        } else {
            i6 = i + ((int) view.getTranslationX());
        }
        i5 = i2;
        chatMessageCell = chatMessageCell2;
        int i122 = i6;
        int translationY2 = i5 + ((int) viewHolder.itemView.getTranslationY());
        int[] iArr2 = new int[4];
        if (chatMessageCell == null) {
        }
        resetAnimation(viewHolder);
        int i142 = i3 - i122;
        i7 = i4 - translationY2;
        if (i7 != 0) {
        }
        int i152 = i142;
        float f62 = f4;
        float f72 = f3;
        float f82 = f2;
        float f92 = f;
        MoveInfoExtended moveInfoExtended2 = new MoveInfoExtended(this, viewHolder, i122, translationY2, i3, i4);
        if (chatMessageCell == null) {
        }
        this.mPendingMoves.add(moveInfoExtended2);
        checkIsRunning();
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0232  */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v14 */
    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void animateMoveImpl(RecyclerView.ViewHolder viewHolder, DefaultItemAnimator.MoveInfo moveInfo) {
        int i;
        View view;
        RecyclerView.ViewHolder viewHolder2;
        TimeInterpolator timeInterpolator;
        ChatMessageCell chatMessageCell;
        MoveInfoExtended moveInfoExtended;
        ChatMessageCell.TransitionParams transitionParams;
        boolean z;
        ChatMessageCell chatMessageCell2;
        char c;
        int i2;
        int[] iArr;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate move impl");
        }
        int i3 = moveInfo.fromX;
        int i4 = moveInfo.fromY;
        int i5 = moveInfo.toY;
        View view2 = viewHolder.itemView;
        int i6 = i5 - i4;
        AnimatorSet animatorSet = new AnimatorSet();
        if (i6 != 0) {
            animatorSet.playTogether(ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, 0.0f));
        }
        this.mMoveAnimations.add(viewHolder);
        MoveInfoExtended moveInfoExtended2 = (MoveInfoExtended) moveInfo;
        if (this.activity != null) {
            View view3 = viewHolder.itemView;
            if (view3 instanceof BotHelpCell) {
                BotHelpCell botHelpCell = (BotHelpCell) view3;
                float translationY = botHelpCell.getTranslationY();
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.addUpdateListener(new AnonymousClass4(botHelpCell, translationY));
                animatorSet.playTogether(ofFloat);
                viewHolder2 = viewHolder;
                view = view2;
                i = i6;
                timeInterpolator = this.translationInterpolator;
                if (timeInterpolator != null) {
                    animatorSet.setInterpolator(timeInterpolator);
                }
                animatorSet.setDuration(getMoveDuration());
                animatorSet.addListener(new AnonymousClass6(viewHolder2, i, view));
                animatorSet.start();
                this.animators.put(viewHolder2, animatorSet);
            }
        }
        View view4 = viewHolder.itemView;
        if (view4 instanceof ChatMessageCell) {
            ChatMessageCell chatMessageCell3 = (ChatMessageCell) view4;
            ChatMessageCell.TransitionParams transitionParams2 = chatMessageCell3.getTransitionParams();
            animatorSet.playTogether(ObjectAnimator.ofFloat(chatMessageCell3, chatMessageCell3.ANIMATION_OFFSET_X, 0.0f));
            if (moveInfoExtended2.animateImage) {
                chatMessageCell3.setImageCoords(moveInfoExtended2.imageX, moveInfoExtended2.imageY, moveInfoExtended2.imageWidth, moveInfoExtended2.imageHeight);
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                float f = chatMessageCell3.getCurrentMessagesGroup() == null ? transitionParams2.captionEnterProgress : chatMessageCell3.getCurrentMessagesGroup().transitionParams.captionEnterProgress;
                float f2 = chatMessageCell3.getCurrentMessagesGroup() == null ? chatMessageCell3.hasCaptionLayout() : chatMessageCell3.getCurrentMessagesGroup().hasCaption ? 1.0f : 0.0f;
                boolean z2 = f != f2;
                if (transitionParams2.animateRadius) {
                    int[] iArr2 = new int[4];
                    for (int i7 = 0; i7 < 4; i7++) {
                        iArr2[i7] = chatMessageCell3.getPhotoImage().getRoundRadius()[i7];
                    }
                    iArr = iArr2;
                } else {
                    iArr = null;
                }
                view = view2;
                transitionParams = transitionParams2;
                chatMessageCell = chatMessageCell3;
                i = i6;
                moveInfoExtended = moveInfoExtended2;
                z = false;
                ofFloat2.addUpdateListener(new ChatListItemAnimator$$ExternalSyntheticLambda1(moveInfoExtended2, transitionParams2, z2, f, f2, chatMessageCell, iArr, viewHolder));
                animatorSet.playTogether(ofFloat2);
            } else {
                chatMessageCell = chatMessageCell3;
                view = view2;
                i = i6;
                z = false;
                transitionParams = transitionParams2;
                moveInfoExtended = moveInfoExtended2;
            }
            if (moveInfoExtended.deltaBottom != 0 || moveInfoExtended.deltaRight != 0 || moveInfoExtended.deltaTop != 0 || moveInfoExtended.deltaLeft != 0) {
                this.recyclerListView.setClipChildren(z);
                this.recyclerListView.invalidate();
                ValueAnimator ofFloat3 = ValueAnimator.ofFloat(1.0f, 0.0f);
                if (moveInfoExtended.animateBackgroundOnly) {
                    transitionParams.toDeltaLeft = -moveInfoExtended.deltaLeft;
                    transitionParams.toDeltaRight = -moveInfoExtended.deltaRight;
                } else {
                    transitionParams.toDeltaLeft = (-moveInfoExtended.deltaLeft) - chatMessageCell.getAnimationOffsetX();
                    transitionParams.toDeltaRight = (-moveInfoExtended.deltaRight) - chatMessageCell.getAnimationOffsetX();
                }
                chatMessageCell2 = chatMessageCell;
                ofFloat3.addUpdateListener(new ChatListItemAnimator$$ExternalSyntheticLambda0(moveInfoExtended, transitionParams, chatMessageCell2));
                Animator[] animatorArr = new Animator[1];
                char c2 = z ? 1 : 0;
                char c3 = z ? 1 : 0;
                animatorArr[c2] = ofFloat3;
                animatorSet.playTogether(animatorArr);
            } else {
                transitionParams.toDeltaLeft = 0.0f;
                transitionParams.toDeltaRight = 0.0f;
                chatMessageCell2 = chatMessageCell;
            }
            MessageObject.GroupedMessages currentMessagesGroup = chatMessageCell2.getCurrentMessagesGroup();
            if (currentMessagesGroup == null) {
                moveInfoExtended.animateChangeGroupBackground = z;
            }
            if (moveInfoExtended.animateChangeGroupBackground) {
                ValueAnimator ofFloat4 = ValueAnimator.ofFloat(1.0f, 0.0f);
                MessageObject.GroupedMessages.TransitionParams transitionParams3 = currentMessagesGroup.transitionParams;
                viewHolder2 = viewHolder;
                c = 0;
                RecyclerListView recyclerListView = (RecyclerListView) viewHolder2.itemView.getParent();
                float f3 = currentMessagesGroup.transitionParams.captionEnterProgress;
                float f4 = currentMessagesGroup.hasCaption ? 1.0f : 0.0f;
                ofFloat4.addUpdateListener(new ChatListItemAnimator$$ExternalSyntheticLambda3(transitionParams3, moveInfoExtended, f3 != f4, f3, f4, recyclerListView));
                ofFloat4.addListener(new AnonymousClass5(this, transitionParams3));
                animatorSet.playTogether(ofFloat4);
            } else {
                viewHolder2 = viewHolder;
                c = 0;
            }
            if (moveInfoExtended.animatePinnedBottom) {
                ValueAnimator ofFloat5 = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat5.addUpdateListener(new ChatListItemAnimator$$ExternalSyntheticLambda5(transitionParams, chatMessageCell2));
                i2 = 1;
                Animator[] animatorArr2 = new Animator[1];
                animatorArr2[c] = ofFloat5;
                animatorSet.playTogether(animatorArr2);
            } else {
                i2 = 1;
            }
            if (moveInfoExtended.animateChangeInternal) {
                ValueAnimator ofFloat6 = ValueAnimator.ofFloat(0.0f, 1.0f);
                transitionParams.animateChange = i2;
                ofFloat6.addUpdateListener(new ChatListItemAnimator$$ExternalSyntheticLambda4(transitionParams, chatMessageCell2));
                Animator[] animatorArr3 = new Animator[i2];
                animatorArr3[c] = ofFloat6;
                animatorSet.playTogether(animatorArr3);
            }
            timeInterpolator = this.translationInterpolator;
            if (timeInterpolator != null) {
            }
            animatorSet.setDuration(getMoveDuration());
            animatorSet.addListener(new AnonymousClass6(viewHolder2, i, view));
            animatorSet.start();
            this.animators.put(viewHolder2, animatorSet);
        }
        viewHolder2 = viewHolder;
        view = view2;
        i = i6;
        timeInterpolator = this.translationInterpolator;
        if (timeInterpolator != null) {
        }
        animatorSet.setDuration(getMoveDuration());
        animatorSet.addListener(new AnonymousClass6(viewHolder2, i, view));
        animatorSet.start();
        this.animators.put(viewHolder2, animatorSet);
    }

    /* renamed from: androidx.recyclerview.widget.ChatListItemAnimator$4 */
    /* loaded from: classes.dex */
    public class AnonymousClass4 implements ValueAnimator.AnimatorUpdateListener {
        final /* synthetic */ float val$animateFrom;
        final /* synthetic */ BotHelpCell val$botCell;

        AnonymousClass4(BotHelpCell botHelpCell, float f) {
            ChatListItemAnimator.this = r1;
            this.val$botCell = botHelpCell;
            this.val$animateFrom = f;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            float measuredHeight = ((((ChatListItemAnimator.this.recyclerListView.getMeasuredHeight() - ChatListItemAnimator.this.activity.getChatListViewPadding()) - ChatListItemAnimator.this.activity.blurredViewBottomOffset) / 2.0f) - (this.val$botCell.getMeasuredHeight() / 2.0f)) + ChatListItemAnimator.this.activity.getChatListViewPadding();
            this.val$botCell.setTranslationY((this.val$animateFrom * (1.0f - floatValue)) + ((((float) this.val$botCell.getTop()) > measuredHeight ? measuredHeight - this.val$botCell.getTop() : 0.0f) * floatValue));
        }
    }

    public static /* synthetic */ void lambda$animateMoveImpl$2(MoveInfoExtended moveInfoExtended, ChatMessageCell.TransitionParams transitionParams, boolean z, float f, float f2, ChatMessageCell chatMessageCell, int[] iArr, RecyclerView.ViewHolder viewHolder, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        float f3 = 1.0f - floatValue;
        float f4 = (moveInfoExtended.imageX * f3) + (transitionParams.animateToImageX * floatValue);
        float f5 = (moveInfoExtended.imageY * f3) + (transitionParams.animateToImageY * floatValue);
        float f6 = (moveInfoExtended.imageWidth * f3) + (transitionParams.animateToImageW * floatValue);
        float f7 = (moveInfoExtended.imageHeight * f3) + (transitionParams.animateToImageH * floatValue);
        if (z) {
            float f8 = (f * f3) + (f2 * floatValue);
            transitionParams.captionEnterProgress = f8;
            if (chatMessageCell.getCurrentMessagesGroup() != null) {
                chatMessageCell.getCurrentMessagesGroup().transitionParams.captionEnterProgress = f8;
            }
        }
        if (transitionParams.animateRadius) {
            ImageReceiver photoImage = chatMessageCell.getPhotoImage();
            int[] iArr2 = transitionParams.animateToRadius;
            photoImage.setRoundRadius((int) ((iArr[0] * f3) + (iArr2[0] * floatValue)), (int) ((iArr[1] * f3) + (iArr2[1] * floatValue)), (int) ((iArr[2] * f3) + (iArr2[2] * floatValue)), (int) ((iArr[3] * f3) + (iArr2[3] * floatValue)));
        }
        chatMessageCell.setImageCoords(f4, f5, f6, f7);
        viewHolder.itemView.invalidate();
    }

    public static /* synthetic */ void lambda$animateMoveImpl$3(MoveInfoExtended moveInfoExtended, ChatMessageCell.TransitionParams transitionParams, ChatMessageCell chatMessageCell, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        if (moveInfoExtended.animateBackgroundOnly) {
            transitionParams.deltaLeft = (-moveInfoExtended.deltaLeft) * floatValue;
            transitionParams.deltaRight = (-moveInfoExtended.deltaRight) * floatValue;
            transitionParams.deltaTop = (-moveInfoExtended.deltaTop) * floatValue;
            transitionParams.deltaBottom = (-moveInfoExtended.deltaBottom) * floatValue;
        } else {
            transitionParams.deltaLeft = ((-moveInfoExtended.deltaLeft) * floatValue) - chatMessageCell.getAnimationOffsetX();
            transitionParams.deltaRight = ((-moveInfoExtended.deltaRight) * floatValue) - chatMessageCell.getAnimationOffsetX();
            transitionParams.deltaTop = ((-moveInfoExtended.deltaTop) * floatValue) - chatMessageCell.getTranslationY();
            transitionParams.deltaBottom = ((-moveInfoExtended.deltaBottom) * floatValue) - chatMessageCell.getTranslationY();
        }
        chatMessageCell.invalidate();
    }

    public static /* synthetic */ void lambda$animateMoveImpl$4(MessageObject.GroupedMessages.TransitionParams transitionParams, MoveInfoExtended moveInfoExtended, boolean z, float f, float f2, RecyclerListView recyclerListView, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        transitionParams.offsetTop = moveInfoExtended.groupOffsetTop * floatValue;
        transitionParams.offsetBottom = moveInfoExtended.groupOffsetBottom * floatValue;
        transitionParams.offsetLeft = moveInfoExtended.groupOffsetLeft * floatValue;
        transitionParams.offsetRight = moveInfoExtended.groupOffsetRight * floatValue;
        if (z) {
            transitionParams.captionEnterProgress = (f * floatValue) + (f2 * (1.0f - floatValue));
        }
        if (recyclerListView != null) {
            recyclerListView.invalidate();
        }
    }

    /* renamed from: androidx.recyclerview.widget.ChatListItemAnimator$5 */
    /* loaded from: classes.dex */
    public class AnonymousClass5 extends AnimatorListenerAdapter {
        final /* synthetic */ MessageObject.GroupedMessages.TransitionParams val$groupTransitionParams;

        AnonymousClass5(ChatListItemAnimator chatListItemAnimator, MessageObject.GroupedMessages.TransitionParams transitionParams) {
            this.val$groupTransitionParams = transitionParams;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            MessageObject.GroupedMessages.TransitionParams transitionParams = this.val$groupTransitionParams;
            transitionParams.backgroundChangeBounds = false;
            transitionParams.drawBackgroundForDeletedItems = false;
        }
    }

    public static /* synthetic */ void lambda$animateMoveImpl$5(ChatMessageCell.TransitionParams transitionParams, ChatMessageCell chatMessageCell, ValueAnimator valueAnimator) {
        transitionParams.changePinnedBottomProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatMessageCell.invalidate();
    }

    public static /* synthetic */ void lambda$animateMoveImpl$6(ChatMessageCell.TransitionParams transitionParams, ChatMessageCell chatMessageCell, ValueAnimator valueAnimator) {
        transitionParams.animateChangeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatMessageCell.invalidate();
    }

    /* renamed from: androidx.recyclerview.widget.ChatListItemAnimator$6 */
    /* loaded from: classes.dex */
    public class AnonymousClass6 extends AnimatorListenerAdapter {
        final /* synthetic */ int val$deltaY;
        final /* synthetic */ RecyclerView.ViewHolder val$holder;
        final /* synthetic */ View val$view;

        AnonymousClass6(RecyclerView.ViewHolder viewHolder, int i, View view) {
            ChatListItemAnimator.this = r1;
            this.val$holder = viewHolder;
            this.val$deltaY = i;
            this.val$view = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            ChatListItemAnimator.this.dispatchMoveStarting(this.val$holder);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (this.val$deltaY != 0) {
                this.val$view.setTranslationY(0.0f);
            }
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            MessageObject.GroupedMessages currentMessagesGroup;
            animator.removeAllListeners();
            ChatListItemAnimator.this.restoreTransitionParams(this.val$holder.itemView);
            if ((this.val$holder.itemView instanceof ChatMessageCell) && (currentMessagesGroup = ((ChatMessageCell) this.val$view).getCurrentMessagesGroup()) != null) {
                currentMessagesGroup.transitionParams.reset();
            }
            if (ChatListItemAnimator.this.mMoveAnimations.remove(this.val$holder)) {
                ChatListItemAnimator.this.dispatchMoveFinished(this.val$holder);
                ChatListItemAnimator.this.dispatchFinishedWhenDone();
            }
        }
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    public void resetAnimation(RecyclerView.ViewHolder viewHolder) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("reset animation");
        }
        super.resetAnimation(viewHolder);
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateChange(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, int i, int i2, int i3, int i4) {
        float f;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate change");
        }
        if (viewHolder == viewHolder2) {
            return animateMove(viewHolder, itemHolderInfo, i, i2, i3, i4);
        }
        View view = viewHolder.itemView;
        if (view instanceof ChatMessageCell) {
            f = ((ChatMessageCell) view).getAnimationOffsetX();
        } else {
            f = view.getTranslationX();
        }
        float translationY = viewHolder.itemView.getTranslationY();
        float alpha = viewHolder.itemView.getAlpha();
        resetAnimation(viewHolder);
        int i5 = (int) ((i3 - i) - f);
        int i6 = (int) ((i4 - i2) - translationY);
        View view2 = viewHolder.itemView;
        if (view2 instanceof ChatMessageCell) {
            ((ChatMessageCell) view2).setAnimationOffsetX(f);
        } else {
            view2.setTranslationX(f);
        }
        viewHolder.itemView.setTranslationY(translationY);
        viewHolder.itemView.setAlpha(alpha);
        if (viewHolder2 != null) {
            resetAnimation(viewHolder2);
            View view3 = viewHolder2.itemView;
            if (view3 instanceof ChatMessageCell) {
                ((ChatMessageCell) view3).setAnimationOffsetX(-i5);
            } else {
                view3.setTranslationX(-i5);
            }
            viewHolder2.itemView.setTranslationY(-i6);
            viewHolder2.itemView.setAlpha(0.0f);
        }
        this.mPendingChanges.add(new DefaultItemAnimator.ChangeInfo(viewHolder, viewHolder2, i, i2, i3, i4));
        checkIsRunning();
        return true;
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    void animateChangeImpl(DefaultItemAnimator.ChangeInfo changeInfo) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate change impl");
        }
        RecyclerView.ViewHolder viewHolder = changeInfo.oldHolder;
        View view = null;
        View view2 = viewHolder == null ? null : viewHolder.itemView;
        RecyclerView.ViewHolder viewHolder2 = changeInfo.newHolder;
        if (viewHolder2 != null) {
            view = viewHolder2.itemView;
        }
        if (view2 != null) {
            ViewPropertyAnimator duration = view2.animate().setDuration(getChangeDuration());
            this.mChangeAnimations.add(changeInfo.oldHolder);
            duration.translationX(changeInfo.toX - changeInfo.fromX);
            duration.translationY(changeInfo.toY - changeInfo.fromY);
            duration.alpha(0.0f).setListener(new AnonymousClass7(changeInfo, duration, view2)).start();
        }
        if (view != null) {
            ViewPropertyAnimator animate = view.animate();
            this.mChangeAnimations.add(changeInfo.newHolder);
            animate.translationX(0.0f).translationY(0.0f).setDuration(getChangeDuration()).alpha(1.0f).setListener(new AnonymousClass8(changeInfo, animate, view)).start();
        }
    }

    /* renamed from: androidx.recyclerview.widget.ChatListItemAnimator$7 */
    /* loaded from: classes.dex */
    public class AnonymousClass7 extends AnimatorListenerAdapter {
        final /* synthetic */ DefaultItemAnimator.ChangeInfo val$changeInfo;
        final /* synthetic */ ViewPropertyAnimator val$oldViewAnim;
        final /* synthetic */ View val$view;

        AnonymousClass7(DefaultItemAnimator.ChangeInfo changeInfo, ViewPropertyAnimator viewPropertyAnimator, View view) {
            ChatListItemAnimator.this = r1;
            this.val$changeInfo = changeInfo;
            this.val$oldViewAnim = viewPropertyAnimator;
            this.val$view = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            ChatListItemAnimator.this.dispatchChangeStarting(this.val$changeInfo.oldHolder, true);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.val$oldViewAnim.setListener(null);
            this.val$view.setAlpha(1.0f);
            this.val$view.setScaleX(1.0f);
            this.val$view.setScaleX(1.0f);
            View view = this.val$view;
            if (view instanceof ChatMessageCell) {
                ((ChatMessageCell) view).setAnimationOffsetX(0.0f);
            } else {
                view.setTranslationX(0.0f);
            }
            this.val$view.setTranslationY(0.0f);
            if (ChatListItemAnimator.this.mChangeAnimations.remove(this.val$changeInfo.oldHolder)) {
                ChatListItemAnimator.this.dispatchChangeFinished(this.val$changeInfo.oldHolder, true);
                ChatListItemAnimator.this.dispatchFinishedWhenDone();
            }
        }
    }

    /* renamed from: androidx.recyclerview.widget.ChatListItemAnimator$8 */
    /* loaded from: classes.dex */
    public class AnonymousClass8 extends AnimatorListenerAdapter {
        final /* synthetic */ DefaultItemAnimator.ChangeInfo val$changeInfo;
        final /* synthetic */ View val$newView;
        final /* synthetic */ ViewPropertyAnimator val$newViewAnimation;

        AnonymousClass8(DefaultItemAnimator.ChangeInfo changeInfo, ViewPropertyAnimator viewPropertyAnimator, View view) {
            ChatListItemAnimator.this = r1;
            this.val$changeInfo = changeInfo;
            this.val$newViewAnimation = viewPropertyAnimator;
            this.val$newView = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            ChatListItemAnimator.this.dispatchChangeStarting(this.val$changeInfo.newHolder, false);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.val$newViewAnimation.setListener(null);
            this.val$newView.setAlpha(1.0f);
            this.val$newView.setScaleX(1.0f);
            this.val$newView.setScaleX(1.0f);
            View view = this.val$newView;
            if (view instanceof ChatMessageCell) {
                ((ChatMessageCell) view).setAnimationOffsetX(0.0f);
            } else {
                view.setTranslationX(0.0f);
            }
            this.val$newView.setTranslationY(0.0f);
            if (ChatListItemAnimator.this.mChangeAnimations.remove(this.val$changeInfo.newHolder)) {
                ChatListItemAnimator.this.dispatchChangeFinished(this.val$changeInfo.newHolder, false);
                ChatListItemAnimator.this.dispatchFinishedWhenDone();
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public RecyclerView.ItemAnimator.ItemHolderInfo recordPreLayoutInformation(RecyclerView.State state, RecyclerView.ViewHolder viewHolder, int i, List<Object> list) {
        RecyclerView.ItemAnimator.ItemHolderInfo recordPreLayoutInformation = super.recordPreLayoutInformation(state, viewHolder, i, list);
        View view = viewHolder.itemView;
        if (view instanceof ChatMessageCell) {
            ItemHolderInfoExtended itemHolderInfoExtended = new ItemHolderInfoExtended(this);
            itemHolderInfoExtended.left = recordPreLayoutInformation.left;
            itemHolderInfoExtended.top = recordPreLayoutInformation.top;
            itemHolderInfoExtended.right = recordPreLayoutInformation.right;
            itemHolderInfoExtended.bottom = recordPreLayoutInformation.bottom;
            ChatMessageCell.TransitionParams transitionParams = ((ChatMessageCell) view).getTransitionParams();
            itemHolderInfoExtended.imageX = transitionParams.lastDrawingImageX;
            itemHolderInfoExtended.imageY = transitionParams.lastDrawingImageY;
            itemHolderInfoExtended.imageWidth = transitionParams.lastDrawingImageW;
            itemHolderInfoExtended.imageHeight = transitionParams.lastDrawingImageH;
            return itemHolderInfoExtended;
        }
        return recordPreLayoutInformation;
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    public void onAllAnimationsDone() {
        super.onAllAnimationsDone();
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("all animations done");
        }
        this.recyclerListView.setClipChildren(true);
        while (!this.runOnAnimationsEnd.isEmpty()) {
            this.runOnAnimationsEnd.remove(0).run();
        }
        cancelAnimators();
    }

    private void cancelAnimators() {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("cancel animations");
        }
        ArrayList arrayList = new ArrayList(this.animators.values());
        this.animators.clear();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Animator animator = (Animator) it.next();
            if (animator != null) {
                animator.cancel();
            }
        }
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimation(RecyclerView.ViewHolder viewHolder) {
        Animator remove = this.animators.remove(viewHolder);
        if (remove != null) {
            remove.cancel();
        }
        super.endAnimation(viewHolder);
        restoreTransitionParams(viewHolder.itemView);
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("end animation");
        }
    }

    public void restoreTransitionParams(View view) {
        view.setAlpha(1.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
        view.setTranslationY(0.0f);
        if (view instanceof BotHelpCell) {
            int measuredHeight = (this.recyclerListView.getMeasuredHeight() / 2) - (view.getMeasuredHeight() / 2);
            ((BotHelpCell) view).setAnimating(false);
            if (view.getTop() > measuredHeight) {
                view.setTranslationY(measuredHeight - view.getTop());
            } else {
                view.setTranslationY(0.0f);
            }
        } else if (view instanceof ChatMessageCell) {
            ChatMessageCell chatMessageCell = (ChatMessageCell) view;
            chatMessageCell.getTransitionParams().resetAnimation();
            chatMessageCell.setAnimationOffsetX(0.0f);
        } else {
            view.setTranslationX(0.0f);
        }
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimations() {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("end animations");
        }
        Iterator<MessageObject.GroupedMessages> it = this.willChangedGroups.iterator();
        while (it.hasNext()) {
            it.next().transitionParams.isNewGroup = false;
        }
        this.willChangedGroups.clear();
        cancelAnimators();
        ChatGreetingsView chatGreetingsView = this.chatGreetingsView;
        if (chatGreetingsView != null) {
            chatGreetingsView.stickerToSendView.setAlpha(1.0f);
        }
        this.greetingsSticker = null;
        this.chatGreetingsView = null;
        for (int size = this.mPendingMoves.size() - 1; size >= 0; size--) {
            DefaultItemAnimator.MoveInfo moveInfo = this.mPendingMoves.get(size);
            restoreTransitionParams(moveInfo.holder.itemView);
            dispatchMoveFinished(moveInfo.holder);
            this.mPendingMoves.remove(size);
        }
        for (int size2 = this.mPendingRemovals.size() - 1; size2 >= 0; size2--) {
            RecyclerView.ViewHolder viewHolder = this.mPendingRemovals.get(size2);
            restoreTransitionParams(viewHolder.itemView);
            dispatchRemoveFinished(viewHolder);
            this.mPendingRemovals.remove(size2);
        }
        for (int size3 = this.mPendingAdditions.size() - 1; size3 >= 0; size3--) {
            RecyclerView.ViewHolder viewHolder2 = this.mPendingAdditions.get(size3);
            restoreTransitionParams(viewHolder2.itemView);
            dispatchAddFinished(viewHolder2);
            this.mPendingAdditions.remove(size3);
        }
        for (int size4 = this.mPendingChanges.size() - 1; size4 >= 0; size4--) {
            endChangeAnimationIfNecessary(this.mPendingChanges.get(size4));
        }
        this.mPendingChanges.clear();
        if (!isRunning()) {
            return;
        }
        for (int size5 = this.mMovesList.size() - 1; size5 >= 0; size5--) {
            ArrayList<DefaultItemAnimator.MoveInfo> arrayList = this.mMovesList.get(size5);
            for (int size6 = arrayList.size() - 1; size6 >= 0; size6--) {
                DefaultItemAnimator.MoveInfo moveInfo2 = arrayList.get(size6);
                restoreTransitionParams(moveInfo2.holder.itemView);
                dispatchMoveFinished(moveInfo2.holder);
                arrayList.remove(size6);
                if (arrayList.isEmpty()) {
                    this.mMovesList.remove(arrayList);
                }
            }
        }
        for (int size7 = this.mAdditionsList.size() - 1; size7 >= 0; size7--) {
            ArrayList<RecyclerView.ViewHolder> arrayList2 = this.mAdditionsList.get(size7);
            for (int size8 = arrayList2.size() - 1; size8 >= 0; size8--) {
                RecyclerView.ViewHolder viewHolder3 = arrayList2.get(size8);
                restoreTransitionParams(viewHolder3.itemView);
                dispatchAddFinished(viewHolder3);
                arrayList2.remove(size8);
                if (arrayList2.isEmpty()) {
                    this.mAdditionsList.remove(arrayList2);
                }
            }
        }
        for (int size9 = this.mChangesList.size() - 1; size9 >= 0; size9--) {
            ArrayList<DefaultItemAnimator.ChangeInfo> arrayList3 = this.mChangesList.get(size9);
            for (int size10 = arrayList3.size() - 1; size10 >= 0; size10--) {
                endChangeAnimationIfNecessary(arrayList3.get(size10));
                if (arrayList3.isEmpty()) {
                    this.mChangesList.remove(arrayList3);
                }
            }
        }
        cancelAll(this.mRemoveAnimations);
        cancelAll(this.mMoveAnimations);
        cancelAll(this.mAddAnimations);
        cancelAll(this.mChangeAnimations);
        dispatchAnimationsFinished();
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    protected boolean endChangeAnimationIfNecessary(DefaultItemAnimator.ChangeInfo changeInfo, RecyclerView.ViewHolder viewHolder) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("end change if necessary");
        }
        Animator remove = this.animators.remove(viewHolder);
        if (remove != null) {
            remove.cancel();
        }
        boolean z = false;
        if (changeInfo.newHolder == viewHolder) {
            changeInfo.newHolder = null;
        } else if (changeInfo.oldHolder != viewHolder) {
            return false;
        } else {
            changeInfo.oldHolder = null;
            z = true;
        }
        restoreTransitionParams(viewHolder.itemView);
        dispatchChangeFinished(viewHolder, z);
        return true;
    }

    public void groupWillTransformToSingleMessage(MessageObject.GroupedMessages groupedMessages) {
        this.willRemovedGroup.put(Integer.valueOf(groupedMessages.messages.get(0).getId()), groupedMessages);
    }

    public void groupWillChanged(MessageObject.GroupedMessages groupedMessages) {
        if (groupedMessages == null) {
            return;
        }
        if (groupedMessages.messages.size() == 0) {
            groupedMessages.transitionParams.drawBackgroundForDeletedItems = true;
            return;
        }
        MessageObject.GroupedMessages.TransitionParams transitionParams = groupedMessages.transitionParams;
        if (transitionParams.top == 0 && transitionParams.bottom == 0 && transitionParams.left == 0 && transitionParams.right == 0) {
            int childCount = this.recyclerListView.getChildCount();
            int i = 0;
            while (true) {
                if (i >= childCount) {
                    break;
                }
                View childAt = this.recyclerListView.getChildAt(i);
                if (childAt instanceof ChatMessageCell) {
                    ChatMessageCell chatMessageCell = (ChatMessageCell) childAt;
                    MessageObject messageObject = chatMessageCell.getMessageObject();
                    if (chatMessageCell.getTransitionParams().wasDraw && groupedMessages.messages.contains(messageObject)) {
                        groupedMessages.transitionParams.top = chatMessageCell.getTop() + chatMessageCell.getBackgroundDrawableTop();
                        groupedMessages.transitionParams.bottom = chatMessageCell.getTop() + chatMessageCell.getBackgroundDrawableBottom();
                        groupedMessages.transitionParams.left = chatMessageCell.getLeft() + chatMessageCell.getBackgroundDrawableLeft();
                        groupedMessages.transitionParams.right = chatMessageCell.getLeft() + chatMessageCell.getBackgroundDrawableRight();
                        groupedMessages.transitionParams.drawCaptionLayout = chatMessageCell.hasCaptionLayout();
                        groupedMessages.transitionParams.pinnedTop = chatMessageCell.isPinnedTop();
                        groupedMessages.transitionParams.pinnedBotton = chatMessageCell.isPinnedBottom();
                        groupedMessages.transitionParams.isNewGroup = true;
                        break;
                    }
                }
                i++;
            }
        }
        this.willChangedGroups.add(groupedMessages);
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x0203  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x023d  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0249  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0257  */
    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void animateAddImpl(RecyclerView.ViewHolder viewHolder) {
        boolean z;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate add impl");
        }
        View view = viewHolder.itemView;
        this.mAddAnimations.add(viewHolder);
        if (viewHolder == this.greetingsSticker) {
            view.setAlpha(1.0f);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        if (view instanceof ChatMessageCell) {
            ChatMessageCell chatMessageCell = (ChatMessageCell) view;
            if (chatMessageCell.getAnimationOffsetX() != 0.0f) {
                animatorSet.playTogether(ObjectAnimator.ofFloat(chatMessageCell, chatMessageCell.ANIMATION_OFFSET_X, chatMessageCell.getAnimationOffsetX(), 0.0f));
            }
            chatMessageCell.setPivotX(chatMessageCell.getBackgroundDrawableLeft() + ((chatMessageCell.getBackgroundDrawableRight() - chatMessageCell.getBackgroundDrawableLeft()) / 2.0f));
            view.animate().translationY(0.0f).setDuration(getAddDuration()).start();
        } else {
            view.animate().translationX(0.0f).translationY(0.0f).setDuration(getAddDuration()).start();
        }
        long max = (1.0f - Math.max(0.0f, Math.min(1.0f, view.getBottom() / this.recyclerListView.getMeasuredHeight()))) * 100.0f;
        if (view instanceof ChatMessageCell) {
            if (viewHolder == this.greetingsSticker) {
                ChatGreetingsView chatGreetingsView = this.chatGreetingsView;
                if (chatGreetingsView != null) {
                    chatGreetingsView.stickerToSendView.setAlpha(0.0f);
                }
                this.recyclerListView.setClipChildren(false);
                ChatMessageCell chatMessageCell2 = (ChatMessageCell) view;
                View view2 = (View) this.chatGreetingsView.getParent();
                float x = this.chatGreetingsView.stickerToSendView.getX() + this.chatGreetingsView.getX() + view2.getX();
                float y = this.chatGreetingsView.stickerToSendView.getY() + this.chatGreetingsView.getY() + view2.getY();
                float imageX = chatMessageCell2.getPhotoImage().getImageX() + this.recyclerListView.getX() + chatMessageCell2.getX();
                float imageY = chatMessageCell2.getPhotoImage().getImageY() + this.recyclerListView.getY() + chatMessageCell2.getY();
                float width = this.chatGreetingsView.stickerToSendView.getWidth();
                float height = this.chatGreetingsView.stickerToSendView.getHeight();
                float imageWidth = chatMessageCell2.getPhotoImage().getImageWidth();
                float imageHeight = chatMessageCell2.getPhotoImage().getImageHeight();
                float f = x - imageX;
                float f2 = y - imageY;
                float imageX2 = chatMessageCell2.getPhotoImage().getImageX();
                float imageY2 = chatMessageCell2.getPhotoImage().getImageY();
                chatMessageCell2.getTransitionParams().imageChangeBoundsTransition = true;
                chatMessageCell2.getTransitionParams().animateDrawingTimeAlpha = true;
                chatMessageCell2.getPhotoImage().setImageCoords(imageX2 + f, imageX2 + f2, width, height);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.addUpdateListener(new ChatListItemAnimator$$ExternalSyntheticLambda6(chatMessageCell2, imageX2, f, imageY2, f2, width, imageWidth, height, imageHeight));
                ofFloat.addListener(new AnonymousClass9(chatMessageCell2, imageX2, imageY2, imageWidth, imageHeight));
                animatorSet.play(ofFloat);
                max = max;
                z = false;
                view.setAlpha(0.0f);
                animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha(), 1.0f));
                if (!z) {
                    view.setScaleX(0.9f);
                    view.setScaleY(0.9f);
                    animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.SCALE_Y, view.getScaleY(), 1.0f));
                    animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.SCALE_X, view.getScaleX(), 1.0f));
                } else {
                    view.setScaleX(1.0f);
                    view.setScaleY(1.0f);
                }
                if (viewHolder != this.greetingsSticker) {
                    animatorSet.setDuration(350L);
                    animatorSet.setInterpolator(new OvershootInterpolator());
                } else {
                    animatorSet.setStartDelay(max);
                    animatorSet.setDuration(250L);
                }
                animatorSet.addListener(new AnonymousClass10(viewHolder, view));
                this.animators.put(viewHolder, animatorSet);
                animatorSet.start();
            }
            MessageObject.GroupedMessages currentMessagesGroup = ((ChatMessageCell) view).getCurrentMessagesGroup();
            if (currentMessagesGroup != null) {
                Long l = this.groupIdToEnterDelay.get(Long.valueOf(currentMessagesGroup.groupId));
                if (l == null) {
                    this.groupIdToEnterDelay.put(Long.valueOf(currentMessagesGroup.groupId), Long.valueOf(max));
                } else {
                    max = l.longValue();
                    if (currentMessagesGroup != null && currentMessagesGroup.transitionParams.backgroundChangeBounds) {
                        animatorSet.setStartDelay(140L);
                    }
                }
            }
            max = max;
            if (currentMessagesGroup != null) {
                animatorSet.setStartDelay(140L);
            }
        }
        z = true;
        view.setAlpha(0.0f);
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha(), 1.0f));
        if (!z) {
        }
        if (viewHolder != this.greetingsSticker) {
        }
        animatorSet.addListener(new AnonymousClass10(viewHolder, view));
        this.animators.put(viewHolder, animatorSet);
        animatorSet.start();
    }

    public static /* synthetic */ void lambda$animateAddImpl$7(ChatMessageCell chatMessageCell, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, ValueAnimator valueAnimator) {
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatMessageCell.getTransitionParams().animateChangeProgress = floatValue;
        if (chatMessageCell.getTransitionParams().animateChangeProgress > 1.0f) {
            chatMessageCell.getTransitionParams().animateChangeProgress = 1.0f;
        }
        float f9 = 1.0f - floatValue;
        chatMessageCell.getPhotoImage().setImageCoords(f + (f2 * f9), f3 + (f4 * f9), (f5 * f9) + (f6 * floatValue), (f7 * f9) + (f8 * floatValue));
        chatMessageCell.invalidate();
    }

    /* renamed from: androidx.recyclerview.widget.ChatListItemAnimator$9 */
    /* loaded from: classes.dex */
    public class AnonymousClass9 extends AnimatorListenerAdapter {
        final /* synthetic */ float val$finalToX;
        final /* synthetic */ float val$finalToY;
        final /* synthetic */ ChatMessageCell val$messageCell;
        final /* synthetic */ float val$toH;
        final /* synthetic */ float val$toW;

        AnonymousClass9(ChatMessageCell chatMessageCell, float f, float f2, float f3, float f4) {
            ChatListItemAnimator.this = r1;
            this.val$messageCell = chatMessageCell;
            this.val$finalToX = f;
            this.val$finalToY = f2;
            this.val$toW = f3;
            this.val$toH = f4;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.val$messageCell.getTransitionParams().resetAnimation();
            this.val$messageCell.getPhotoImage().setImageCoords(this.val$finalToX, this.val$finalToY, this.val$toW, this.val$toH);
            if (ChatListItemAnimator.this.chatGreetingsView != null) {
                ChatListItemAnimator.this.chatGreetingsView.stickerToSendView.setAlpha(1.0f);
            }
            this.val$messageCell.invalidate();
        }
    }

    /* renamed from: androidx.recyclerview.widget.ChatListItemAnimator$10 */
    /* loaded from: classes.dex */
    public class AnonymousClass10 extends AnimatorListenerAdapter {
        final /* synthetic */ RecyclerView.ViewHolder val$holder;
        final /* synthetic */ View val$view;

        AnonymousClass10(RecyclerView.ViewHolder viewHolder, View view) {
            ChatListItemAnimator.this = r1;
            this.val$holder = viewHolder;
            this.val$view = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            ChatListItemAnimator.this.dispatchAddStarting(this.val$holder);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            this.val$view.setAlpha(1.0f);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            animator.removeAllListeners();
            this.val$view.setAlpha(1.0f);
            this.val$view.setScaleX(1.0f);
            this.val$view.setScaleY(1.0f);
            this.val$view.setTranslationY(0.0f);
            this.val$view.setTranslationY(0.0f);
            if (ChatListItemAnimator.this.mAddAnimations.remove(this.val$holder)) {
                ChatListItemAnimator.this.dispatchAddFinished(this.val$holder);
                ChatListItemAnimator.this.dispatchFinishedWhenDone();
            }
        }
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    protected void animateRemoveImpl(RecyclerView.ViewHolder viewHolder) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate remove impl");
        }
        View view = viewHolder.itemView;
        this.mRemoveAnimations.add(viewHolder);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha(), 0.0f);
        dispatchRemoveStarting(viewHolder);
        ofFloat.setDuration(getRemoveDuration());
        ofFloat.addListener(new AnonymousClass11(view, viewHolder));
        this.animators.put(viewHolder, ofFloat);
        ofFloat.start();
        this.recyclerListView.stopScroll();
    }

    /* renamed from: androidx.recyclerview.widget.ChatListItemAnimator$11 */
    /* loaded from: classes.dex */
    public class AnonymousClass11 extends AnimatorListenerAdapter {
        final /* synthetic */ RecyclerView.ViewHolder val$holder;
        final /* synthetic */ View val$view;

        AnonymousClass11(View view, RecyclerView.ViewHolder viewHolder) {
            ChatListItemAnimator.this = r1;
            this.val$view = view;
            this.val$holder = viewHolder;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            animator.removeAllListeners();
            this.val$view.setAlpha(1.0f);
            this.val$view.setScaleX(1.0f);
            this.val$view.setScaleY(1.0f);
            this.val$view.setTranslationX(0.0f);
            this.val$view.setTranslationY(0.0f);
            if (ChatListItemAnimator.this.mRemoveAnimations.remove(this.val$holder)) {
                ChatListItemAnimator.this.dispatchRemoveFinished(this.val$holder);
                ChatListItemAnimator.this.dispatchFinishedWhenDone();
            }
        }
    }

    public void setShouldAnimateEnterFromBottom(boolean z) {
        this.shouldAnimateEnterFromBottom = z;
    }

    public void onDestroy() {
        onAllAnimationsDone();
    }

    public boolean willRemoved(View view) {
        RecyclerView.ViewHolder childViewHolder = this.recyclerListView.getChildViewHolder(view);
        if (childViewHolder != null) {
            return this.mPendingRemovals.contains(childViewHolder) || this.mRemoveAnimations.contains(childViewHolder);
        }
        return false;
    }

    public boolean willAddedFromAlpha(View view) {
        RecyclerView.ViewHolder childViewHolder;
        if (!this.shouldAnimateEnterFromBottom && (childViewHolder = this.recyclerListView.getChildViewHolder(view)) != null) {
            return this.mPendingAdditions.contains(childViewHolder) || this.mAddAnimations.contains(childViewHolder);
        }
        return false;
    }

    public void onGreetingStickerTransition(RecyclerView.ViewHolder viewHolder, ChatGreetingsView chatGreetingsView) {
        this.greetingsSticker = viewHolder;
        this.chatGreetingsView = chatGreetingsView;
        this.shouldAnimateEnterFromBottom = false;
    }

    public void setReversePositions(boolean z) {
        this.reversePositions = z;
    }

    /* loaded from: classes.dex */
    public class MoveInfoExtended extends DefaultItemAnimator.MoveInfo {
        public boolean animateBackgroundOnly;
        public boolean animateChangeGroupBackground;
        public boolean animateChangeInternal;
        boolean animateImage;
        public boolean animatePinnedBottom;
        boolean animateRemoveGroup;
        int deltaBottom;
        int deltaLeft;
        int deltaRight;
        int deltaTop;
        public int groupOffsetBottom;
        public int groupOffsetLeft;
        public int groupOffsetRight;
        public int groupOffsetTop;
        float imageHeight;
        float imageWidth;
        float imageX;
        float imageY;

        MoveInfoExtended(ChatListItemAnimator chatListItemAnimator, RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4) {
            super(viewHolder, i, i2, i3, i4);
        }
    }

    /* loaded from: classes.dex */
    public class ItemHolderInfoExtended extends RecyclerView.ItemAnimator.ItemHolderInfo {
        float imageHeight;
        float imageWidth;
        float imageX;
        float imageY;

        ItemHolderInfoExtended(ChatListItemAnimator chatListItemAnimator) {
        }
    }
}
