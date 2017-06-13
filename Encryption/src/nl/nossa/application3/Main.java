package nl.nossa.application3;

/**
 *
 * @author Raymond
 */
public class Main {

    private static final String SIGNED_FILE_NAME = "contents_Signed by Raymond.txt";

    public static void main(String[] args) {
        SignedChecker signedChecker = new SignedChecker();
        boolean isVerified = signedChecker.checkIfSigned(SIGNED_FILE_NAME);
        System.out.println(isVerified);
    }

}
