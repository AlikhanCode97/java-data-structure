/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projectwia1002;

import java.io.Serializable;

/**
 *
 * @author Alikhan
 */
public class TransactionInput implements Serializable {

    public String transactionOutputId;
    public TransactionOutput UnspentOutput;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;

    }

}
