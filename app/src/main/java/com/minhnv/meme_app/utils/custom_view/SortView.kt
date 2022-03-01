package com.minhnv.meme_app.utils.custom_view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView.LABEL_VISIBILITY_LABELED
import com.minhnv.meme_app.R
import com.minhnv.meme_app.databinding.ItemSortBinding

typealias DidSelectedSortListCommunities = (String, String, String) -> Unit

class SortView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CardView(context, attrs) {

    var didSelectedSortListCommunities: DidSelectedSortListCommunities? = null
    private val linearLayout = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
    }

    private val sections = mutableListOf(
        SortItem(1, "User"),
        SortItem(2, "Top"),
        SortItem(3, "Hot")
    )
    private val sorts = mutableListOf(
        SortItem(1, "Rising"),
        SortItem(2, "Time"),
        SortItem(3, "Top"),
        SortItem(4, "Viral"),
    )

    private val days = mutableListOf(
        SortItem(1, "Day"),
        SortItem(2, "Week"),
        SortItem(3, "Month")
    )
    private val sortItemAdapter = SortItemAdapter().apply {
        set(sections)
    }

    private val bottomSort = BottomNavigationView(context).apply {
        menu.clear()
        inflateMenu(R.menu.menu_sort)
        labelVisibilityMode = LABEL_VISIBILITY_LABELED
        setOnItemSelectedListener {
            return@setOnItemSelectedListener when (it.itemId) {
                R.id.section -> {
                    sortItemAdapter.set(sections)
                    true
                }
                R.id.sort -> {
                    sortItemAdapter.set(sorts)
                    true
                }
                R.id.day -> {
                    sortItemAdapter.set(days)
                    true
                }
                R.id.search -> {
                    val section = menu.findItem(R.id.section).title.toString().lowercase()
                    val sort = menu.findItem(R.id.sort).title.toString().lowercase()
                    val window = menu.findItem(R.id.day).title.toString().lowercase()
                    didSelectedSortListCommunities?.invoke(
                        section, sort, window
                    )
                    true
                }
                else -> false
            }
        }
    }
    private val rycSort = RecyclerView(context).apply {
        val typedValue = TypedValue()
        val theme = context.theme
        theme.resolveAttribute(R.attr.colorError, typedValue, true)
        @ColorInt val color = typedValue.data
        setBackgroundColor(color)
        layoutManager = LinearLayoutManager(context)
        setHasFixedSize(true)
        adapter = sortItemAdapter
        addItemDecoration(DividerItemDecoration(context, LinearLayout.VERTICAL))
        sortItemAdapter.didItemSortSelected = {
            when {
                sections.contains(it) -> {
                    bottomSort.menu.findItem(R.id.section).title = it.name
                }
                sorts.contains(it) -> {
                    bottomSort.menu.findItem(R.id.sort).title = it.name
                }
                days.contains(it) -> {
                    bottomSort.menu.findItem(R.id.day).title = it.name
                }
            }
        }
    }

    init {
        radius = resources.getDimension(com.intuit.sdp.R.dimen._10sdp)
        addView(linearLayout)
        val bottomParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearLayout.addView(bottomSort, bottomParams)
        linearLayout.addView(rycSort, params)
    }
}

data class SortItem(
    val id: Int,
    val name: String
)

typealias DidItemSortSelected = (SortItem) -> Unit

class SortItemAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val dataSource: MutableList<SortItem> = mutableListOf()
    var didItemSortSelected: DidItemSortSelected? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = ItemSortBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SortItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SortItemViewHolder) {
            holder.bind(dataSource[position])
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    fun set(list: MutableList<SortItem>) {
        dataSource.clear()
        dataSource.addAll(list)
        notifyDataSetChanged()
    }

    inner class SortItemViewHolder(
        private val binding: ItemSortBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(sortItem: SortItem) {
            binding.tvSortName.text = sortItem.name
            binding.root.setOnClickListener {
                didItemSortSelected?.invoke(sortItem)
            }
        }
    }

}

