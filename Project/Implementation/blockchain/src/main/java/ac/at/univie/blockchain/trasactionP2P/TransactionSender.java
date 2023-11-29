package ac.at.univie.blockchain.trasactionP2P;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.json.JSONObject;

import ac.at.univie.blockchain.transactionDB.TransactionNetworkDB;

/**
 * Class for sending transactions.
 */
public class TransactionSender {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private TransactionNetworkDB portDB = TransactionNetworkDB.getInstanceOf();

    public void sendRegistration(List<Integer> portList, JSONObject transactionObject)
            throws UnknownHostException, IOException {
        try {
            for (int currentPort = 301; currentPort < 304; currentPort++) {
                if (portDB.getMyPort() != currentPort) {
                    sendMessage(transactionObject, currentPort);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAllConnections(socket, bufferedWriter);
        }

    }

    public void sendPortsAfterRegistration(Integer port, JSONObject portsList)
            throws UnknownHostException, IOException {
        try {
            sendMessage(portsList, port);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAllConnections(socket, bufferedWriter);
        }

    }

    public void sendListSizeToSenderPort(int port, JSONObject listSize) {
        try {
            sendMessage(listSize, port);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAllConnections(socket, bufferedWriter);
        }
    }

    public void sendMessageToAll(JSONObject transactionObject)
            throws UnknownHostException, IOException {
        try {
            for (int currentPort = 301; currentPort < 304; currentPort++) {
                sendMessage(transactionObject, currentPort);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeAllConnections(socket, bufferedWriter);
        }

    }

    private void sendMessage(JSONObject transactionObject, int currentPort) throws UnknownHostException, IOException {
        this.socket = new Socket("localhost", currentPort);
        this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        bufferedWriter.write(transactionObject.toString());
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private void closeAllConnections(Socket socket, BufferedWriter bufferedWriter) {
        try {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }

            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
