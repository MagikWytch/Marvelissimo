package io.magikwytch.marvel.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.magikwytch.marvel.CharacterDetailActivity
import io.magikwytch.marvel.R
import io.magikwytch.marvel.network.dto.character.MarvelCharacter
import io.magikwytch.marvel.realm.dto.RealmCharacter
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.character_row.view.*

class CharacterAdapter : RecyclerView.Adapter<CharacterViewHolder>() {

    var characters: MutableList<MarvelCharacter> = mutableListOf()


    override fun getItemCount(): Int {
        return characters.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.character_row, parent, false)

        return CharacterViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position]
        holder.characterTitle.text = character.name

        val urlToCharacterThumbnail: String = character.thumbnail.path + "." + character.thumbnail.extension
        Picasso.get()
            .load(urlToCharacterThumbnail)
            .into(holder.characterThumbnail)

        // TODO Get favorite working in general list view
        /*var realm: Realm = Realm.getDefaultInstance()
        var favoriteList: RealmResults<RealmCharacter> = realm.where(RealmCharacter::class.java).findAll()
        favoriteList.forEach { realmCharacter ->
            if (realmCharacter.id == character.id) {
                holder.characterFavorite.visibility = View.VISIBLE
            } else {
                holder.characterFavorite.visibility = View.GONE
            }
        }*/

        holder.character = character
    }
}

class CharacterViewHolder(val view: View, var character: MarvelCharacter? = null) : RecyclerView.ViewHolder(view) {
    val characterTitle: TextView = view.textView_character_title
    val characterThumbnail: ImageView = view.imageView_character_thumbnail
    //val characterFavorite: ImageView = view.imageView_favorite_mark
    val context = view.context

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, CharacterDetailActivity::class.java)
            intent.putExtra("characterId", character?.id)
            view.context.startActivity(intent)
        }
    }
}