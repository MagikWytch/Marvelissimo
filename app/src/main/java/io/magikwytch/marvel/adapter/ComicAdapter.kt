package io.magikwytch.marvel.adapter

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import io.magikwytch.marvel.ComicDetailActivity
import io.magikwytch.marvel.R
import io.magikwytch.marvel.network.dto.comic.Comic
import kotlinx.android.synthetic.main.comic_row.view.*

class ComicAdapter : RecyclerView.Adapter<ComicViewHolder>() {

    var comics: MutableList<Comic> = mutableListOf()

    override fun getItemCount(): Int {
        return comics.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.comic_row, parent, false)
        return ComicViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: ComicViewHolder, position: Int) {
        val comic = comics[position]
        holder.comicTitle.text = comic.title

        val urlToComicThumbnail: String = comic.thumbnail.path + "." + comic.thumbnail.extension
        Picasso.get()
            .load(urlToComicThumbnail)
            .into(holder.comicThumbnail)

        holder.comic = comic
    }
}

class ComicViewHolder(val view: View, var comic: Comic? = null) : RecyclerView.ViewHolder(view) {
    val comicTitle: TextView = view.textView_comic_title
    val comicThumbnail: ImageView = view.imageView_comic_thumbnail
    val context = view.context

    init {
        view.setOnClickListener {
            val intent = Intent(view.context, ComicDetailActivity::class.java)
            intent.putExtra("comicId", comic?.id)
            view.context.startActivity(intent)
        }
    }

}