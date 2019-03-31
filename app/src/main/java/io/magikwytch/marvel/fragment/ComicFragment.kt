package io.magikwytch.marvel.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.magikwytch.marvel.R
import io.magikwytch.marvel.adapter.ComicAdapter
import io.magikwytch.marvel.network.MarvelApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_comic.view.*

class ComicFragment : Fragment() {
    private lateinit var linearLayoutManager: LinearLayoutManager

    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comic, container, false)
        linearLayoutManager = LinearLayoutManager(activity)
        view.recyclerView_comic.layoutManager = linearLayoutManager
        val adapter = ComicAdapter()
        view.recyclerView_comic.adapter = adapter
        MarvelApi.getService().getAllComics(0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { wrapper ->
                adapter.comics.addAll(wrapper.data.results)
                adapter.notifyDataSetChanged()
            }

        val dumbScroll = object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    MarvelApi.getService().getAllComics(adapter.comics.size)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { wrapper ->
                            adapter.comics.addAll(wrapper.data.results)
                            adapter.notifyDataSetChanged()
                        }
                }
            }
        }

        view.recyclerView_comic.addOnScrollListener(dumbScroll)

        view.button_comic_search.setOnClickListener {
            view.recyclerView_comic.removeOnScrollListener(dumbScroll)
            val titleStartsWith = view.editText_comic_name.text.toString()

            MarvelApi.getService().getComicByTitleStartsWith(titleStartsWith)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { wrapper ->
                    adapter.comics.clear()
                    adapter.comics.addAll(wrapper.data.results)
                    adapter.notifyDataSetChanged()
                }
        }

        view.button_comic_clear.setOnClickListener {
            view.recyclerView_comic.addOnScrollListener(dumbScroll)
            view.editText_comic_name.text.clear()

            MarvelApi.getService().getAllComics(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { wrapper ->
                    adapter.comics.clear()
                    adapter.comics.addAll(wrapper.data.results)
                    adapter.notifyDataSetChanged()
                }

        }
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ComicFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}