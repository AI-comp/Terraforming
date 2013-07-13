Terraforming for CEDEC 2013
========================

### 必要な環境
- JDK 1.6以上

### 遊び方
- ユーザープレイモード

        java -jar Terraforming.jar
        java -jar Terraforming.jar -u 3

    - 上のコマンドでは、プレイヤーは1人で、残りの2プレイヤーは内蔵AIとなる。

    - 人数は下のコマンドのように -u オプションを付けることで指定でき、1人から3人までプレイヤー、残りを内蔵AIが担当する。

- AIプレイモード

        java -jar Terraforming.jar -a "java -cp SampleAI/Java Main" "SampleAI/Haskell/Main.exe" "scala.bat -cp SampleAI/Scala Main"
        java -jar Terraforming.jar -a "java -cp SampleAI/Java Main" "SampleAI/Cpp/a.exe"
        
    - 作成したAIプログラムを動かす場合、 -a オプションを付け、動かしたいAIプログラムの実行コマンドをダブルクォーテーション(")で囲った引数で指定する。

    - -a オプションに引数として与えるAIは必ずしも3つである必要はなく、下のコマンド例のように2つしか与えなければ、デフォルトのサンプルAIプログラムが利用される。