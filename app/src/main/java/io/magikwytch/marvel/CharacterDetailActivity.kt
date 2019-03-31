package io.magikwytch.marvel

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.magikwytch.marvel.adapter.CharacterViewHolder
import io.magikwytch.marvel.network.MarvelApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.util.BackpressureHelper.add
import io.reactivex.schedulers.Schedulers

class CharacterDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        val characterId = intent.getIntExtra("characterId", -1)


        MarvelApi.getService().getCharacterById(characterId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { wrapper ->
                val character = wrapper.data.results[0]

                val thumbnail: ImageView = findViewById(R.id.imageView_character_detail_thumbnail)
                val urlToCharacterThumbnail: String = character.thumbnail.path + "." + character.thumbnail.extension
                Picasso.get()
                    .load(urlToCharacterThumbnail)
                    .into(thumbnail)

                val title: TextView = findViewById(R.id.textView_character_detail_name)
                title.text = character.name

                val description: TextView = findViewById(R.id.textView_character_detail_description)
                description.text = character.description

                val characterComics: TextView = findViewById(R.id.textView_character_detail_comicList)
                var listOfComics = ""
                for (item in character.comics.items) {
                    listOfComics += item.name + ", "
                    characterComics.text = listOfComics
                }

            }


    }


}