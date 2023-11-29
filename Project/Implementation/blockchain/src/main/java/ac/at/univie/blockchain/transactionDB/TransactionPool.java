package ac.at.univie.blockchain.transactionDB;

import java.util.ArrayList;
import java.util.List;

import ac.at.univie.blockchain.objects.ItemTransaction;

public class TransactionPool {
    private static TransactionPool tsPool = new TransactionPool();
    private List<ItemTransaction> itemTransactionsPool = new ArrayList<>();

    private TransactionPool() {
    }

    public static TransactionPool getInstanceOf() {
        return tsPool;
    }

    public void addToTransactionPool(ItemTransaction itemTransaction) {
        if (this.itemTransactionsPool != null)
            this.itemTransactionsPool.add(itemTransaction);

    }

    public void clearPool() {
        itemTransactionsPool.clear();
    }

    public List<ItemTransaction> getItemTransaction() {
        return this.itemTransactionsPool;
    }
}
