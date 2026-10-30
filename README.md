# TOTP Generator

Java Swingを使用したデスクトップ向けTOTP（Time-based One-Time Password）生成アプリケーションです。
このドキュメントはChatGPTにより作成されています。

## Features

- Base32形式の秘密鍵に対応
- 6桁のTOTPを生成
- 30秒ごとに自動更新
- OTPの残り時間を表示
- 生成したOTPをクリップボードにコピー
- 秘密鍵を外部サーバーに送信しないローカル動作

## Requirements

- Java JDK 8以上

## Build

```bash
cd src
javac Main.java TOTPFrame.java TOTPGenerator.java Base32.java
jar cfm TOTPGenerator.jar MANIFEST.MF *.class
```

## Run

```bash
java -jar TOTPGenerator.jar
```

## Project Structure

```text
TOTPGenerator/
├── .gitignore
├── README.md
├── MANIFEST.MF
└── src/
    ├── Main.java
    ├── TOTPFrame.java
    ├── TOTPGenerator.java
    └── Base32.java
```

## License

未設定
