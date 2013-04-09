package syndeticlogic.tiro.model;

public class Trial {
    private Long id;
    private final TrialMeta meta;
    
    public Trial(Long id, TrialMeta meta) {
        this.id = id;
        this.meta = meta;
    }
    
    public Trial(TrialMeta meta){
        this(-1L, meta);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrialMeta getMeta() {
        return meta;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((meta == null) ? 0 : meta.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Trial other = (Trial) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (meta == null) {
            if (other.meta != null)
                return false;
        } else if (!meta.equals(other.meta))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "Trial [id=" + id + ", meta=" + meta + "]";
    }
}
