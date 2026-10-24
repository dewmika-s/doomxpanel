package com.example.arenalab

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.*
import android.content.Context

class MainActivity : Activity() {
    private val bg = Color.rgb(9, 11, 18)
    private val panel = Color.rgb(19, 23, 35)
    private val panel2 = Color.rgb(25, 30, 45)
    private val white = Color.rgb(241, 244, 255)
    private val muted = Color.rgb(151, 161, 184)
    private val purple = Color.rgb(124, 92, 255)
    private val cyan = Color.rgb(54, 220, 226)
    private lateinit var root: LinearLayout
    private lateinit var logBox: LinearLayout
    private lateinit var latencyValue: TextView
    private lateinit var stabilityValue: TextView
    private var latency = 42
    private var running = false
    private val prefs by lazy { getSharedPreferences("arena_lab", Context.MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        buildUi()
        addLog("Session initialized", "SYSTEM")
    }

    private fun buildUi() {
        val scroll = ScrollView(this).apply { setBackgroundColor(bg); fillViewport = true }
        root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(18), dp(18), dp(18), dp(28)) }
        scroll.addView(root)
        setContentView(scroll)

        val brandRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val logo = TextView(this).apply {
            text = "A"; textSize = 22f; setTextColor(Color.WHITE); gravity = Gravity.CENTER
            background = rounded(purple, 16); typeface = Typeface.DEFAULT_BOLD
        }
        brandRow.addView(logo, LinearLayout.LayoutParams(dp(48), dp(48)))
        val brandText = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(12), 0, 0, 0) }
        brandText.addView(text("ARENA LAB", 18f, white, true))
        brandText.addView(text("GAME SYSTEMS • LEARNING SIMULATOR", 10f, muted, true))
        brandRow.addView(brandText, LinearLayout.LayoutParams(0, -2, 1f))
        val demoBadge = text("DEMO", 10f, cyan, true).apply { setPadding(dp(10), dp(7), dp(10), dp(7)); background = rounded(Color.rgb(18, 54, 61), 30) }
        brandRow.addView(demoBadge)
        root.addView(brandRow)
        space(22)

        val hero = card()
        hero.addView(text("TRAINING CONSOLE", 11f, cyan, true))
        spaceIn(hero, 7)
        hero.addView(text("Understand the system.\nNot the exploit.", 27f, white, true))
        spaceIn(hero, 10)
        hero.addView(text("An offline, interactive model for exploring game-assistance concepts, telemetry and configuration state. No connection to Free Fire or other games.", 13f, muted, false))
        spaceIn(hero, 15)
        val statusRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val dot = View(this).apply { background = rounded(cyan, 20) }
        statusRow.addView(dot, LinearLayout.LayoutParams(dp(8), dp(8)))
        val statusText = text("  SANDBOX READY  •  LOCAL ONLY", 10f, cyan, true)
        statusRow.addView(statusText)
        hero.addView(statusRow)
        root.addView(hero)
        space(14)

        val stats = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        stats.addView(statCard("SIM FPS", "120", "+/- model"), LinearLayout.LayoutParams(0, dp(100), 1f))
        stats.addView(View(this), LinearLayout.LayoutParams(dp(10), 1))
        stats.addView(statCard("LATENCY", "42 ms", "simulated"), LinearLayout.LayoutParams(0, dp(100), 1f))
        stats.addView(View(this), LinearLayout.LayoutParams(dp(10), 1))
        stats.addView(statCard("STATUS", "SAFE", "offline"), LinearLayout.LayoutParams(0, dp(100), 1f))
        root.addView(stats)
        space(22)

        sectionTitle("FEATURE MODELS", "Toggle concepts to see state and event logs")
        val features = card()
        featureSwitch(features, "Target assistance model", "Toy model only • no aiming or game input", "assist_model", false)
        separator(features)
        featureSwitch(features, "Awareness overlay model", "Displays a simulated overlay state", "overlay_model", true)
        separator(features)
        featureSwitch(features, "Performance profile", "Changes demo telemetry only", "performance_model", false)
        separator(features)
        featureSwitch(features, "Verbose event logging", "Record control changes in this session", "verbose_logs", true)
        root.addView(features)
        space(22)

        sectionTitle("NETWORK & TELEMETRY LAB", "Adjust simulated values — nothing is sent over a network")
        val telemetry = card()
        val latencyHeader = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        latencyHeader.addView(text("Simulated latency", 14f, white, true), LinearLayout.LayoutParams(0, -2, 1f))
        latencyValue = text("42 ms", 14f, cyan, true)
        latencyHeader.addView(latencyValue)
        telemetry.addView(latencyHeader)
        val seek = SeekBar(this).apply { max = 250; progress = latency; progressTintList = android.content.res.ColorStateList.valueOf(purple); thumbTintList = android.content.res.ColorStateList.valueOf(cyan) }
        telemetry.addView(seek)
        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, p: Int, user: Boolean) { latency = p; latencyValue.text = "$p ms"; stabilityValue.text = if (p < 70) "STABLE" else if (p < 150) "VARIABLE" else "HIGH DELAY" }
            override fun onStartTrackingTouch(s: SeekBar?) {}
            override fun onStopTrackingTouch(s: SeekBar?) { addLog("Latency model set to $latency ms", "TELEMETRY") }
        })
        spaceIn(telemetry, 12)
        val stabilityRow = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        stabilityRow.addView(text("Connection quality model", 13f, muted, false), LinearLayout.LayoutParams(0, -2, 1f))
        stabilityValue = text("STABLE", 11f, cyan, true)
        stabilityRow.addView(stabilityValue)
        telemetry.addView(stabilityRow)
        root.addView(telemetry)
        space(22)

        sectionTitle("SIMULATION CONTROL", "Start or reset a local demo session")
        val controls = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val start = button("▶  Start demo", purple)
        start.setOnClickListener {
            running = !running
            start.text = if (running) "Ⅱ  Pause demo" else "▶  Start demo"
            addLog(if (running) "Demo simulation started" else "Demo simulation paused", "SESSION")
            Toast.makeText(this, if (running) "Demo started — local simulation only" else "Demo paused", Toast.LENGTH_SHORT).show()
        }
        controls.addView(start, LinearLayout.LayoutParams(0, dp(50), 1f))
        controls.addView(View(this), LinearLayout.LayoutParams(dp(10), 1))
        val reset = button("Reset", panel2)
        reset.setOnClickListener {
            running = false; latency = 42; start.text = "▶  Start demo"; seek.progress = 42
            addLog("Demo state reset", "SESSION")
            Toast.makeText(this, "Simulation reset", Toast.LENGTH_SHORT).show()
        }
        controls.addView(reset, LinearLayout.LayoutParams(dp(100), dp(50)))
        root.addView(controls)
        space(22)

        sectionTitle("EVENT LOG", "Recent local actions")
        val logCard = card()
        logBox = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        logCard.addView(logBox)
        val clear = text("CLEAR LOG", 10f, cyan, true).apply { gravity = Gravity.END; setPadding(0, dp(14), 0, 0); setOnClickListener { logBox.removeAllViews(); addLog("Log cleared", "SYSTEM") } }
        logCard.addView(clear)
        root.addView(logCard)
        space(18)

        val disclaimer = card()
        disclaimer.addView(text("ACADEMIC SAFETY NOTE", 11f, cyan, true))
        spaceIn(disclaimer, 7)
        disclaimer.addView(text("This app is a standalone educational simulation. It does not inspect game memory, inject code, automate aiming, alter packets, bypass anti-cheat, or connect to Free Fire. The displayed telemetry is illustrative, not measured from a real game.", 12f, muted, false))
        root.addView(disclaimer)
        space(8)
        root.addView(text("ARENA LAB  /  UNIVERSITY PROJECT BUILD 1.0.0", 10f, muted, true).apply { gravity = Gravity.CENTER; setPadding(0, dp(12), 0, 0) })
    }

    private fun featureSwitch(parent: LinearLayout, title: String, subtitle: String, key: String, default: Boolean) {
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL; setPadding(0, dp(10), 0, dp(10)) }
        val labels = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        labels.addView(text(title, 14f, white, true))
        labels.addView(text(subtitle, 11f, muted, false).apply { setPadding(0, dp(4), 0, 0) })
        row.addView(labels, LinearLayout.LayoutParams(0, -2, 1f))
        val sw = Switch(this).apply {
            isChecked = prefs.getBoolean(key, default)
            showText = false
            thumbTintList = android.content.res.ColorStateList.valueOf(if (isChecked) cyan else muted)
            setOnCheckedChangeListener { _, checked ->
                prefs.edit().putBoolean(key, checked).apply()
                thumbTintList = android.content.res.ColorStateList.valueOf(if (checked) cyan else muted)
                if (prefs.getBoolean("verbose_logs", true) || key == "verbose_logs") addLog("${title}: ${if (checked) "ON" else "OFF"}", "FEATURE")
            }
        }
        row.addView(sw)
        parent.addView(row)
    }

    private fun statCard(label: String, value: String, note: String): LinearLayout {
        val box = card().apply { setPadding(dp(12), dp(14), dp(12), dp(12)) }
        box.addView(text(label, 9f, muted, true))
        spaceIn(box, 9)
        box.addView(text(value, 20f, white, true))
        box.addView(text(note, 9f, muted, false))
        return box
    }

    private fun sectionTitle(title: String, subtitle: String) {
        root.addView(text(title, 15f, white, true))
        root.addView(text(subtitle, 11f, muted, false).apply { setPadding(0, dp(4), 0, dp(10)) })
    }

    private fun card(): LinearLayout = LinearLayout(this).apply {
        orientation = LinearLayout.VERTICAL
        setPadding(dp(16), dp(16), dp(16), dp(16))
        background = rounded(panel, 18)
    }

    private fun text(value: String, size: Float, color: Int, bold: Boolean): TextView = TextView(this).apply {
        text = value; textSize = size; setTextColor(color); if (bold) typeface = Typeface.create("sans-serif", Typeface.BOLD)
    }

    private fun button(label: String, color: Int): TextView = TextView(this).apply {
        text = label; textSize = 13f; gravity = Gravity.CENTER; setTextColor(white); typeface = Typeface.DEFAULT_BOLD
        background = rounded(color, 14); isClickable = true; isFocusable = true
    }

    private fun rounded(color: Int, radius: Int): GradientDrawable = GradientDrawable().apply { setColor(color); cornerRadius = dp(radius).toFloat() }
    private fun separator(parent: LinearLayout) { parent.addView(View(this).apply { setBackgroundColor(Color.rgb(42, 48, 66)) }, LinearLayout.LayoutParams(-1, dp(1))) }
    private fun space(height: Int) { root.addView(View(this), LinearLayout.LayoutParams(1, dp(height))) }
    private fun spaceIn(parent: LinearLayout, height: Int) { parent.addView(View(this), LinearLayout.LayoutParams(1, dp(height))) }
    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    private fun addLog(message: String, tag: String) {
        if (!::logBox.isInitialized) return
        val row = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(0, dp(7), 0, dp(7)) }
        val heading = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        heading.addView(text("[$tag]", 10f, cyan, true))
        heading.addView(text("  ${java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())}", 10f, muted, false))
        row.addView(heading)
        row.addView(text(message, 12f, white, false).apply { setPadding(0, dp(3), 0, 0) })
        logBox.addView(row, 0)
        while (logBox.childCount > 8) logBox.removeViewAt(logBox.childCount - 1)
    }
}
