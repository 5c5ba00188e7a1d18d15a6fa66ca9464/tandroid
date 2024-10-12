package com.google.android.gms.internal.mlkit_language_id;

import org.telegram.messenger.NotificationCenter;

/* loaded from: classes.dex */
public enum zzaj implements zzet {
    zzd(0),
    zze(1),
    zzf(2),
    zzg(3),
    zzh(11),
    zzi(12),
    zzj(13),
    zzk(21),
    zzl(22),
    zzm(23),
    zzn(NotificationCenter.fileNewChunkAvailable),
    zzo(NotificationCenter.filePreparingFailed),
    zzp(NotificationCenter.dialogsUnreadCounterChanged),
    zzq(NotificationCenter.messagePlayingProgressDidChanged),
    zzr(NotificationCenter.recordStarted),
    zzs(NotificationCenter.recordStartError),
    zzt(NotificationCenter.recordStopped),
    zzu(NotificationCenter.recordPaused),
    zza(NotificationCenter.didStartedCall),
    zzb(NotificationCenter.groupCallUpdated),
    zzc(NotificationCenter.groupCallScreencastStateChanged),
    zzv(NotificationCenter.groupCallSpeakingUsersUpdated),
    zzw(NotificationCenter.appDidLogout),
    zzx(NotificationCenter.configLoaded),
    zzy(NotificationCenter.needDeleteDialog),
    zzz(NotificationCenter.newEmojiSuggestionsAvailable),
    zzaa(NotificationCenter.themeUploadedToServer),
    zzab(NotificationCenter.goingToPreviewTheme),
    zzac(NotificationCenter.locationPermissionGranted),
    zzad(NotificationCenter.locationPermissionDenied),
    zzae(NotificationCenter.reloadInterface),
    zzaf(NotificationCenter.suggestedLangpack),
    zzag(191),
    zzah(NotificationCenter.dialogPhotosUpdate),
    zzai(NotificationCenter.channelRecommendationsLoaded),
    zzaj(NotificationCenter.savedMessagesDialogsUpdate),
    zzak(311),
    zzal(312),
    zzam(313),
    zzan(314),
    zzao(315),
    zzap(321),
    zzaq(322),
    zzar(323),
    zzas(324),
    zzat(331),
    zzau(332),
    zzav(333),
    zzaw(334),
    zzax(341),
    zzay(342),
    zzaz(343),
    zzba(344),
    zzbb(351),
    zzbc(352),
    zzbd(353),
    zzbe(354),
    zzbf(31),
    zzbg(32),
    zzbh(33),
    zzbi(41),
    zzbj(42),
    zzbk(43),
    zzbl(51),
    zzbm(52),
    zzbn(53),
    zzbo(61),
    zzbp(62),
    zzbq(63),
    zzbr(71),
    zzbs(72),
    zzbt(73),
    zzbu(81),
    zzbv(82),
    zzbw(83),
    zzbx(91),
    zzby(92),
    zzbz(93),
    zzca(111),
    zzcb(112),
    zzcc(113),
    zzcd(121),
    zzce(122),
    zzcf(123),
    zzcg(NotificationCenter.httpFileDidLoad),
    zzch(NotificationCenter.httpFileDidFailedLoad),
    zzci(NotificationCenter.didUpdateConnectionState),
    zzcj(102),
    zzck(103),
    zzcl(104),
    zzcm(105),
    zzcn(NotificationCenter.giftsToUserSent),
    zzco(NotificationCenter.didStartedMultiGiftsSelector),
    zzcp(NotificationCenter.boostedChannelByUser),
    zzcq(NotificationCenter.boostByChannelCreated),
    zzcr(100),
    zzcs(101),
    zzct(NotificationCenter.newLocationAvailable),
    zzcu(NotificationCenter.liveLocationsCacheChanged),
    zzcv(NotificationCenter.webRtcMicAmplitudeEvent),
    zzcw(NotificationCenter.storyQualityUpdate),
    zzcx(NotificationCenter.openBoostForUsersDialog),
    zzcy(NotificationCenter.groupRestrictionsUnlockedByBoosts),
    zzcz(203),
    zzda(NotificationCenter.groupPackUpdated),
    zzdb(NotificationCenter.timezonesUpdated),
    zzdc(NotificationCenter.customStickerCreated),
    zzdd(NotificationCenter.premiumFloodWaitReceived),
    zzde(NotificationCenter.availableEffectsUpdate),
    zzdf(NotificationCenter.starOptionsLoaded),
    zzdg(NotificationCenter.starGiftOptionsLoaded),
    zzdh(NotificationCenter.onDatabaseOpened),
    zzdi(NotificationCenter.onDownloadingFilesChanged),
    zzdj(NotificationCenter.billingConfirmPurchaseError),
    zzdk(NotificationCenter.wallpaperSettedToUser),
    zzdl(NotificationCenter.storiesUpdated),
    zzdm(NotificationCenter.storiesListUpdated),
    zzdn(NotificationCenter.storiesReadUpdated),
    zzdo(NotificationCenter.nearEarEvent),
    zzdp(361),
    zzdq(371);

    private static final zzes zzdr = new zzes() { // from class: com.google.android.gms.internal.mlkit_language_id.zzam
    };
    private final int zzds;

    zzaj(int i) {
        this.zzds = i;
    }

    public static zzev zzb() {
        return zzal.zza;
    }

    @Override // java.lang.Enum
    public final String toString() {
        return "<" + zzaj.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzds + " name=" + name() + '>';
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzet
    public final int zza() {
        return this.zzds;
    }
}
