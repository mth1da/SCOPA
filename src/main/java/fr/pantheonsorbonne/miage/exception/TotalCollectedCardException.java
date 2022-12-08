package main.java.fr.pantheonsorbonne.miage.exception;

public class TotalCollectedCardException extends Throwable {
    public TotalCollectedCardException(int count){
        super("Total collected card is "+count+" (must be 40)");
    }

}