package projectwia1002;

import java.io.Serializable;
import java.util.Date;

public class Block implements Serializable {

    public String hash;
    public String prevHash;
    public String data;
    public long timeStamp;
    private int nonce;
    public MyLinkedList<Transaction> tList = new MyLinkedList<>();
    public Block nextBlock;

    public Block(String prevHash) {
        this.prevHash = prevHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return Support.hashing(this.prevHash + this.data + Integer.toString(nonce) + Long.toString(this.timeStamp));
    }

    public void mineBlock(int difficulty) {
        data = Support.makeMerkelTree(tList);
        nonce = 0;
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }

    public boolean addTransactions(Transaction transaction) {
        if (transaction == null) {
            return false;
        }
        if ((transaction.doTransaction() != true)) {
            System.out.println("Transaction failed ");
            return false;
        }
        tList.addLast(transaction);
        return true;
    }

    public boolean addTransactionsNew(Transaction transaction) {
        if (transaction == null) {
            return false;
        }

        if ((transaction.doTransactionNew() != true)) {
            System.out.println("Transaction failed ");
            return false;
        }
        tList.addLast(transaction);
        return true;
    }

}
