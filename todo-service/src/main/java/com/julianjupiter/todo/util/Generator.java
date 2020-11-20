package com.julianjupiter.todo.util;

/*import com.julianjupiter.peoplepower.service.security.config.PasswordEncoderType;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;*/

import java.util.Base64;

public class Generator {
    public static void main(String[] args) {

/*        final var clientSecret1 = UUID.randomUUID().toString();
        System.out.println(clientSecret1 + "|" + password(PasswordEncoderType.BCRYPT, clientSecret1));
        final var clientSecret2 = UUID.randomUUID().toString();
        System.out.println(clientSecret2 + "|" + password(PasswordEncoderType.BCRYPT, clientSecret2));

        final var password = "password";
        System.out.println("admin password:" + password(PasswordEncoderType.BCRYPT, password));
        System.out.println("joserizal password:" + password(PasswordEncoderType.BCRYPT, password));

        String peoplePowerClient = encodeBase64("peoplepower-client:61c89681-5c3f-487d-ab40-d11ee37b627b");
        System.out.println(peoplePowerClient);
        String peoplePowerServer = encodeBase64("peoplepower-server:e84f7073-d86a-4bf5-baeb-e15ba8ab6f97");
        System.out.println(peoplePowerServer);*/
    }

/*    public static String password(PasswordEncoderType passwordEncoderType, String password ) {
        PasswordEncoderType argon2 = PasswordEncoderType.ARGON2;
        PasswordEncoderType bcrypt = PasswordEncoderType.BCRYPT;
        PasswordEncoderType pbkdf2 = PasswordEncoderType.PBKDF2;
        PasswordEncoderType scrypt = PasswordEncoderType.SCRYPT;

        Map<String, PasswordEncoder> encoders = Map.of(
                argon2.value(), argon2.newPasswordEncoder(),
                bcrypt.value(), bcrypt.newPasswordEncoder(),
                pbkdf2.value(), pbkdf2.newPasswordEncoder(),
                scrypt.value(), scrypt.newPasswordEncoder()
        );

        final var passwordEncoder = new DelegatingPasswordEncoder(passwordEncoderType.value(), encoders);

        return passwordEncoder.encode(password);
    }*/

    public static String encodeBase64(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }
}