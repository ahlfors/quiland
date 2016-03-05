package feuyeux.strualgo.sort;

public interface ISort {
    boolean isDescending();

    int[] sort(int[] number);

    boolean compare(int x, int y);
}
