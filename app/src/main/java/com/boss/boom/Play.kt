package com.boss.boom

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_play.*
import java.util.*


class Play : AppCompatActivity() {

    private val TAG = "Play"



    private val mAppUnitId: String by lazy {

        "ca-app-pub-5681723961160711~7963339365"
    }

    private val mInterstitialAdUnitId: String by lazy {

        "ca-app-pub-5681723961160711/7771767677"
    }

    private lateinit var mInterstitialAd: InterstitialAd

    var value = 2
    var i = 0
    var buttonCount = 0
    private var initialTime = 0L
    private var finalTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)

        mInterstitialAd = InterstitialAd(this)

        initializeInterstitialAd(mAppUnitId)

        loadInterstitialAd(mInterstitialAdUnitId)

        val intent = intent;
        value = (intent.getStringExtra("finalValue")).toInt()


        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var checkValidityButton = false
        var check = false

        button.setOnClickListener {
            performCount()
            var random = Random()
            var dx = random.nextFloat() * (displayMetrics.widthPixels-(button.right-button.left))
            var dy = random.nextFloat() * (displayMetrics.heightPixels-(button.bottom-button.top))


            if(button.text == "START"){
                initialTime = System.currentTimeMillis()
            }

            if(!check)
                checkValidityButton = setTextInButton()
            else
                check = false


            if(checkValidityButton){
                Log.d(TAG, "Enter above loaded")
                if(mInterstitialAd.isLoaded){
                    Log.d(TAG, "Enter in loaded")
                    mInterstitialAd.show()
                }
               else{
                        sendToAnotherActivity()
                    }
            }
            else{
                i++
                button.text = i.toString()
                scoreValue.text = i.toString()
                it.animate().x(dx).y(dy).setDuration(0).start()
            }

        }


        layout.setOnClickListener{

            if(button.text!="START"){
                var str = setTextInButton()
                performCount()
                if(str){

                    check = true
                    checkValidityButton = false
                    button.performClick()

                }
                else
                {

                    if(mInterstitialAd.isLoaded){
                        mInterstitialAd.show()
                    }
                    else{
                            sendToAnotherActivity()
                        }

                    }
            }

        }

        runAdEvents()
    }

    private fun sendToAnotherActivity(){

            finalTime = (System.currentTimeMillis()-initialTime)/(1000)
            Log.d("time",finalTime.toString())
            val intent = Intent(this,ResultActivity::class.java)
            intent.putExtra("no_of_button_clicked",i.toString())
            intent.putExtra("total_time",finalTime.toString())
            startActivity(intent)


    }
    private fun setTextInButton(): Boolean {
        if(i==0)
            return false
        var check = i;
        if(i%value==0)
            return true
        while (check!=0){
            if(check%10==value)
                return true
            check /= 10
        }
        return false
    }

    private fun performCount()
    {
        buttonCount++
    }

    @Override
    override fun onBackPressed()
    {
        return
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    private fun initializeInterstitialAd(appUnitId: String) {

        MobileAds.initialize(this, appUnitId)

    }

    private fun loadInterstitialAd(interstitialAdUnitId: String) {

        mInterstitialAd.adUnitId = interstitialAdUnitId
        mInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    private fun runAdEvents() {

        mInterstitialAd.adListener = object : AdListener() {


            override fun onAdClicked() {
                super.onAdOpened()
                mInterstitialAd.adListener.onAdClosed()
            }


            override fun onAdClosed() {
                sendToAnotherActivity()
                finish()
            }
        }
    }

}






