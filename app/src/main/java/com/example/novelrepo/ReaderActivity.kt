package com.example.novelrepo
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.card.MaterialCardView


class ReaderActivity : AppCompatActivity() {

    private lateinit var topBar: MaterialCardView
    private lateinit var settingsPanel: MaterialCardView
    private lateinit var scrollReader: NestedScrollView
    private lateinit var readerBackground: FrameLayout
    private lateinit var readerText: TextView
    private lateinit var chapterTitle: TextView
    private lateinit var btnBack: ImageButton
    private lateinit var btnChapterList: ImageButton
    private lateinit var btnIncreaseText: Button
    private lateinit var btnDecreaseText: Button
    private lateinit var spinnerFont: Spinner
    private lateinit var themeLight: RadioButton
    private lateinit var themeBrown: RadioButton
    private lateinit var themeDark: RadioButton

    private var controlsVisible = false
    private var currentTextSize = 20f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        initViews()
        initAnimations()
        initListeners()
        setupFontSpinner()
    }

    private fun initViews() {
        topBar = findViewById(R.id.topBar)
        settingsPanel = findViewById(R.id.settingsPanel)
        scrollReader = findViewById(R.id.scrollReader)
        readerBackground = findViewById(R.id.readerBackground)
        readerText = findViewById(R.id.readerText)
        chapterTitle = findViewById(R.id.chapterTitle)
        btnBack = findViewById(R.id.btnBack)
        btnChapterList = findViewById(R.id.btnChapterList)
        btnIncreaseText = findViewById(R.id.btnIncreaseText)
        btnDecreaseText = findViewById(R.id.btnDecreaseText)
        spinnerFont = findViewById(R.id.spinnerFont)
        themeLight = findViewById(R.id.themeLight)
        themeBrown = findViewById(R.id.themeBrown)
        themeDark = findViewById(R.id.themeDark)
    }

    private fun initAnimations() {
        topBar.post { topBar.translationY = -topBar.height.toFloat() }
        settingsPanel.post { settingsPanel.translationX = settingsPanel.width.toFloat() }
    }

    private fun initListeners() {

        findViewById<ConstraintLayout>(R.id.rootReader).setOnClickListener {
            toggleControls()
        }

        btnBack.setOnClickListener { finish() }

        btnChapterList.setOnClickListener { showChapterDialog() }

        btnIncreaseText.setOnClickListener {
            currentTextSize += 2f
            readerText.textSize = currentTextSize
        }

        btnDecreaseText.setOnClickListener {
            if (currentTextSize > 12f) {
                currentTextSize -= 2f
                readerText.textSize = currentTextSize
            }
        }

        themeLight.setOnClickListener { applyTheme("#FFFFFF", "#000000") }
        themeBrown.setOnClickListener { applyTheme("#F7F2E8", "#222222") }
        themeDark.setOnClickListener { applyTheme("#1E1E1E", "#FFFFFF") }
    }

    private fun toggleControls() {
        if (controlsVisible) {
            hideControls()
        } else {
            showControls()
        }
        controlsVisible = !controlsVisible
    }

    private fun showControls() {
        topBar.animate().translationY(0f).setDuration(250).start()
        settingsPanel.animate().translationX(0f).setDuration(250).start()
    }

    private fun hideControls() {
        topBar.animate().translationY(-topBar.height.toFloat()).setDuration(250).start()
        settingsPanel.animate().translationX(settingsPanel.width.toFloat()).setDuration(250).start()
    }
    // TODO Chapter handling needs to be done with actual data fetching, which right now I dont have.
    private fun showChapterDialog() {
        val chapters = arrayOf("Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4")

        AlertDialog.Builder(this)
            .setTitle("Chapters")
            .setItems(chapters) { _, index -> openChapter(index + 1) }
            .show()
    }

    private fun setupFontSpinner() {

        val fontMap = mapOf(
            0 to R.font.lora,
            1 to R.font.merriweather,
            2 to R.font.opensans
        )

        spinnerFont.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            fontMap.keys.map { listOf("Lora", "Merriweather", "Open Sans")[it] }
        )

        spinnerFont.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                readerText.typeface = ResourcesCompat.getFont(this@ReaderActivity, fontMap[pos]!!)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    private fun applyTheme(bg: String, text: String) {
        readerBackground.setBackgroundColor(Color.parseColor(bg))
        readerText.setTextColor(Color.parseColor(text))
    }
        //TODO: Chapter handling needs to be done with actual data fetching, which right now I dont have.
    private fun openChapter(number: Int) {
        Toast.makeText(this, "Open Chapter $number", Toast.LENGTH_SHORT).show()
    }
}


