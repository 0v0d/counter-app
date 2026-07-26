package com.example.counterapp

import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasAnyAncestor
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 実アプリ(MainActivity)を起動してユーザー操作を再現するE2Eテスト
 */
@RunWith(AndroidJUnit4::class)
class CounterAppE2ETest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun getString(@StringRes id: Int) = composeRule.activity.getString(id)

    private fun clickIncrement(times: Int = 1) = repeat(times) {
        composeRule.onNodeWithContentDescription("Increment").performClick()
    }

    private fun clickDecrement(times: Int = 1) = repeat(times) {
        composeRule.onNodeWithContentDescription("Decrement").performClick()
    }

    @Test
    fun 起動時はカウンターが0でリセットボタンは表示されない() {
        composeRule.onNodeWithText("0").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Reset").assertDoesNotExist()
    }

    @Test
    fun プラスボタンをタップするとカウンターが増える() {
        clickIncrement(times = 3)

        composeRule.onNodeWithText("3").assertIsDisplayed()
    }

    @Test
    fun マイナスボタンをタップするとカウンターが減る() {
        clickIncrement(times = 3)
        clickDecrement()

        composeRule.onNodeWithText("2").assertIsDisplayed()
    }

    @Test
    fun カウンターが0のときマイナスをタップしても0のまま() {
        clickDecrement()

        composeRule.onNodeWithText("0").assertIsDisplayed()
    }

    @Test
    fun カウンターが1以上になるとリセットボタンが表示される() {
        clickIncrement()

        composeRule.onNodeWithContentDescription("Reset").assertIsDisplayed()
    }

    @Test
    fun リセット確認ダイアログでキャンセルすると値は変わらない() {
        clickIncrement(times = 5)
        composeRule.onNodeWithContentDescription("Reset").performClick()

        composeRule.onNodeWithText(getString(R.string.ResetMessage)).assertIsDisplayed()
        composeRule.onNodeWithText(getString(R.string.Cancel)).performClick()

        composeRule.onNodeWithText(getString(R.string.ResetMessage)).assertDoesNotExist()
        composeRule.onNodeWithText("5").assertIsDisplayed()
    }

    @Test
    fun リセット確認ダイアログでリセットすると0に戻る() {
        clickIncrement(times = 5)
        composeRule.onNodeWithContentDescription("Reset").performClick()

        // ダイアログ内のリセットボタン(画面上のリセットボタンと区別するためダイアログ内に限定)
        composeRule.onNode(
            hasText(getString(R.string.Reset)) and hasAnyAncestor(isDialog())
        ).performClick()

        composeRule.onNodeWithText("0").assertIsDisplayed()
        composeRule.onNodeWithContentDescription("Reset").assertDoesNotExist()
    }

    @Test
    fun 設定画面へ遷移して戻ってもカウンターの値が保持される() {
        clickIncrement(times = 2)

        composeRule.onNodeWithText(getString(R.string.Settings)).performClick()
        composeRule.onNodeWithText(getString(R.string.Theme)).assertIsDisplayed()

        composeRule.onNodeWithText(getString(R.string.Counter)).performClick()

        composeRule.onNodeWithText("2").assertIsDisplayed()
    }

    @Test
    fun 設定画面でテーマを切り替えられる() {
        composeRule.onNodeWithText(getString(R.string.Settings)).performClick()

        composeRule.onNodeWithText(getString(R.string.Dark)).performClick()
        composeRule.onNodeWithText(getString(R.string.Dark)).assertIsSelected()
        composeRule.onNodeWithText(getString(R.string.System)).assertIsNotSelected()

        // 端末のDataStoreに永続化されるためデフォルトのSystemに戻す
        composeRule.onNodeWithText(getString(R.string.System)).performClick()
        composeRule.onNodeWithText(getString(R.string.System)).assertIsSelected()
    }
}