package syndeticlogic.tiro.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TrialMeta {
    private long id;
    private final String name;
    
    public TrialMeta(long id, String name) {
        this.name = name;
        this.id = id;
    }    
    
    public TrialMeta(String name) {
        this(-1, name);
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        TrialMeta other = (TrialMeta) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    public static class TrialMetaRowMapper implements RowMapper<TrialMeta> {
        @Override
        public TrialMeta mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TrialMeta(rs.getLong("id"), rs.getString("name"));
        }
    }   
}
