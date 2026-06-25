package br.com.brunoccbertolini.cocktailhelperapp.presentation.screen.settings

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.brunoccbertolini.cocktailhelperapp.R
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.MeasureSystem
import br.com.brunoccbertolini.cocktailhelperapp.domain.model.ThemeMode
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailText
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.atoms.CocktailTextStyle
import br.com.brunoccbertolini.cocktailhelperapp.presentation.design.molecules.SectionHeader
import br.com.brunoccbertolini.cocktailhelperapp.ui.theme.Spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                start = Spacing.lg,
                end = Spacing.lg,
                top = contentPadding.calculateTopPadding() + Spacing.lg,
                bottom = contentPadding.calculateBottomPadding() + Spacing.lg
            )
    ) {
        CocktailText(text = stringResource(R.string.settings), style = CocktailTextStyle.Headline)
        Spacer(Modifier.height(Spacing.xl))

        SectionHeader(title = stringResource(R.string.appearance))
        Spacer(Modifier.height(Spacing.sm))
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            ThemeMode.entries.forEachIndexed { index, mode ->
                SegmentedButton(
                    selected = state.themeMode == mode,
                    onClick = { viewModel.onAction(SettingsAction.SetThemeMode(mode)) },
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = ThemeMode.entries.size)
                ) { Text(stringResource(mode.labelRes())) }
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Spacer(Modifier.height(Spacing.md))
            SettingSwitchRow(
                title = stringResource(R.string.dynamic_color),
                subtitle = stringResource(R.string.dynamic_color_desc),
                checked = state.useDynamicColor,
                onCheckedChange = { viewModel.onAction(SettingsAction.SetDynamicColor(it)) }
            )
        }

        Spacer(Modifier.height(Spacing.xl))
        SectionHeader(title = stringResource(R.string.measurements))
        Spacer(Modifier.height(Spacing.sm))
        SingleChoiceSegmentedButtonRow {
            SegmentedButton(
                selected = state.measureSystem == MeasureSystem.Original,
                onClick = { viewModel.onAction(SettingsAction.SetMeasureSystem(MeasureSystem.Original)) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2)
            ) { Text(stringResource(R.string.oz)) }
            SegmentedButton(
                selected = state.measureSystem == MeasureSystem.Metric,
                onClick = { viewModel.onAction(SettingsAction.SetMeasureSystem(MeasureSystem.Metric)) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2)
            ) { Text(stringResource(R.string.ml)) }
        }
    }
}

private fun ThemeMode.labelRes(): Int = when (this) {
    ThemeMode.SYSTEM -> R.string.theme_system
    ThemeMode.LIGHT -> R.string.theme_light
    ThemeMode.DARK -> R.string.theme_dark
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            CocktailText(text = title, style = CocktailTextStyle.Body)
            CocktailText(
                text = subtitle,
                style = CocktailTextStyle.Caption,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.width(Spacing.md))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
