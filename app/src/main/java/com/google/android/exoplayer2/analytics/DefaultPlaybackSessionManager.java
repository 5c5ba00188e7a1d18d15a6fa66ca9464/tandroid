package com.google.android.exoplayer2.analytics;

import android.util.Base64;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.analytics.PlaybackSessionManager;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
/* loaded from: classes3.dex */
public final class DefaultPlaybackSessionManager implements PlaybackSessionManager {
    private static final Random RANDOM = new Random();
    private static final int SESSION_ID_LENGTH = 12;
    private String currentSessionId;
    private PlaybackSessionManager.Listener listener;
    private final Timeline.Window window = new Timeline.Window();
    private final Timeline.Period period = new Timeline.Period();
    private final HashMap<String, SessionDescriptor> sessions = new HashMap<>();
    private Timeline currentTimeline = Timeline.EMPTY;

    @Override // com.google.android.exoplayer2.analytics.PlaybackSessionManager
    public void setListener(PlaybackSessionManager.Listener listener) {
        this.listener = listener;
    }

    @Override // com.google.android.exoplayer2.analytics.PlaybackSessionManager
    public synchronized String getSessionForMediaPeriodId(Timeline timeline, MediaSource.MediaPeriodId mediaPeriodId) {
        int windowIndex;
        windowIndex = timeline.getPeriodByUid(mediaPeriodId.periodUid, this.period).windowIndex;
        return getOrAddSession(windowIndex, mediaPeriodId).sessionId;
    }

    @Override // com.google.android.exoplayer2.analytics.PlaybackSessionManager
    public synchronized boolean belongsToSession(AnalyticsListener.EventTime eventTime, String sessionId) {
        SessionDescriptor sessionDescriptor = this.sessions.get(sessionId);
        if (sessionDescriptor == null) {
            return false;
        }
        sessionDescriptor.maybeSetWindowSequenceNumber(eventTime.windowIndex, eventTime.mediaPeriodId);
        return sessionDescriptor.belongsToSession(eventTime.windowIndex, eventTime.mediaPeriodId);
    }

    @Override // com.google.android.exoplayer2.analytics.PlaybackSessionManager
    public synchronized void updateSessions(AnalyticsListener.EventTime eventTime) {
        Assertions.checkNotNull(this.listener);
        SessionDescriptor currentSession = this.sessions.get(this.currentSessionId);
        if (eventTime.mediaPeriodId != null && currentSession != null) {
            boolean isAlreadyFinished = false;
            if (currentSession.windowSequenceNumber == -1) {
                if (currentSession.windowIndex != eventTime.windowIndex) {
                    isAlreadyFinished = true;
                }
            } else if (eventTime.mediaPeriodId.windowSequenceNumber < currentSession.windowSequenceNumber) {
                isAlreadyFinished = true;
            }
            if (isAlreadyFinished) {
                return;
            }
        }
        SessionDescriptor eventSession = getOrAddSession(eventTime.windowIndex, eventTime.mediaPeriodId);
        if (this.currentSessionId == null) {
            this.currentSessionId = eventSession.sessionId;
        }
        if (!eventSession.isCreated) {
            eventSession.isCreated = true;
            this.listener.onSessionCreated(eventTime, eventSession.sessionId);
        }
        if (eventSession.sessionId.equals(this.currentSessionId) && !eventSession.isActive) {
            eventSession.isActive = true;
            this.listener.onSessionActive(eventTime, eventSession.sessionId);
        }
    }

    @Override // com.google.android.exoplayer2.analytics.PlaybackSessionManager
    public synchronized void handleTimelineUpdate(AnalyticsListener.EventTime eventTime) {
        Assertions.checkNotNull(this.listener);
        Timeline previousTimeline = this.currentTimeline;
        this.currentTimeline = eventTime.timeline;
        Iterator<SessionDescriptor> iterator = this.sessions.values().iterator();
        while (iterator.hasNext()) {
            SessionDescriptor session = iterator.next();
            if (!session.tryResolvingToNewTimeline(previousTimeline, this.currentTimeline)) {
                iterator.remove();
                if (session.isCreated) {
                    if (session.sessionId.equals(this.currentSessionId)) {
                        this.currentSessionId = null;
                    }
                    this.listener.onSessionFinished(eventTime, session.sessionId, false);
                }
            }
        }
        handlePositionDiscontinuity(eventTime, 4);
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0021 A[Catch: all -> 0x00da, TryCatch #0 {, blocks: (B:3:0x0001, B:10:0x0011, B:11:0x001b, B:13:0x0021, B:15:0x002d, B:17:0x0036, B:20:0x0044, B:25:0x004f, B:26:0x0052, B:28:0x005c, B:30:0x0078, B:33:0x0082, B:35:0x008e, B:37:0x0094, B:39:0x00a0, B:41:0x00ac, B:43:0x00c5, B:45:0x00cb), top: B:50:0x0001 }] */
    @Override // com.google.android.exoplayer2.analytics.PlaybackSessionManager
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public synchronized void handlePositionDiscontinuity(AnalyticsListener.EventTime eventTime, int reason) {
        boolean hasAutomaticTransition;
        Iterator<SessionDescriptor> iterator;
        SessionDescriptor currentSessionDescriptor;
        SessionDescriptor contentSession;
        Assertions.checkNotNull(this.listener);
        if (reason != 0 && reason != 3) {
            hasAutomaticTransition = false;
            iterator = this.sessions.values().iterator();
            while (iterator.hasNext()) {
                SessionDescriptor session = iterator.next();
                if (session.isFinishedAtEventTime(eventTime)) {
                    iterator.remove();
                    if (session.isCreated) {
                        boolean isRemovingCurrentSession = session.sessionId.equals(this.currentSessionId);
                        boolean isAutomaticTransition = hasAutomaticTransition && isRemovingCurrentSession && session.isActive;
                        if (isRemovingCurrentSession) {
                            this.currentSessionId = null;
                        }
                        this.listener.onSessionFinished(eventTime, session.sessionId, isAutomaticTransition);
                    }
                }
            }
            SessionDescriptor previousSessionDescriptor = this.sessions.get(this.currentSessionId);
            currentSessionDescriptor = getOrAddSession(eventTime.windowIndex, eventTime.mediaPeriodId);
            this.currentSessionId = currentSessionDescriptor.sessionId;
            if (eventTime.mediaPeriodId != null && eventTime.mediaPeriodId.isAd() && (previousSessionDescriptor == null || previousSessionDescriptor.windowSequenceNumber != eventTime.mediaPeriodId.windowSequenceNumber || previousSessionDescriptor.adMediaPeriodId == null || previousSessionDescriptor.adMediaPeriodId.adGroupIndex != eventTime.mediaPeriodId.adGroupIndex || previousSessionDescriptor.adMediaPeriodId.adIndexInAdGroup != eventTime.mediaPeriodId.adIndexInAdGroup)) {
                MediaSource.MediaPeriodId contentMediaPeriodId = new MediaSource.MediaPeriodId(eventTime.mediaPeriodId.periodUid, eventTime.mediaPeriodId.windowSequenceNumber);
                contentSession = getOrAddSession(eventTime.windowIndex, contentMediaPeriodId);
                if (contentSession.isCreated && currentSessionDescriptor.isCreated) {
                    this.listener.onAdPlaybackStarted(eventTime, contentSession.sessionId, currentSessionDescriptor.sessionId);
                }
            }
        }
        hasAutomaticTransition = true;
        iterator = this.sessions.values().iterator();
        while (iterator.hasNext()) {
        }
        SessionDescriptor previousSessionDescriptor2 = this.sessions.get(this.currentSessionId);
        currentSessionDescriptor = getOrAddSession(eventTime.windowIndex, eventTime.mediaPeriodId);
        this.currentSessionId = currentSessionDescriptor.sessionId;
        if (eventTime.mediaPeriodId != null) {
            MediaSource.MediaPeriodId contentMediaPeriodId2 = new MediaSource.MediaPeriodId(eventTime.mediaPeriodId.periodUid, eventTime.mediaPeriodId.windowSequenceNumber);
            contentSession = getOrAddSession(eventTime.windowIndex, contentMediaPeriodId2);
            if (contentSession.isCreated) {
                this.listener.onAdPlaybackStarted(eventTime, contentSession.sessionId, currentSessionDescriptor.sessionId);
            }
        }
    }

    @Override // com.google.android.exoplayer2.analytics.PlaybackSessionManager
    public void finishAllSessions(AnalyticsListener.EventTime eventTime) {
        PlaybackSessionManager.Listener listener;
        this.currentSessionId = null;
        Iterator<SessionDescriptor> iterator = this.sessions.values().iterator();
        while (iterator.hasNext()) {
            SessionDescriptor session = iterator.next();
            iterator.remove();
            if (session.isCreated && (listener = this.listener) != null) {
                listener.onSessionFinished(eventTime, session.sessionId, false);
            }
        }
    }

    private SessionDescriptor getOrAddSession(int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
        SessionDescriptor bestMatch = null;
        long bestMatchWindowSequenceNumber = Long.MAX_VALUE;
        for (SessionDescriptor sessionDescriptor : this.sessions.values()) {
            sessionDescriptor.maybeSetWindowSequenceNumber(windowIndex, mediaPeriodId);
            if (sessionDescriptor.belongsToSession(windowIndex, mediaPeriodId)) {
                long windowSequenceNumber = sessionDescriptor.windowSequenceNumber;
                if (windowSequenceNumber == -1 || windowSequenceNumber < bestMatchWindowSequenceNumber) {
                    bestMatch = sessionDescriptor;
                    bestMatchWindowSequenceNumber = windowSequenceNumber;
                } else if (windowSequenceNumber == bestMatchWindowSequenceNumber && ((SessionDescriptor) Util.castNonNull(bestMatch)).adMediaPeriodId != null && sessionDescriptor.adMediaPeriodId != null) {
                    bestMatch = sessionDescriptor;
                }
            }
        }
        if (bestMatch == null) {
            String sessionId = generateSessionId();
            SessionDescriptor bestMatch2 = new SessionDescriptor(sessionId, windowIndex, mediaPeriodId);
            this.sessions.put(sessionId, bestMatch2);
            return bestMatch2;
        }
        return bestMatch;
    }

    private static String generateSessionId() {
        byte[] randomBytes = new byte[12];
        RANDOM.nextBytes(randomBytes);
        return Base64.encodeToString(randomBytes, 10);
    }

    /* loaded from: classes3.dex */
    public final class SessionDescriptor {
        private MediaSource.MediaPeriodId adMediaPeriodId;
        private boolean isActive;
        private boolean isCreated;
        private final String sessionId;
        private int windowIndex;
        private long windowSequenceNumber;

        public SessionDescriptor(String sessionId, int windowIndex, MediaSource.MediaPeriodId mediaPeriodId) {
            DefaultPlaybackSessionManager.this = r3;
            this.sessionId = sessionId;
            this.windowIndex = windowIndex;
            this.windowSequenceNumber = mediaPeriodId == null ? -1L : mediaPeriodId.windowSequenceNumber;
            if (mediaPeriodId != null && mediaPeriodId.isAd()) {
                this.adMediaPeriodId = mediaPeriodId;
            }
        }

        public boolean tryResolvingToNewTimeline(Timeline oldTimeline, Timeline newTimeline) {
            int resolveWindowIndexToNewTimeline = resolveWindowIndexToNewTimeline(oldTimeline, newTimeline, this.windowIndex);
            this.windowIndex = resolveWindowIndexToNewTimeline;
            if (resolveWindowIndexToNewTimeline == -1) {
                return false;
            }
            MediaSource.MediaPeriodId mediaPeriodId = this.adMediaPeriodId;
            if (mediaPeriodId == null) {
                return true;
            }
            int newPeriodIndex = newTimeline.getIndexOfPeriod(mediaPeriodId.periodUid);
            return newPeriodIndex != -1;
        }

        public boolean belongsToSession(int eventWindowIndex, MediaSource.MediaPeriodId eventMediaPeriodId) {
            return eventMediaPeriodId == null ? eventWindowIndex == this.windowIndex : this.adMediaPeriodId == null ? !eventMediaPeriodId.isAd() && eventMediaPeriodId.windowSequenceNumber == this.windowSequenceNumber : eventMediaPeriodId.windowSequenceNumber == this.adMediaPeriodId.windowSequenceNumber && eventMediaPeriodId.adGroupIndex == this.adMediaPeriodId.adGroupIndex && eventMediaPeriodId.adIndexInAdGroup == this.adMediaPeriodId.adIndexInAdGroup;
        }

        public void maybeSetWindowSequenceNumber(int eventWindowIndex, MediaSource.MediaPeriodId eventMediaPeriodId) {
            if (this.windowSequenceNumber == -1 && eventWindowIndex == this.windowIndex && eventMediaPeriodId != null) {
                this.windowSequenceNumber = eventMediaPeriodId.windowSequenceNumber;
            }
        }

        public boolean isFinishedAtEventTime(AnalyticsListener.EventTime eventTime) {
            if (this.windowSequenceNumber == -1) {
                return false;
            }
            if (eventTime.mediaPeriodId == null) {
                return this.windowIndex != eventTime.windowIndex;
            } else if (eventTime.mediaPeriodId.windowSequenceNumber > this.windowSequenceNumber) {
                return true;
            } else {
                if (this.adMediaPeriodId == null) {
                    return false;
                }
                int eventPeriodIndex = eventTime.timeline.getIndexOfPeriod(eventTime.mediaPeriodId.periodUid);
                int adPeriodIndex = eventTime.timeline.getIndexOfPeriod(this.adMediaPeriodId.periodUid);
                if (eventTime.mediaPeriodId.windowSequenceNumber < this.adMediaPeriodId.windowSequenceNumber || eventPeriodIndex < adPeriodIndex) {
                    return false;
                }
                if (eventPeriodIndex > adPeriodIndex) {
                    return true;
                }
                if (!eventTime.mediaPeriodId.isAd()) {
                    return eventTime.mediaPeriodId.nextAdGroupIndex == -1 || eventTime.mediaPeriodId.nextAdGroupIndex > this.adMediaPeriodId.adGroupIndex;
                }
                int eventAdGroup = eventTime.mediaPeriodId.adGroupIndex;
                int eventAdIndex = eventTime.mediaPeriodId.adIndexInAdGroup;
                return eventAdGroup > this.adMediaPeriodId.adGroupIndex || (eventAdGroup == this.adMediaPeriodId.adGroupIndex && eventAdIndex > this.adMediaPeriodId.adIndexInAdGroup);
            }
        }

        private int resolveWindowIndexToNewTimeline(Timeline oldTimeline, Timeline newTimeline, int windowIndex) {
            if (windowIndex < oldTimeline.getWindowCount()) {
                oldTimeline.getWindow(windowIndex, DefaultPlaybackSessionManager.this.window);
                for (int periodIndex = DefaultPlaybackSessionManager.this.window.firstPeriodIndex; periodIndex <= DefaultPlaybackSessionManager.this.window.lastPeriodIndex; periodIndex++) {
                    Object periodUid = oldTimeline.getUidOfPeriod(periodIndex);
                    int newPeriodIndex = newTimeline.getIndexOfPeriod(periodUid);
                    if (newPeriodIndex != -1) {
                        return newTimeline.getPeriod(newPeriodIndex, DefaultPlaybackSessionManager.this.period).windowIndex;
                    }
                }
                return -1;
            } else if (windowIndex < newTimeline.getWindowCount()) {
                return windowIndex;
            } else {
                return -1;
            }
        }
    }
}
