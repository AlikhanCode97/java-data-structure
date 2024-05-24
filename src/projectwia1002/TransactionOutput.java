/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectwia1002;

import java.io.Serializable;
import java.security.PublicKey;

/**
 *
 * @author Alikhan
 */
public class TransactionOutput implements Serializable {

    public String transactionId;
    public float value;
    public PublicKey receipientKey;
    String id;

    public TransactionOutput(PublicKey receipient, float value, String transactionId) {
        this.receipientKey = receipient;
        this.value = value;
        this.transactionId = transactionId;
        this.id = Support.hashing(Support.KeyToString(receipientKey) + Float.toString(value) + transactionId);

    }

    public boolean isMine(PublicKey publicKey) {
        return publicKey.equals(receipientKey);
    }

}
