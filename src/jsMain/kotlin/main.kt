import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.browser.window
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLTextAreaElement
import org.w3c.dom.get

fun main() {
    window.onload = {
        Toggler(
            wrapperDiv = getById("div-reveal-options"),
            button = getById("btn-toggle-options")
        )
        ThemeSwitcher(
            toggleButton = getById("btn-toggle-theme")
        )
        TocHandler(
            tocInputElement = getById("md") as HTMLTextAreaElement,
            tocOutputElement = getById("toc") as HTMLTextAreaElement,
            btnCopy = getById("btn-copy"),
            optionsDiv = getById("options"),
            btnGenerate = getById("btn-generate"),
            selectProfile = getById("select-profile"),
            btnStoreOptions = getById("btn-store-options"),
            btnResetOptions = getById("btn-reset-options")
        )
        Modal(
            modal = getById("about-modal"),
            showBtn = getById("btn-show-modal"),
            closeBtn = getById("btn-close-modal")
        )
    }
}

fun getById(id: String): HTMLElement =
    document.getElementById(id) as? HTMLElement ?: throw RuntimeException("No element with '$id' in doc")

fun HTMLElement.addOnClickListener(callback: () -> Unit) =
    this.addEventListener("click", { callback() })

class Toggler(val wrapperDiv: HTMLElement, button: HTMLElement) {
    private val cls = "hide"

    init {
        button.addEventListener("click", { toggle() })
    }

    private fun toggle() {
        with(wrapperDiv.classList) {
            if (contains(cls)) remove(cls) else add(cls)
        }
    }
}

class Modal(val modal: HTMLElement, showBtn: HTMLElement, closeBtn: HTMLElement) {
    init {
        showBtn.addOnClickListener { show() }
        closeBtn.addOnClickListener { hide() }
        window.addEventListener("click", {
            if (it.target == modal) hide()
        })
    }

    fun show() {
        modal.style.display = "block"
    }

    fun hide() {
        modal.style.display = "none"
    }
}


class ThemeSwitcher(val toggleButton: HTMLElement) {
    private val DARK_CLS = "dark"
    private val html: HTMLElement = document.getElementsByTagName("html")[0] as HTMLElement
    private var isDark: Boolean = localStorage.getItem(DARK_CLS) != null

    init {
        applyTheme()
        toggleButton.addOnClickListener { toggleTheme() }
    }

    fun toggleTheme() {
        isDark = !isDark
        applyTheme()
    }

    private fun applyTheme() {
        if (isDark) {
            console.log("applying dark theme")
            localStorage.setItem(DARK_CLS, "true")
            html.classList.add(DARK_CLS)
        } else {
            console.log("applying light theme")
            localStorage.removeItem(DARK_CLS)
            html.classList.remove(DARK_CLS)
        }
    }
}
