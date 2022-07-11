package ca.uvic.ecg.webModel;

public class ErrorInfo {

    private String errorMessage;
    private Integer errorCode;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorInfo(String msg, Integer errorCode) {
        this.errorMessage = msg;
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    //TODO: get rid of the code part, since we don;t have to return the status code here.
    public static final ErrorInfo OK = new ErrorInfo("OK.",1000);
    public static final ErrorInfo UNKNOWN = new ErrorInfo("Unknown Error, " +
            "might be the format of input Object",1001);
    public static final ErrorInfo DUPLICATE_USER = new ErrorInfo("User is already registered, " +
            "please check again.",1003);
    public static final ErrorInfo DUPLICATE_Device = new ErrorInfo("Device is already registered, " +
            "please check again.",1004);
    public static final ErrorInfo UNAUTHORIZED = new ErrorInfo("Unauthorized User.",1006);
    public static final ErrorInfo LOGIN_SUCCESS = new ErrorInfo("Login successfully.",1010);
    public static final ErrorInfo INVALID_VERIFYCODE = new ErrorInfo("Sorry, your verification " +
            "code is not valid, please try again",1011);
    public static final ErrorInfo InvalidCliniId = new ErrorInfo("Invalid clinicId, please check " +
            "again.",1012);
    public static final ErrorInfo InvalidDeviceId = new ErrorInfo("Invalid deviceId, please check " +
            "again.",1013);
    public static final ErrorInfo InvalidPatientId = new ErrorInfo("Invalid patientId, please check " +
            "again.",1015);
    public static final ErrorInfo InvalidEcgTestId = new ErrorInfo("Invalid Ecg Test Id, please check " +
            "again.",1016);
    public static final ErrorInfo NoData = new ErrorInfo("There are no relevant data in the " +
            "database , please check again.",1017);
    public static final ErrorInfo UpdatedSuccess = new ErrorInfo("Updated information successfully",1018);
    public static final ErrorInfo NoEmail = new ErrorInfo("There is no such email in the database",1020);
    public static final ErrorInfo WrongCode = new ErrorInfo("Your verification code is not matching",1021);
    public static final ErrorInfo Expired = new ErrorInfo("Your verification code is expired.",1022);
    public static final ErrorInfo NoPatient = new ErrorInfo("the patient id doesn't exist in the " +
            "database, please check the patient-id in the url",1023);
    public static final ErrorInfo UnauthroziedPatient = new ErrorInfo("”the ecg test " +
            "doesn't belong to the current patient, please check the request again.",1024);
    public static final ErrorInfo ConflictClinicId = new ErrorInfo("Your clinic Id of input " +
            "object is not matching the clinic in the url.",1025);
    public static final ErrorInfo UnKnown = new ErrorInfo("Unknown Error, " +
            "might be the format of input Object",1026);
    public static final ErrorInfo DUPLICATE_RECORD = new ErrorInfo("Your record is already scheduled",1027);
    public static final ErrorInfo Conflict_Schedule = new ErrorInfo("Your Schedule is " +
            "Conflict with others",1028);
    public static final ErrorInfo DUPLICATE_EcgTest = new ErrorInfo("Your EcgTest is " +
            "already scheduled or conflict with others",1029);
    public static final ErrorInfo DUPLICATE_Phone = new ErrorInfo("The macAddress of your phone " +
            "is already registered in the database",1030);
    public static final ErrorInfo No_EcgTest = new ErrorInfo("Your EcgTest is " +
            "not created yet",1031);
    public static final ErrorInfo DublicateData = new ErrorInfo("Find data with same startTime, updated it",1032);
    public static final ErrorInfo No_Appointment = new ErrorInfo("Cannot find the appointment",1033);
    public static final ErrorInfo MissingInput = new ErrorInfo("Some input is missing",1034);
    public static final ErrorInfo WrongClinicName = new ErrorInfo("Invalid Clinic Name",1035);
    public static final ErrorInfo NoTemplate = new ErrorInfo("There is not template",1036);
    public static final ErrorInfo AppointmentNotStarted = new ErrorInfo("Your Appointment is not started yet",1037);
    public static final ErrorInfo InvalidNurseId = new ErrorInfo("Invalid clinicId, please check again.",1038);
    public static final ErrorInfo Invalid_Input = new ErrorInfo("Input is illegal, please check the date",1039);
    public static final ErrorInfo Short_Pswd = new ErrorInfo("Password should be longer than 10",1040);
    public static final ErrorInfo Missing_Input = new ErrorInfo("Some input are missing",1041);
    public static final ErrorInfo Has_Returned = new ErrorInfo("Device has already returned, please check input",1042);
    public static final ErrorInfo Is_Running = new ErrorInfo("The Test is still in progress, please terminate it first", 1043);

    public static final ErrorInfo UnauthroziedComputer = new ErrorInfo("”You are accessing computer " +
            "form other clinic.",1044);
}
