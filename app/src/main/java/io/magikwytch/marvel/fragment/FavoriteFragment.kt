package io.magikwytch.marvel.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import io.magikwytch.marvel.R
import io.magikwytch.marvel.adapter.CharacterAdapter
import io.magikwytch.marvel.network.MarvelApi
import io.magikwytch.marvel.realm.dto.RealmCharacter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_favorite.view.*

class FavoriteFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        linearLayoutManager = LinearLayoutManager(activity)
        view.recyclerView_favorite.layoutManager = linearLayoutManager
        val adapter = CharacterAdapter()
        view.recyclerView_favorite.adapter = adapter

        var realm: Realm = Realm.getDefaultInstance()
        var favoriteList: RealmResults<RealmCharacter> = realm.where(RealmCharacter::class.java).findAll()

        favoriteList.forEach { realmCharacter ->
            MarvelApi.getService().getCharacterById(realmCharacter.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { wrapper ->
                    adapter.characters.addAll(wrapper.data.results)
                    adapter.notifyDataSetChanged()
                }
        }
        realm.close()

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
            FavoriteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
