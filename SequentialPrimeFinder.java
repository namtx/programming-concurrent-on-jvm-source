public class SequentialPrimeFinder extends AbstractPrimeFinder {

    public static void main(String[] args) {
        new SequentialPrimeFinder().timeAndCompute(10_000_000);
    }

    @Override
    public int countPrimes(int number) {
        return countPrimesInRange(1, number);
    }
}
