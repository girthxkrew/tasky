@file:OptIn(ExperimentalMaterial3Api::class)

package com.rkm.tasky.feature.edit.screen

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rkm.tasky.R
import com.rkm.tasky.feature.edit.viewmodel.EditScreenViewModel
import com.rkm.tasky.navigation.EditActionType
import com.rkm.tasky.ui.theme.DividerColor
import com.rkm.tasky.ui.theme.EditScreenTopAppBarColor
import com.rkm.tasky.ui.theme.EditScreenTopBarAppTitleTextColor
import com.rkm.tasky.ui.theme.EditScreenTopBarSaveTextColor

@Composable
fun EditScreenRoot(
    modifier: Modifier,
    viewModel: EditScreenViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onSavePressed: () -> Unit
) {
    val text by viewModel.text.collectAsStateWithLifecycle()

    EditScreen(
        modifier = modifier,
        text = text,
        onTextUpdate = viewModel::updateText,
        action = viewModel.action,
        onNavigateBack = onNavigateBack,
        onSavePressed = onSavePressed
        )
}

@Composable
private fun EditScreen(
    modifier: Modifier,
    text: String,
    onTextUpdate: (String) -> Unit,
    action: EditActionType,
    onNavigateBack: () -> Unit,
    onSavePressed: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CenterAlignedTopAppBar(
            title = { TopBarTitle(action.id) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = EditScreenTopAppBarColor
            ),
            navigationIcon = {
                IconButton( onClick = onNavigateBack) {
                    Icon(
                        modifier = Modifier.fillMaxSize(0.5f),
                        painter = painterResource(R.drawable.edit_screen_back),
                        contentDescription = null
                    )
                }
            },
            actions = {
                TextButton(onClick = onSavePressed) {
                    Text(
                        text = stringResource(R.string.edit_screen_save),
                        style = MaterialTheme.typography.titleMedium,
                        color = EditScreenTopBarSaveTextColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
        HorizontalDivider(
            modifier = Modifier.padding(
                horizontal = 16.dp),
            color = DividerColor,
            thickness = 2.dp
        )

        BasicTextField(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(top = 24.dp, start = 16.dp, end = 16.dp),
            value = text,
            onValueChange = onTextUpdate,
            textStyle = when(action) {
                is EditActionType.Title -> MaterialTheme.typography.bodyLarge
                is EditActionType.Description -> MaterialTheme.typography.bodyMedium
            }
        )
    }

}

@Composable
private fun TopBarTitle(@StringRes id: Int) {
    Text(
        text = stringResource(id),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = EditScreenTopBarAppTitleTextColor
    )
}

@Preview(
    showSystemUi = true
)
@Composable
private fun PreviewEditScreen() {
    EditScreen(
        Modifier.fillMaxSize(),
        "Project NeXt",
        {},
        EditActionType.Title,
        {},
        {}
    )
}