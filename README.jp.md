Terraforming for CEDEC 2013
========================

### 動作環境
- JRE/JDK 1.6以上

### 遊び方
- ヘルプの表示

        java -jar Terraforming.jar -h

- ユーザープレイモード

        java -jar Terraforming.jar
        java -jar Terraforming.jar -u 2
        java -jar Terraforming.jar -u 3

    - ユーザがプレイヤーを操作する場合、"-u"オプションを付け、ユーザが操作するプレイヤー数を指定する。オプションがない場合は"-u 1"を指定したものとして扱う。
    - 1つ目のコマンド例では、ユーザが1プレイヤーを操作、内蔵したサンプルAIプログラムが2,3プレイヤーを操作する。
    - 2つ目のコマンド例では、"-u"オプションを利用することで、ユーザが1,2プレイヤーを操作する、内蔵したサンプルAIプログラムが3プレイヤーを操作する。
    - 3つ目のコマンド例では、"-u"オプションを利用することで、ユーザが1,2,3プレイヤーを操作する。

- AIプレイモード

        java -jar Terraforming.jar -a "java -cp SampleAI/Java Main"
        java -jar Terraforming.jar -a "java -cp SampleAI/Java Main" "SampleAI/Cpp/a.exe"
        java -jar Terraforming.jar -a "java -cp SampleAI/Java Main" "SampleAI/Cpp/a.exe" "scala.bat -cp SampleAI/Scala Main"
        
    - 作成したAIプログラムを動かす場合、"-a"オプションを付け、動かしたいAIプログラムの実行コマンドをダブルクォーテーション(")で囲った引数で与える。
    - 1つ目のコマンド例では、JavaのサンプルAIが1プレイヤーを操作、内蔵したサンプルAIプログラムが2,3プレイヤーを操作する。
    - 2つ目のコマンド例では、JavaのサンプルAIが1プレイヤーを操作、C++のサンプルAIが2プレイヤーを操作、内蔵したサンプルAIプログラムが3プレイヤーを操作する。
    - 3つ目のコマンド例では、JavaのサンプルAIが1プレイヤーを操作、C++のサンプルAIが2プレイヤーを操作、ScalaのサンプルAIが3プレイヤーを操作する。
