package ac.at.univie.blockchain.objects;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ac.at.univie.blockchain.merkleTree.MerkleTree;
import ac.at.univie.blockchain.merkleTree.Node;
import ac.at.univie.blockchain.transactionDB.TransactionsDB;

/**
 * The block is the base object of the blockchain. It contains
 * hash, previous hash, data to be mined on the blockchain, the
 * .
 */
public class Block {
    private String hash;
    private String previousHash;
    private List<ItemTransaction> itemTransactionsPool;
    private MerkleTree mTree = new MerkleTree();
    private TransactionsDB transactionsDB = TransactionsDB.getInstanceOf();

    private static final Logger logger = LogManager.getLogger(Block.class);

    public Block() {
    }

    public Block(List<ItemTransaction> itemTransactionsPool) {
        Node root = mTree.generateTree(itemTransactionsPool);
        this.itemTransactionsPool = itemTransactionsPool;
        Integer size = transactionsDB.getTransactions().size();
        if (transactionsDB.getTransactions().size() == 0) {
            this.previousHash = "00000000000000000000000000000000000000000000000000000000000000000";
        } else
            this.previousHash = transactionsDB.getTransactions().get(size - 1).getHash();
        this.hash = mTree.printLevelOrderTraversal(root);
        logger.info("Block has been created with hash code: " + hash);

        // this.hash = runMerkleTree(itemTransactionsPool);
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    
    public List<ItemTransaction> getItemTransactionsPool() {
        return itemTransactionsPool;
    }

    public void setItemTransactionPool(List<ItemTransaction> tList) {
        this.itemTransactionsPool = tList;
    }

    public void addToTransactionPool(ItemTransaction transaction) {
        this.itemTransactionsPool.add(transaction);
    }

}
