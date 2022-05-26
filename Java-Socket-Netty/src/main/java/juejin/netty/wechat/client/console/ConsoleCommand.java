package juejin.netty.wechat.client.console;

import io.netty.channel.Channel;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ConsoleCommand {

    void exec(BufferedReader bufferedReader, Channel channel) throws InterruptedException, IOException;

    default String readLine(BufferedReader bufferedReader) {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * BufferedReader.readLine() blocks until input is provided (even if the thread is interrupted. so we have this.
     *
     * @see <a href='https://stackoverflow.com/questions/3595926/how-to-interrupt-bufferedreaders-readline'>how-to-interrupt-bufferedreaders-readline'</a>
     */
    default String interruptReadLine(BufferedReader reader) throws InterruptedException, IOException {

        Pattern line = Pattern.compile("^(.*)\\R");
        Matcher matcher;
        boolean interrupted;

        StringBuilder result = new StringBuilder();
        int chr = -1;
        do {
            if (reader.ready()) {
                chr = reader.read();
            } else {
                Thread.yield();
            }
            if (chr > -1) result.append((char) chr);
            matcher = line.matcher(result.toString());
            interrupted = Thread.interrupted(); // resets flag, call only once
        } while (!interrupted && !matcher.matches());

        if (interrupted) throw new InterruptedException();

        return (matcher.matches() ? matcher.group(1) : "");
    }

}