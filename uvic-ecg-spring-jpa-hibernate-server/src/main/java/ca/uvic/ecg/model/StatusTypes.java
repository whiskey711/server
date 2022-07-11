package ca.uvic.ecg.model;

public class StatusTypes {

    public StatusTypes(){}

    public enum StatusType {
        NOTSTARTED, // 0
        HOOKUP, // 1
        RECORDING, // 2
        TERMINATED; // 3
    }
}
