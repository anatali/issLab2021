/**
 AnswerAvailable
 ===============================================================
 Utility class to capture information about the reply to a request
 sent by the server over the ws connection.
 The put operation is called by onMessage
 ===============================================================
 */
package it.unibo.supports;

public class AnswerAvailable{
    private String  answer  = null;
    //private boolean engaged = false;
    //public void engage(){ engaged = true;}
    public synchronized void put(String info, String move) {
        //if( engaged ){
            answer = info;
            notify();
       // }else{  System.out.println("        AnswerAvailable | put not engaged for info=" + info + " move=" + move);  }
    }
    public synchronized String get( ) {
        while (answer == null){
            try { wait(); }
            catch (InterruptedException e) { }
            finally { }
        }
        String myAnswer = answer;
        answer           = null;
        //engaged          = false;
        return myAnswer;
    }
}