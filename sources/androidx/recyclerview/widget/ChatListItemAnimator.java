package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.LongSparseArray;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ChatListItemAnimator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.Utilities;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Cells.BotHelpCell;
import org.telegram.ui.Cells.ChatMessageCell;
import org.telegram.ui.ChatActivity;
import org.telegram.ui.Components.ChatGreetingsView;
import org.telegram.ui.Components.CubicBezierInterpolator;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.Components.ThanosEffect;
import org.telegram.ui.TextMessageEnterTransition;
import org.telegram.ui.VoiceMessageEnterTransition;
/* loaded from: classes.dex */
public class ChatListItemAnimator extends DefaultItemAnimator {
    public static final Interpolator DEFAULT_INTERPOLATOR = new CubicBezierInterpolator(0.19919472913616398d, 0.010644531250000006d, 0.27920937042459737d, 0.91025390625d);
    private final ChatActivity activity;
    private ChatGreetingsView chatGreetingsView;
    private Utilities.Callback0Return<ThanosEffect> getThanosEffectContainer;
    private RecyclerView.ViewHolder greetingsSticker;
    private final RecyclerListView recyclerListView;
    private final Theme.ResourcesProvider resourcesProvider;
    private boolean reversePositions;
    private boolean shouldAnimateEnterFromBottom;
    private Utilities.Callback0Return<Boolean> supportsThanosEffectContainer;
    private HashMap<Integer, MessageObject.GroupedMessages> willRemovedGroup = new HashMap<>();
    private ArrayList<MessageObject.GroupedMessages> willChangedGroups = new ArrayList<>();
    HashMap<RecyclerView.ViewHolder, Animator> animators = new HashMap<>();
    ArrayList<View> thanosViews = new ArrayList<>();
    ArrayList<Runnable> runOnAnimationsEnd = new ArrayList<>();
    HashMap<Long, Long> groupIdToEnterDelay = new HashMap<>();
    private final ArrayList<RecyclerView.ViewHolder> toBeSnapped = new ArrayList<>();

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
        int i;
        boolean z = !this.mPendingRemovals.isEmpty();
        boolean z2 = !this.mPendingMoves.isEmpty();
        boolean z3 = !this.mPendingChanges.isEmpty();
        boolean z4 = !this.mPendingAdditions.isEmpty();
        if (z || z2 || z4 || z3) {
            boolean z5 = false;
            if (this.shouldAnimateEnterFromBottom) {
                boolean z6 = false;
                while (i < this.mPendingAdditions.size()) {
                    if (this.reversePositions) {
                        i = this.mPendingAdditions.get(i).getLayoutPosition() != (this.recyclerListView.getAdapter() == null ? 0 : this.recyclerListView.getAdapter().getItemCount()) - 1 ? i + 1 : 0;
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
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    ChatListItemAnimator.this.lambda$runPendingAnimations$0(valueAnimator);
                }
            });
            ofFloat.setDuration(getRemoveDuration() + getMoveDuration());
            ofFloat.start();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$runPendingAnimations$0(ValueAnimator valueAnimator) {
        ChatActivity chatActivity = this.activity;
        if (chatActivity != null) {
            chatActivity.onListItemAnimatorTick();
        } else {
            this.recyclerListView.invalidate();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:111:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0161  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0193  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void runAlphaEnterTransition() {
        final boolean z;
        Iterator<RecyclerView.ViewHolder> it;
        MessageObject.GroupedMessages currentMessagesGroup;
        MessageObject messageObject;
        Utilities.Callback0Return<Boolean> callback0Return;
        boolean z2 = !this.mPendingRemovals.isEmpty();
        boolean z3 = !this.mPendingMoves.isEmpty();
        boolean z4 = !this.mPendingChanges.isEmpty();
        boolean z5 = !this.mPendingAdditions.isEmpty();
        if (!z2 && !z3 && !z5 && !z4) {
            return;
        }
        boolean z6 = (this.getThanosEffectContainer == null || (callback0Return = this.supportsThanosEffectContainer) == null || !callback0Return.run().booleanValue()) ? false : true;
        if (z6) {
            LongSparseArray longSparseArray = null;
            int i = 0;
            while (i < this.mPendingRemovals.size()) {
                RecyclerView.ViewHolder viewHolder = this.mPendingRemovals.get(i);
                if (this.toBeSnapped.contains(viewHolder)) {
                    View view = viewHolder.itemView;
                    if ((view instanceof ChatMessageCell) && ((ChatMessageCell) view).getCurrentMessagesGroup() != null && (messageObject = ((ChatMessageCell) viewHolder.itemView).getMessageObject()) != null && messageObject.getGroupId() != 0) {
                        if (longSparseArray == null) {
                            longSparseArray = new LongSparseArray();
                        }
                        ArrayList arrayList = (ArrayList) longSparseArray.get(messageObject.getGroupId());
                        if (arrayList == null) {
                            long groupId = messageObject.getGroupId();
                            ArrayList arrayList2 = new ArrayList();
                            longSparseArray.put(groupId, arrayList2);
                            arrayList = arrayList2;
                        }
                        this.toBeSnapped.remove(viewHolder);
                        this.mPendingRemovals.remove(i);
                        i--;
                        arrayList.add(viewHolder);
                    }
                }
                i++;
            }
            if (longSparseArray != null) {
                z = false;
                for (int i2 = 0; i2 < longSparseArray.size(); i2++) {
                    ArrayList<RecyclerView.ViewHolder> arrayList3 = (ArrayList) longSparseArray.valueAt(i2);
                    if (arrayList3.size() > 0) {
                        View view2 = arrayList3.get(0).itemView;
                        if (!(!(view2 instanceof ChatMessageCell) || (currentMessagesGroup = ((ChatMessageCell) view2).getCurrentMessagesGroup()) == null || currentMessagesGroup.messages.size() <= arrayList3.size())) {
                            this.mPendingRemovals.addAll(arrayList3);
                        } else {
                            animateRemoveGroupImpl(arrayList3);
                            z = true;
                        }
                    }
                }
                it = this.mPendingRemovals.iterator();
                while (it.hasNext()) {
                    RecyclerView.ViewHolder next = it.next();
                    boolean z7 = this.toBeSnapped.remove(next) && z6;
                    animateRemoveImpl(next, z7);
                    if (z7) {
                        z = true;
                    }
                }
                this.mPendingRemovals.clear();
                if (z3) {
                    final ArrayList<DefaultItemAnimator.MoveInfo> arrayList4 = new ArrayList<>();
                    arrayList4.addAll(this.mPendingMoves);
                    this.mMovesList.add(arrayList4);
                    this.mPendingMoves.clear();
                    Runnable runnable = new Runnable() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Iterator it2 = arrayList4.iterator();
                            while (it2.hasNext()) {
                                DefaultItemAnimator.MoveInfo moveInfo = (DefaultItemAnimator.MoveInfo) it2.next();
                                ChatListItemAnimator.this.animateMoveImpl(moveInfo.holder, moveInfo, z);
                            }
                            arrayList4.clear();
                            ChatListItemAnimator.this.mMovesList.remove(arrayList4);
                        }
                    };
                    if (this.delayAnimations && z2) {
                        ViewCompat.postOnAnimationDelayed(arrayList4.get(0).holder.itemView, runnable, z ? 0L : getMoveAnimationDelay());
                    } else {
                        runnable.run();
                    }
                }
                if (z4) {
                    final ArrayList<DefaultItemAnimator.ChangeInfo> arrayList5 = new ArrayList<>();
                    arrayList5.addAll(this.mPendingChanges);
                    this.mChangesList.add(arrayList5);
                    this.mPendingChanges.clear();
                    Runnable runnable2 = new Runnable() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Iterator it2 = arrayList5.iterator();
                            while (it2.hasNext()) {
                                ChatListItemAnimator.this.animateChangeImpl((DefaultItemAnimator.ChangeInfo) it2.next());
                            }
                            arrayList5.clear();
                            ChatListItemAnimator.this.mChangesList.remove(arrayList5);
                        }
                    };
                    if (this.delayAnimations && z2) {
                        ViewCompat.postOnAnimationDelayed(arrayList5.get(0).oldHolder.itemView, runnable2, 0L);
                    } else {
                        runnable2.run();
                    }
                }
                if (z5) {
                    return;
                }
                ArrayList arrayList6 = new ArrayList();
                arrayList6.addAll(this.mPendingAdditions);
                this.mPendingAdditions.clear();
                Collections.sort(arrayList6, new Comparator() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda9
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        int lambda$runAlphaEnterTransition$1;
                        lambda$runAlphaEnterTransition$1 = ChatListItemAnimator.lambda$runAlphaEnterTransition$1((RecyclerView.ViewHolder) obj, (RecyclerView.ViewHolder) obj2);
                        return lambda$runAlphaEnterTransition$1;
                    }
                });
                Iterator it2 = arrayList6.iterator();
                while (it2.hasNext()) {
                    animateAddImpl((RecyclerView.ViewHolder) it2.next());
                }
                arrayList6.clear();
                return;
            }
        }
        z = false;
        it = this.mPendingRemovals.iterator();
        while (it.hasNext()) {
        }
        this.mPendingRemovals.clear();
        if (z3) {
        }
        if (z4) {
        }
        if (z5) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$runAlphaEnterTransition$1(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        return viewHolder2.itemView.getTop() - viewHolder.itemView.getTop();
    }

    private void runMessageEnterTransition() {
        int i;
        boolean z = !this.mPendingRemovals.isEmpty();
        boolean z2 = !this.mPendingMoves.isEmpty();
        boolean z3 = !this.mPendingChanges.isEmpty();
        boolean z4 = !this.mPendingAdditions.isEmpty();
        if (z || z2 || z4 || z3) {
            int i2 = 0;
            while (i < this.mPendingAdditions.size()) {
                View view = this.mPendingAdditions.get(i).itemView;
                if (view instanceof ChatMessageCell) {
                    ChatMessageCell chatMessageCell = (ChatMessageCell) view;
                    i = (chatMessageCell.getCurrentPosition() != null && (chatMessageCell.getCurrentPosition().flags & 1) == 0) ? i + 1 : 0;
                }
                i2 += this.mPendingAdditions.get(i).itemView.getHeight();
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
            if (z4) {
                ArrayList arrayList2 = new ArrayList();
                arrayList2.addAll(this.mPendingAdditions);
                this.mPendingAdditions.clear();
                Iterator it3 = arrayList2.iterator();
                while (it3.hasNext()) {
                    animateAddImpl((RecyclerView.ViewHolder) it3.next(), i2);
                }
                arrayList2.clear();
            }
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

    public void animateAddImpl(final RecyclerView.ViewHolder viewHolder, int i) {
        final View view = viewHolder.itemView;
        final ViewPropertyAnimator animate = view.animate();
        this.mAddAnimations.add(viewHolder);
        view.setTranslationY(i);
        viewHolder.itemView.setScaleX(1.0f);
        viewHolder.itemView.setScaleY(1.0f);
        View view2 = viewHolder.itemView;
        ChatMessageCell chatMessageCell = view2 instanceof ChatMessageCell ? (ChatMessageCell) view2 : null;
        if (chatMessageCell == null || !chatMessageCell.getTransitionParams().ignoreAlpha) {
            viewHolder.itemView.setAlpha(1.0f);
        }
        ChatActivity chatActivity = this.activity;
        if (chatActivity != null && chatMessageCell != null && chatActivity.animatingMessageObjects.contains(chatMessageCell.getMessageObject())) {
            this.activity.animatingMessageObjects.remove(chatMessageCell.getMessageObject());
            if (this.activity.getChatActivityEnterView().canShowMessageTransition()) {
                if (chatMessageCell.getMessageObject().isVoice()) {
                    if (Math.abs(view.getTranslationY()) < view.getMeasuredHeight() * 3.0f) {
                        new VoiceMessageEnterTransition(chatMessageCell, this.activity.getChatActivityEnterView(), this.recyclerListView, this.activity.messageEnterTransitionContainer, this.resourcesProvider).start();
                    }
                } else if (SharedConfig.getDevicePerformanceClass() != 0 && Math.abs(view.getTranslationY()) < this.recyclerListView.getMeasuredHeight()) {
                    ChatActivity chatActivity2 = this.activity;
                    new TextMessageEnterTransition(chatMessageCell, chatActivity2, this.recyclerListView, chatActivity2.messageEnterTransitionContainer, this.resourcesProvider).start();
                }
                this.activity.getChatActivityEnterView().startMessageTransition();
            }
        }
        animate.translationY(0.0f).setDuration(getMoveDuration()).setInterpolator(this.translationInterpolator).setListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ChatListItemAnimator.this.dispatchAddStarting(viewHolder);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                view.setTranslationY(0.0f);
                View view3 = view;
                if (view3 instanceof ChatMessageCell) {
                    ((ChatMessageCell) view3).getTransitionParams().messageEntering = false;
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                View view3 = view;
                if (view3 instanceof ChatMessageCell) {
                    ((ChatMessageCell) view3).getTransitionParams().messageEntering = false;
                }
                animate.setListener(null);
                if (ChatListItemAnimator.this.mAddAnimations.remove(viewHolder)) {
                    ChatListItemAnimator.this.dispatchAddFinished(viewHolder);
                    ChatListItemAnimator.this.dispatchFinishedWhenDone();
                }
            }
        }).start();
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

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, int i, int i2, int i3, int i4) {
        int i5;
        int translationX;
        ChatMessageCell chatMessageCell;
        float f;
        float f2;
        float f3;
        float f4;
        int i6;
        int i7;
        boolean z;
        float f5;
        int i8;
        MessageObject.GroupedMessages groupedMessages;
        int i9;
        int i10;
        View view = viewHolder.itemView;
        if (view instanceof ChatMessageCell) {
            ChatMessageCell chatMessageCell2 = (ChatMessageCell) view;
            int animationOffsetX = i + ((int) chatMessageCell2.getAnimationOffsetX());
            i5 = chatMessageCell2.getTransitionParams().lastTopOffset != chatMessageCell2.getTopMediaOffset() ? i2 + (chatMessageCell2.getTransitionParams().lastTopOffset - chatMessageCell2.getTopMediaOffset()) : i2;
            chatMessageCell = chatMessageCell2;
            translationX = animationOffsetX;
        } else {
            i5 = i2;
            translationX = i + ((int) view.getTranslationX());
            chatMessageCell = null;
        }
        int translationY = i5 + ((int) viewHolder.itemView.getTranslationY());
        int[] iArr = new int[4];
        if (chatMessageCell != null) {
            float imageX = chatMessageCell.getPhotoImage().getImageX();
            float imageY = chatMessageCell.getPhotoImage().getImageY();
            f2 = chatMessageCell.getPhotoImage().getImageWidth();
            float imageHeight = chatMessageCell.getPhotoImage().getImageHeight();
            for (int i11 = 0; i11 < 4; i11++) {
                iArr[i11] = chatMessageCell.getPhotoImage().getRoundRadius()[i11];
            }
            f4 = imageX;
            f3 = imageY;
            f = imageHeight;
        } else {
            f = 0.0f;
            f2 = 0.0f;
            f3 = 0.0f;
            f4 = 0.0f;
        }
        resetAnimation(viewHolder);
        int i12 = i3 - translationX;
        int i13 = i4 - translationY;
        if (i13 != 0) {
            view.setTranslationY(-i13);
        }
        int i14 = i12;
        float f6 = f;
        float f7 = f2;
        float f8 = f3;
        float f9 = f4;
        MoveInfoExtended moveInfoExtended = new MoveInfoExtended(this, viewHolder, translationX, translationY, i3, i4);
        if (chatMessageCell != null) {
            ChatMessageCell.TransitionParams transitionParams = chatMessageCell.getTransitionParams();
            if (!transitionParams.supportChangeAnimation()) {
                if (i14 == 0 && i13 == 0) {
                    dispatchMoveFinished(viewHolder);
                    return false;
                }
                if (i14 != 0) {
                    view.setTranslationX(-i14);
                }
                this.mPendingMoves.add(moveInfoExtended);
                checkIsRunning();
                return true;
            }
            MessageObject.GroupedMessages currentMessagesGroup = chatMessageCell.getCurrentMessagesGroup();
            if (i14 != 0) {
                chatMessageCell.setAnimationOffsetX(-i14);
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
                    int i15 = 0;
                    while (true) {
                        if (i15 >= 4) {
                            break;
                        } else if (transitionParams.imageRoundRadius[i15] != transitionParams.animateToRadius[i15]) {
                            transitionParams.animateRadius = true;
                            break;
                        } else {
                            i15++;
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
                                for (int i16 = 0; i16 < 4; i16++) {
                                    transitionParams.animateToRadius[i16] = photoImage.getRoundRadius()[i16];
                                }
                            }
                            photoImage.setRoundRadius(transitionParams.imageRoundRadius);
                        }
                        chatMessageCell.setImageCoords(moveInfoExtended.imageX, moveInfoExtended.imageY, moveInfoExtended.imageWidth, moveInfoExtended.imageHeight);
                    }
                }
                if (currentMessagesGroup == null && transitionParams.wasDraw) {
                    boolean isOutOwner = chatMessageCell.getMessageObject().isOutOwner();
                    if (((isOutOwner && transitionParams.lastDrawingBackgroundRect.left != chatMessageCell.getBackgroundDrawableLeft()) || !(isOutOwner || transitionParams.lastDrawingBackgroundRect.right == chatMessageCell.getBackgroundDrawableRight())) || transitionParams.lastDrawingBackgroundRect.top != chatMessageCell.getBackgroundDrawableTop() || transitionParams.lastDrawingBackgroundRect.bottom != chatMessageCell.getBackgroundDrawableBottom()) {
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
                i6 = i13;
                i7 = i14;
            } else {
                this.willChangedGroups.remove(currentMessagesGroup);
                RecyclerListView recyclerListView = (RecyclerListView) viewHolder.itemView.getParent();
                MessageObject.GroupedMessages.TransitionParams transitionParams3 = currentMessagesGroup.transitionParams;
                int i17 = 0;
                int i18 = 0;
                int i19 = 0;
                int i20 = 0;
                int i21 = 0;
                boolean z5 = true;
                while (i18 < recyclerListView.getChildCount()) {
                    View childAt = recyclerListView.getChildAt(i18);
                    if (childAt instanceof ChatMessageCell) {
                        ChatMessageCell chatMessageCell3 = (ChatMessageCell) childAt;
                        if (chatMessageCell3.getCurrentMessagesGroup() == currentMessagesGroup && !chatMessageCell3.getMessageObject().deleted) {
                            int left = chatMessageCell3.getLeft() + chatMessageCell3.getBackgroundDrawableLeft();
                            groupedMessages = currentMessagesGroup;
                            int left2 = chatMessageCell3.getLeft() + chatMessageCell3.getBackgroundDrawableRight();
                            i9 = i13;
                            int top = chatMessageCell3.getTop() + chatMessageCell3.getBackgroundDrawableTop();
                            i10 = i14;
                            int top2 = chatMessageCell3.getTop() + chatMessageCell3.getBackgroundDrawableBottom();
                            if (i21 == 0 || left < i21) {
                                i21 = left;
                            }
                            if (i17 == 0 || left2 > i17) {
                                i17 = left2;
                            }
                            if (chatMessageCell3.getTransitionParams().wasDraw || transitionParams3.isNewGroup) {
                                if (i19 == 0 || top < i19) {
                                    i19 = top;
                                }
                                if (i20 == 0 || top2 > i20) {
                                    i20 = top2;
                                }
                                z5 = false;
                            }
                            i18++;
                            currentMessagesGroup = groupedMessages;
                            i13 = i9;
                            i14 = i10;
                        }
                    }
                    groupedMessages = currentMessagesGroup;
                    i9 = i13;
                    i10 = i14;
                    i18++;
                    currentMessagesGroup = groupedMessages;
                    i13 = i9;
                    i14 = i10;
                }
                i6 = i13;
                i7 = i14;
                transitionParams3.isNewGroup = false;
                if (i19 == 0 && i20 == 0 && i21 == 0 && i17 == 0) {
                    moveInfoExtended.animateChangeGroupBackground = false;
                    transitionParams3.backgroundChangeBounds = false;
                } else {
                    int i22 = (-i19) + transitionParams3.top;
                    moveInfoExtended.groupOffsetTop = i22;
                    int i23 = (-i20) + transitionParams3.bottom;
                    moveInfoExtended.groupOffsetBottom = i23;
                    int i24 = (-i21) + transitionParams3.left;
                    moveInfoExtended.groupOffsetLeft = i24;
                    int i25 = (-i17) + transitionParams3.right;
                    moveInfoExtended.groupOffsetRight = i25;
                    moveInfoExtended.animateChangeGroupBackground = true;
                    transitionParams3.backgroundChangeBounds = true;
                    transitionParams3.offsetTop = i22;
                    transitionParams3.offsetBottom = i23;
                    transitionParams3.offsetLeft = i24;
                    transitionParams3.offsetRight = i25;
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
                    moveInfoExtended.deltaLeft = left3 - transitionParams4.left;
                    moveInfoExtended.deltaRight = left4 - transitionParams4.right;
                    moveInfoExtended.deltaTop = top3 - transitionParams4.top;
                    moveInfoExtended.deltaBottom = top4 - transitionParams4.bottom;
                    moveInfoExtended.animateBackgroundOnly = false;
                    transitionParams.deltaLeft = (int) ((-i8) - chatMessageCell.getAnimationOffsetX());
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
            if (i7 == 0 && i6 == 0 && !moveInfoExtended.animateImage && !moveInfoExtended.animateRemoveGroup && !moveInfoExtended.animateChangeGroupBackground && !moveInfoExtended.animatePinnedBottom && !moveInfoExtended.animateBackgroundOnly && !moveInfoExtended.animateChangeInternal) {
                dispatchMoveFinished(viewHolder);
                return false;
            }
        } else {
            View view2 = viewHolder.itemView;
            if (view2 instanceof BotHelpCell) {
                ((BotHelpCell) view2).setAnimating(true);
            } else if (i14 == 0 && i13 == 0) {
                dispatchMoveFinished(viewHolder);
                return false;
            } else if (i14 != 0) {
                view.setTranslationX(-i14);
            }
        }
        this.mPendingMoves.add(moveInfoExtended);
        checkIsRunning();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    public void animateMoveImpl(RecyclerView.ViewHolder viewHolder, DefaultItemAnimator.MoveInfo moveInfo) {
        animateMoveImpl(viewHolder, moveInfo, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0227  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x022d  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x023b  */
    /* JADX WARN: Type inference failed for: r10v1 */
    /* JADX WARN: Type inference failed for: r10v2, types: [boolean] */
    /* JADX WARN: Type inference failed for: r10v3 */
    /* JADX WARN: Type inference failed for: r2v11 */
    /* JADX WARN: Type inference failed for: r2v16 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    protected void animateMoveImpl(final RecyclerView.ViewHolder viewHolder, DefaultItemAnimator.MoveInfo moveInfo, boolean z) {
        final ChatMessageCell chatMessageCell;
        View view;
        int i;
        ?? r10;
        final ChatMessageCell.TransitionParams transitionParams;
        final MoveInfoExtended moveInfoExtended;
        final ChatMessageCell chatMessageCell2;
        final RecyclerView.ViewHolder viewHolder2;
        char c;
        int i2;
        int[] iArr;
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
        final MoveInfoExtended moveInfoExtended2 = (MoveInfoExtended) moveInfo;
        if (this.activity != null) {
            View view3 = viewHolder.itemView;
            if (view3 instanceof BotHelpCell) {
                final BotHelpCell botHelpCell = (BotHelpCell) view3;
                final float translationY = botHelpCell.getTranslationY();
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.4
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                        float measuredHeight = ((((ChatListItemAnimator.this.recyclerListView.getMeasuredHeight() - ChatListItemAnimator.this.activity.getChatListViewPadding()) - ChatListItemAnimator.this.activity.blurredViewBottomOffset) / 2.0f) - (botHelpCell.getMeasuredHeight() / 2.0f)) + ChatListItemAnimator.this.activity.getChatListViewPadding();
                        botHelpCell.setTranslationY((translationY * (1.0f - floatValue)) + ((((float) botHelpCell.getTop()) > measuredHeight ? measuredHeight - botHelpCell.getTop() : 0.0f) * floatValue));
                    }
                });
                animatorSet.playTogether(ofFloat);
                viewHolder2 = viewHolder;
                view = view2;
                i = i6;
                if (z) {
                    animatorSet.setInterpolator(CubicBezierInterpolator.EASE_OUT);
                } else {
                    Interpolator interpolator = this.translationInterpolator;
                    if (interpolator != null) {
                        animatorSet.setInterpolator(interpolator);
                    }
                }
                animatorSet.setDuration(((float) getMoveDuration()) * (z ? 1.9f : 1.0f));
                final View view4 = view;
                final int i7 = i;
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.6
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        ChatListItemAnimator.this.dispatchMoveStarting(viewHolder2);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        if (i7 != 0) {
                            view4.setTranslationY(0.0f);
                        }
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        animator.removeAllListeners();
                        ChatListItemAnimator.this.restoreTransitionParams(viewHolder2.itemView);
                        View view5 = viewHolder2.itemView;
                        if (view5 instanceof ChatMessageCell) {
                            ChatMessageCell chatMessageCell3 = (ChatMessageCell) view5;
                            if (chatMessageCell3.makeVisibleAfterChange) {
                                chatMessageCell3.makeVisibleAfterChange = false;
                                chatMessageCell3.setVisibility(0);
                            }
                            MessageObject.GroupedMessages currentMessagesGroup = chatMessageCell3.getCurrentMessagesGroup();
                            if (currentMessagesGroup != null) {
                                currentMessagesGroup.transitionParams.reset();
                            }
                        }
                        if (ChatListItemAnimator.this.mMoveAnimations.remove(viewHolder2)) {
                            ChatListItemAnimator.this.dispatchMoveFinished(viewHolder2);
                            ChatListItemAnimator.this.dispatchFinishedWhenDone();
                        }
                    }
                });
                animatorSet.start();
                this.animators.put(viewHolder2, animatorSet);
            }
        }
        View view5 = viewHolder.itemView;
        if (view5 instanceof ChatMessageCell) {
            ChatMessageCell chatMessageCell3 = (ChatMessageCell) view5;
            final ChatMessageCell.TransitionParams transitionParams2 = chatMessageCell3.getTransitionParams();
            animatorSet.playTogether(ObjectAnimator.ofFloat(chatMessageCell3, chatMessageCell3.ANIMATION_OFFSET_X, 0.0f));
            if (moveInfoExtended2.animateImage) {
                chatMessageCell3.setImageCoords(moveInfoExtended2.imageX, moveInfoExtended2.imageY, moveInfoExtended2.imageWidth, moveInfoExtended2.imageHeight);
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
                final float f = chatMessageCell3.getCurrentMessagesGroup() == null ? transitionParams2.captionEnterProgress : chatMessageCell3.getCurrentMessagesGroup().transitionParams.captionEnterProgress;
                final float hasCaptionLayout = chatMessageCell3.getCurrentMessagesGroup() == null ? chatMessageCell3.hasCaptionLayout() : chatMessageCell3.getCurrentMessagesGroup().hasCaption;
                boolean z2 = f != hasCaptionLayout;
                if (transitionParams2.animateRadius) {
                    int[] iArr2 = new int[4];
                    for (int i8 = 0; i8 < 4; i8++) {
                        iArr2[i8] = chatMessageCell3.getPhotoImage().getRoundRadius()[i8];
                    }
                    iArr = iArr2;
                } else {
                    iArr = null;
                }
                final boolean z3 = z2;
                view = view2;
                transitionParams = transitionParams2;
                chatMessageCell = chatMessageCell3;
                i = i6;
                moveInfoExtended = moveInfoExtended2;
                final int[] iArr3 = iArr;
                r10 = 0;
                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatListItemAnimator.lambda$animateMoveImpl$2(ChatListItemAnimator.MoveInfoExtended.this, transitionParams2, z3, f, hasCaptionLayout, chatMessageCell, iArr3, viewHolder, valueAnimator);
                    }
                });
                animatorSet.playTogether(ofFloat2);
            } else {
                chatMessageCell = chatMessageCell3;
                view = view2;
                i = i6;
                r10 = 0;
                transitionParams = transitionParams2;
                moveInfoExtended = moveInfoExtended2;
            }
            if (moveInfoExtended.deltaBottom != 0 || moveInfoExtended.deltaRight != 0 || moveInfoExtended.deltaTop != 0 || moveInfoExtended.deltaLeft != 0) {
                this.recyclerListView.setClipChildren(r10);
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
                ofFloat3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatListItemAnimator.lambda$animateMoveImpl$3(ChatListItemAnimator.MoveInfoExtended.this, transitionParams, chatMessageCell2, valueAnimator);
                    }
                });
                Animator[] animatorArr = new Animator[1];
                animatorArr[r10] = ofFloat3;
                animatorSet.playTogether(animatorArr);
            } else {
                transitionParams.toDeltaLeft = 0.0f;
                transitionParams.toDeltaRight = 0.0f;
                chatMessageCell2 = chatMessageCell;
            }
            MessageObject.GroupedMessages currentMessagesGroup = chatMessageCell2.getCurrentMessagesGroup();
            if (currentMessagesGroup == null) {
                moveInfoExtended.animateChangeGroupBackground = r10;
            }
            if (moveInfoExtended.animateChangeGroupBackground) {
                ValueAnimator ofFloat4 = ValueAnimator.ofFloat(1.0f, 0.0f);
                final MessageObject.GroupedMessages.TransitionParams transitionParams3 = currentMessagesGroup.transitionParams;
                viewHolder2 = viewHolder;
                c = 0;
                final RecyclerListView recyclerListView = (RecyclerListView) viewHolder2.itemView.getParent();
                final float f2 = currentMessagesGroup.transitionParams.captionEnterProgress;
                final float f3 = currentMessagesGroup.hasCaption ? 1.0f : 0.0f;
                final boolean z4 = f2 != f3;
                final MoveInfoExtended moveInfoExtended3 = moveInfoExtended;
                ofFloat4.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda3
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatListItemAnimator.lambda$animateMoveImpl$4(MessageObject.GroupedMessages.TransitionParams.this, moveInfoExtended3, z4, f2, f3, recyclerListView, valueAnimator);
                    }
                });
                ofFloat4.addListener(new AnimatorListenerAdapter(this) { // from class: androidx.recyclerview.widget.ChatListItemAnimator.5
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        MessageObject.GroupedMessages.TransitionParams transitionParams4 = transitionParams3;
                        transitionParams4.backgroundChangeBounds = false;
                        transitionParams4.drawBackgroundForDeletedItems = false;
                    }
                });
                animatorSet.playTogether(ofFloat4);
            } else {
                viewHolder2 = viewHolder;
                c = 0;
            }
            if (moveInfoExtended.animatePinnedBottom) {
                ValueAnimator ofFloat5 = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat5.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda5
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatListItemAnimator.lambda$animateMoveImpl$5(ChatMessageCell.TransitionParams.this, chatMessageCell2, valueAnimator);
                    }
                });
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
                ofFloat6.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda4
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatListItemAnimator.lambda$animateMoveImpl$6(ChatMessageCell.TransitionParams.this, chatMessageCell2, valueAnimator);
                    }
                });
                Animator[] animatorArr3 = new Animator[i2];
                animatorArr3[c] = ofFloat6;
                animatorSet.playTogether(animatorArr3);
            }
            if (z) {
            }
            animatorSet.setDuration(((float) getMoveDuration()) * (z ? 1.9f : 1.0f));
            final View view42 = view;
            final int i72 = i;
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.6
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    ChatListItemAnimator.this.dispatchMoveStarting(viewHolder2);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    if (i72 != 0) {
                        view42.setTranslationY(0.0f);
                    }
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    animator.removeAllListeners();
                    ChatListItemAnimator.this.restoreTransitionParams(viewHolder2.itemView);
                    View view52 = viewHolder2.itemView;
                    if (view52 instanceof ChatMessageCell) {
                        ChatMessageCell chatMessageCell32 = (ChatMessageCell) view52;
                        if (chatMessageCell32.makeVisibleAfterChange) {
                            chatMessageCell32.makeVisibleAfterChange = false;
                            chatMessageCell32.setVisibility(0);
                        }
                        MessageObject.GroupedMessages currentMessagesGroup2 = chatMessageCell32.getCurrentMessagesGroup();
                        if (currentMessagesGroup2 != null) {
                            currentMessagesGroup2.transitionParams.reset();
                        }
                    }
                    if (ChatListItemAnimator.this.mMoveAnimations.remove(viewHolder2)) {
                        ChatListItemAnimator.this.dispatchMoveFinished(viewHolder2);
                        ChatListItemAnimator.this.dispatchFinishedWhenDone();
                    }
                }
            });
            animatorSet.start();
            this.animators.put(viewHolder2, animatorSet);
        }
        viewHolder2 = viewHolder;
        view = view2;
        i = i6;
        if (z) {
        }
        animatorSet.setDuration(((float) getMoveDuration()) * (z ? 1.9f : 1.0f));
        final View view422 = view;
        final int i722 = i;
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.6
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ChatListItemAnimator.this.dispatchMoveStarting(viewHolder2);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                if (i722 != 0) {
                    view422.setTranslationY(0.0f);
                }
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                animator.removeAllListeners();
                ChatListItemAnimator.this.restoreTransitionParams(viewHolder2.itemView);
                View view52 = viewHolder2.itemView;
                if (view52 instanceof ChatMessageCell) {
                    ChatMessageCell chatMessageCell32 = (ChatMessageCell) view52;
                    if (chatMessageCell32.makeVisibleAfterChange) {
                        chatMessageCell32.makeVisibleAfterChange = false;
                        chatMessageCell32.setVisibility(0);
                    }
                    MessageObject.GroupedMessages currentMessagesGroup2 = chatMessageCell32.getCurrentMessagesGroup();
                    if (currentMessagesGroup2 != null) {
                        currentMessagesGroup2.transitionParams.reset();
                    }
                }
                if (ChatListItemAnimator.this.mMoveAnimations.remove(viewHolder2)) {
                    ChatListItemAnimator.this.dispatchMoveFinished(viewHolder2);
                    ChatListItemAnimator.this.dispatchFinishedWhenDone();
                }
            }
        });
        animatorSet.start();
        this.animators.put(viewHolder2, animatorSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: private */
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

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateMoveImpl$5(ChatMessageCell.TransitionParams transitionParams, ChatMessageCell chatMessageCell, ValueAnimator valueAnimator) {
        transitionParams.changePinnedBottomProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatMessageCell.invalidate();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void lambda$animateMoveImpl$6(ChatMessageCell.TransitionParams transitionParams, ChatMessageCell chatMessageCell, ValueAnimator valueAnimator) {
        transitionParams.animateChangeProgress = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        chatMessageCell.invalidate();
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateChange(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, RecyclerView.ItemAnimator.ItemHolderInfo itemHolderInfo, int i, int i2, int i3, int i4) {
        float translationX;
        if (viewHolder == viewHolder2) {
            return animateMove(viewHolder, itemHolderInfo, i, i2, i3, i4);
        }
        View view = viewHolder.itemView;
        if (view instanceof ChatMessageCell) {
            translationX = ((ChatMessageCell) view).getAnimationOffsetX();
        } else {
            translationX = view.getTranslationX();
        }
        float translationY = viewHolder.itemView.getTranslationY();
        float alpha = viewHolder.itemView.getAlpha();
        resetAnimation(viewHolder);
        int i5 = (int) ((i3 - i) - translationX);
        int i6 = (int) ((i4 - i2) - translationY);
        View view2 = viewHolder.itemView;
        if (view2 instanceof ChatMessageCell) {
            ((ChatMessageCell) view2).setAnimationOffsetX(translationX);
        } else {
            view2.setTranslationX(translationX);
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
    public void animateChangeImpl(final DefaultItemAnimator.ChangeInfo changeInfo) {
        RecyclerView.ViewHolder viewHolder = changeInfo.oldHolder;
        final View view = viewHolder == null ? null : viewHolder.itemView;
        RecyclerView.ViewHolder viewHolder2 = changeInfo.newHolder;
        final View view2 = viewHolder2 != null ? viewHolder2.itemView : null;
        if (view != null) {
            final ViewPropertyAnimator duration = view.animate().setDuration(getChangeDuration());
            this.mChangeAnimations.add(changeInfo.oldHolder);
            duration.translationX(changeInfo.toX - changeInfo.fromX);
            duration.translationY(changeInfo.toY - changeInfo.fromY);
            duration.alpha(0.0f).setListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.7
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    ChatListItemAnimator.this.dispatchChangeStarting(changeInfo.oldHolder, true);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    duration.setListener(null);
                    view.setAlpha(1.0f);
                    view.setScaleX(1.0f);
                    view.setScaleX(1.0f);
                    View view3 = view;
                    if (view3 instanceof ChatMessageCell) {
                        ((ChatMessageCell) view3).setAnimationOffsetX(0.0f);
                    } else {
                        view3.setTranslationX(0.0f);
                    }
                    view.setTranslationY(0.0f);
                    if (ChatListItemAnimator.this.mChangeAnimations.remove(changeInfo.oldHolder)) {
                        ChatListItemAnimator.this.dispatchChangeFinished(changeInfo.oldHolder, true);
                        ChatListItemAnimator.this.dispatchFinishedWhenDone();
                    }
                }
            }).start();
        }
        if (view2 != null) {
            final ViewPropertyAnimator animate = view2.animate();
            this.mChangeAnimations.add(changeInfo.newHolder);
            animate.translationX(0.0f).translationY(0.0f).setDuration(getChangeDuration()).alpha(1.0f).setListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.8
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    ChatListItemAnimator.this.dispatchChangeStarting(changeInfo.newHolder, false);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    animate.setListener(null);
                    view2.setAlpha(1.0f);
                    view2.setScaleX(1.0f);
                    view2.setScaleX(1.0f);
                    View view3 = view2;
                    if (view3 instanceof ChatMessageCell) {
                        ((ChatMessageCell) view3).setAnimationOffsetX(0.0f);
                    } else {
                        view3.setTranslationX(0.0f);
                    }
                    view2.setTranslationY(0.0f);
                    if (ChatListItemAnimator.this.mChangeAnimations.remove(changeInfo.newHolder)) {
                        ChatListItemAnimator.this.dispatchChangeFinished(changeInfo.newHolder, false);
                        ChatListItemAnimator.this.dispatchFinishedWhenDone();
                    }
                }
            }).start();
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    public void onAllAnimationsDone() {
        super.onAllAnimationsDone();
        this.recyclerListView.setClipChildren(true);
        while (!this.runOnAnimationsEnd.isEmpty()) {
            this.runOnAnimationsEnd.remove(0).run();
        }
        cancelAnimators();
    }

    private void cancelAnimators() {
        ThanosEffect run;
        ArrayList arrayList = new ArrayList(this.animators.values());
        this.animators.clear();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Animator animator = (Animator) it.next();
            if (animator != null) {
                animator.cancel();
            }
        }
        if (this.thanosViews.isEmpty() || (run = this.getThanosEffectContainer.run()) == null) {
            return;
        }
        run.kill();
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator, androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimation(RecyclerView.ViewHolder viewHolder) {
        ThanosEffect run;
        Animator remove = this.animators.remove(viewHolder);
        if (remove != null) {
            remove.cancel();
        }
        if (this.thanosViews.contains(viewHolder.itemView) && (run = this.getThanosEffectContainer.run()) != null) {
            run.cancel(viewHolder.itemView);
        }
        super.endAnimation(viewHolder);
        restoreTransitionParams(viewHolder.itemView);
    }

    /* JADX INFO: Access modifiers changed from: private */
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
        if (isRunning()) {
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
    }

    @Override // androidx.recyclerview.widget.DefaultItemAnimator
    protected boolean endChangeAnimationIfNecessary(DefaultItemAnimator.ChangeInfo changeInfo, RecyclerView.ViewHolder viewHolder) {
        ThanosEffect run;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("end change if necessary");
        }
        Animator remove = this.animators.remove(viewHolder);
        if (remove != null) {
            remove.cancel();
        }
        if (this.thanosViews.contains(viewHolder.itemView) && (run = this.getThanosEffectContainer.run()) != null) {
            run.cancel(viewHolder.itemView);
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
    public void animateAddImpl(final RecyclerView.ViewHolder viewHolder) {
        boolean z;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate add impl");
        }
        final View view = viewHolder.itemView;
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
                final ChatMessageCell chatMessageCell2 = (ChatMessageCell) view;
                View view2 = (View) this.chatGreetingsView.getParent();
                float x = this.chatGreetingsView.stickerToSendView.getX() + this.chatGreetingsView.getX() + view2.getX();
                float y = this.chatGreetingsView.stickerToSendView.getY() + this.chatGreetingsView.getY() + view2.getY();
                float imageX = chatMessageCell2.getPhotoImage().getImageX() + this.recyclerListView.getX() + chatMessageCell2.getX();
                float imageY = chatMessageCell2.getPhotoImage().getImageY() + this.recyclerListView.getY() + chatMessageCell2.getY();
                final float width = this.chatGreetingsView.stickerToSendView.getWidth();
                final float height = this.chatGreetingsView.stickerToSendView.getHeight();
                final float imageWidth = chatMessageCell2.getPhotoImage().getImageWidth();
                final float imageHeight = chatMessageCell2.getPhotoImage().getImageHeight();
                final float f = x - imageX;
                final float f2 = y - imageY;
                final float imageX2 = chatMessageCell2.getPhotoImage().getImageX();
                final float imageY2 = chatMessageCell2.getPhotoImage().getImageY();
                chatMessageCell2.getTransitionParams().imageChangeBoundsTransition = true;
                chatMessageCell2.getTransitionParams().animateDrawingTimeAlpha = true;
                chatMessageCell2.getPhotoImage().setImageCoords(imageX2 + f, imageX2 + f2, width, height);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda6
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        ChatListItemAnimator.lambda$animateAddImpl$7(ChatMessageCell.this, imageX2, f, imageY2, f2, width, imageWidth, height, imageHeight, valueAnimator);
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.9
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        chatMessageCell2.getTransitionParams().resetAnimation();
                        chatMessageCell2.getPhotoImage().setImageCoords(imageX2, imageY2, imageWidth, imageHeight);
                        if (ChatListItemAnimator.this.chatGreetingsView != null) {
                            ChatListItemAnimator.this.chatGreetingsView.stickerToSendView.setAlpha(1.0f);
                        }
                        chatMessageCell2.invalidate();
                    }
                });
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
                animatorSet.addListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.10
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        ChatListItemAnimator.this.dispatchAddStarting(viewHolder);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationCancel(Animator animator) {
                        view.setAlpha(1.0f);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        animator.removeAllListeners();
                        view.setAlpha(1.0f);
                        view.setScaleX(1.0f);
                        view.setScaleY(1.0f);
                        view.setTranslationY(0.0f);
                        view.setTranslationY(0.0f);
                        if (ChatListItemAnimator.this.mAddAnimations.remove(viewHolder)) {
                            ChatListItemAnimator.this.dispatchAddFinished(viewHolder);
                            ChatListItemAnimator.this.dispatchFinishedWhenDone();
                        }
                    }
                });
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
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.10
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ChatListItemAnimator.this.dispatchAddStarting(viewHolder);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                view.setAlpha(1.0f);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                animator.removeAllListeners();
                view.setAlpha(1.0f);
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                view.setTranslationY(0.0f);
                view.setTranslationY(0.0f);
                if (ChatListItemAnimator.this.mAddAnimations.remove(viewHolder)) {
                    ChatListItemAnimator.this.dispatchAddFinished(viewHolder);
                    ChatListItemAnimator.this.dispatchFinishedWhenDone();
                }
            }
        });
        this.animators.put(viewHolder, animatorSet);
        animatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
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

    protected void animateRemoveImpl(final RecyclerView.ViewHolder viewHolder, boolean z) {
        Utilities.Callback0Return<ThanosEffect> callback0Return;
        if (BuildVars.LOGS_ENABLED) {
            StringBuilder sb = new StringBuilder();
            sb.append("animate remove impl ");
            sb.append(z ? " with thanos" : "");
            FileLog.d(sb.toString());
        }
        final View view = viewHolder.itemView;
        this.mRemoveAnimations.add(viewHolder);
        if (z && (callback0Return = this.getThanosEffectContainer) != null) {
            dispatchRemoveStarting(viewHolder);
            callback0Return.run().animate(view, new Runnable() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ChatListItemAnimator.this.lambda$animateRemoveImpl$8(view, viewHolder);
                }
            });
            this.thanosViews.add(view);
        } else {
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, View.ALPHA, view.getAlpha(), 0.0f);
            dispatchRemoveStarting(viewHolder);
            ofFloat.setDuration(getRemoveDuration());
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.ChatListItemAnimator.11
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    animator.removeAllListeners();
                    view.setAlpha(1.0f);
                    view.setScaleX(1.0f);
                    view.setScaleY(1.0f);
                    view.setTranslationX(0.0f);
                    view.setTranslationY(0.0f);
                    if (ChatListItemAnimator.this.mRemoveAnimations.remove(viewHolder)) {
                        ChatListItemAnimator.this.dispatchRemoveFinished(viewHolder);
                        ChatListItemAnimator.this.dispatchFinishedWhenDone();
                    }
                }
            });
            this.animators.put(viewHolder, ofFloat);
            ofFloat.start();
        }
        this.recyclerListView.stopScroll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateRemoveImpl$8(View view, RecyclerView.ViewHolder viewHolder) {
        view.setVisibility(0);
        if (this.mRemoveAnimations.remove(viewHolder)) {
            dispatchRemoveFinished(viewHolder);
            dispatchFinishedWhenDone();
        }
        this.thanosViews.remove(view);
    }

    private void animateRemoveGroupImpl(final ArrayList<RecyclerView.ViewHolder> arrayList) {
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("animate remove group impl with thanos");
        }
        this.mRemoveAnimations.addAll(arrayList);
        ThanosEffect run = this.getThanosEffectContainer.run();
        for (int i = 0; i < arrayList.size(); i++) {
            dispatchRemoveStarting(arrayList.get(i));
        }
        final ArrayList<View> arrayList2 = new ArrayList<>();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.add(arrayList.get(i2).itemView);
        }
        run.animateGroup(arrayList2, new Runnable() { // from class: androidx.recyclerview.widget.ChatListItemAnimator$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                ChatListItemAnimator.this.lambda$animateRemoveGroupImpl$9(arrayList2, arrayList);
            }
        });
        this.thanosViews.add(arrayList2.get(0));
        this.recyclerListView.stopScroll();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$animateRemoveGroupImpl$9(ArrayList arrayList, ArrayList arrayList2) {
        for (int i = 0; i < arrayList.size(); i++) {
            ((View) arrayList.get(i)).setVisibility(0);
        }
        if (this.mRemoveAnimations.removeAll(arrayList2)) {
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                dispatchRemoveFinished((RecyclerView.ViewHolder) arrayList2.get(i2));
            }
            dispatchFinishedWhenDone();
        }
        this.thanosViews.removeAll(arrayList);
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
        if (this.shouldAnimateEnterFromBottom || (childViewHolder = this.recyclerListView.getChildViewHolder(view)) == null) {
            return false;
        }
        return this.mPendingAdditions.contains(childViewHolder) || this.mAddAnimations.contains(childViewHolder);
    }

    public void onGreetingStickerTransition(RecyclerView.ViewHolder viewHolder, ChatGreetingsView chatGreetingsView) {
        this.greetingsSticker = viewHolder;
        this.chatGreetingsView = chatGreetingsView;
        this.shouldAnimateEnterFromBottom = false;
    }

    public void setReversePositions(boolean z) {
        this.reversePositions = z;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
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

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ItemHolderInfoExtended extends RecyclerView.ItemAnimator.ItemHolderInfo {
        float imageHeight;
        float imageWidth;
        float imageX;
        float imageY;

        ItemHolderInfoExtended(ChatListItemAnimator chatListItemAnimator) {
        }
    }

    public void prepareThanos(RecyclerView.ViewHolder viewHolder) {
        MessageObject messageObject;
        if (viewHolder == null) {
            return;
        }
        this.toBeSnapped.add(viewHolder);
        View view = viewHolder.itemView;
        if (!(view instanceof ChatMessageCell) || (messageObject = ((ChatMessageCell) view).getMessageObject()) == null) {
            return;
        }
        messageObject.deletedByThanos = true;
    }

    public void setOnSnapMessage(Utilities.Callback0Return<Boolean> callback0Return, Utilities.Callback0Return<ThanosEffect> callback0Return2) {
        this.supportsThanosEffectContainer = callback0Return;
        this.getThanosEffectContainer = callback0Return2;
    }
}
