# テスト

| 種類 | 自動実行 | 実行環境 |
| --- | --- | --- |
| UNIT | main への push・Pull Request | JVM（エミュレーター不要） |
| E2E | 毎週月曜 3:00 JST | Android API 36 エミュレーター1台 |
| VRT | 既存の Screenshots workflow | Robolectric |

UNIT・E2E は GitHub Actions の **Run workflow** から手動でも実行できます。
週次 E2E はデフォルトブランチを対象とし、workflow をそのブランチに取り込むと有効になります。
予定時刻から遅れて開始される場合があります。
UNIT の push 実行は main に限定し、PR とブランチの push による二重実行を避けています。

## UNIT

```sh
./gradlew :app:testDebugUnitTest --tests '*UnitTest'
```

`app/src/test/` に `*UnitTest` というクラス名で追加してください。
CI はこの命名規則で対象を選び、`CounterAppVRTest` の画像テストは Screenshots workflow が担当します。
現在はカウンターの増減、0〜1000の境界値、ViewModel の状態更新とリセットを検証します。
HTML・JUnit XML レポートは `unit-test-results` Artifact に7日間保存します。

## E2E

端末またはエミュレーターを起動して実行します。

```sh
./gradlew :app:connectedDebugAndroidTest
```

`app/src/androidTest/` のテストを実行します。
CI では毎回新しいエミュレーターを作成し、Gradle の依存関係をキャッシュします。
HTML・JUnit XML レポートは `e2e-test-results` Artifact に14日間保存します。
失敗時も生成済みのレポートを保存し、テスト失敗は CI の失敗として扱います。
