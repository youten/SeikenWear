SeikenWear
----
Android WearからジェスチャでPowerPointを操作してみたものです。
後でもう少しマシにするかもしれませんがイロイロ適当です。
MacとWinのキーコードって結構違うんですね。

## SeikenWear
-Andorid WearなAndroid Studio Projectです。
-WearのServiceで加速度センサで時計の向きを取得して母艦に通知。
-Wearを真上に向けた状態から、手前・奥・左・右に90度倒して戻す、という動作をジェスチャとして認識しています。

## PptSender
-GETリクエストでキーイベントを発行するHTTPサーバです。
-Runnable jarにしてjava -jar PptSender.jarみたいにして起動することを想定しています。
-JettyなServletです。セキュリティも何もあったもんじゃないので遠隔操作にはご注意ください。
-JettyはJettyOnAndroidのjarを再結合したものを使ってますが要らないものがいっぱい入ってます。
-http://greety.sakura.ne.jp/redo/2014/04/jetty-on-androidwebsocket.html

## License
Apache License Version 2.0
