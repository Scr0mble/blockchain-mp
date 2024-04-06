import java.util.Arrays;


public class Hash {
  /* Fields */

  private byte[] curhash;

  /* Constructors */

  public Hash(byte[] data) {
    this.curhash = data;
  }

  /* Methods */

  public byte[] getData() {
    return this.curhash;
  }

  /**
   * 
   * if the first three bytes of the hash are 0, it's valid.
   */
  public boolean isValid() {
    if (curhash.length >= 3 && curhash[0] == 0 && curhash[1] == 0 && curhash[2] == 0) {
      return true;
    } else {
      return false;
    }
  }

  public String toString() {
    int[] temp = new int[this.curhash.length];
    String result = "";
    for (int i = 0; i < this.curhash.length; i++) {
      temp[i] = Byte.toUnsignedInt(this.curhash[i]);
    }
    for (int i = 0; i < temp.length; i++) {
      String hex = String.format("%02x", temp[i]);
      result += hex;
    }
    return result;
  }

  public boolean equals(Object other) {
    if (other instanceof Hash) {
      Hash otherHash = (Hash) other;
      return Arrays.equals(this.curhash, otherHash.getData());
    } else {
      return false;
    }
  }

}


