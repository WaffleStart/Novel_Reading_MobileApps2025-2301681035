package com.example.novelrepo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import com.example.novelrepo.data.Book
import com.example.novelrepo.viewmodel.BookViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ReaderActivity : AppCompatActivity() {

    private val viewModel: BookViewModel by viewModels()
    private var currentBookId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reader)

        // Views
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val chapterTitle = findViewById<TextView>(R.id.chapterTitle)
        val readerText = findViewById<TextView>(R.id.readerText)
        val scrollReader = findViewById<NestedScrollView>(R.id.scrollReader)

        // Back button
        btnBack.setOnClickListener { onBackPressed() }

        // Get book id from intent (sent by BookDetailsActivity)
        currentBookId = intent?.getIntExtra("book_id", -1) ?: -1
        if (currentBookId == -1) currentBookId = 1

        // Observe the book and populate reader
        viewModel.loadBook(currentBookId).observe(this, Observer { book ->
            if (book == null) return@Observer
            chapterTitle.text = "${book.title}"
            readerText.text = longTextFor(book)
            // scroll to top when opening a new book
            scrollReader.post { scrollReader.scrollTo(0, 0) }
        })

        setupBottomNav()
    }

    private fun longTextFor(book: Book): String {
        return when (book.id) {
            1 -> buildHobbitText(book)
            2 -> buildLastLightText(book)
            3 -> buildSilentGroveText(book)
            4 -> buildWhispersText(book)
            else -> buildDefaultLongText(book)
        }
    }

    // Long sample content for each book. Replace with real chapters or load from assets/raw as needed.
    private fun buildHobbitText(book: Book): String {
        val intro = """
            Chapter One — An Unexpected Journey

            In a hole in the ground there lived a hobbit. Not a nasty, dirty, wet hole, filled with the ends of worms and an oozy smell,
            nor yet a dry, bare, sandy hole with nothing in it to sit down on or to eat: it was a hobbit-hole, and that means comfort.

            ${book.description}

            Bilbo Baggins had never expected to be swept into such a long and winding tale. He found himself walking through green fields
            and shadowed woods, meeting dwarves and strangers, and learning that courage is not the absence of fear but the choice to move
            forward despite it.
        """.trimIndent()

        val middle = """
            The company traveled on. They crossed rivers and climbed ridges. They sang songs of old and told stories by the fire. Each night
            the stars seemed to listen, and each morning the road revealed another mystery. They found ruins half-swallowed by ivy, and
            they found kindness in unexpected places.

            Days became weeks. The map that once seemed simple grew complex with notes and warnings. The world was larger than any hobbit
            had imagined, and Bilbo learned that small hands can hold great deeds.
        """.trimIndent()

        val closing = """
            At the edge of the wild they paused. The wind carried a scent of smoke and something older than memory. The path ahead was
            uncertain, but the company had become a kind of family, bound by shared purpose and the quiet knowledge that they would
            not turn back now.

            (Continue reading...)
        """.trimIndent()

        // Repeat to ensure long scrollable content
        return (intro + "\n\n" + middle + "\n\n" + closing + "\n\n").repeat(8)
    }

    private fun buildLastLightText(book: Book): String {
        val part1 = """
            Chapter Twelve — After the Storm

            The city lay quiet in the hours after the storm. Streets that had once thrummed with life now held only the echo of footsteps
            and the distant drip of water from broken eaves. She walked the avenues with a steady, careful pace, cataloguing the damage,
            the small mercies, the places where light still lingered.
        """.trimIndent()

        val part2 = """
            In the weeks that followed, neighbors became allies, and strangers became the hands that lifted what remained. The work was slow,
            the progress measured in small victories: a cleared doorway, a repaired window, a meal shared on a stoop. Each act stitched the
            city back together, not as it had been, but into something new.
        """.trimIndent()

        val part3 = """
            She discovered notes tucked beneath floorboards, names written on the backs of photographs, and a ledger of debts that told a
            different story than the one told in the papers. The truth she uncovered was not a single revelation but a series of quiet
            reckonings. People remembered who they were when the noise fell away.
        """.trimIndent()

        return (part1 + "\n\n" + book.description + "\n\n" + part2 + "\n\n" + part3 + "\n\n").repeat(7)
    }

    private fun buildSilentGroveText(book: Book): String {
        val p1 = """
            Chapter Five — The Grove Keeps Its Secrets

            The grove was older than the town, older than the roads that led to it. Trees arched like cathedral ribs, and the air inside
            smelled of moss and rain. She stepped beneath the canopy and felt the world narrow to the sound of her own breath.
        """.trimIndent()

        val p2 = """
            For hours she wandered among trunks and roots, listening for a voice that might tell her why the grove had been silent for so long.
            The silence was not empty; it was full of memory. Leaves told stories in the wind. Stones remembered footsteps. The grove kept
            its secrets close, but it did not refuse company.
        """.trimIndent()

        val p3 = """
            As dusk fell, the light changed, and with it the shape of things. Shadows lengthened and the path home seemed both nearer and farther.
            She learned that some answers arrive slowly, like sap rising in spring, and that patience is a kind of faith.
        """.trimIndent()

        return (p1 + "\n\n" + book.description + "\n\n" + p2 + "\n\n" + p3 + "\n\n").repeat(8)
    }

    private fun buildWhispersText(book: Book): String {
        val p1 = """
            Chapter Eight — Echoes in the Dark

            The whispers began as a rumor, a sound at the edge of sleep. People said they heard voices in the walls, a susurrus that promised
            knowledge and demanded a price. He listened and learned that not all whispers are lies; some are warnings, some are memories.
        """.trimIndent()

        val p2 = """
            He followed the trail of whispers into places he had avoided: abandoned halls, shuttered rooms, and the narrow alleys where the
            city kept its old debts. Each whisper revealed a fragment, and each fragment fit into a pattern that was both beautiful and terrible.
        """.trimIndent()

        val p3 = """
            The story that unfolded was not one of heroes and villains but of choices and consequences. The whispers asked him to remember,
            and remembering is a dangerous thing.
        """.trimIndent()

        return (p1 + "\n\n" + book.description + "\n\n" + p2 + "\n\n" + p3 + "\n\n").repeat(7)
    }

    private fun buildDefaultLongText(book: Book): String {
        val base = """
            Chapter — Untitled

            ${book.description}

            This is extended placeholder text to ensure the reader view requires scrolling. Replace with real chapters or load content
            from files or a remote source as your app grows. Repeat paragraphs, add sections, or stream content for very long works.
        """.trimIndent()
        return base.repeat(10)
    }

    private fun setupBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        // keep the bottom nav invisible selection if you used that pattern
        bottomNav.selectedItemId = R.id.invisible

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.nav_discover -> {
                    startActivity(Intent(this, DiscoverActivity::class.java))
                    true
                }
                R.id.nav_library -> {
                    startActivity(Intent(this, LibraryActivity::class.java))
                    true
                }
                R.id.nav_create -> {
                    startActivity(Intent(this, CreateBookActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
