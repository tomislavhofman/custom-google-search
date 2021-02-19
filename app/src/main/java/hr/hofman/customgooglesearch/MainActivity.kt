package hr.hofman.customgooglesearch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.hofman.customgooglesearch.ui.resultitems.ResultItemsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, ResultItemsFragment())
                .commit()
        }
    }
}
