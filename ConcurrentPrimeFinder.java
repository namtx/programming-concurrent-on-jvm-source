import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ConcurrentPrimeFinder extends AbstractPrimeFinder {
    private final int poolSize;
    private final int numberOfParts;

    public ConcurrentPrimeFinder(int poolSize, int numberOfParts) {
        this.poolSize = poolSize;
        this.numberOfParts = numberOfParts;
    }

    public static void main(String[] args) {
        new ConcurrentPrimeFinder(2, 2).timeAndCompute(10_000_000);
    }

    @Override
    public int countPrimes(int number) {
        int count = 0;
        try {
            final List<Callable<Integer>> partitions = new ArrayList<>();
            final int chunksPerPartition = number / this.numberOfParts;

            for (int i = 0; i < numberOfParts; i++) {
                final int lower = (i * chunksPerPartition) + 1;
                final int upper = (i == this.numberOfParts - 1) ? number : lower + chunksPerPartition - 1;
                partitions.add(() -> countPrimesInRange(lower, upper));
            }

            final ExecutorService executorPool = Executors.newFixedThreadPool(this.poolSize);
            final List<Future<Integer>> resultFromParts = executorPool.invokeAll(partitions, 1000, TimeUnit.SECONDS);
            executorPool.shutdown();
            for (final Future<Integer> result : resultFromParts) {
                count += result.get();
            }

            return count;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
