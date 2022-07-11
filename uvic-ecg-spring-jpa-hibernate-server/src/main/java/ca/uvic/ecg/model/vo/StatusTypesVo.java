package ca.uvic.ecg.model.vo;

public class StatusTypesVo {

    public StatusTypesVo(){}

    public enum StatusType {
        NOTSTARTED,
        HOOKUP,
        RECORDING,
        TERMINATED;
    }
}
