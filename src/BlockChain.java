import java.io.PrintWriter;


public class BlockChain {
  Node<Block> first = null, last = null;

  private static class Node<T> {
    /* FIELDS */

    T block;
    Node<T> nextNode;

    /* CONSTRUCTORS */

    private Node(T data, Node<T> next) {
      this.block = data;
      this.nextNode = next;
    }

    private Node(T data) {
      this.block = data;
      this.nextNode = null;
    }
  }


  /* CONSTRUCTORS */
  public BlockChain(int initial) {
    this.first = new Node<Block>(mine(initial));
    this.last = this.first;
  }

  /* METHODS */

  /*
   * I wrote the "mine" function in Block.java whenever we create new Block(), nonce is
   * automatically mined to find the valid hash
   */
  public Block mine(int amount) {
    Hash lastHash = null;
    int index = 0;
    if (last != null) {
      lastHash = last.block.getHash();
      index = last.block.getNum() + 1;
    }
    Block blk = new Block(index, amount, lastHash);
    System.out.println(blk.getNonce());
    return blk;
  }

  public int getSize() {
    if (first == null)
      return 0;
    return last.block.getNum() - first.block.getNum() + 1;
  }

  public void append(Block blk) throws IllegalArgumentException {
    if (!blk.getHash().isValid()) {
      throw new IllegalArgumentException("append failed because cur hash is invalid");
    }
    if (blk.getPrevHash() != null && !blk.getPrevHash().isValid()) {
      throw new IllegalArgumentException("append failed because previous hash is invalid");
    }
    // append (we assume we have at least 1 element in the list):
    last.nextNode = new Node<Block>(blk);
    last = last.nextNode;
  }

  public boolean removeLast() {
    if (this.first != this.last) {
      Node<Block> cur = this.first;
      while (cur.nextNode != last) {
        cur = cur.nextNode;
      }
      cur.nextNode = null;
      this.last = cur;
      return true;
    }
    return false;
  }

  /**
   * gets the hash of the last block
   * 
   * @return
   */
  public Hash getHash() {
    return this.last.block.getHash();
  }

  /**
   * needs to check: 1. hash validities; 2. transaction validity
   */
  public boolean isValidBlockChain() {
    Node<Block> cur = this.first;
    int bal = 0;
    int initial = cur.block.getAmount();
    while (cur != null) {
      // if bal < 0, alexis has negative balance
      // if bal > initial, blake has negative balance
      if (bal < 0 || bal > initial) {
        System.out.println("bal: " + bal + "initial: " + initial);
        System.out.println("transaction invalid 1");
        return false;
      }
      // we are iterating through every node so we don't need to check prevHash.isValid()
      if (!cur.block.getHash().isValid()) {
        System.out.println("hash invalid");

        return false;
      }
      System.out.println("amnt befor math" + bal);
      bal += cur.block.getAmount();
      cur = cur.nextNode;
    }
    // we need to check the last node's balance validity
    if (bal < 0 || bal > initial) {
      System.out.println("transaction invalid 2");
      return false;
    }
    // else, it's valid
    return true;
  }

  void printBalances(PrintWriter pen) {
    int Alexis = first.block.getAmount();
    int initial = Alexis;
    Node<Block> cur = first.nextNode;

    while (cur != null) {
      Alexis += cur.block.getAmount();
      cur = cur.nextNode;
    }
    pen.println("Alexis: " + Alexis + ", Blake: " + (initial - Alexis));
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    Node<Block> cur = this.first;
    while (cur != null) {
      sb.append(cur.block.toString() + "\n");
      cur = cur.nextNode;
    }

    return sb.toString();
  }
}
