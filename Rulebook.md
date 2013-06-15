Rule book of Terraforming
=

## Terraforming

### 目次

* ストーリー
* 概要
    * プレイ人数
    * ゲーム概要
* フィールド
    * Wasteland / 荒野
    * Undeveloped land / 領土
    * Developed land / 支配地
    * Hole / 穴
* ゲームの目的
    * 終了条件
    * 勝敗
* ゲームの開始
    * 初期状態
* 行動
    * 侵攻コマンド
    * 建造コマンド
    * 建造レシピ
        * 移動系
            * 橋
        * ロボット系
            * シールド
            * 攻撃塔
            * ロボット製造工場
        * Material系
            * 採掘装置
        * 得点系
            * 公園
            * 広場
            * 公共施設

#### 注意
* ルール、テキストは開発中のため、大幅に変更される可能性があります。

### ストーリー
　小惑星探査採掘ロボット”Asterobots”は効率よく小惑星を採掘するための人工知能をプログラミングコンペティションJavaChallenge2012によって公募したことが話題を呼んだ。これによって実用化されたAsterobotは数多の成果を上げ、小惑星についての研究は急速に進むことになった。<br>
　中でも一番の成果は小惑星内のMaterialが豊富であることにより、小惑星を人の住める環境にするTerraformingが現実味を帯びてきたことである。その結果、小惑星を観光地にしてほしいという要望が高まった。<br>
　そこで今度は、小惑星にあるMaterialを使って与えられたレシピを元に建物を配置し、都市を作成するロボットの人工知能をプログラミングコンペティションCEDEC CHALLENGE 2013で公募することを決定した。

　果たして今度もロボットは期待通りの成果が出せるのだろうか？

### 概要
#### プレイ人数
3人(固定)
### ゲーム概要
プレイヤーはロボットを操作して侵攻もしくは建造を行い、小惑星の開発を行う。敵対するプレイヤーより多くの土地の開発を行うことがこのゲームの最終目標である。
### フィールド
正六角形127マスを、一辺を7個とする正六角形の形に敷き詰めて使用する。フィールドの座標系は中心を原点とし以下のように設定されている(図では、簡単のため一辺を3個としてある)。

それぞれのマスは4つの状態に分かれており、どの状態にあるかでロボットの侵攻及び建物の建造が行えるかどうかが変化する。

* __Wasteland / 荒野__

    以前Asterobotによって採掘が行われた未整備の土地であり、今回のゲームの初期状態。この状態のマスにロボットが進入すると、マスが占領され下の領土に変化する。

* __Undeveloped land / 領土__

    プレイヤーによって占領された土地。各マスにはMaterialが1個存在する。敵対するプレイヤーの領土にはロボットを進入させることで侵略することができる。ロボットがいるこの状態のマスに対して建造コマンドを実行すると、建物が建ち下の支配地に変化する。

* __Developed land / 支配地__

    プレイヤーが建造コマンドによって建物を建てた後の土地。すべていずれかのプレイヤーの所有物であり、敵対するプレイヤーの支配地にはロボットを進入させることはできない。

* __Hole / 穴__

    フィールドに15個程度存在する障害物。ロボットが進入することはできるが、進入すると穴に落ちたことになりそれ以降移動できなくなる。橋(後述)を建造すると建造したプレイヤーの支配地となり、そのマスに存在するロボットは移動ができるようになる。穴には橋のみ建造することができる。

|未占領チームによる侵攻|占領チームによる建造|種類                   |
|----------------------|--------------------|-----------------------|
|○                    |×                  |荒野 : Wasteland       |
|○                    |○                  |領土 : Undeveloped land|
|×                    |×                  |支配地 : Developed land|
|△                    |△                  |穴 : Hole              |

*穴には侵攻することができるが、橋を建造しないと出ることはできない。また、穴にはいずれかのプレイヤーの橋だけを建造することができる。

### ゲームの目的

#### 終了条件

以下の条件のいずれかを満たす。

1. マップに存在するマスがすべていずれかのプレイヤーの支配地となる
1. 規定のターン数が経過する

#### 勝敗
以下のルールに基づいて点数を計算し、最も点数の高いプレイヤーを勝者とする。

1. 公共施設(後述) → 1棟につき10点
1. 支配地 → 1マスにつき3点
1. 領土 → 1マスにつき1点

### ゲームの開始
#### 初期状態
各プレイヤーの初期位置及び穴の位置は以下の方法でランダムに決定される。

1. マップの3分の1をランダム生成（穴や初期位置1箇所）
1. 生成したものを複製して残りの3分の2を生成
1. 対称的なマップの完成

初期位置となったマスは各プレイヤーの支配地となり、毎ターン開始時にそこから5台のロボットが生成される。

### 行動
#### ターンの流れ

1. ターン開始時処理
    1. プレイヤーの本拠地及び所有するロボット製造工場マスに規定の数のロボットを追加
    1.  シールドの効果範囲内にいる自分のロボットの数を半減させる
1. 行動
    1. 侵攻コマンドを行うか建設コマンドを行うかの選択
    1. 選択後、実際のコマンドを入力
    1. finishコマンドで終了させることができる
1. ターン終了時処理
    1. シールドの効果範囲内にいる自分のロボットの数を倍増させる
    1. 攻撃塔の攻撃範囲内にいるうちで一番近い敵ロボットを一定割合で減少させる

#### 侵攻コマンド
侵攻コマンドでは、すべてのロボットを1ターンに1マス動かすことができる。

ロボットは荒野、操作するプレイヤーの領土または支配地、敵対するプレイヤーの領土、穴のいずれかに向かって移動させることができる。

荒野に進入した場合、そのマスは占領され自分の領土になる。

敵の領土に侵攻した場合、自分のロボットと敵のロボットは戦闘を行い消滅する。

戦闘後に自分のロボットが残っていた場合、もしくは敵のロボットが0体のマスに侵攻した場合は、そのマスは自分の領土になる。同じマスに複数のロボットを侵攻させた場合は差し引き分が残る。

穴に進入した場合、そのマスに入ったロボットは移動することができなくなる。敵対するプレイヤーの領土と同様に、敵のロボットがいる穴に侵攻すると戦闘が行われロボットが消滅する。

#### 建造コマンド
建造コマンドでは、自分の領土(橋のみ、自分のロボットが存在している穴)にロボットを消費することで建物を建てて支配地へと変化させ、建てた建物に応じた様々な効果を得ることができる。

消費するロボット数は建物によって異なり、建造する際には消費数より多いロボットが建造地となるマスに存在していなければならない。

また、建造することができるのは、建造地となるマスおよびそれに隣接したマスのうち、自分の領土となっているマスにあるMaterialの個数の合計以下のコストを持つ建物である。

例えば下図の中央に建物を建てるとき、このマスと隣接したマスのうち、自分の領土となっているマスは5つあり、Materialの合計は6であるため、6コスト以下の建物を建造することができる。

建造を行ってもMaterialは消費されない(厳密には、建造を行ったマスは支配地へと変化するためそのマスのMaterialは消失する)。

#### 建造レシピ
_移動系_

* 橋 4コスト/10体

    穴を自分の支配地に変更する。橋を建造する場合、建造地となるマスは自分の領土ではなく自分のロボットが存在する穴でなければならない

_ロボット系_

* シールド 6コスト/25体

    周囲1マスの自分の所有しているロボットの数は自分のターンの終了時に2倍になり開始時に1/2になる

* 攻撃塔 5コスト/25体

    自分のターンの終了直後、攻撃塔から5マス以内で最も近いマスにいる敵ロボットの数が10%(最低でも1体は)減る（同じ距離のマスだったら？）

* ロボット工場 4コスト/50体

    毎ターン開始時にそこからロボットを1つ生成する

_Material系_

* 採掘装置 5コスト/20体

    隣接した自分の領土のMaterialが1個増える

_得点系_

* 公園 4コスト/1体

    建造したマスを支配地に変更する

* 広場 9コスト/1体

    建造したマスと周囲1マスの自分の領土を支配地に変更する

* 公共施設 19コスト/1体

    建造すると1棟につき勝利点10点を獲得する。建造したマスと周囲2マスの自分の領土を支配地に変更する