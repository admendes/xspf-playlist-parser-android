package com.musiq

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.activity_artist.*
import com.musiq.Constants as Constants


class AlbumActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener {
    private var itemList = generateDummyList(500)
    private var adapter = ItemAdapter(itemList, this)
    private var mAlbumList: ArrayList<Album> = ArrayList()
    private var mArtistName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        /**
        et_search_album.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                filter(s.toString())
            }
        })
        **/

        rv_album.adapter = adapter
        rv_album.layoutManager = LinearLayoutManager(this)
        rv_album.setHasFixedSize(true)
        mArtistName = intent.getStringExtra(Constants.ARTIST_NAME)
        mAlbumList = intent.getParcelableArrayListExtra(Constants.ALBUM_LIST)

        itemList = generateDummyList(mAlbumList.size)
        adapter = ItemAdapter(itemList, this)

        var i = 0
        while (i < mAlbumList!!.size) {
            itemList[i].text = mAlbumList!![i].name
            i++
        }

        refreshData()
    }

    private fun refreshData() {
        rv_album.adapter = adapter
    }

    private fun filter(text: String) {
        val filteredList: ArrayList<Item> = ArrayList()
        for (item in itemList) {
            if (item.getText1().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }
        adapter.filterList(filteredList)
    }

    private fun generateDummyList(size: Int): List<Item> {
        val list = ArrayList<Item>()

        for (i in 0 until size) {
            val item = Item("item ${i}")
            list += item
        }
        return list
    }

    override fun onItemClick(position: Int, pos2: String) {
        //Toast.makeText(this, "Item $position clicked", Toast.LENGTH_SHORT).show()
        val clickedItem: Item = itemList[position]
        clickedItem.text = "$pos2"
        adapter.notifyItemChanged(position)
        openTrackActivity(position)
    }

    private fun openTrackActivity(position: Int) {
        val intent = Intent(this, TrackActivity::class.java)
        intent.putExtra(Constants.ARTIST_NAME, mArtistName)
        intent.putExtra(Constants.ALBUM_NAME, mAlbumList[position].name)
        intent.putExtra(Constants.TRACK_LIST, mAlbumList[position].trackList!!)
        intent.putExtra(Constants.ALBUM_LIST, mAlbumList!!)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    /**
    private fun openArtistActivity() {
        val intent = Intent(this, ArtistActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    override fun onBackPressed() {
        openArtistActivity()
        finish()
    }
    **/
}