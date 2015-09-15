package Helpers;

public class JsonDownloadException extends Exception{
    public JsonDownloadException(){
        super("Json file not downloaded");
    }
}
