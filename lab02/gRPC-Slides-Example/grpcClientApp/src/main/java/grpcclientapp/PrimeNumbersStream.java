package grpcclientapp;

import io.grpc.stub.StreamObserver;
import servicestubs.IntNumber;

import java.util.concurrent.CountDownLatch;

public class PrimeNumbersStream implements StreamObserver<IntNumber> {

    boolean completed=false;
    private final CountDownLatch done;

    public PrimeNumbersStream() {
        this.done = null;
    }

    public PrimeNumbersStream(CountDownLatch done) {
        this.done = done;
    }

    @Override
    public void onNext(IntNumber reply) {
        System.out.println("Another prime number: " + reply.getIntnumber());
    }
    @Override
    public void onError(Throwable throwable) {
        System.out.println("Completed with error: "  +throwable.getMessage());
        completed=true;
        if (done != null) {
            done.countDown();
        }
    }
    @Override
    public void onCompleted() {
        System.out.println("Prime numbers completed");
        completed=true;
        if (done != null) {
            done.countDown();
        }
    }

    public boolean isCompleted() {
        return completed;
    }
}
