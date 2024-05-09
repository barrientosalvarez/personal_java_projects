package blockchain;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public HashMap<String, TransactionOutput> UTX0s = new HashMap<>();

    public Wallet() {
        generateKeyPair();
    }


    public void generateKeyPair() {
        try{
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initializa(ecSpec, random);

            KeyPair keyPair = keyGen.generateKeyPair();
            publicKey = keyPair.getPublic();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public float getBalence() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: Blockchain.UTX0s.entrySet()) {
            TransactionOutput UTX0 = item.getValue();
            if (UTX0.isMine(publicKey)){
                UTX0s.put(UTX0.id, UTX0);
                total += UTX0.value;
            }
        }
        return total;
    }

    public Transaction sendFunds(PublicKey _recipient, float value) {
        if (getBalance() < value){
            System.out.println("#Not Enough founds to send transaction. Transaction Discarted");
        }

        ArrayList<TransactionOutput> inputs = new ArrayList<>();
        float total = 0;

        for (Map.Entry<String, TransactionOutput> item: UTX0s.entrySet()) {
            TransactionOutput UTX0 = item.getValue();
            total += UTX0.value;
            inputs.add(new TransactionInput(UTX0.id));
            if (total > value)
                break;
        }

        Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        for(TransactionInput input: inputs){
            UTX0s.remove(input.transactionOutputId);
        }

        return newTransaction;
    }
}
