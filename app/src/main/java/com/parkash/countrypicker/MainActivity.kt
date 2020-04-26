package com.parkash.countrypicker

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.parkash.countrypicker.adapter.DataAdapter
import com.parkash.countrypicker.adapter.IndexAdapter
import com.parkash.countrypicker.databinding.ActivityMainBinding
import com.parkash.countrypicker.model.DataClass
import com.parkash.countrypicker.utils.HeaderItemDecoration


class MainActivity : AppCompatActivity(), IndexAdapter.CallBack, View.OnClickListener {

    private var exit: Boolean = false
    private var latter: Char = 'A'
    private var binding: ActivityMainBinding? = null
    private var indexAdapter: IndexAdapter? = null
    private var dataAdapter: DataAdapter? = null
    private var indexArrayList = ArrayList<String>()
    private var dataArrayList = ArrayList<String>()
    private var sortedAppsList = ArrayList<DataClass>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding!!.closeIV.setOnClickListener(this)
        indexArrayList.addAll(resources.getStringArray(R.array.alphabet))
        dataArrayList.addAll(resources.getStringArray(R.array.countryName))
        binding!!.indexRV.layoutManager = LinearLayoutManager(this)
        binding!!.dataRV.layoutManager = LinearLayoutManager(this)

        setIndexAdapter()
        setDataAdapter()

        binding!!.searchET.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding!!.searchET.text.toString().isNotEmpty()) {
                    binding!!.indexRV.visibility = View.GONE
                    searchQuery(binding!!.searchET.text.toString())
                } else {
                    showOriginalView()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        binding!!.dataRV.addItemDecoration(
            HeaderItemDecoration(
                binding!!.dataRV
            ) { itemPosition ->
                if (itemPosition >= 0 && itemPosition < dataAdapter!!.itemCount) {
                    return@HeaderItemDecoration sortedAppsList[itemPosition].isHeader
                } else false
            })

        binding!!.searchET.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard()
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun searchQuery(data: String) {
        sortedAppsList.clear()
        dataArrayList.forEach { list ->

            if (list.startsWith(data, true)) {
                sortedAppsList.add(
                    DataClass(
                        list,
                        false
                    )
                )
            }
        }

        dataAdapterUpdate()
        binding!!.errorTV.visibility = if (sortedAppsList.size == 0) View.VISIBLE else View.GONE
    }

    private fun setDataAdapter() {
        binding!!.indexRV.visibility = View.VISIBLE
        sortedAppsList.clear()
        val dataArrayList = dataArrayList.sortedBy { it }
        dataArrayList.forEachIndexed { position, list ->
            if (position == 0) {
                latter = list[0]
                sortedAppsList.add(
                    DataClass(
                        latter.toString(),
                        true
                    )
                )
                sortedAppsList.add(
                    DataClass(
                        list,
                        false
                    )
                )

            } else {
                if (latter != list[0]) {
                    latter = list[0]
                    sortedAppsList.add(
                        DataClass(
                            latter.toString(),
                            true
                        )
                    )
                    sortedAppsList.add(
                        DataClass(
                            list,
                            false
                        )
                    )
                } else {
                    sortedAppsList.add(
                        DataClass(
                            list,
                            false
                        )
                    )
                }
            }
        }
        dataAdapterUpdate()
    }

    private fun dataAdapterUpdate() {
        if (dataAdapter == null) {
            dataAdapter =
                DataAdapter(sortedAppsList)
            binding!!.dataRV.adapter = dataAdapter
        } else {
            dataAdapter!!.notifyDataSetChanged()
        }
    }

    private fun setIndexAdapter() {
        if (indexAdapter == null) {
            indexAdapter = IndexAdapter(
                indexArrayList,
                this
            )
            binding!!.indexRV.adapter = indexAdapter
        } else {
            indexAdapter!!.notifyDataSetChanged()
        }
    }

    override fun indexSelected(data: String) {
        var got = false
        sortedAppsList.forEachIndexed { index, data1 ->
            if (data1.name[0] == data[0]) {
                got = true
                binding!!.dataRV.smoothSnapToPosition(index)
                return
            } else {
                got = false
            }

        }
        if (!got) {
            Toast.makeText(this, getString(R.string.no_data_found), Toast.LENGTH_LONG).show()
        }
    }

    private fun RecyclerView.smoothSnapToPosition(
        position: Int,
        snapMode: Int = LinearSmoothScroller.SNAP_TO_START
    ) {
        val smoothScroller = object : LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int = snapMode
            override fun getHorizontalSnapPreference(): Int = snapMode
        }
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

    fun hideKeyboard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding!!.searchET.windowToken, 0)
    }

    override fun onBackPressed() {
        backAction()
    }

    private fun backAction() {
        if (exit) {
            finishAffinity()
        } else {
            Toast.makeText(this, getString(R.string.press_one_more_time), Toast.LENGTH_LONG).show()
            exit = true
            Handler().postDelayed({ exit = false }, (2 * 1000).toLong())
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.closeIV -> {
                binding!!.searchET.text!!.clear()
            }
        }
    }

    private fun showOriginalView() {
        binding!!.errorTV.visibility = View.GONE
        hideKeyboard()
        binding!!.indexRV.visibility = View.VISIBLE
        setDataAdapter()
    }
}
