package ac.at.univie.blockchain.blockchain;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import org.json.JSONObject;

import ac.at.univie.blockchain.objects.Block;
import ac.at.univie.blockchain.objects.ItemTransaction;
import ac.at.univie.blockchain.transactionDB.TransactionNetworkDB;
import ac.at.univie.blockchain.transactionDB.TransactionPool;
import ac.at.univie.blockchain.transactionDB.TransactionsDB;
import ac.at.univie.blockchain.trasactionP2P.TransactionSender;

public class CLIHandler {
    private TransactionPool tsPool = TransactionPool.getInstanceOf();
    private TransactionNetworkDB portDB = TransactionNetworkDB.getInstanceOf();
    private TransactionsDB transactionsDB = TransactionsDB.getInstanceOf();
    private Integer myPort = 301;
    private Scanner scanner = new Scanner(System.in);
    private TransactionSender tSender = new TransactionSender();
    private JSONObject obj = new JSONObject();
    Logger logger = Logger.getLogger(CLIHandler.class.getName());

    public void checkForSingleAuthor() {
        String searchedAuthor;
        System.out.println("Insert searched Author");
        searchedAuthor = scanner.next();

        List<ItemTransaction> foundTransactionsList = new LinkedList<>();
        for (Block eachBlock : transactionsDB.getTransactions()) {
            for (ItemTransaction eachTransaction : eachBlock.getItemTransactionsPool())
                if (eachTransaction.getAuthorName().equals(searchedAuthor)) {
                    foundTransactionsList.add(eachTransaction);
                }
        }

        System.out.println("Author name: " + searchedAuthor);
        System.out.println("*************************");
        for (ItemTransaction eachTransaction : foundTransactionsList) {
            System.out.println("Book name: " + eachTransaction.getBookName());
            System.out.println("Book publisching date: " + eachTransaction.getBookAddingDate());
            System.out.println("ISBN: " + eachTransaction.getISBN());
            System.out.println("Added by: " + eachTransaction.getPort());

            System.out.println();
            System.out.println();

        }

    }

    public void printBlockchain() {
        List<Block> itemTransactionsList = transactionsDB.getTransactions();

        for (Block singleBlock : itemTransactionsList) {
            System.out.println("Previous hash: " + singleBlock.getPreviousHash());
            System.out.println("Current hash: " + singleBlock.getHash());
            for (int i = 0; i < singleBlock.getHash().length() + 16; i++) {
                System.out.print("-");
            }
            System.out.println();
            for (ItemTransaction singleTransaction : singleBlock.getItemTransactionsPool()) {
                System.out.println("Author name: " + singleTransaction.getAuthorName());
                System.out.println("Book name: " + singleTransaction.getBookName());
                System.out.println("Book date: " + singleTransaction.getBookAddingDate());
                System.out.println("ISBN: " + singleTransaction.getISBN());
                System.out.println("Added by: " + singleTransaction.getPort());
                System.out.println();
            }
            System.out.println();
        }
    }

    public void addBookToLibrary() throws UnknownHostException, IOException {

        String bookAuthor;
        String bookName;
        String date;
        String ISBN;

        System.out.println("Insert author name: ");
        bookAuthor = scanner.next();
        System.out.println("Insert book name:");
        bookName = scanner.next();
        System.out.println("Insert publishing date in format DD.MM.YYYY:");
        date = scanner.next();
        System.out.println("Insert ISBN:");
        ISBN = scanner.next();

        boolean rc = checkIfAlreadyAdded(ISBN);

        if (!rc) {

            JSONObject transactionObject = new JSONObject();

            transactionObject.put("TypeOfMessage", "transaction");
            transactionObject.put("authorName", bookAuthor);
            transactionObject.put("bookName", bookName);
            transactionObject.put("date", date);
            transactionObject.put("ISBN", ISBN);

            transactionObject.put("port", portDB.getMyPort());

            tSender.sendMessageToAll(transactionObject);
        } else
            logger.info("Book with same ISBN already added.");

    }

    public boolean checkIfAlreadyAdded(String ISBN) {
        boolean foundInBlockchain = false, foundInPool = false, rc = false;
        List<Block> blockList = transactionsDB.getTransactions();
        for (Block eachBlock : blockList) {
            for (ItemTransaction eachPool : eachBlock.getItemTransactionsPool()) {
                if (eachPool.getISBN().equals(ISBN)) {
                    foundInBlockchain = true;
                }
            }
        }
        for (ItemTransaction eachTransaction : tsPool.getItemTransaction()) {
            if (eachTransaction.getISBN().equals(ISBN)) {
                foundInBlockchain = true;
            }
        }
        if (foundInBlockchain || foundInPool) {
            rc = true;
        }

        return rc;
    }

    public void registerNewPeer() throws UnknownHostException, IOException {
        portDB.setMyPort(myPort);
        portDB.addPortToDB(myPort);
        obj.put("TypeOfMessage", "reg");
        obj.put("port", myPort);
        tSender.sendRegistration(portDB.getConnectedPortsList(), obj);
    }

}
