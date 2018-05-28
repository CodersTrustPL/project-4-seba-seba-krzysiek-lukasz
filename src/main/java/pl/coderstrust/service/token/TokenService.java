package pl.coderstrust.service.token;

import org.springframework.stereotype.Service;
import pl.coderstrust.model.Token;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
public class TokenService {

  private HashMap<String, Token> tokens = new HashMap<>();

  String generateToken() {
    String number;
    do {
      number = UUID.randomUUID().toString();
    } while (tokenExist(number));
    tokens.put(number, new Token(number));
    return number;
  }

  boolean validateToken(String number) {
    Token tokenToValidate = tokens.get(number);
    if (tokenToValidate == null) {
      return false;
    }
    if (tokenToValidate.getDateTime().isBefore(LocalDateTime.now().minusMinutes(15))) {
      tokens.remove(number);
      return false;
    }
    return true;
  }

  boolean tokenExist(String number) {
    return tokens.containsKey(number);
  }
}
