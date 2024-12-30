package com.easyapps.zkplayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.easyapps.zkplayer.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import com.easyapps.zkplayer.utils.JsonUtils.int
import com.easyapps.zkplayer.utils.JsonUtils.jsonArray
import com.easyapps.zkplayer.utils.JsonUtils.jsonObject
import com.easyapps.zkplayer.utils.JsonUtils.string
import com.easyapps.zkplayer.utils.NetworkApi
import com.easyapps.zkplayer.utils.OtherFunction.goWithUrl
import com.easyapps.zkplayer.utils.PreferencesUtil.getFromPref
import com.easyapps.zkplayer.utils.PreferencesUtil.setFromPref
import com.easyapps.zkplayer.utils.TempDB
import com.easyapps.zkplayer.utils.ads.InterstitialAds
import com.easyapps.zkplayer.utils.dialog.CustomDialog
import com.easyapps.zkplayer.utils.dialog.LoadingDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit  var navController: NavController
    private val api = NetworkApi()
    private var adsTime:Long = 30000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navController.graph = navGraph
        setupWithNavController(binding.bottomNavigationView, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.contentFragment-> setState(visibility = false, isLight = true)
                R.id.moreFragment-> setState(visibility = true, isLight = true)
                else -> setState(visibility = true, isLight = false)
            }
        }

        loadingDialog = LoadingDialog(this)
        setConfig()
    }

    private fun  setState(visibility:Boolean,isLight: Boolean){
        binding.bottomNavigationView.isVisible = visibility
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLight
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightNavigationBars = isLight
    }


    private fun showAdsAfterMinute() = lifecycleScope.launch(Dispatchers.Main) {
        delay(adsTime)
        InterstitialAds(this@MainActivity, TempDB.adsInterstitial)
        adsTime *= 2
    }


    private fun setConfig() {
        val json = getFromPref(this, "globalJson", null)
        if (json.isNullOrEmpty()) {
            loadingDialog.show()
            setConfiguration()
        } else {
            TempDB.globalJsonObject = JSONObject(json)
            setAdsParams()
            setConfiguration()
        }
    }


    private fun setConfiguration() = lifecycleScope.launch {
        val results = api.startRequest()
        if (results.isNotEmpty()) {
            if (getGlobalJson() == null) {
                TempDB.globalJsonObject = JSONObject(results)
                setGlobalJson(results)
                setAdsParams()
            } else {
                setGlobalJson(results)
            }
        } else {
            if (getGlobalJson() == null) showError()
        }
    }

    private fun showError() = with(CustomDialog.Builder(this)) {
        loadingDialog.dismiss()
        setMessage(getString(R.string.noInternetConnection))
        setAnimation("error.json")
        setPositiveButton(getString(R.string.try_again)) { tryAgain() }
        show()
    }

    private fun tryAgain() {
        loadingDialog.show()
        setConfiguration()
    }

    private fun setAdsParams() {
        loadingDialog.dismiss()
        val jsonConfig = TempDB.globalJsonObject.jsonObject("config")
        TempDB.adsBanner = jsonConfig.jsonArray("adsBanner")
        TempDB.adsInterstitial = jsonConfig.jsonArray("adsInterstitial")
        TempDB.adsReward = jsonConfig.jsonArray("adsReward")
        val version = jsonConfig.int("version", BuildConfig.VERSION_CODE)
        val message = jsonConfig.string("message")
        val url = jsonConfig.string("urlMessage")
        if (BuildConfig.VERSION_CODE < version) checkVersion(message, url)
        else navController.currentDestination?.let {
            navController.popBackStack()
            navController.navigate(it.id)
            showAdsAfterMinute()
        }
    }



    private fun checkVersion(message: String, url: String) = CustomDialog.Builder(this).apply {
        setMessage(message)
        setAnimation("update.json")
        setPositiveButton(getString(R.string.go)) {
            goWithUrl(this@MainActivity, url)
            finish()
        }
        show()
    }


    private fun getGlobalJson() = getFromPref(this, "globalJson", null)
    private fun setGlobalJson(json:String) = setFromPref(this, "globalJson", json)
}