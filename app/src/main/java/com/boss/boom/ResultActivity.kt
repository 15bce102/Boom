package com.boss.boom


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_result.*


class ResultActivity : AppCompatActivity() {

    private var highScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val prefs =
            getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE)
        highScore = prefs.getInt("key", 0);

        val intent = intent;
        var noOfButtonClicked = intent.getStringExtra("no_of_button_clicked")
        var totalTime = intent.getStringExtra("total_time")
        editButton.text  = noOfButtonClicked
        timeButton.text = "$totalTime sec"

        if(Integer.parseInt(noOfButtonClicked)>highScore){
            finalResult.text = noOfButtonClicked
            val editor = prefs.edit()
            editor.putInt("key", Integer.parseInt(noOfButtonClicked))
            editor.commit()
        }
        else{
            finalResult.text = highScore.toString()
        }





        shareView.setOnClickListener{
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            val shareBody = "Can you beat my score of ${finalResult.text}? Wanna try it? Click here \n\n" +
                    "https://play.google.com/store/apps/details?id=com.boss.boom \n\n" +
                    "I guarantee you have played this game in childhood. "+
                    "Give it a try for sure"

            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "LADDU game")
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(sharingIntent, "Share via"))
        }

        playAgain.setOnClickListener{
            val i = Intent(this,Introduction::class.java)
            startActivity(i)
        }
        Quits.setOnClickListener{
            val svc = Intent(this, SoundHandler::class.java)
            stopService(svc)
            finishAffinity()
        }
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
}
