package session;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public final class Session {

    private String accessToken;
    private Date expDate;

    public Session(){
        setAccessToken();
        setExpDate();
    }

    public boolean isSessionAlive(){
        if(this.accessToken.length() == 16 && this.expDate.after(new Date())){
            return true;
        }
        else {
            return false;
        }
    }

    private void setAccessToken(){
        String symbols = "abcdefghijklmnopqrstuvwxyz0123456789";

        this.accessToken = new Random().ints(16,8, symbols.length())
                .mapToObj(symbols::charAt)
                .map(Objects::toString)
                .collect(Collectors.joining());
    }
    private void setExpDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE,1);

        this.expDate = calendar.getTime();
    }
}
