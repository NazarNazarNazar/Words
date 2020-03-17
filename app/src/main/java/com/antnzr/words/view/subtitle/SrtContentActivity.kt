package com.antnzr.words.view.subtitle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.antnzr.words.R
import com.antnzr.words.utils.SUBTITLE_FILE_NAME

class SrtContentActivity : AppCompatActivity(),
    SrtContentFragment.OnSrtContentFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subtitle)

        val subtitleFileName = intent.getStringExtra(SUBTITLE_FILE_NAME)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, SrtContentFragment.newInstance(subtitleFileName))
        fragmentTransaction.commit()
    }

    override fun onFragmentInteraction() {}
}
