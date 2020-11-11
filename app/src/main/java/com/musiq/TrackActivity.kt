package com.musiq

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_artist.*
import kotlinx.android.synthetic.main.activity_track.*
import com.musiq.Constants as Constants


class TrackActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener {
    private var itemList = generateDummyList(500)
    private var adapter = ItemAdapter(itemList, this)
    private var mTrackList: ArrayList<Track> = ArrayList()
    private var mAlbumList: ArrayList<Album> = ArrayList()
    private var mArtistName: String = ""
    private var mAlbumName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        /**
        et_search_track.addTextChangedListener(object : TextWatcher {
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

        rv_track.adapter = adapter
        rv_track.layoutManager = LinearLayoutManager(this)
        rv_track.setHasFixedSize(true)
        mArtistName = intent.getStringExtra(Constants.ARTIST_NAME)
        mAlbumName = intent.getStringExtra(Constants.ALBUM_NAME)
        mTrackList = intent.getParcelableArrayListExtra(Constants.TRACK_LIST)
        mAlbumList = intent.getParcelableArrayListExtra(Constants.ALBUM_LIST)

        itemList = generateDummyList(mTrackList.size)
        adapter = ItemAdapter(itemList, this)

        var i = 0
        while (i < mTrackList!!.size) {
            itemList[i].text = mTrackList!![i].name
            i++
        }

        refreshData()
    }

    private fun refreshData() {
        rv_track.adapter = adapter
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
        val track = mTrackList[position].name
        val i = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=${mArtistName} ${track}"))
        startActivity(i)
        //openArtistActivity()
    }

    private fun openArtistActivity() {
        val intent = Intent(this, ArtistActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    /**
    private fun openAlbumActivity() {
        val intent = Intent(this, AlbumActivity::class.java)
        intent.putExtra(Constants.ARTIST_NAME, mArtistName)
        intent.putExtra(Constants.ALBUM_LIST, mAlbumList!!)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        finish()
    }

    override fun onBackPressed() {
        openAlbumActivity()
        finish()
    }
    **/
}