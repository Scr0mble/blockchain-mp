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

  /* I wrote the "mine" function in Block.java
     whenever we create new Block(), nonce is automatically mined to 
     find the valid hash */
  public Block mine(int amount) {
    Hash lastHash = null;
    int index = 0;
    if (last != null) {
      lastHash = last.block.getHash();
      index = last.block.getNum() + 1;
    }
    return new Block(index, amount, lastHash);
  }

  public int getSize() {
    if (first == null) return 0;
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
    if(this.first != this.last) {
      Node<Block> cur = this.first;
      while(cur.nextNode != last) {
        cur = cur.nextNode;
      }
      cur.nextNode = null;
      this.last = cur;
      return true;
    }
    return false;
  }

  public Hash getHash() {
    return this.last.block.getHash();
  }

  public boolean isValidBlockChain() {
    int bal = this.first.block.getAmount();
    Node<Block> cur = this.first.nextNode;
    while(cur != null) {
      bal += cur.block.getAmount();
      if(bal < 0) {
        return false;
      }
      // Add checks for validity like in append
    }
  }

  void printBalances(PrintWriter pen) {
    // implement this
  }
}
