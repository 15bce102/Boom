package com.boss.boom

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_introduction.*



class Introduction : AppCompatActivity() {

    var passValue = "2"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)

        val svc = Intent(this, SoundHandler::class.java)
        startService(svc)

        val prefs =
            getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE)
        var check = prefs.getBoolean("myPrefKey",true)

        if(check){
            showBasicDialog()
            val editor = prefs.edit()
            editor.putBoolean("myPrefKey", false)
            editor.commit()
        }



        val value = resources.getStringArray(R.array.value)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, value
            )
            spinner.adapter = adapter


            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {

                    passValue = value[position]

                }


                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        play.setOnClickListener {

            val i = Intent(this,Play::class.java)
            i.putExtra("finalValue",passValue)
            startActivity(i)
        }

        rules.setOnClickListener{
            val i = Intent(this,Rules::class.java)
            startActivity(i)
        }

        quit.setOnClickListener{

            stopService(svc)
            finishAffinity()
        }
    }

    private fun showBasicDialog(){
        val builder = AlertDialog.Builder(this,R.style.AlertDialogCustom)
        builder.setTitle("Laddu game has some unique set of rules. Please go through with them first")
        builder.setMessage("laddu numbers are the number which are multiple of the choosen value or the number which consist the choosen value is also termed as laddu number.\n" +
                "            \n Example - Suppose you choose 3 as your value then\n" +
                "laddu numbers are - 3,6,9,12,13,15,etc,\n\nTap on button if you think number is NOT laddu number and tap oter than button if you think number is laddu number\n\n" +
                "Enjoy Playing")
        builder.setPositiveButton("OK") { dialog, id ->  dialog.dismiss()
        }
        builder.show()
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
