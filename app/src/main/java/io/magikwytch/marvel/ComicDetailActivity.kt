package io.magikwytch.marvel

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.magikwytch.marvel.network.MarvelApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ComicDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic)

        val comicId = intent.getIntExtra("comicId", -1)


        MarvelApi.getService().getComicById(comicId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { wrapper ->
                val comic = wrapper.data.results[0]

                val thumbnail: ImageView = findViewById(R.id.imageView_comic_detail_thumbnail)
                val urlToComicThumbnail: String = comic.thumbnail.path + "." + comic.thumbnail.extension
                Picasso.get()
                    .load(urlToComicThumbnail)
                    .into(thumbnail)

                val title: TextView = findViewById(R.id.textView_comic_detail_title)
                title.text = comic.title

                val description: TextView = findViewById(R.id.textView_comic_detail_description)
                description.text = comic.description

                val comicCharacters: TextView = findViewById(R.id.textView_comic_detail_characterList)
                var listOfCharacters = ""
                for (item in comic.characters.items) {
                    listOfCharacters += item.name + ", "
                    comicCharacters.text = listOfCharacters
                }
            }
    }
}