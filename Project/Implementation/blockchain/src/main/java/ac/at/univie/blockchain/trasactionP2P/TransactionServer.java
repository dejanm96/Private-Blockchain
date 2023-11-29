package ac.at.univie.blockchain.trasactionP2P;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ac.at.univie.blockchain.objects.Block;
import ac.at.univie.blockchain.objects.ItemTransaction;
import ac.at.univie.blockchain.transactionDB.TransactionNetworkDB;
import ac.at.univie.blockchain.transactionDB.TransactionPool;
import ac.at.univie.blockchain.transactionDB.TransactionsDB;

/**
 * In this class all the messages will be accepted
 * and the kind of message thats gotten will be handeled
 */
public class TransactionServer extends Thread {

    private Socket socket;
    private ServerSocket serverSocket;
    private BufferedReader bufferedReader;
    private TransactionSender tsender = new TransactionSender();
    private boolean alreadySent = false;

    private TransactionNetworkDB transactionNetworkDB = TransactionNetworkDB.getInstanceOf();
    private TransactionPool transactionPool = TransactionPool.getInstanceOf();
    private TransactionsDB transactionsDB = TransactionsDB.getInstanceOf();

    private static final String TYPE_OF_MESSAGE = "TypeOfMessage";

    Logger logger = Logger.getLogger(TransactionServer.class.getName());

    public TransactionServer(Integer serverPort) {
        try {
            this.serverSocket = new ServerSocket(serverPort);
            logger.info("Socket with port: " + serverPort + " opened.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeEverything(Socket socket, ServerSocket serverSocket, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }

            if (socket != null) {
                socket.close();
            }

            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String clientMessage;

        try {
            while (true) {
                if (this.serverSocket != (null)) {
                    socket = this.serverSocket.accept();
                    this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    clientMessage = bufferedReader.readLine();
                    JSONObject obj = new JSONObject(clientMessage);

                    String typeOfMessage = obj.get(TYPE_OF_MESSAGE).toString();

                    switch (typeOfMessage) {
                        case "reg":
                            handleRegistration(obj);
                            break;
                        case "lis":
                            handlePortList(obj);
                            break;
                        case "transaction":
                            handleTransactions(obj);
                            break;
                        case "listSize":
                            handleListSize(obj);
                            break;
                        case "luckyPort":
                            handleLuckyPort(obj);
                            break;
                        case "blockchain":
                            handleBlockchain(obj);
                            break;
                        default:
                            System.out.println("wrong message type");
                    }

                }
            }
        } catch (

        IOException e) {
            closeEverything(socket, serverSocket, bufferedReader);
        }
    }

    private void handleBlockchain(JSONObject obj) throws JSONException {
        String authorName;
        String bookName;
        String bookAddingDate;
        String ISBN;
        String port;

        String hash;
        String previousHash;

        JSONArray itemTransactionObjects = obj.getJSONArray("blockchain");

        Block block = new Block();

        for (int i = 0; i < itemTransactionObjects.length(); i++) {
            List<ItemTransaction> transactionItems = new LinkedList<>();
            JSONObject object = itemTransactionObjects.getJSONObject(i);
            hash = object.get("hash").toString();
            previousHash = object.get("previousHash").toString();

            JSONArray itemPool = object.getJSONArray("itemTransactionsPool");
            for (int j = 0; j < itemPool.length(); j++) {

                ItemTransaction transaction = new ItemTransaction();
                JSONObject objectInner = itemPool.getJSONObject(j);

                authorName = objectInner.get("authorName").toString();
                bookName = objectInner.get("bookName").toString();
                bookAddingDate = objectInner.get("bookAddingDate").toString();
                ISBN = objectInner.get("ISBN").toString();
                port = objectInner.get("port").toString();

                transaction.setAuthorName(authorName);
                transaction.setBookAddingDate(bookAddingDate);
                transaction.setBookName(bookName);
                transaction.setISBN(ISBN);
                transaction.setPort(port);

                transactionItems.add(transaction);

            }

            block.setHash(hash);
            block.setPreviousHash(previousHash);
            block.setItemTransactionPool(transactionItems);

        }
        logger.info("New block has been added to blockchain!");
        transactionsDB.addBlockToBlockchain(block);
    }

    private void handleLuckyPort(JSONObject obj) throws JSONException, UnknownHostException, IOException {
        alreadySent = false;
        if (obj.get("luckyPort").equals(transactionNetworkDB.getMyPort())) {

            Block block = new Block(transactionPool.getItemTransaction());

            transactionsDB.addBlockToBlockchain(block);
            JSONObject blockchainToSend = new JSONObject();
            blockchainToSend.put(TYPE_OF_MESSAGE, "blockchain");
            blockchainToSend.put("blockchain", transactionsDB.getTransactions());

            tsender.sendMessageToAll(blockchainToSend);

            if (transactionsDB.getTransactions().contains(block))
                transactionsDB.deleteABlock(block);

            transactionPool.clearPool();
        } else {
            transactionPool.clearPool();

        }
    }

    private void handleListSize(JSONObject obj) throws JSONException, UnknownHostException, IOException {
        Integer listeSize = Integer.parseInt(obj.get("size").toString());
        if (listeSize == 4 && alreadySent == false) {
            int smallest = Collections.min(transactionNetworkDB.getConnectedPortsList());
            int biggest = Collections.max(transactionNetworkDB.getConnectedPortsList());

            int randomPort = (int) Math.floor(Math.random() * (biggest - smallest + 1) + smallest);

            JSONObject randomNumberMessage = new JSONObject();
            randomNumberMessage.put(TYPE_OF_MESSAGE, "luckyPort");
            randomNumberMessage.put("luckyPort", randomPort);

            tsender.sendMessageToAll(randomNumberMessage);
            alreadySent = true;
        }
    }

    private void handleTransactions(JSONObject obj) throws JSONException {
        String authorName = obj.get("authorName").toString();
        String bookName = obj.get("bookName").toString();
        String date = obj.get("date").toString();
        String ISBN = obj.get("ISBN").toString();
        String port = obj.get("port").toString();

        ItemTransaction transaction = new ItemTransaction(authorName, bookName, date, ISBN);
        transaction.setPort(port);
        logger.info("New Book added to the list");
        transactionPool.addToTransactionPool(transaction);

        JSONObject transactionPoolSizeReplay = new JSONObject();
        transactionPoolSizeReplay.put("TypeOfMessage", "listSize");
        transactionPoolSizeReplay.put("size", transactionPool.getItemTransaction().size());

        tsender.sendListSizeToSenderPort(Integer.parseInt(obj.get("port").toString()),
                transactionPoolSizeReplay);
    }

    private void handlePortList(JSONObject obj) throws JSONException {
        JSONArray newSetOfPorts = obj.getJSONArray("portList");
        List<Integer> portSetToAdd = new LinkedList<>();
        for (Object current : newSetOfPorts) {
            portSetToAdd.add((Integer) current);
        }
        transactionNetworkDB.addAllConnections(portSetToAdd);
    }

    private void handleRegistration(JSONObject obj) throws JSONException, UnknownHostException, IOException {
        String port = obj.get("port").toString();
        Integer newRegisteredPort = Integer.parseInt(port);
        Integer addingValue = transactionNetworkDB.addPortToDB(newRegisteredPort);

        if (addingValue == 1) {
            logger.info("New Peer connected with port: " + port + "!");
        } else
            logger.info("Peer with port: " + port + " is already connected!");

        if (transactionNetworkDB.getConnectedPortsList().size() >= 1) {
            obj.put(TYPE_OF_MESSAGE, "lis");
            obj.put("portList", transactionNetworkDB.getConnectedPortsList());
            tsender.sendPortsAfterRegistration(newRegisteredPort, obj);
        }
    }

}