package br.ifsp.demo.exception;

public class InvalidNoteInterval extends IllegalArgumentException{
    public InvalidNoteInterval(String message) {
        super(message);
    }
}
