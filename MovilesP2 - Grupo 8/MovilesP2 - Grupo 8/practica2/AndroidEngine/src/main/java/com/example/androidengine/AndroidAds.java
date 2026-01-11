package com.example.androidengine;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;


/**
 * AndroidAds implementa los anuncions de Android.
 */
public class AndroidAds {

    // Actividad principal.
    private Activity activity;

    // AdView.
    private AdView adView;

    // RewardedAd.
    private RewardedAd rewardedAd;

    /**
     * CONSTRUCTORA.
     * @param activity
     */
    public AndroidAds(Activity activity) {
        this.activity = activity;
        MobileAds.initialize(activity, initializationStatus -> {
        });
    }

    /**
     * Método que carga el anuncio de banner.
     * @param adViewId
     */
    public void loadBannerAd(int adViewId) {
        adView = activity.findViewById(adViewId);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    /**
     * Método que hace que el banner sea visible o no.
     * @param visible
     */
    public void setBannerVisible(boolean visible) {
        if (adView == null) return;

        activity.runOnUiThread(() -> {
            adView.setVisibility(visible ? View.VISIBLE : View.GONE);
        });
    }

    /**
     * Método que carga el rewarded ad.
     * @param adId
     */
    public void loadRewardedAd(String adId) {
        RewardedAd.load(activity, adId, new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(RewardedAd ad) {
                rewardedAd = ad;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                rewardedAd = null;
            }
        });
    }

    /**
     * Método que muestra el rewarded ad.
     * @param onReward
     */
    public void showRewardedAd(Runnable onReward) {
        if (rewardedAd != null) {
            activity.runOnUiThread(() -> rewardedAd.show(activity, rewardItem -> {
                if (onReward != null) {
                    onReward.run();
                }
                loadRewardedAd(rewardedAd.getAdUnitId());
            }));
        } else {
        }
    }

}
