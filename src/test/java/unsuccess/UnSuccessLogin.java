package unsuccess;

public class UnSuccessLogin {
    private String error;

    public void UnSuccessLogin(String error){
        this.error = error;
    }
    public String getError(){
        return error;
    }

}
