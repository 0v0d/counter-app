# プロジェクト固有の ProGuard ルールをここに追加する。
# 適用する設定ファイルは build.gradle の proguardFiles で指定できる。
#
# 詳しくは次を参照:
#   http://developer.android.com/guide/developing/tools/proguard.html

# WebView で JavaScript を使う場合は、次のコメントを外して
# JavaScript インターフェースのクラスを完全修飾名で指定する:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# デバッグ用のスタックトレースに行番号を残す場合は、次のコメントを外す。
#-keepattributes SourceFile,LineNumberTable

# 行番号を残す場合に元のソースファイル名を隠すには、次のコメントを外す。
#-renamesourcefileattribute SourceFile