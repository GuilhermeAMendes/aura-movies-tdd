package br.ifsp.demo.domain.exception;

public class InvalidNoteInterval extends IllegalArgumentException{
    public InvalidNoteInterval(String message) {
        super(message);
    }
}
