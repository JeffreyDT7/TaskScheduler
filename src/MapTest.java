/**
 * Created by jinyangsuo on 19/05/15.
 */

import java.io.*;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

public class MapTest {

    public static class Task{
        String taskName;
        int releasetime;
        int deadline;
        public Task(String s, int t1, int t2){
            taskName = s;
            releasetime = t1;
            deadline = t2;
        }

        public Task(){}

        public void setTaskName(String s){
            taskName = s;
        }
        public String getTaskName(){
            return taskName;
        }

        public void setReleasetime(int t1){
            releasetime = t1;
        }

        public int getReleasetime(){
            return releasetime;
        }

        public void setDeadline(int t2){
            deadline = t2;
        }
        public int getDeadline(){
            return deadline;
        }
    }

    static void scheduler(String file1, String file2, Integer m){
        try {
            //Read file1 and store the content in String.
            FileInputStream inputFile = new FileInputStream(file1);
            //BufferedReader readBuffer = new BufferedReader(new InputStreamReader(inputFile));
            int value; //ascii value (BufferedReader.read() return ascii value whch reads every character)

            String[] tiny = new String[100];
            for(int i = 0; i < 100; i++)
                tiny[i] = "";

            int i = 0;
            while ((value = inputFile.read()) != -1){
                char S = (char) value;
                if (S == ' ' || S == '\n'){
                    ++i;
                }else{

                    tiny[i] += S;
                }
            }

            // store strings into Task. Three string describe a task at a time.
            int total = (i+1)/3; // the number of task.
            if ( (i + 1) % 3 != 0 ) {
                System.out.println("The task attributes of file1 are illegal!");
                return;
            }

            HeapPriorityQueue<Integer, String> heap = new HeapPriorityQueue<Integer, String>();

            Task[] task = new Task[total + 1];
            for(int j = 0;  j < total + 1; j++)
                task[j] = new Task();
            int  k = 0;

            for (int j = 0; j <= i; ){
                char a = tiny[j].charAt(0);
                if (!Character.isAlphabetic(a) ) {
                    System.out.print(a+"\n");
                    System.out.println("task name is illegal!");
                    return;
                }

                // Deadline stored as key and Taskname stored as value in Entry<K,V>.
                task[k].setTaskName(tiny[j++]);
                task[k].setReleasetime(Integer.parseInt(tiny[j++]));
                task[k].setDeadline(Integer.parseInt(tiny[j++]));

                // insert
                heap.insert(task[k].getDeadline(), task[k].getTaskName());
                k++;
            }

            String sortedS = new String();

            while(!heap.isEmpty()){

                int r = 0;

                for (int j = 0; j <= total; j++){
                    if(task[j].getTaskName().equals(heap.min().getValue())) {
                        r = task[j].getReleasetime();
//                        System.out.println("hello");
                        break;
                    }

                }
//                System.out.println("hi");
//                System.out.println(heap.min().getValue());

                sortedS += heap.removeMin().getValue()+" "+ r +" ";
            }


            // whether file already exists.
            File f = new File(file2);
//            if (f.exists()){
//                System.out.println("file2 already exits");
//            }
            FileOutputStream outputFile = new FileOutputStream(f);

            String outputS = new String();
            for(int j = 0; j < total; j++)
                outputS += task[j].getTaskName();
//            outputFile.write(outputS.getBytes());
            outputFile.write(sortedS.getBytes());
        }catch (FileNotFoundException e){
            System.out.println("file1 does not exits.");
        }catch (IOException e) {
            e.printStackTrace();
        }catch (NumberFormatException e) {
            System.out.println("Release time or deadlines are illegal!");
        }


    }


    public static void main(String[] args){
        MapTest.scheduler("samplefile1.txt", "feasibleschedule1", 4);


    }

}
