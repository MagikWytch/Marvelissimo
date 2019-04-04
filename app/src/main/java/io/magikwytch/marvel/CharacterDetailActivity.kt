package io.magikwytch.marvel

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import com.squareup.picasso.Picasso
import io.magikwytch.marvel.network.MarvelApi
import io.magikwytch.marvel.network.dto.character.MarvelCharacter
import io.magikwytch.marvel.realm.dto.RealmCharacter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm


class CharacterDetailActivity : AppCompatActivity() {
    lateinit var realm: Realm
    lateinit var character: MarvelCharacter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)
        realm = Realm.getDefaultInstance()

        val favoriteButton: ToggleButton = this.findViewById(R.id.button_favorite)

        val characterId = intent.getIntExtra("characterId", -1)
        MarvelApi.getService().getCharacterById(characterId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { wrapper ->
                character = wrapper.data.results[0]

                val thumbnail: ImageView = findViewById(R.id.imageView_character_detail_thumbnail)
                val urlToCharacterThumbnail: String = character.thumbnail.path + "." + character.thumbnail.extension
                Picasso.get()
                    .load(urlToCharacterThumbnail)
                    .into(thumbnail)

                val title: TextView = findViewById(R.id.textView_character_detail_title)
                title.text = character.name

                val description: TextView = findViewById(R.id.textView_character_detail_description)
                description.text = character.description

                val characterComics: TextView = findViewById(R.id.textView_character_detail_comicList)
                var listOfComics = ""
                for (item in character.comics.items) {
                    listOfComics += item.name + "\n"
                    characterComics.text = listOfComics
                }

                var isMarkedAsFavorite: Boolean = checkIfFavorite(character)
                if (isMarkedAsFavorite) {
                    favoriteButton.isChecked = true
                }

            }

        favoriteButton.setOnClickListener {
            if (favoriteButton.isChecked) {
                realm.beginTransaction()
                val realmCharacter: RealmCharacter = realm.createObject(RealmCharacter::class.java, this.character.id)
                realmCharacter.name = this.character.name
                realmCharacter.thumbnail = this.character.thumbnail.path + "." + this.character.thumbnail.extension
                realm.commitTransaction()
            } else {
                realm.executeTransactionAsync {
                    it.where(RealmCharacter::class.java)
                        .equalTo("id", this.character.id)
                        .findFirst()
                        ?.deleteFromRealm()
                }
            }
        }
    }

    private fun checkIfFavorite(character: MarvelCharacter): Boolean {
        realm.where(RealmCharacter::class.java)
            .equalTo("id", this.character.id)
            .findFirst() ?: return false

        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}