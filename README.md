# LGWF-PointManager

LGWFのポイントをLGWと共有したりアイテムとして受け取るためのプラグイン。
LGWFでキルすると1ポイントもらえる。

## Command

- give  プレイヤーにポイントを与えられる(マイナス値可) [運営専用]
```bash
/lpm give <PlayerID> <Number>
```

- point  プレイヤーのポイントを確認できる
```bash
/lpm point <PlayerID or Blank>
```

- send  プレイヤーに自分のポイントを送ることができる
```bash
/lpm send <PlayerID> <Number>
```

- item  ポイントをアイテムとして受け取る [LGW専用]
```bash
/lpm item
```

## Permission

- For lpm command.
```bash
 lpm.admin
```
