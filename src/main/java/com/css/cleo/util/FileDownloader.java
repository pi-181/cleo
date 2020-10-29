package com.css.cleo.util;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class FileDownloader implements Runnable {
    private final String link;
    private final File outFile;
    private final Consumer<Double> progressConsumer;
    private final Consumer<Exception> exceptionConsumer;
    private final Consumer<File> onDone;
    public boolean stop = false;

    public FileDownloader(String link,
                          File outFile,
                          Consumer<Double> progressConsumer,
                          Consumer<Exception> exceptionConsumer,
                          Consumer<File> onDone) {
        this.link = link;
        this.outFile = outFile;
        this.progressConsumer = progressConsumer;
        this.exceptionConsumer = exceptionConsumer;
        this.onDone = onDone;
    }

    public void run() {
        try {
            URL url = new URL(link);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            long completeFileSize = httpConnection.getContentLength();

            BufferedInputStream in = new BufferedInputStream(httpConnection.getInputStream());
            FileOutputStream fos = new FileOutputStream(outFile);
            BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);

            byte[] data = new byte[1024];
            long downloadedFileSize = 0;

            int x;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                if (stop)
                    return;

                downloadedFileSize += x;

                final double finalDownloadedFileSize = downloadedFileSize;
                SwingUtilities.invokeLater(() ->
                        progressConsumer.accept(finalDownloadedFileSize / completeFileSize));

                bout.write(data, 0, x);
            }
            bout.close();
            in.close();

            onDone.accept(outFile);
        } catch (IOException e) {
            exceptionConsumer.accept(e);
        }
    }

    public void stop() {
        stop = true;
    }

    public static FileDownloader downloadFile(String link, File outFile, Consumer<Double> progressConsumer,
                                    Consumer<Exception> exceptionConsumer, Consumer<File> onDone) {
        FileDownloader fileDownloader = new FileDownloader(link, outFile, progressConsumer, exceptionConsumer, onDone);
        Thread thread = new Thread(fileDownloader);
        thread.setDaemon(true);
        thread.setName("FileDownloader");
        thread.start();
        return fileDownloader;
    }

}
