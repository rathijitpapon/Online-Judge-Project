import com.sun.org.apache.xerces.internal.parsers.CachingParserPool;

import java.io.*;
import java.util.concurrent.ExecutorService;

public class ReadThreadServer implements Runnable{
    NetworkUtil nc;

    public ReadThreadServer(NetworkUtil nc) {
        this.nc = nc;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (true)  {
            Object object = nc.read();
            SubmitData temp = null;
            if (object instanceof LogInData){
                String userName = ((LogInData) object).getHandle();
                String pass = ((LogInData) object).getPassword();
                String info = null;
                try {
                    info = SignIn(userName, pass);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (info == null)
                    info = "Found";
                nc.write(info);
            }
            if (object instanceof SignUpData) {
                String email = ((SignUpData) object).getEmail();
                String name = ((SignUpData) object).getName();
                String handle = ((SignUpData) object).getHandle();
                String password = ((SignUpData) object).getPassword();
                Object b = SignUp(email, name, handle, password);
                nc.write(b);
            }
            if(object instanceof SubmitData) {
                temp = (SubmitData) object;
                String lang = temp.getLanguage();
                if(lang.equalsIgnoreCase("C++")||lang.equalsIgnoreCase("C")) {
                    synchronized (this){

                        ProcessExecutor processExecutor = new ProcessExecutor();
                        try {
                            int verdictVal = processExecutor.CompilationCpp(temp);
                            String verdict = null;
                            verdict = verdictSet(verdictVal, verdict);
                            nc.write(new VerdictData(verdict, temp.getProblem()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else if(lang.equalsIgnoreCase("Java")){
                    synchronized (this) {
                        ProcessExecutor processExecutor = new ProcessExecutor();
                        try {
                            int verdictVal = processExecutor.CompilationJava(temp);
                            String verdict = null;
                            verdict = verdictSet(verdictVal, verdict);
                            nc.write(new VerdictData(verdict, temp.getProblem()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (object.toString().equals("BreakUp")){
                nc.closeConnection();
            }
        }
    }

    private String verdictSet(int verdictVal, String verdict) {
        if(verdictVal==0) {
            verdict = "Accepted";
        }
        else if (verdictVal==-1)
        {
            verdict = "Wrong Answer";
        }
        else if(verdictVal==-3)
        {
            verdict = "Compilation Error";
        }
        else if(verdictVal==1)
        {
            verdict = "Runtime Error";
        }
        else if(verdictVal==-2)
        {
            verdict = "Time Limit Exceeded";
        }
        return verdict;
    }

    private static String SignIn(String loginNameText, String pass) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("userDetails.txt"));
        try{
            while (true){
                String data = bufferedReader.readLine();
                if (data == null)
                    break;

                String[] s = data.split("\t\t");
                if((loginNameText.equals(s[1]) || loginNameText.equals(s[2])) && pass.equals(s[3])){
                    return data;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        bufferedReader.close();

        return null;
    }

    private static Object SignUp(String email, String name, String handle, String password){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("userDetails.txt"));
            BufferedWriter bufferedWriter= new BufferedWriter(new FileWriter("tmp.txt"));
            while (true){
                String data = bufferedReader.readLine();
                if (data == null || data.length()<4)
                    break;

                String s[] = data.split("\t\t");
                if(s[1].equals(handle) || s[2].equals(email)){
                    bufferedReader.close();
                    bufferedWriter.close();
                    return null;
                }
                bufferedWriter.write(data+"\n");
            }
            bufferedWriter.write(name+"\t\t"+handle+"\t\t"+email+"\t\t"+password+"\n");
            bufferedReader.close();
            bufferedWriter.close();

            BufferedReader bufferedReader1 = new BufferedReader(new FileReader("tmp.txt"));
            BufferedWriter bufferedWriter1= new BufferedWriter(new FileWriter("userDetails.txt"));
            while (true){
                String data = bufferedReader1.readLine();
                if(data == null)
                    break;
                bufferedWriter1.write(data+"\n");
            }
            bufferedReader1.close();
            bufferedWriter1.close();

        } catch (Exception e){
            e.printStackTrace();
        }

        return "Ok";
    }
}
