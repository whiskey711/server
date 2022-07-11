package ca.uvic.ecg.model;

public class PhoneResponse {
    private Integer frequency;
    private Boolean containData;

    public PhoneResponse(Integer freq, Boolean Cont) {
        this.containData = Cont;
        this.frequency = freq;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public Boolean getcontainData() {
        return containData;
    }

    public void setcontainData(Boolean containData) {
        this.containData = containData;
    }
}
