# テスト

| 種類 | 自動実行 | 実行環境 |
| --- | --- | --- |
| UNIT | main への push・Pull Request | JVM（エミュレーター不要） |
| E2E | 毎週月曜 3:00 JST | Android API 36 エミュレーター1台 |
| VRT | 既存の Screenshots workflow | Robolectric |
| detekt・Android Lint | main への push・Pull Request | 静的解析 |

UNIT・E2E は GitHub Actions の **Run workflow** から手動でも実行できます。
週次 E2E はデフォルトブランチを対象とし、workflow をそのブランチに取り込むと有効になります。
予定時刻から遅れて開始される場合があります。
UNIT の push 実行は main に限定し、PR とブランチの push による二重実行を避けています。

## pre-commit

コミット前に detekt・Android Lint・UNIT を実行します。クローンごとに一度、`.git/hooks/` にコピーしてください。
`.githooks/pre-commit` を変更したときも、再度コピーが必要です。

```sh
cp .githooks/pre-commit .git/hooks/pre-commit
```

`core.hooksPath` を使わずコピーするのは、Claude Code・Codex が `.githooks/` には書き込めても
`.git/hooks/` には書き込めないためです。エージェントがコミット時に実行される内容を書き換えられないようにしています。

`app/`・`gradle/`・`config/detekt/`・`*.kts`・`gradle.properties` を含まないコミット（ドキュメントのみなど）では実行しません。
ステージしていない変更も含めた作業ツリーを検査します。急ぎの場合は `git commit --no-verify` で省略できます。

## UNIT

```sh
./gradlew :app:testDebugUnitTest --tests '*UnitTest'
```

`app/src/test/` に `*UnitTest` というクラス名で追加してください。
CI はこの命名規則で対象を選び、`CounterAppVRTest` の画像テストは Screenshots workflow が担当します。
現在はカウンターの増減、0〜1000の境界値、ViewModel の状態更新とリセットを検証します。
HTML・JUnit XML レポートは `unit-test-results` Artifact に7日間保存します。

カバレッジは Kover で計測し、PR にコメントします（全体と変更ファイルの行カバレッジ）。
CI と同じく `*UnitTest` のみを対象とし、Hilt・Compose コンパイラーの生成コードと Preview は除外しています。

```sh
./gradlew :app:testDebugUnitTest --tests '*UnitTest' :app:koverHtmlReportDebug
```

XML レポートも `unit-test-results` Artifact に含まれます。

## E2E

端末またはエミュレーターを起動して実行します。

```sh
./gradlew :app:connectedDebugAndroidTest
```

`app/src/androidTest/` のテストを実行します。
CI では毎回新しいエミュレーターを作成し、Gradle の依存関係をキャッシュします。
HTML・JUnit XML レポートは `e2e-test-results` Artifact に14日間保存します。
失敗時も生成済みのレポートを保存し、テスト失敗は CI の失敗として扱います。

## 静的解析

```sh
./gradlew detekt :app:lintDebug
```

detekt はアプリ・UNIT・E2E の Kotlin ソースを検査します。
Gradle 9.6.1 / Kotlin 2.4.10 に対応する `2.0.0-alpha.6` を使用しています。
Compose の関数名・Preview・UI の数値、および日本語のテスト名に合わせた設定は
`config/detekt/detekt.yml` にあります。既存の問題を一括で無視する baseline は使っていません。
Android Lint は debug バリアントを検査し、エラーがあれば CI を失敗させます。
PR では reviewdog が SARIF レポートを読み、変更した行の指摘をレビューコメントします。
CI では Android SDK のセットアップとビルドを共有するため、UNIT と同じジョブで実行します。
レポートは `detekt-reports` と `android-lint-reports` Artifact に7日間保存します。

## Dependabot と自動マージ

Gradle の依存関係・プラグインと GitHub Actions を毎週月曜 3:00 JST に確認します。
パッチ・マイナー更新はエコシステムごとにまとめた PR を作成します。
メジャー更新は個別 PR となり、自動マージの対象にはなりません。

Dependabot 自身が作成した同一リポジトリ内の PR だけに squash 自動マージを設定します。
`main` の必須チェックは `gitleaks`、`detekt`、`lint-and-unit-tests`、`compare` です。
E2E は週次実行のためマージ条件には含めません。
自動マージの有効化や必須チェックの設定が欠けている場合、workflow は失敗してマージを止めます。

GitHub 側では **Settings → General → Allow auto-merge** と、
**Settings → Rules → Rulesets → main** の必須チェック設定を使います。
上記4件を GitHub Actions からのチェックとして指定し、ブランチが最新であることも要求します。
workflow と `.github/dependabot.yml` がデフォルトブランチに入ると自動化が有効になります。

Dependabot の PR ではトークンが読み取り専用になるため、Screenshots workflow は
画像ブランチへの push・PR コメントを省略し、結果を Artifact に保存します。
Lint の指摘・カバレッジのコメントも同様に省略します。
