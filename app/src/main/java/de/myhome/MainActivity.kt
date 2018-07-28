package de.myhome

import android.app.Activity
import android.os.Bundle
import android.widget.Switch
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import java.io.File

class MainActivity : Activity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		MainView(filesDir).setContentView(this)
	}
}

class MainView(filesDir: File) : AnkoComponent<MainActivity> {
	private val fileName = "myHome.info"
	private val dir = filesDir

	override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
		verticalLayout {
			val props = load()
			val door = switch {
				text = context.getString(R.string.doorClosed)
				textSize = 20F
				isChecked = props["doorClosed"]!!
			}
			val window = switch {
				text = context.getString(R.string.windowClosed)
				textSize = 20F
				isChecked = props["windowClosed"]!!
			}

			door.onCheckedChange { _, _ ->
				save(door, window)
			}
			window.onCheckedChange { _, _ ->
				save(door, window)
			}
		}
	}

	private fun save(x: Switch, y: Switch) {
		File(dir, fileName).writeText("doorClosed: ${x.isChecked}" + "\n" + "windowClosed: ${y.isChecked}")
	}

	private fun load(): Map<String, Boolean> {
		val props = HashMap<String, Boolean>()
		val file = File(dir, fileName)

		if (!file.exists()) {
			props["doorClosed"] = false
			props["windowClosed"] = false
		} else
			file.readLines().forEach {
				val entry = it.split(":")
				props[entry[0].trim()] = entry[1].trim().toBoolean()
			}

		return props
	}
}