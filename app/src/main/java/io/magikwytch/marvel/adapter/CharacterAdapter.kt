package io.magikwytch.marvel.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.magikwytch.marvel.R
import io.magikwytch.marvel.network.dto.character.MarvelCharacter
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


    }
}

class CharacterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val characterTitle: TextView = view.textView_character_title
    val characterThumbnail: ImageView = view.imageView_character_thumbnail

}