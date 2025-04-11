package co.com.ancas.playground.sec08;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileWriter {
    private final Path path;
    private BufferedWriter bufferedWriter;

    public FileWriter(Path path) {
        this.path = path;
    }

    private void createFile(){
        try {
            this.bufferedWriter= Files.newBufferedWriter(path);
        }catch (Exception e){
            throw new RuntimeException("Error creating file",e);
        }
    }

    private  void closeFile(){
        try {
            this.bufferedWriter.close();
        }catch (Exception e){
            throw new RuntimeException("Error closing file",e);
        }
    }

    private void write(String content) {
        try {
            this.bufferedWriter.write(content);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Mono<Void> create(Flux<String> flux, Path path) {
        var writer = new FileWriter(path);
        return flux.doOnNext(writer::write)
                .doFirst(writer::createFile)
                .doFinally(s -> writer.closeFile())
                .then();
    }
}
