/*
 * Copyright 2017 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.tivi.showdetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.navArgs
import app.tivi.R
import app.tivi.TiviActivity
import app.tivi.showdetails.details.ShowDetailsFragment
import app.tivi.showdetails.episodedetails.EpisodeDetailsFragment
import app.tivi.util.observeEvent

class ShowDetailsActivity : TiviActivity() {

    companion object {
        private const val KEY_SHOW_ID = "show_id"

        fun createIntent(context: Context, id: Long): Intent {
            return Intent(context, ShowDetailsActivity::class.java).apply {
                putExtra(KEY_SHOW_ID, id)
            }
        }
    }

    private lateinit var navigatorViewModel: ShowDetailsNavigatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details)

        navigatorViewModel = ViewModelProviders.of(this, viewModelFactory)
                .get(ShowDetailsNavigatorViewModel::class.java)

        navigatorViewModel.events.observeEvent(this) {
            when (it) {
                is NavigateUpEvent -> onNavigateUp()
                is ShowEpisodeDetailsEvent -> showEpisodeDetails(it.episodeId)
            }
        }

        findViewById<View>(R.id.details_root).systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        postponeEnterTransition()
    }

    override fun handleIntent(intent: Intent) {
        val args: ShowDetailsActivityArgs by navArgs()
        supportFragmentManager.beginTransaction()
                .replace(R.id.details_content, ShowDetailsFragment.create(args.showId))
                .commit()
    }

    private fun showEpisodeDetails(episodeId: Long) {
        EpisodeDetailsFragment.create(episodeId)
                .show(supportFragmentManager, "episode")
    }
}