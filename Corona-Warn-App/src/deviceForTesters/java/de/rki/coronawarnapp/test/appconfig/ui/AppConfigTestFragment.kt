package de.rki.coronawarnapp.test.appconfig.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import de.rki.coronawarnapp.R
import de.rki.coronawarnapp.databinding.FragmentTestAppconfigBinding
import de.rki.coronawarnapp.test.menu.ui.TestMenuItem
import de.rki.coronawarnapp.util.di.AutoInject
import de.rki.coronawarnapp.util.ui.observe2
import de.rki.coronawarnapp.util.ui.viewBindingLazy
import de.rki.coronawarnapp.util.viewmodel.CWAViewModelFactoryProvider
import de.rki.coronawarnapp.util.viewmodel.cwaViewModels
import org.joda.time.DateTimeZone
import org.joda.time.format.ISODateTimeFormat
import javax.inject.Inject

@SuppressLint("SetTextI18n")
class AppConfigTestFragment : Fragment(R.layout.fragment_test_appconfig), AutoInject {

    @Inject lateinit var viewModelFactory: CWAViewModelFactoryProvider.Factory
    private val vm: AppConfigTestFragmentViewModel by cwaViewModels { viewModelFactory }

    private val binding: FragmentTestAppconfigBinding by viewBindingLazy()

    private val timeFormatter = ISODateTimeFormat.dateTime()
        .withZone(DateTimeZone.forID("Europe/Berlin"))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.currentConfig.observe2(this) {
            binding.currentConfiguration.text = it.rawConfig.toString()
            binding.lastUpdate.text = timeFormatter.print(it.updatedAt)
            binding.timeOffset.text = "${it.localOffset.millis}ms"
        }

        binding.forceDownload.setOnClickListener { vm.forceDownload() }
    }

    companion object {
        val MENU_ITEM = TestMenuItem(
            title = "Remote Config Data",
            description = "View & Control the remote config.",
            targetId = R.id.test_appconfig_fragment
        )
    }
}
