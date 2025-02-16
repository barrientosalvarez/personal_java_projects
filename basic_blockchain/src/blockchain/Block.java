
package blockchain;

import java.util.Date;


public class Block {
    public String hash;
    public String previousHash;
    private String data;
    private long timeStamp;
    private int nonce;

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        
        this.hash = this.calculatedHash();
    }

    public String calculatedHash() {
        String calculatedHash = StringUtil.apllySha256(
                    previousHash +
                    Long.toString(timeStamp) +
                    Integer.toString(nonce) +
                    data
                );

        return calculatedHash;
    }

    public void mineBlock(int difficulty) {
        String target = StringUtil.getDifficultyString(difficulty);
        while(!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }

        System.out.println("Block Mined!!! : " + hash);

    }
}
