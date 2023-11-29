package ac.at.univie.blockchain.transactionDB;

import java.util.LinkedList;
import java.util.List;

import ac.at.univie.blockchain.objects.Block;

/**
 * Actual Blockchain where the Blocks will be stored.
 * When the list gets to 5 transactions, they will be then
 * mined on the blockchain.
 * This is the Database of the Blockchain.
 */
public class TransactionsDB {

    private static TransactionsDB transactionsDB = new TransactionsDB();

    private List<Block> transactions = new LinkedList<>();

    private TransactionsDB() {
    }

    public static TransactionsDB getInstanceOf() {
        return transactionsDB;
    }

    public void addBlockToBlockchain(Block block) {
        transactions.add(block);
    }

    public List<Block> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Block> transactions) {
        this.transactions = transactions;
    }

    public void deleteABlock(Block block) {
        transactions.remove(block);
    }

}
