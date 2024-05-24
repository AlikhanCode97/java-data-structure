package projectwia1002;

import java.io.Serializable;
import java.security.*;


public class Transaction implements Serializable{
	private static int sequence = 0;
	public String transactionId;
	public PublicKey AccountSender;
	public PublicKey AccountGetter;
	public float value;
	public byte[] signature;
	public Transaction nextTransaction;

	public MyLinkedList<TransactionInput> inputs = new MyLinkedList<>();
	public MyLinkedList<TransactionOutput> outputs = new MyLinkedList<>();
	
	public Transaction(PublicKey sender, PublicKey receiver, float value,  MyLinkedList<TransactionInput> inputs) {
		this.AccountSender = sender;
		this.AccountGetter = receiver;
		this.value = value;
		this.inputs = inputs;


}
	public void generateSignature(PrivateKey privateKey) {
		String input = Support.KeyToString(AccountSender)+ Support.KeyToString(AccountGetter) + Float.toString(value);
		signature = Support.applySignature(privateKey, input);
	}

	public boolean VerifySignature() {
		String input = Support.KeyToString(AccountSender)+ Support.KeyToString(AccountGetter) + Float.toString(value);
        return Support.verifySignature(AccountSender, input, signature);
	}

	public boolean doTransaction() {
		if(!VerifySignature()) {
			System.out.println("Signature couldn't be verified / Ivalid transaction");
			return false;
		}
		for (int i = 0 ; i < inputs.getSize(); i++){
			inputs.get(i).UnspentOutput = BlockChain.MainHashMap.get(inputs.get(i).transactionOutputId);
		}

		if(getTotalInputs() < BlockChain.minTransaction)
		{
			System.out.println("Transaction too small : " + getTotalInputs());
			return false;
		}

		float left =  getTotalInputs() -value;
		transactionId = calculateHash();
		outputs.addLast(new TransactionOutput(AccountGetter,value ,transactionId));
		outputs.addLast(new TransactionOutput(AccountSender ,left,transactionId));

		for (int i = 0 ; i < outputs.getSize(); i++){
			BlockChain.MainHashMap.put(outputs.get(i).id,outputs.get(i));
		}
		for (int i = 0 ; i < inputs.getSize(); i++){
			if(inputs.get(i) == null)continue;
			BlockChain.MainHashMap.remove(inputs.get(i).UnspentOutput.id);
		}
		System.out.println("Transaction is approved. Your remaining balance: " + BlockChain.account.getAccountBalance());
		return true;
	}

	public float getTotalInputs() {
		float total =0;
		for (int i = 0 ; i < inputs.getSize(); i++)
		{
			if(inputs.get(i).UnspentOutput == null)
			{
				continue;
			}
			total = total + inputs.get(i).UnspentOutput.value;
		}
		return total;
	}
	public float getOutputsValue() {
		float total = 0;
		for (int i = 0 ; i < outputs.getSize(); i++){
			total += outputs.get(i).value;
		}
		return total;
}
	public String calculateHash() {
		sequence++; 
		return Support.hashing(
				Support.KeyToString(AccountSender) +
						Support.KeyToString(AccountGetter) +
				Float.toString(value) + sequence
				);
}
	public String getTransactionId() {
		return transactionId;
	}
	public boolean doTransactionNew() {
		if(!VerifySignature()) {

			return false;
		}
		outputs.addLast(new TransactionOutput(AccountGetter,value ,transactionId));
		BlockChain.MainHashMap.put(outputs.get(0).id,outputs.get(0));
		return true;
	}
}





