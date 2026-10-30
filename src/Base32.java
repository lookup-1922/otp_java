package src;
class Base32 {
  public static byte[] decode(String input) {
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";
    input = input.toUpperCase();

    int outputLength = input.length() * 5 / 8;
    byte[] result = new byte[outputLength];

    int buffer = 0;
    int bitsLeft = 0;
    int index = 0;

    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      int value = alphabet.indexOf(c);

      if (value == -1) {
        throw new IllegalArgumentException("Invalid Base32 character: " + c);
      }

      buffer = (buffer << 5) | value;
      bitsLeft += 5;

      if (bitsLeft >= 8) {
        result[index++] = (byte) ((buffer >> (bitsLeft - 8)) & 0xFF);

        bitsLeft -= 8;
        buffer &= (1 << bitsLeft) - 1;
      }
    }

    return result;
  }
}
