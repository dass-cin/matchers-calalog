package br.cin.ufpe.dass.matchers.exception;

import java.io.File;

public class InvalidOntologyFileException extends Exception {
    private String file;

    public InvalidOntologyFileException(File file) {
        this.file = file.getName();
    }

    public String getFile() {
        return this.file;
    }

    public void setFile(String file) {
        this.file = file;
    }

}
