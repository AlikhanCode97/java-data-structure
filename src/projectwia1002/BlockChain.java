package projectwia1002;

import java.io.*;
import java.security.PublicKey;
import java.util.Scanner;
import static java.lang.System.exit;

public class BlockChain {

    public static Scanner scanner = new Scanner(System.in);
    public static MyLinkedList<Block> blockchain = new MyLinkedList<>();
    public static HashMap<String, TransactionOutput> MainHashMap = new HashMap<String, TransactionOutput>();
    public static float minTransaction = 0.1f;
    public static Account account = new Account("");
    public static String hashReg = "0";
    private static int difficulty = 1;
    public static Block block = new Block(hashReg);
    public static MyLinkedList<Account> myLinkedList = new MyLinkedList<>();

    public static void main(String args[]) throws IOException, ClassNotFoundException {
        GetWalletValues();
        while (true) {
            GettingAllAccounts();
            mainLobby();
        }

    }

    public static void mainLobby() {
        System.out.println("ALICash.C0M");
        System.out.println("1.Register\n2.Send Money (whole numbers)\n3.Balance enquiry\n4.Quit");
        int entry = scanner.nextInt();
        switch (entry) {
            case 1:
                register(account = new Account());
                break;

            case 2:
                send(hashReg);
                break;

            case 3:
                getBalance();
                break;
            case 4:
                Support.SaveWallet();
                if (isChainValid()) {
                    addBlock(block);
                    Support.SaveChain();
                }
                System.out.println("Data saved");
                exit(0);
            default:
                System.out.println("wrong");
                break;
        }
    }

    public static void register(Account account) {

        block.addTransactionsNew(account.doTransactionNew(account.publicKey, 1000));

        Support.saveAccountData(account);
        System.out.println("Generated public key and private key. If you loose your private key then you will loose the access for your coinbase.");
        System.out.println("Your public key:" + account.publicKeyString);
        System.out.println("Your private key: " + Support.KeyToString(account.privateKey));
        System.out.println("Your Balance: " + account.getAccountBalance());
        System.out.println();
        System.out.println();
    }

    public static void send(String previous) {
        System.out.println("Enter your public key");
        String Key = scanner.next();
        if (Support.isSuchKey(Key)) {
            System.out.println("Enter your private key :");
            Key = scanner.next();
            Support.CurrentPrivateKeyValue = Support.KeyToString(account.privateKey);
            if (Key.equals(Support.CurrentPrivateKeyValue)) {

                System.out.println("The credentials are correct.");
                System.out.println("Your balance :" + account.getAccountBalance() + " aliCoin");
                System.out.println("Enter amount you want to send :");
                float amount = scanner.nextFloat();
                System.out.println("Enter receiver's public key");
                Key = scanner.next();
                PublicKey publicKeyTo = null;
                if ((publicKeyTo = Support.isSuchGetter(Key)) != null) {
                    block.addTransactions(account.doTransaction(publicKeyTo, amount));
                } else {
                    System.out.println("no such receiver");
                }
                System.out.println();

            } else {
                System.out.println("Not correct private credentials");
                System.out.println();
            }

        } else {
            System.out.println("No such public key.");
        }
    }

    private static void getBalance() {
        System.out.println("Enter your public key");
        String Key = scanner.next();
        if (Support.isSuchKey(Key)) {
            System.out.println("Your balance :" + account.getAccountBalance() + " aliCoin");
            System.out.println();
        } else {
            System.out.println("There is no such key.");
        }
    }

    //subCommands
    public static void GettingAllAccounts() throws IOException, ClassNotFoundException {

        File newFile = new File("key.txt");
        if (newFile.length() != 0) {
            FileInputStream fileStream = new FileInputStream(newFile);
            ObjectInputStream input = new ObjectInputStream(fileStream);
            myLinkedList = (MyLinkedList) input.readObject();
            Account.accNumber = myLinkedList.getSize();
            input.close();
        }
    }

    public static void GetWalletValues() throws IOException, ClassNotFoundException {

        File newFile = new File("Wallet.txt");
        if (newFile.length() != 0) {
            FileInputStream fileStream = new FileInputStream(newFile);
            ObjectInputStream input = new ObjectInputStream(fileStream);
            MainHashMap = (HashMap<String, TransactionOutput>) input.readObject();
            input.close();
        }
    }

    public static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for (int i = 1; i < blockchain.getSize(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);

            // Validating if hash is computed correctly for current block
            String hash = currentBlock.calculateHash();
            if (!currentBlock.hash.equals(hash)) {
                System.out.println("hash is computed incorrectly");
                return false;
            }

            // Validating if previousblock hash matches
            if (!previousBlock.hash.equals(currentBlock.prevHash)) {
                System.out.println("previous block hash doesn't match");
                return false;
            }

            // Validate if block is mined correctly
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }

    public static void addBlock(Block newBlock) {
        newBlock.mineBlock(difficulty);
        blockchain.addLast(newBlock);

    }

}
