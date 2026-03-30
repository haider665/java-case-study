package com.solvians.showcase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

public class CertificateUpdateGenerator {
    private final int threads;
    private final int quotes;

    public CertificateUpdateGenerator(int threads, int quotes) {
        this.threads = threads;
        this.quotes = quotes;
    }

    public Stream<CertificateUpdate> generateQuotes() {
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        List<Future<CertificateUpdate>> futures = new ArrayList<>();

        for (int i = 0; i < threads * quotes; i++) {
            futures.add(executor.submit(CertificateUpdate::new));
        }

        List<CertificateUpdate> certificateUpdateList = new ArrayList<>();
        for (Future<CertificateUpdate> future : futures) {
            try {
                certificateUpdateList.add(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        return certificateUpdateList.stream();
    }
}
