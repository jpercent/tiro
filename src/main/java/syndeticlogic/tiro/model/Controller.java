package syndeticlogic.tiro.model;


public class Controller {
    private Long id;
    private TrialMeta trialMeta;
    private ControllerMeta controllerMeta;
    
    public Controller(Long id, TrialMeta trialMeta, ControllerMeta controllerMeta) {
        this.id = id;
        this.trialMeta = trialMeta;
        this.controllerMeta = controllerMeta;
    }
    
    public Controller(TrialMeta trialMeta, ControllerMeta controllerMeta) {
        this(-1L, trialMeta, controllerMeta);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrialMeta getTrialMeta() {
        return trialMeta;
    }

    public void setTrialMeta(TrialMeta trialMeta) {
        this.trialMeta = trialMeta;
    }

    public ControllerMeta getControllerMeta() {
        return controllerMeta;
    }

    public void setControllerMeta(ControllerMeta controllerMeta) {
        this.controllerMeta = controllerMeta;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((controllerMeta == null) ? 0 : controllerMeta.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((trialMeta == null) ? 0 : trialMeta.hashCode());
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
        Controller other = (Controller) obj;
        if (controllerMeta == null) {
            if (other.controllerMeta != null)
                return false;
        } else if (!controllerMeta.equals(other.controllerMeta))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (trialMeta == null) {
            if (other.trialMeta != null)
                return false;
        } else if (!trialMeta.equals(other.trialMeta))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Controller [id=" + id + ", trialMeta=" + trialMeta + ", controllerMeta=" + controllerMeta + "]";
    }
}
