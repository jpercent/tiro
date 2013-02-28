package syndeticlogic.tiro.trial;

public class TrialMeta {
    private long id;
    private final String name;
    
    public TrialMeta(String name) {
        this.name = name;
        id = -1;
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long l) {
        this.id = l;
    }

    public String getName() {
        return name;
    }

    public TrialMeta(long id, String name) {
        this(name);
        this.id = id;
    }
}
