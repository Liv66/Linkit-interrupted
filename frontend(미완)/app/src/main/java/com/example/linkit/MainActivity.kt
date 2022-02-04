package com.example.linkit

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.*
import android.view.View.INVISIBLE
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.example.linkit.database.Folder
import com.example.linkit.database.FolderAndLinkDao
import com.example.linkit.database.FolderDatabase
import com.example.linkit.database.Link
import com.example.linkit.databinding.ActivityMainBinding
import com.example.linkit.homefragment.HomeFragmentDirections
import com.example.linkit.linklistfrgament.LinkListFragmentDirections
import com.example.linkit.network.Email
import com.example.linkit.network.ResGetData
import com.example.linkit.objects.Account
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var mySearchView: SearchView
    private lateinit var mySearchViewEditText: EditText
    private lateinit var navController: NavController
    private lateinit var inflater: MenuInflater
    private lateinit var mainMenu: Menu
    private lateinit var searchMenu: MenuItem
    private lateinit var navGraph: NavGraph
    private lateinit var repository: Repository
    private lateinit var databaseDao: FolderAndLinkDao
    var aa: Link? = Link()
    var qq: Folder? = Folder()

    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.SplashTheme)

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val host: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.my_nav_host_fragment) as NavHostFragment?
                ?: return

        navController = host.navController
        navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        appBarConfiguration = AppBarConfiguration(navController.graph)

        setupBottomNavMenu(navController)

        databaseDao = FolderDatabase.getInstance(application).folderDao()
        repository = Repository(databaseDao)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.accountFragment -> hide()
                R.id.linkFragment -> hide()
                R.id.loginFragment -> hide()
                else -> show()
            }
        }

        // 계정정보 싱글톤 초기화
        Account.init(this)

        // 외부에서 공유하기 기능 받아들이기
        directSharesheetIntent()

        if (Account.isGuest) {
            navGraph.startDestination = R.id.homeFragment
            navController.graph = navGraph
        }

//        if (Account.email != null) {
//            runBlocking {
//                get(Account.email.toString(), repository)
//            }
//        }
//      자동로그인

        runBlocking {
            get("rlawjdgjs000", repository)
        }

        actionBar?.setTitle("")
        actionBar?.setHomeButtonEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(false)


        setAlarm()
    }

    fun setAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)  // 1
        val pendingIntent = PendingIntent.getBroadcast(     // 2
            this, AlarmReceiver.NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 14)
        }
        if (Account.toggle) {
            alarmManager.setInexactRepeating(   // 5
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
            Log.d("@@@@@", "알람킴")
        } else {
            alarmManager.cancel(pendingIntent)
            Log.d("@@@@@", "알람끔")
        }
    }

    private fun hide() {
        val bottomNav = binding.bottomNavigationView
        val toolbar = binding.toolbar

        bottomNav.visibility = INVISIBLE
        toolbar.visibility = INVISIBLE
    }

    private fun show() {
        val bottomNav = binding.bottomNavigationView
        val toolbar = binding.toolbar

        bottomNav.visibility = View.VISIBLE
        toolbar.visibility = View.VISIBLE
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = binding.bottomNavigationView
        bottomNav.setupWithNavController(navController)
        bottomNav.itemIconSize = resources.getDimension(R.dimen.bottomNav_icon_size).toInt()
    }

    private fun directSharesheetIntent() {
        if (intent?.action == null) return

        when (intent.type) {
            "text/plain" -> handleSendText()
        }
    }

    private fun handleSendText() {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_nav_host_fragment).navigateUp(appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)

        inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        getSystemService(Context.SEARCH_SERVICE) as SearchManager

        mainMenu = menu
        searchMenu = menu.findItem(R.id.search_menu_item)
        searchMenu.isVisible = true

        this.mySearchView = searchMenu.actionView as SearchView
        this.mySearchView.apply {
            this.queryHint = "검색어를 입력해주세요."
            this.setOnQueryTextListener(this@MainActivity)
            this.setOnQueryTextFocusChangeListener { _, hasExpand ->
                when (hasExpand) {
                    true -> {
                        Log.d("##55", "서치뷰 열림")
                    }
                    false -> {
                        binding.toolbar.collapseActionView()
                    }
                }
            }
            //서치뷰에서 에딧텍스트를 가져온다
            mySearchViewEditText = this.findViewById(androidx.appcompat.R.id.search_src_text)
        }

        this.mySearchViewEditText.apply {
            this.filters = arrayOf(InputFilter.LengthFilter(15))
            this.setTextColor(Color.WHITE)
            this.setHintTextColor(Color.WHITE)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController(R.id.my_nav_host_fragment)) || super.onOptionsItemSelected(
            item
        )
    }

    //서치뷰 검색어 입력 이벤트
    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()) {
            try {
                navController.navigate(
                    HomeFragmentDirections.actionHomeFragmentToLinkListFragment(
                        null,
                        query
                    )
                )
            } catch (e: Exception) {
                navController.navigate(
                    LinkListFragmentDirections.actionLinkListFragmentSelf(
                        null,
                        query
                    )
                )
            }
            binding.toolbar.title = query
        }
        binding.toolbar.collapseActionView()

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        return true
    }

    suspend fun get(email: String, repository: Repository) {

        //처음 로딩시 or 새로 고침시 호출할 함수

        try {
            val res = repository.rd_get(Email(email))
            Log.d("@@@@@@@","${res.body()?.res_data}")
            res.body()?.res_data?.forEach {
                if (it.meta.type == 1) {
                    getLink(it, repository)
                } else {
                    getFolder(it, repository)
                }
            }
        } catch (e: java.lang.Exception) {
            Log.d("@@12", "$e")
        }

    }

    suspend fun getFolder(dd: ResGetData, repository: Repository) {
        withContext(Dispatchers.IO) {
            qq = repository.getFolder(dd.meta.snode)
            if (qq == null) {
                var folder = Folder(name = dd.content.name,share = true,snodes =dd.meta.snode,key = dd.meta.snode!!)
                repository.insertFolder(folder)
            } else {
                qq!!.name = dd.content.name
                repository.updateFolder(qq!!)
            }
        }
    }

    suspend fun getLink(dd: ResGetData, repository: Repository) {
        withContext(Dispatchers.IO) {
            aa = repository.getLink(dd.meta.snode)
            var tagString = ""
            dd.content.tags?.forEach {
                tagString = tagString.plus(" $it")
            }
            Log.d("@@@@@@@","링크 준비 $aa")
            if (aa == null) {
                val link = Link(url = dd.content.url, memo = dd.content.memo, tags = tagString, parent = dd.meta.snode!!)
                Log.d("@@@@@@@","링크 불러오기 $link")
                repository.insertLink(link)
            } else {
                aa!!.url = dd.content.url
                aa!!.memo = dd.content.memo
                aa!!.tags = tagString
                repository.updateLink(aa!!)

            }
        }
    }
}
