package ac.at.univie.blockchain.merkleTree;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ac.at.univie.blockchain.objects.ItemTransaction;
import ac.at.univie.blockchain.transactionDB.TransactionsDB;

//This class has been used from the page https://www.pranaybathini.com/2021/05/merkle-tree.html
public class MerkleTree {

    private TransactionsDB transactionsDB = TransactionsDB.getInstanceOf();
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private HashGenerator generator = new HashGenerator();

    public Node generateTree(List<ItemTransaction> dataBlocks) {
        ArrayList<Node> childNodes = new ArrayList<>();

        String previousHash;

        Integer size = transactionsDB.getTransactions().size();

        if (transactionsDB.getTransactions().size() == 0) {
            previousHash = "000000000000000000000000000000000000";
        } else
            previousHash = transactionsDB.getTransactions().get(size - 1).toString();

        String timeStamp = dtf.format(LocalDateTime.now()).toString();

        for (ItemTransaction transaction : dataBlocks) {
            childNodes
                    .add(new Node(null, null,
                            generator.generateHash(previousHash + timeStamp + transaction.toString())));
        }

        return buildTree(childNodes);
    }

    private Node buildTree(ArrayList<Node> children) {
        ArrayList<Node> parents = new ArrayList<>();

        while (children.size() != 1) {
            int index = 0, length = children.size();
            while (index < length) {
                Node leftChild = children.get(index);
                Node rightChild = null;

                if ((index + 1) < length) {
                    rightChild = children.get(index + 1);
                } else {
                    rightChild = new Node(null, null, leftChild.getHash());
                }

                String parentHash = generator.generateHash(leftChild.getHash() + rightChild.getHash());
                parents.add(new Node(leftChild, rightChild, parentHash));
                index += 2;
            }
            children = parents;
            parents = new ArrayList<>();
        }
        return children.get(0);
    }

    public String printLevelOrderTraversal(Node root) {
        String stringToReturn = " ";
        if ((root.getLeft() == null && root.getRight() == null)) {
            stringToReturn = root.getHash();
        }
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        queue.add(null);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node != null) {
                stringToReturn = root.getHash();
                break;
            } else {
                System.out.println();
                if (!queue.isEmpty()) {
                    queue.add(null);
                }
            }

        }

        return stringToReturn;
    }

}
