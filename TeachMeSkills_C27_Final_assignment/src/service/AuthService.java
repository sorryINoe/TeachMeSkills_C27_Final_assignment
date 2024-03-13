package service;

import coder.Coder;
import session.Session;
import storage.StorageMock;

public class AuthService {

    public static Session auth(String login, String password){
        StorageMock storage = new StorageMock();
        String loginFromStorage = storage.getLogin();
        String passwordFromStorage = storage.getPassword();

        String decodedLogin = Coder.decode(loginFromStorage);
        String decodedPassword = Coder.decode(passwordFromStorage);

        if(login.equalsIgnoreCase(decodedLogin) && password.equals(decodedPassword)){
            System.out.println("Ok");
            return new Session();
        }
        else {
            System.out.println("(((");
            return null;
        }

    }

}
