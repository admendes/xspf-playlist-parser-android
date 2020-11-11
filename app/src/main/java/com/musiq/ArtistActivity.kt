package com.musiq

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_artist.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException


class ArtistActivity : AppCompatActivity(), ItemAdapter.OnItemClickListener {
    private var itemList = generateDummyList(500)
    private var adapter = ItemAdapter(itemList, this)
    //private var parsedList : ArrayList<Artist>? = ArrayList()
    private var parsedMap : LinkedHashMap<String, Artist>? = LinkedHashMap()
    //internal lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artist)

        et_search_artist.addTextChangedListener(object : TextWatcher {
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

        rv_artist.adapter = adapter
        rv_artist.layoutManager = LinearLayoutManager(this)
        rv_artist.setHasFixedSize(true)

        val pullParserFactory: XmlPullParserFactory
        try {
            pullParserFactory = XmlPullParserFactory.newInstance()
            val parser = pullParserFactory.newPullParser()
            val inputStream = applicationContext.assets.open("all.xml")
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)

            val result = parseXml(parser)
            parsedMap = result

            itemList = generateDummyList(result!!.size)
            adapter = ItemAdapter(itemList, this)

            var i = 0
            if (result != null) {
                for (entry in result){
                    itemList[i].text = entry.value.name
                    i++
                }
            }

        } catch (e: XmlPullParserException){
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        refreshData()
    }

    private fun refreshData() {
        rv_artist.adapter = adapter
    }

    @Throws (XmlPullParserException::class, IOException::class)
    fun parseXml (parser: XmlPullParser) : LinkedHashMap<String, Artist>? {
        var artists: LinkedHashMap<String, Artist>? = null
        var eventType = parser.eventType
        var artist: Artist? = null
        var album: Album? = null
        var track: Track? = null
        var currentArtist: String = ""
        var currentAlbum: String = ""
        var artistName: String = ""
        var albumName: String = ""
        var changedArtist: Boolean = true
        var changedAlbum: Boolean = true

        while (eventType != XmlPullParser.END_DOCUMENT) {
            val name: String
            when (eventType) {
                XmlPullParser.START_DOCUMENT -> artists = LinkedHashMap()
                XmlPullParser.START_TAG -> {
                    name = parser.name

                    if (name == "track") {
                        track = Track()
                    }
                    else if (track != null){
                        if (name == "location"){
                            track.location = parser.nextText()
                        } else if (name == "title"){
                            track.name = parser.nextText()
                        } else if (name == "creator"){
                            artistName = parser.nextText()
                            if (artistName != currentArtist) {
                                artist = Artist()
                                artist.name = artistName
                                currentArtist = artist.name
                                changedArtist = true
                            } else {
                                changedArtist = false
                            }
                        } else if (name == "album") {
                            albumName = parser.nextText()
                            if (albumName != currentAlbum) {
                                album = Album()
                                album.name = albumName
                                currentAlbum = album.name
                                changedAlbum = true
                            } else {
                                changedAlbum = false
                            }
                        } else if (name == "duration") {
                            track.duration = parser.nextText()
                        } else if (name == "trackNum") {
                            track.num = parser.nextText()
                        } else if (name == "image") {
                            parser.nextText()
                        }
                    }
                }
                XmlPullParser.END_TAG -> {
                    name = parser.name
                    if (name.equals("track", ignoreCase = true)) {
                        if (artist == null){
                            artist = Artist()
                            artist.name = "UNKNOWN ARTIST"
                            currentArtist = artist.name
                            changedArtist = true
                        }
                        if (changedArtist)
                            artists!![currentArtist] = artist
                        if (changedAlbum) {
                            if (album == null) {
                                album = Album()
                                album.name = "NO ALBUM"
                                changedAlbum = true
                            }
                            artist.albumList!!.add(album)
                        }
                        if (track == null){
                            track = Track()
                            track.name = "UNKNOWN TRACK"
                        }
                        album!!.trackList!!.add(track)
                    }
                }
            }
            eventType = parser.next()
        }
        return artists
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

    override fun onItemClick(position: Int, key: String) {
        //Toast.makeText(this, "Item ${parsedList!![position].name} clicked", Toast.LENGTH_SHORT).show()
        val clickedItem: Item = itemList[position]
        //clickedItem.text = "${parsedList!![position].name} adsda"
        adapter.notifyItemChanged(position)
        openAlbumActivity(key)
    }

    /**
    fun saveData() {
        val sharedPreferences =
            getSharedPreferences(Constants.SHARED_PREFS, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet(Constants.ALBUM_LIST, parsedList!![position].albumList)
        editor.apply()
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show()
    }
    **/

    private fun openAlbumActivity(key: String) {
        val intent = Intent(this, AlbumActivity::class.java)
        intent.putExtra(Constants.ARTIST_NAME, parsedMap!!.getValue(key).name)
        intent.putExtra(Constants.ALBUM_LIST, parsedMap!!.getValue(key).albumList!!)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        //finish()
    }
}