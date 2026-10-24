# テスト

| 種類 | 自動実行 | 実行環境 |
| --- | --- | --- |
| UNIT | main への push・Pull Request | JVM（エミュレーター不要） |
| E2E | 毎週月曜 3:00 JST | Android API 36 エミュレーター1台 |
| VRT | 既存の Screenshots workflow | Robolectric |
| detekt・Android Lint | main への push・Pull Request | 静的解析 |
| ktlint | ローカルの pre-commit のみ | Kotlin・Gradle スクリプトの整形 |

UNIT・E2E は GitHub Actions の **Run workflow** から手動でも実行できます。
週次 E2E はデフォルトブランチを対象とし、workflow をそのブランチに取り込むと有効になります。
予定時刻から遅れて開始される場合があります。
UNIT の push 実行は main に限定し、PR とブランチの push による二重実行を避けています。

## pre-commit

コミット前に ktlint で整形し、detekt・Android Lint・UNIT を実行します。クローンごとに一度、`.git/hooks/` にコピーしてください。
`.githooks/pre-commit` を変更したときも、再度コピーが必要です。

```sh
cp .githooks/pre-commit .git/hooks/pre-commit
```

`core.hooksPath` を使わずコピーするのは、Claude Code・Codex が `.githooks/` には書き込めても
`.git/hooks/` には書き込めないためです。エージェントがコミット時に実行される内容を書き換えられないようにしています。

`app/`・`gradle/`・`config/detekt/`・`*.kts`・`gradle.properties`・`.editorconfig`・`.githooks/pre-commit` を含まないコミット（ドキュメントのみなど）では実行しません。
ステージしていない変更も含めた作業ツリー全体を整形・検査します。
整形後に Kotlin ファイル（`*.kt`・`*.kts`）の未ステージ差分があれば、コミットを停止します。
`git diff` で内容を確認し、必要な変更を `git add` してから再度コミットしてください。
部分ステージや別作業の未ステージ差分も自動でコミットに含めないよう、フックは `git add` しません。
Kotlin の未ステージ差分を残したままコミットする場合は、先に別作業へ退避してください。

### ktlint

```sh
./gradlew ktlintFormat  # 自動整形
./gradlew ktlintCheck   # 書き換えずに確認
```

アプリ・UNIT・VRT・E2E の Kotlin ソースと、ルート・app の Gradle Kotlin スクリプトを対象にします。
`build/` 以下の生成コードは除外します。`.editorconfig` で Kotlin 公式スタイルに対応する
`intellij_idea` を使い、行長を detekt と同じ 120 文字に揃えています。
Compose の関数名と日本語のテスト名を許可しています。

CI では ktlint の整形・チェックを実行しません。自動整形で意図しない差分が含まれる可能性があるため、
整形結果をローカルで確認してからコミットする運用にしています。
`ktlintCheck` 自体はファイルを書き換えませんが、ktlint はローカルで完結させる方針です。

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
