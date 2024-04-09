import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.ByteBuffer;
import java.util.Random;

public class Block {
  // +--------+-------------------------------------------------------
  // | Fields |
  // +--------+
  private int num;
  private int data;
  private long nonce;
  private Hash prevHash;
  private Hash curHash;

  // +--------------+-------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * creates a new Block with the mining operation to discover nonce
   * 
   * @param num
   * @param amount
   * @param prevHash
   */
  public Block(int num, int amount, Hash prevHash) {
    this.num = num;
    this.data = amount;
    this.prevHash = prevHash;

    // Mining:
    this.curHash = mine(num, data, prevHash);
  }

  /**
   * creates a new Block with give nonce. Does not perform mining as nonce is given
   * 
   * @param num
   * @param amount
   * @param prevHash
   * @param nonce
   */
  Block(int num, int amount, Hash prevHash, long nonce) {
    this.num = num;
    this.data = amount;
    this.prevHash = prevHash;
    this.curHash = calculateHash(num, data, prevHash, nonce);
  }

  // +---------+------------------------------------------------------
  // | Methods |
  // +---------+

  /**
   * Calculates the hash for the current block and returns it
   * 
   * @param num
   * @param data
   * @param prevHash
   * @param nonce
   */
  private Hash calculateHash(int num, int data, Hash prevHash, long nonce) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");

      // cast num, data, prevHash, nonce into byte[] format
      byte[] numBytes = ByteBuffer.allocate(Integer.BYTES).putInt(num).array();
      byte[] dataBytes = ByteBuffer.allocate(Integer.BYTES).putInt(data).array();
      byte[] prevHashBytes = null;
      if (prevHash != null) {
        prevHashBytes = prevHash.getData();
      }
      byte[] nonceBytes = ByteBuffer.allocate(Long.BYTES).putLong(nonce).array();

      // update md with num, data, prevHash, nonce (in byte[] format)
      md.update(numBytes);
      md.update(dataBytes);
      if (prevHashBytes != null) {
        md.update(prevHashBytes);
      }
      md.update(nonceBytes);

      // create the hash object
      byte[] hashInBytes = md.digest();
      return new Hash(hashInBytes);

    } catch (NoSuchAlgorithmException e) { // this exception shouln't occur but let's catch it
      throw new RuntimeException("failed to fetch SHA-256 for message digest");
    }
  }

  /**
   * performs the mining action and returns the hash for the current block in addition to updating it's nonce to match this hash
   * 
   * @param num
   * @param data
   * @param prevHash
   */
  private Hash mine(int num, int data, Hash prevHash) {
    Random rd = new Random();
    Hash tempHash;
    long randomNonce;

    do {
      // update nonce
      randomNonce = rd.nextLong();
      tempHash = calculateHash(num, data, prevHash, randomNonce);
    } while (!tempHash.isValid());

    this.nonce = randomNonce; 
    // loop ended, so it's valid hash.
    return tempHash;
  }

  // GETTERs below:

  /**
   * Returns the value in num
   */
  public int getNum() {
    return this.num;
  }

  /**
   * Returns the value in data
   */
  public int getAmount() {
    return this.data;
  }

  /**
   * Returns the value in nonce
   */
  public long getNonce() {
    return this.nonce;
  }

  /**
   * Returns the value in prevHash
   */
  public Hash getPrevHash() {
    return this.prevHash;
  }

  /**
   * Returns the value in curHash
   */
  public Hash getHash() {
    return this.curHash;
  }

  /**
   * Returns the the values stored in the fields of the current block represented as a string
   */
  public String toString() {
    String blockString = "Block " + this.num + " (Amount: " + this.data + ", Nonce: " + this.nonce
        + ", prevHash: " + this.prevHash + ", hash: " + this.curHash + ")";
    return blockString;
  }


} // class Block
