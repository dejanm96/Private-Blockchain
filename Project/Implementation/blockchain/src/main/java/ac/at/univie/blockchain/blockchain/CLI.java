package ac.at.univie.blockchain.blockchain;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Logger;

import ac.at.univie.blockchain.trasactionP2P.TransactionServer;

/**
 * CLI where user can register peers, add books,
 * check a single book in library or print the blockcahin
 */
public class CLI {
    private Integer userAnswer = -1;
    private Scanner scanner = new Scanner(System.in);
    private Integer myPort = 301;
    private TransactionServer tServer = new TransactionServer(myPort);
    private CLIHandler cliHandler = new CLIHandler();

    Logger logger = Logger.getLogger(CLI.class.getName());

    public CLI() {
        tServer.start();
    }

    public void startCLI() throws UnknownHostException, IOException {

        while (userAnswer != 0) {
            System.out.println("Choose the action you want to execute:");
            System.out.println("1. Register new Peer");
            System.out.println("2. Add new Book to Library");
            System.out.println("3. Print the Blockchain");
            System.out.println("4. Check the selling of a single Book in all libraries");
            System.out.println("To close choose 0");

            this.userAnswer = scanner.nextInt();

            switch (this.userAnswer) {
                case 1:
                    cliHandler.registerNewPeer();
                    break;
                case 2:
                    cliHandler.addBookToLibrary();
                    break;
                case 3:
                    cliHandler.printBlockchain();
                    break;
                case 4:
                    cliHandler.checkForSingleAuthor();
                    break;
                case 0:
                    System.exit(1);
                    break;
                default:
                    System.out.println("Wrong user Input(Try 1, 2, 3 or 4)");
                    break;
            }
        }

    }

}
