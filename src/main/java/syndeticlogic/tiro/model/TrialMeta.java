package syndeticlogic.tiro.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TrialMeta {
    private long id;
    private final String os;
    private final String name;
    
    public TrialMeta(long id, String os, String name) {
        this.name = name;
        this.os = os;
        this.id = id;
    }    
    
    public TrialMeta(String os, String name) {
        this(-1, os, name);
    }
    
    public long getId() {
        return id;
    }
    
    public void setId(long l) {
        this.id = l;
    }

	public String getOS() {
		return os;
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
		result = prime * result + ((os == null) ? 0 : os.hashCode());
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
		if (os == null) {
			if (other.os != null)
				return false;
		} else if (!os.equals(other.os))
			return false;
		return true;
	}

	public static class TrialMetaRowMapper implements RowMapper<TrialMeta> {
        @Override
        public TrialMeta mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new TrialMeta(rs.getLong("id"), rs.getString("os"), rs.getString("name"));
        }
    }
}
