package projectwia1002;

import java.io.*;
import java.security.*;
import java.util.HashMap;

public class Account implements Serializable {

    public static int accNumber;
    public PrivateKey privateKey;
    public PublicKey publicKey;
    public String publicKeyString;
    public Account nextAccount;
    public HashMap<String, TransactionOutput> OutputMap = new HashMap<String, TransactionOutput>();

    public Account() {
        accNumber++;
        generateKey();
    }

    public Account(String empty) {
    }

    public Account(PrivateKey privateKey, PublicKey publicKey, String publicKeyString) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.publicKeyString = publicKeyString;

    }

    public void generateKey() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
            publicKeyString = "ALICoin" + accNumber;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float getAccountBalance() {

        float total = 0;
        for(int i = 0 ; i < BlockChain.MainHashMap.size(); i++){
            TransactionOutput Output = BlockChain.MainHashMap.entrySet().get(i).getValue();
            if (Output.receipientKey.equals(publicKey)) {
                this.OutputMap.put(Output.id, Output);
                total += Output.value;
            }
        }
        return total;
    }

    public Transaction doTransaction(PublicKey getterKey, float value) {

        if (getAccountBalance() < value) {
            System.out.println("Account Balance is Not enough");
            return null;
        }

        MyLinkedList<TransactionInput> inputs = new MyLinkedList<>();

        float total = 0;
        for (HashMap.Entry<String, TransactionOutput> item : OutputMap.entrySet()) {
            TransactionOutput Output = item.getValue();
            total += Output.value;
            inputs.addLast(new TransactionInput(Output.id));
            if (total > value) {
                break;
            }
        }

        Transaction newTransaction = new Transaction(publicKey, getterKey, value, inputs);
        newTransaction.generateSignature(privateKey);

        for (int i = 0; i < inputs.getSize(); i++) {
            OutputMap.remove(inputs.get(i).transactionOutputId);
        }
        return newTransaction;
    }

    public Transaction doTransactionNew(PublicKey getterKey, float value) {

        Transaction newTransaction = new Transaction(publicKey, publicKey, value, null);
        newTransaction.generateSignature(privateKey);

        return newTransaction;
    }

    public static int getAccNumber() {
        return accNumber;
    }

    @Override
    public String toString() {
        String string = "1.Public key:" + publicKeyString + "  2. PrivateKey: " + Support.KeyToString(privateKey);
        return string;
    }
}
