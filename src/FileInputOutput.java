import java.io.*;

/**
 * Created by jinyangsuo on 19/05/15.
 */
public class FileInputOutput {



    public static void main(String[] args) throws Exception{
        File inputfile = new File("samplefile1.txt");
        FileInputStream fileInputStream = new FileInputStream(inputfile);
        int value = fileInputStream.read();
        char c = (char)value;
        System.out.println(c);
        String line = new String ();
        File outputfile = new File("testoutput.txt");
        FileOutputStream writestream = new FileOutputStream(outputfile);
//        OutputStream outputStream = new OutputStream() ;
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writestream));
        writer.write("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        writer.close();
    }
}
