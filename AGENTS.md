# AGENTS.md

Jetpack Compose のカウンターアプリです。概要は [docs/README.md](docs/README.md)、テストと CI は [docs/TESTING.md](docs/TESTING.md) を参照してください。

## 変更後に通すもの

```sh
./gradlew detekt :app:lintDebug
./gradlew :app:testDebugUnitTest --tests '*UnitTest'
```

## 約束事

- テストクラス名は UNIT を `*UnitTest`、VRT を `*VRTest` にする（CI はこの名前で対象を選ぶ）。
- コメントとコミットメッセージは日本語で書く。コミットメッセージは `type: 要約`（例: `fix: ...`、`ci: ...`）。
- push は作業ブランチにだけ行い、強制 push と `--no-verify` は使わない。`main` へは PR 経由で入れる。
