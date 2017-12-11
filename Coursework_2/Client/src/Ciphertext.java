import java.math.BigInteger;

public class Ciphertext {

    public String decode(String s, BigInteger secret_full){


        // Take the FIRST TWO bytes of the full 128bit secret key. This is used as a key for ECS.
        int secret_short = ((secret_full.toByteArray()[0] & 0xff) << 8) | (secret_full.toByteArray()[1] & 0xff);
        Logger.Log(Logger.Level.INFO, "Using secret key " + secret_short + " to decrypt ciphertext.");

        // Calculate the subsitution and transpose shifts.
        int sub_shift = secret_short % 26;
        int transpose_shift = secret_short % 8;
        Logger.Log("Substitution shift amount: " + sub_shift);
        Logger.Log("Transpose shift amount: " + transpose_shift);

        // To decode the process is -> sub -> sub -> transpose -> transpose = plaintext + padding.
        String sub1 = substitution(s, -sub_shift);
        String sub2 = substitution(sub1, -sub_shift);
        String transpose1 = transpose(sub2, -transpose_shift);
        String transpose2 = transpose(transpose1, -transpose_shift);


        // Trim trailing ZZZZZ's
        for(int i = transpose2.length()-1; i > 0; i--){
            if(transpose2.charAt(i)=='Z'){
                transpose2 = transpose2.substring(0, i);
            } else {
                break;
            }
        }

        Logger.Log("Final ciphertext generated!");
        Logger.Log(transpose2);
        return sub2;
    }


    // For each char in the string, shift by sub_shift.
    private String substitution(String s, int sub_shift){
        StringBuilder sb = new StringBuilder();
        for(char c : s.toCharArray()){
            sb.append(sub_shift(c, sub_shift));
        }
        return sb.toString();
    }


    // Shift an individual char (c) by shift places.
    // Should be compatiable with +- values of shift, to allow encode implementation later.
    private char sub_shift(char c, int shift){
        int char_int = (int)c;
        int new_int = char_int + shift;
        if(new_int > 90){
            new_int = 64 + (new_int - 90);
        }

        if(new_int < 65){
            new_int = 90 - (64 - new_int);
        }
        char out = (char)new_int;
        return out;
    }


    private String transpose(String s, int shift_amount){
        StringBuilder sb = new StringBuilder();
        for(String in : s.split("(?<=\\G.{8})")){
            char[] string = in.toCharArray();
            char[] out = new char[8];
            for(int i = 0; i < 8; i++){
                int new_pos = i + shift_amount;
                if(new_pos < 0) { new_pos = 8 + new_pos; }
                if(new_pos >= 8) {new_pos = new_pos % 8;}
                out[new_pos] = string[i];
            }
            sb.append(out);
        }
        return sb.toString();
    }

}
