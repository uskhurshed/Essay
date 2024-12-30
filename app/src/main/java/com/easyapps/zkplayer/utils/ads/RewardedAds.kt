package com.easyapps.zkplayer.utils.ads

import android.app.Activity
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader
import org.json.JSONArray

class RewardedAds(private val activity: Activity, banners: JSONArray = JSONArray("[]")) {

    private var copy = JSONArray()

    private var rewardedAdYandex: com.yandex.mobile.ads.rewarded.RewardedAd? = null
    private var rewardedAdGoogle: com.google.android.gms.ads.rewarded.RewardedAd? = null

    var onSuccess: () -> Unit = {}
    var onShow: () -> Unit = {}

    init {
        copy = JSONArray(banners.toString())
    }

    fun waterFall(removeFirst: Boolean = false,onAdLoaded: () -> Unit = {}) {
        if (copy.length() <= 0) return
        if (removeFirst) copy.remove(0)

        val adConfig = copy.optJSONObject(0) ?: return
        when (adConfig.optString("service")) {
            "g" -> loadGoogle(onAdLoaded)
            "y" -> loadYandex(onAdLoaded)
        }

    }

    private fun loadYandex(onAdLoaded: () -> Unit = {}) {
        val id = copy.optJSONObject(0).optString("id")
        val adRequestConfiguration = AdRequestConfiguration.Builder(id).build()

        RewardedAdLoader(activity).apply {
            setAdLoadListener(object : RewardedAdLoadListener {
                override fun onAdLoaded(rewarded: com.yandex.mobile.ads.rewarded.RewardedAd) {
                    rewardedAdYandex = rewarded
                    onAdLoaded.invoke()
                    onShow = { yandexEvents() }
                }
                override fun onAdFailedToLoad(error: AdRequestError) = waterFall(true,onAdLoaded)
            })
            loadAd(adRequestConfiguration)
        }

    }

    private fun yandexEvents() = rewardedAdYandex?.apply {
        setAdEventListener(object : RewardedAdEventListener {
            override fun onAdShown() {}
            override fun onAdClicked() {}
            override fun onAdImpression(impressionData: ImpressionData?) {}
            override fun onAdDismissed() { rewardedAdYandex = null }

            override fun onAdFailedToShow(adError: com.yandex.mobile.ads.common.AdError) {
                Toast.makeText(activity, "Реклама не загрузилась!", Toast.LENGTH_SHORT).show()
                rewardedAdYandex = null
            }

            override fun onRewarded(reward: Reward) = onSuccess.invoke()
        })
        show(activity)
    }


    private fun loadGoogle(onAdLoaded: () -> Unit) {
        val id = copy.optJSONObject(0).optString("id")
        val adRequest = AdRequest.Builder().build()

        com.google.android.gms.ads.rewarded.RewardedAd.load(activity,id, adRequest, object : RewardedAdLoadCallback() {
            override fun onAdLoaded(ad: com.google.android.gms.ads.rewarded.RewardedAd) {
                rewardedAdGoogle = ad
                onAdLoaded.invoke()
                onShow = { googleEvents() }
            }

            override fun onAdFailedToLoad(adError: LoadAdError) = waterFall(true, onAdLoaded)

        })
    }


    fun googleEvents() = rewardedAdGoogle?.apply {
        fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {}
            override fun onAdShowedFullScreenContent() {}
            override fun onAdImpression() {}
            override fun onAdDismissedFullScreenContent() { rewardedAdGoogle = null }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                Toast.makeText(activity, "Реклама не загрузилась!", Toast.LENGTH_SHORT).show()
                rewardedAdGoogle = null
            }
        }
        show(activity) { onSuccess.invoke() }
    }


}
