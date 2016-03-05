package feuyeux.pattern.b.observer.dem;

public class BabyEvent extends java.util.EventObject {
    private static final long serialVersionUID = 1L;
    private BabySource source;

    public BabyEvent(BabySource source) {
        super(source);
        this.source = source;
    }

    public BabySource getSource() {
        return source;
    }
}
