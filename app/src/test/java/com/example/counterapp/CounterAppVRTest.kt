package com.example.counterapp

import androidx.annotation.StringRes
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isSelected
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/**
 * 実アプリ(MainActivity)をRobolectric上で起動して各画面の見た目を記録するVRT
 * 動作の検証はandroidTestのCounterAppE2ETestが担当し、こちらは見た目の差分検出に特化する
 */
// Robolectric 4.16はSDK 37未対応のため36でエミュレートする
@Suppress("NonAsciiCharacters", "TestFunctionName")
@Config(sdk = [36], qualifiers = RobolectricDeviceQualifiers.Pixel7)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@RunWith(AndroidJUnit4::class)
class CounterAppVRTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun getString(@StringRes id: Int) = composeRule.activity.getString(id)

    private fun clickIncrement(times: Int = 1) = repeat(times) {
        composeRule.onNodeWithContentDescription("Increment").performClick()
    }

    @Test
    fun 起動時のカウンター画面() {
        composeRule.onRoot().captureRoboImage()
    }

    @Test
    fun カウントアップ後はリセットボタンが表示される() {
        clickIncrement(times = 3)

        composeRule.onRoot().captureRoboImage()
    }

    @Test
    fun リセット確認ダイアログ() {
        clickIncrement()
        composeRule.onNodeWithContentDescription("Reset").performClick()

        composeRule.onNode(isDialog()).captureRoboImage()
    }

    @Test
    fun 設定画面() {
        composeRule.onNodeWithText(getString(R.string.Settings)).performClick()

        composeRule.onRoot().captureRoboImage()
    }

    @Test
    fun 設定画面でダークテーマを選択() {
        composeRule.onNodeWithText(getString(R.string.Settings)).performClick()
        composeRule.onNodeWithText(getString(R.string.Dark)).performClick()

        // テーマはDataStore経由で非同期に反映されるため、選択状態になるまで待つ
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodes(hasText(getString(R.string.Dark)) and isSelected())
                .fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onRoot().captureRoboImage()
    }
}
