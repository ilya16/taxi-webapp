package model;

import model.utils.Encryptor;

public class Main {
    public static void main(String[] args) {
        String str = "123";
        String hash = Encryptor.hashPassword(str);

        System.out.println(Encryptor.hashPassword(str));
        System.out.println(Encryptor.hashPassword(str));

        System.out.println(Encryptor.checkPass(str, Encryptor.hashPassword(str)));
        System.out.println(Encryptor.checkPass("123", Encryptor.hashPassword(str)));
        System.out.println(Encryptor.checkPass(str, Encryptor.hashPassword(str)));
        System.out.println(Encryptor.checkPass("123", Encryptor.hashPassword(str)));

    }
}
