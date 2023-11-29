package ac.at.univie.blockchain.transactionDB;

import java.util.LinkedList;
import java.util.List;

/**
 * All registered ports are stored in the TransactionNetworkDB.
 * User can register/add port to the DB or get the whole list
 * of connected ports.
 */
public class TransactionNetworkDB {

    private static TransactionNetworkDB transactionNetworkDB = new TransactionNetworkDB();

    private List<Integer> connectedPortsList = new LinkedList<>();
    private Integer myPort;

    private TransactionNetworkDB() {
    }

    public static TransactionNetworkDB getInstanceOf() {
        return transactionNetworkDB;
    }

    public Integer addPortToDB(Integer port) {
        Integer returnValue = 0;
        if (port != null && !connectedPortsList.contains(port)) {
            this.connectedPortsList.add(port);
            returnValue = 1;
        }
        return returnValue;
    }

    public List<Integer> getConnectedPortsList() {
        return connectedPortsList;
    }

    public void addAllConnections(List<Integer> wholeList) {
        this.connectedPortsList = wholeList;
    }

    public void setMyPort(Integer myPort) {
        this.myPort = myPort;
    }

    public Integer getMyPort() {
        return this.myPort;
    }

}
