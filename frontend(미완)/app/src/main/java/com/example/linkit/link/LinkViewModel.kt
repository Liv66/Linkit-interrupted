package com.example.linkit.link


import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


import com.example.linkit.Repository
import com.example.linkit.database.Link
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.lang.Exception


class LinkViewModel(aa: Link, private val repository: Repository) :
    ViewModel() {

    private var _url = MutableLiveData<String>()
    private var _des = MutableLiveData<String>()
    private var _title = MutableLiveData<String>()
    private var _img = MutableLiveData<String>()

    val url: LiveData<String>
        get() = _url

    val des: LiveData<String>
        get() = _des

    val title: LiveData<String>
        get() = _title

    val img: LiveData<String>
        get() = _img

    var ab = mutableListOf<String?>()

    init {
        viewModelScope.launch {
            val job = viewModelScope.launch(Dispatchers.IO) {
                val h = urlCrawling(aa.url)
                Log.d("!@12", "$h")
                ab.add(h?.get("url"))
                ab.add(h?.get("title"))
                ab.add(h?.get("description"))
                ab.add(h?.get("image"))
            }
            job.join()
            dsa()
        }
    }

    fun dsa() {
        _url.value = ab[0]
        _title.value = ab[1]
        _des.value = ab[2]
        _img.value = ab[3]
        Log.d("!@12", "{${_url.value}")
    }

    fun updateLink(link: Link) {
        viewModelScope.launch {
            update(link)
        }
    }


    private suspend fun update(link: Link) {
        withContext(Dispatchers.IO) {
            repository.updateLink(link)
        }
    }

    fun urlCrawling(url: String?): HashMap<String, String>? {

        val map = HashMap<String, String>()

        try {
            val con: Connection = Jsoup.connect(url)
            val doc: Document = con.get()
            var orgTags: Elements = doc.select("meta[property^=og:]")
            if (orgTags.size != 0) {
                for (i in 0 until orgTags.size) {
                    val tag: Element = orgTags[i]
                    when (tag.attr("property")) {
                        "og:url" -> {
                            map["url"] = tag.attr("content")
                        }
                        "og:title" -> {
                            map["title"] = tag.attr("content");

                        }
                        "og:image" -> {
                            map["image"] = tag.attr("content");

                        }
                        "og:description" -> {
                            map["description"] = tag.attr("content");
                        }
                    }
                }
            } else {
                orgTags = doc.select("meta[name^=twitter:]")
                Log.d("!@12","$orgTags")
                for (i in 0 until orgTags.size) {
                    val tag: Element = orgTags[i]
                    when (tag.attr("name")) {
                        "twitter:title" -> {
                            map["title"] = tag.attr("content");

                        }
                        "twitter:image" -> {
                            map["image"] = tag.attr("content");

                        }
                        "twitter:description" -> {
                            map["description"] = tag.attr("content");
                        }
                    }
                }
            }
            return map
        } catch (e: Exception) {
            Log.d("!@12", "에러 : $e")
            return null
        }
    }
}

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context).load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade()).into(view)
    }
}
