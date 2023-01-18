package huffman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class contains methods which, when used together, perform the
 * entire Huffman Coding encoding and decoding process
 *
 * @author Ishaan Ivaturi
 * @author Prince Rawal
 */
public class HuffmanCoding {
    private String fileName;
    private ArrayList<CharFreq> sortedCharFreqList;
    private TreeNode huffmanRoot;
    private String[] encodings;

    /**
     * Constructor used by the driver, sets filename
     * DO NOT EDIT
     * @param f The file we want to encode
     */
    public HuffmanCoding(String f) {
        fileName = f;
    }

    /**
     * Reads from filename character by character, and sets sortedCharFreqList
     * to a new ArrayList of CharFreq objects with frequency > 0, sorted by frequency
     */
    public void makeSortedList() {
        /* Your code goes here */
        sortedCharFreqList = new ArrayList();
        StdIn.setFile(fileName);

        int[] jacobIsCool = new int[128];
        int character_count=0;
        if (StdIn.hasNextChar()) {
            do {
                char index = StdIn.readChar();
                ++jacobIsCool[index];
                ++character_count;
            } while (StdIn.hasNextChar());
        }
        char i=(char)0;
        if (i < jacobIsCool.length) {
            do {
                if (jacobIsCool[i] != 0) {
                    CharFreq charFrequeuncy = new CharFreq(i, (double) jacobIsCool[i] / character_count);
                    sortedCharFreqList.add(charFrequeuncy);
                }
                i++;
            } while (i < jacobIsCool.length);
        }
        if (sortedCharFreqList.size() != 1) {
        } else {
            CharFreq fix = new CharFreq((char)((sortedCharFreqList.get(0).getCharacter()+1)%128), 0.0);
            sortedCharFreqList.add(fix);
        }
        Collections.sort(sortedCharFreqList);
    }





    /**
     * Uses sortedCharFreqList to build a huffman coding tree, and stores its root
     * in huffmanRoot
     */
    private static TreeNode dequeue_smallest(Queue<TreeNode> q1, Queue<TreeNode> q2){
        TreeNode one = null;
        TreeNode two = null;
        if(!q1.isEmpty()){
            one = q1.peek();
        }
        if(!q2.isEmpty()){
            two = q2.peek();
        }
        TreeNode result;
        if (one != null && two != null){
            if (one.getData().getProbOcc()<=two.getData().getProbOcc())
                result = q1.dequeue();
            else
                result = q2.dequeue();
        }
        else if (one != null)
            result = q1.dequeue();
        else if (two != null)
            result = q2.dequeue();
        else
            result = null;
        return result;
    }

    public void makeTree(){

        Queue<TreeNode> src= new Queue<>(), dest = new Queue<TreeNode>();
        for(CharFreq CharFreq: sortedCharFreqList)
            src.enqueue(new TreeNode(CharFreq, null, null));

        TreeNode left, right;
        while (!src.isEmpty()){
            left = dequeue_smallest(src, dest);
            right = dequeue_smallest(src, dest);
            if (left != null && right != null){
                TreeNode combined_node = combine_tree_nodes(left, right);
                dest.enqueue(combined_node);
            }
            else if (left != null)
                dest.enqueue(left);
            else
                dest.enqueue(right);
        }
        while (dest.size() > 1){
            left = dest.dequeue();
            right = dest.dequeue();
            TreeNode combined_node = combine_tree_nodes(left, right);
            dest.enqueue(combined_node);
        }
        huffmanRoot = dest.dequeue();

    }

    public static TreeNode combine_tree_nodes(TreeNode left, TreeNode right){
        double combined_freq = left.getData().getProbOcc() + right.getData().getProbOcc();
        CharFreq combined_CharFreq_node = new CharFreq(null, combined_freq);
        TreeNode combined_node = new TreeNode(combined_CharFreq_node, left, right);
        return combined_node;
    }


    /**
     * Uses huffmanRoot to create a string array of size 128, where each
     * index in the array contains that ASCII character's bitstring encoding. Characters not
     * present in the huffman coding tree should have their spots in the array left null.
     * Set encodings to this array.
     */
    public void makeEncodings() {
        String[] codes = new String[128];
        ArrayList<String> bits = new ArrayList<>();
        beforeJacob(huffmanRoot, codes, bits);
        encodings = codes;
    }

    private void beforeJacob(TreeNode huffmanRoot, String[] codes, ArrayList<String> bits) {
        if(huffmanRoot.getData().getCharacter()!=null){
            codes[huffmanRoot.getData().getCharacter()]=String.join("", bits);
            bits.remove(bits.size()-1);
            return;

        }
        if (huffmanRoot.getLeft() == null) {
        } else {
            bits.add("0");
        }
        beforeJacob(huffmanRoot.getLeft(), codes, bits);
        if (huffmanRoot.getRight() == null) {
        } else {
            bits.add("1");
        }
        beforeJacob(huffmanRoot.getRight(), codes, bits);
        if (bits.isEmpty()) {
            return;
        } else {
            bits.remove(bits.size() - 1);
        }
    }

    /**
     * Using encodings and filename, this method makes use of the writeBitString method
     * to write the final encoding of 1's and 0's to the encoded file.
     *
     * @param encodedFile The file name into which the text file is to be encoded
     */
    public void encode(String encodedFile) {
        StdIn.setFile(fileName);

        /* Your code goes here */
        String str = ("");
        if (StdIn.hasNextChar()) {
            do {
                str += encodings[(int) StdIn.readChar()];

            } while (StdIn.hasNextChar());
        }
        writeBitString(encodedFile,str);

    }

    /**
     * Writes a given string of 1's and 0's to the given file byte by byte
     * and NOT as characters of 1 and 0 which take up 8 bits each
     * DO NOT EDIT
     *
     * @param filename The file to write to (doesn't need to exist yet)
     * @param bitString The string of 1's and 0's to write to the file in bits
     */
    public static void writeBitString(String filename, String bitString) {
        byte[] bytes = new byte[bitString.length() / 8 + 1];
        int bytesIndex = 0, byteIndex = 0, currentByte = 0;

        // Pad the string with initial zeroes and then a one in order to bring
        // its length to a multiple of 8. When reading, the 1 signifies the
        // end of padding.
        int padding = 8 - (bitString.length() % 8);
        String pad = "";
        for (int i = 0; i < padding-1; i++) pad = pad + "0";
        pad = pad + "1";
        bitString = pad + bitString;

        // For every bit, add it to the right spot in the corresponding byte,
        // and store bytes in the array when finished
        for (char c : bitString.toCharArray()) {
            if (c != '1' && c != '0') {
                System.out.println("Invalid characters in bitstring");
                return;
            }

            if (c == '1') currentByte += 1 << (7-byteIndex);
            byteIndex++;

            if (byteIndex == 8) {
                bytes[bytesIndex] = (byte) currentByte;
                bytesIndex++;
                currentByte = 0;
                byteIndex = 0;
            }
        }

        // Write the array of bytes to the provided file
        try {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(bytes);
            out.close();
        }
        catch(Exception e) {
            System.err.println("Error when writing to file!");
        }
    }

    /**
     * Using a given encoded file name, this method makes use of the readBitString method
     * to convert the file into a bit string, then decodes the bit string using the
     * tree, and writes it to a decoded file.
     *
     * @param encodedFile The file which has already been encoded by encode()
     * @param decodedFile The name of the new file we want to decode into
     */
    public void decode(String encodedFile, String decodedFile) {
        StdOut.setFile(decodedFile);

        /* Your code goes here */
        makeEncodings();
        String bits = readBitString(encodedFile);

        TreeNode roots = huffmanRoot;
        char[] charArray = bits.toCharArray();
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            Character strings = charArray[i];
            if (strings != '0') {
            } else {
                roots = roots.getLeft();
            }
            if (strings != '1') {
            } else {
                roots = roots.getRight();
            }
            if (roots.getData().getCharacter() == null) {
                continue;
            }
            StdOut.print(roots.getData().getCharacter());
            roots = huffmanRoot;
        }
    }

    /**
     * Reads a given file byte by byte, and returns a string of 1's and 0's
     * representing the bits in the file
     * DO NOT EDIT
     *
     * @param filename The encoded file to read from
     * @return String of 1's and 0's representing the bits in the file
     */
    public static String readBitString(String filename) {
        String bitString = "";

        try {
            FileInputStream in = new FileInputStream(filename);
            File file = new File(filename);

            byte bytes[] = new byte[(int) file.length()];
            in.read(bytes);
            in.close();

            // For each byte read, convert it to a binary string of length 8 and add it
            // to the bit string
            for (byte b : bytes) {
                bitString = bitString +
                        String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            }

            // Detect the first 1 signifying the end of padding, then remove the first few
            // characters, including the 1
            for (int i = 0; i < 8; i++) {
                if (bitString.charAt(i) == '1') return bitString.substring(i+1);
            }

            return bitString.substring(8);
        }
        catch(Exception e) {
            System.out.println("Error while reading file!");
            return "";
        }
    }

    /*
     * Getters used by the driver.
     * DO NOT EDIT or REMOVE
     */

    public String getFileName() {
        return fileName;
    }

    public ArrayList<CharFreq> getSortedCharFreqList() {
        return sortedCharFreqList;
    }

    public TreeNode getHuffmanRoot() {
        return huffmanRoot;
    }

    public String[] getEncodings() {
        return encodings;
    }
}
