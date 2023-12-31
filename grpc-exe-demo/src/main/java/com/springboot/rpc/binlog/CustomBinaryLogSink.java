package com.springboot.rpc.binlog;

import com.google.protobuf.MessageLite;
import io.grpc.protobuf.services.BinaryLogSink;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomBinaryLogSink implements BinaryLogSink {

    private final String outPath;
    private final OutputStream out;
    private boolean closed;

    CustomBinaryLogSink(String path) throws IOException {
        File outFile = new File(path);
        outPath = outFile.getAbsolutePath();
        log.info("Writing binary logs to {}", outFile.getAbsolutePath());
        out = new BufferedOutputStream(new FileOutputStream(outFile));
    }

    String getPath() {
        return this.outPath;
    }

    @Override
    public synchronized void write(MessageLite message) {
        if (closed) {
            log.info("Attempt to write after TempFileSink is closed.");
            return;
        }
        try {
            // message.writeDelimitedTo(out);

            out.write(message.toString().getBytes(StandardCharsets.UTF_8));
            out.flush();
        } catch (IOException e) {
            log.info("Caught exception while writing", e);
            closeQuietly();
        }
    }

    @Override
    public synchronized void close() throws IOException {
        if (closed) {
            return;
        }
        closed = true;
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    private synchronized void closeQuietly() {
        try {
            close();
        } catch (IOException e) {
            log.info("Caught exception while closing", e);
        }
    }
}