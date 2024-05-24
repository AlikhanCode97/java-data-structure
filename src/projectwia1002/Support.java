package projectwia1002;

import java.io.*;
import java.security.MessageDigest;
import java.util.Base64;
import java.security.*;

public class Support {

    public static String CurrentPrivateKeyValue;
    public static String CurrentPublicKeyValue;

    public static String hashing(String dataToHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(dataToHash.getBytes("UTF-8"));
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xff & bytes[i]);
                if (hex.length() == 1) {
                    buffer.append('0');
                }
                buffer.append(hex);
            }
            return buffer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] applySignature(PrivateKey privateKey, String input) {
        Signature dsa;
        byte[] output;
        try {
            dsa = Signature.getInstance("DSA", "SUN");
            dsa.initSign(privateKey);
            byte[] bytes = input.getBytes();
            dsa.update(bytes);
            output = dsa.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    public static boolean verifySignature(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature verify = Signature.getInstance("DSA", "SUN");
            verify.initVerify(publicKey);
            verify.update(data.getBytes());
            return verify.verify(signature);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String KeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static boolean isSuchKey(String key) {
        try {
            for (int i = 0; i < BlockChain.myLinkedList.getSize(); i++) {
                if (BlockChain.myLinkedList.get(i).publicKeyString.equals(key)) {
                    BlockChain.account.publicKey = BlockChain.myLinkedList.get(i).publicKey;
                    BlockChain.account.privateKey = BlockChain.myLinkedList.get(i).privateKey;
                    BlockChain.account.publicKeyString = BlockChain.myLinkedList.get(i).publicKeyString;
                    return true;
                }
            }
        } catch (Exception e) {

        }
        return false;
    }

    public static PublicKey isSuchGetter(String key) {
        try {
            FileInputStream fileStream = new FileInputStream("key.txt");
            ObjectInputStream objStream = new ObjectInputStream(fileStream);

            PublicKey temp = null;
            BlockChain.myLinkedList = (MyLinkedList<Account>) objStream.readObject();
            for (int i = 0; i < BlockChain.myLinkedList.getSize(); i++) {
                if (BlockChain.myLinkedList.get(i).publicKeyString.equals(key)) {
                    temp = BlockChain.myLinkedList.get(i).publicKey;
                    return temp;
                }
            }
        } catch (Exception e) {
            new IOException();
            new ClassCastException();
        }
        return null;
    }

    public static String makeMerkelTree(MyLinkedList<Transaction> transactionList) {
        int length = transactionList.getSize();

        MyLinkedList<String> currentLayer = new MyLinkedList<>();
        for (int i = 0; i < transactionList.getSize(); i++) {
            currentLayer.addLast(transactionList.get(i).transactionId);
        }
        while (length > 1) {
            MyLinkedList<String> nextLayer = new MyLinkedList<>();
            for (int i = 1; i < length; i++) {
                nextLayer.addLast(hashing(currentLayer.get(i - 1) + currentLayer.get(i)));
            }
            currentLayer = nextLayer;
            length = nextLayer.getSize();

        }
        String merkleRoot = (currentLayer.getSize() == 1) ? currentLayer.get(0) : "";
        return merkleRoot;
    }

    public static void saveAccountData(Account account) {
        try {
            BlockChain.myLinkedList.addLast(account);
            FileOutputStream key = new FileOutputStream("key.txt");
            ObjectOutputStream keyStream = new ObjectOutputStream(key);
            keyStream.writeObject(BlockChain.myLinkedList);
            keyStream.close();
        } catch (Exception e) {
            new IOException();
        }
    }

    public static void SaveChain() {
        try {
            FileOutputStream key = new FileOutputStream("blocks.txt");
            ObjectOutputStream keyStream = new ObjectOutputStream(key);
            keyStream.writeObject(BlockChain.blockchain);
            keyStream.close();
        } catch (Exception e) {
            new IOException();
        }

    }

    public static void SaveWallet() {
        try {
            FileOutputStream key = new FileOutputStream("wallet.txt");
            ObjectOutputStream keyStream = new ObjectOutputStream(key);
            keyStream.writeObject(BlockChain.MainHashMap);
            keyStream.close();
        } catch (Exception e) {
            new IOException();
        }

    }
}
