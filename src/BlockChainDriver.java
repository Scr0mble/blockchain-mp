import java.io.PrintWriter;
import java.util.Scanner;

public class BlockChainDriver {
  public static void main(String args[]) {
    PrintWriter pen = new PrintWriter(System.out, true);
    Scanner keyboard = new Scanner(System.in);
    String command = "";
    String listOfCommands =
        "Valid commands:\n" + "\tmine: discovers the nonce for a given transaction\n"
            + "\tappend: appends a new block onto the end of the chain\n"
            + "\tremove: removes the last block from the end of the chain\n"
            + "\tcheck: checks that the block chain is valid\n"
            + "\treport: reports the balances of Alexis and Blake\n"
            + "\thelp: prints this list of commands\n" + "\tquit: quits the program\n";

    // we initialize a new BlockChain.java
    BlockChain chain = new BlockChain(Integer.valueOf(args[0]));

    pen.println(listOfCommands);

    // loop through the commands
    do {
      pen.println("Command?: ");
      command = keyboard.nextLine();
      if (command.equals("mine")) {
        int amount;
        pen.println("Amount Transfered? ");
        amount = keyboard.nextInt();
        keyboard.nextLine(); // consume the newline
        Block blk = chain.mine(amount);
        pen.println("amount = " + amount + ", nonce = " + blk.getNonce());
      } else if (command.equals("append")) {
        int amount;
        long nonce;
        int num = chain.getSize();
        pen.println("Amount transferred? ");
        amount = keyboard.nextInt();
        keyboard.nextLine(); // consume the newline
        pen.println("Nonce? ");
        nonce = keyboard.nextLong();
        keyboard.nextLine(); // consume the newline
        chain.append(new Block(num, amount, chain.getHash(), nonce));
      } else if (command.equals("remove")) {
        // attempts to remove. If it retuns false, print "remove ffailed"
        if (!chain.removeLast()) {
          pen.println("remove failed");
        }
      } else if (command.equals("check")) {
        if (chain.isValidBlockChain()) {
          pen.println("Chain is valid!");
        } else {
          pen.println("Chain is INVALID");
        }
      } else if (command.equals("report")) {
        chain.printBalances(pen);
      } else if (command.equals("help")) {
        // print
        pen.println(listOfCommands);
      } else if (command.equals("quit")) {
        // do nothing. the loop will end.
      } else {
        pen.println("Invalid command. Please try again.");
      }

      // print current stats:
      pen.println(chain);

      pen.flush();

    } while (!command.equals("quit"));
  }



}
