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
import android.widget.Toast

import io.magikwytch.marvel.R
import io.magikwytch.marvel.adapter.CharacterAdapter
import io.magikwytch.marvel.network.MarvelApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_character.view.*

class CharacterFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_character, container, false)
        linearLayoutManager = LinearLayoutManager(activity)
        view.recyclerView_character.layoutManager = linearLayoutManager
        val adapter = CharacterAdapter()
        view.recyclerView_character.adapter = adapter
        MarvelApi.getService().getAllCharacters(0)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { wrapper ->
                adapter.characters.addAll(wrapper.data.results)
                adapter.notifyDataSetChanged()
            }

        val dumbScroll = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    MarvelApi.getService().getAllCharacters(adapter.characters.size)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { wrapper ->
                            adapter.characters.addAll(wrapper.data.results)
                            adapter.notifyDataSetChanged()
                        }
                }
            }
        }

        view.recyclerView_character.addOnScrollListener(dumbScroll)

        view.button_character_search.setOnClickListener {
            if (!view.editText_character_name.text.isEmpty()) {

                view.recyclerView_character.removeOnScrollListener(dumbScroll)
                val nameStartsWith = view.editText_character_name.text.toString()

                MarvelApi.getService().getCharacterByNameStartsWith(nameStartsWith)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { wrapper ->
                        adapter.characters.clear()
                        adapter.characters.addAll(wrapper.data.results)
                        adapter.notifyDataSetChanged()
                    }
            } else {
                Toast.makeText(this.context, "Please enter a name", Toast.LENGTH_SHORT).show()
            }
        }

        view.button_character_clear.setOnClickListener {
            view.recyclerView_character.addOnScrollListener(dumbScroll)
            view.editText_character_name.text.clear()

            MarvelApi.getService().getAllCharacters(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { wrapper ->
                    adapter.characters.clear()
                    adapter.characters.addAll(wrapper.data.results)
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
            CharacterFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
