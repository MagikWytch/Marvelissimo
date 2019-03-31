package io.magikwytch.marvel.adapter

import android.content.Intent
import android.os.Bundle
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
import io.magikwytch.marvel.network.dto.comic.ComicList
import io.magikwytch.marvel.network.dto.comic.ComicSummary
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

        holder.character = character
    }
}

class CharacterViewHolder(val view: View, var character: MarvelCharacter? = null) : RecyclerView.ViewHolder(view) {
    val characterTitle: TextView = view.textView_character_title
    val characterThumbnail: ImageView = view.imageView_character_thumbnail
    val context = view.context

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, CharacterDetailActivity::class.java)
            intent.putExtra("characterId", character?.id)
            view.context.startActivity(intent)
        }
    }

}