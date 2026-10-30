package src;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;

class TOTPGenerator {
  private byte[] calculateHmac(byte[] key, byte[] message) {
    byte[] result;
    try {
      // HMAC-SHA1を指定してMacインスタンスを取得
      Mac mac = Mac.getInstance("HmacSHA1");
      // 秘密鍵を設定するためのSecretKeySpecを作成
      SecretKeySpec secretKey = new SecretKeySpec(key, "HmacSHA1");
      // Macに秘密鍵を登録
      mac.init(secretKey);
      // メッセージを計算して結果を返す
      result = mac.doFinal(message);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  private byte[] getCounterBytes(long counter) {
    ByteBuffer buffer = ByteBuffer.allocate(8);
    buffer.putLong(counter);
    byte[] array = buffer.array();
    return array;
  }

  private int dynamicTruncate(byte[] hash) {
    int offset = hash[19] & 0x0F;
    int result = ((hash[offset] & 0x7F) << 24)
        | ((hash[offset + 1] & 0xFF) << 16)
        | ((hash[offset + 2] & 0xFF) << 8)
        | (hash[offset + 3] & 0xFF);
    return result;
  }

  private String formatOtp(int value) {
    int otp = value % 1_000_000;
    String str = String.format("%06d", otp);
    return str;
  }

  public String generateTOTP(String secret) {
    long currentTimeSeconds = System.currentTimeMillis() / 1000;
    return generateTOTP(secret, currentTimeSeconds);
  }

  String generateTOTP(String secret, long currentTimeSeconds) {
    // Base32形式の秘密鍵をbyte[]に変換する
    byte[] secretKey = Base32.decode(secret);

    // 現在のUNIX時間を秒単位で取得する
    // long currentTimeSeconds = System.currentTimeMillis() / 1000;

    // 30秒単位のカウンタ値を計算する
    long counter = currentTimeSeconds / 30;

    // カウンタ値を8バイトに変換する
    byte[] counterBytes = getCounterBytes(counter);

    // HMAC-SHA1を計算する
    byte[] hmacResult = calculateHmac(secretKey, counterBytes);

    // Dynamic Truncationで整数を取得する
    int truncatedValue = dynamicTruncate(hmacResult);

    // 6桁のOTPに変換して返す
    String otp = formatOtp(truncatedValue);

    return otp;
  }
}
