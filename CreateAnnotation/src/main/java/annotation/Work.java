package annotation;

/**
 * @author abogaichuk
 */
public class Work {
    private Client main;

    @Duplicate()
    private Client dupl, dupl2;

    public Work(Client main, Client dupl, Client dupl2) {
        this.main = main;
        this.dupl = dupl;
        this.dupl2 = dupl2;
    }

    public Client getMain() {
        return main;
    }

    public void setMain(Client main) {
        this.main = main;
    }

    public Client getDupl() {
        return dupl;
    }

    public void setDupl(Client dupl) {
        this.dupl = dupl;
    }

    public Client getDupl2() {
        return dupl2;
    }

    public void setDupl2(Client dupl2) {
        this.dupl2 = dupl2;
    }
}
