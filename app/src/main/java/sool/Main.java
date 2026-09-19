package sool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class Main {
    private Main() {}

    public static void main(String... arguments) throws IOException {
        var bytes = System.in.readAllBytes();
        var string = new String(bytes, StandardCharsets.UTF_8);

        System.out.println(bytes.length);

        var lexer = new Lexer(string);

        System.out.println(lexer.toStream().toList());
    }
}
