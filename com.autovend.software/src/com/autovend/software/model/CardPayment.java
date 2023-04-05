package com.autovend.software.model;

import java.math.BigDecimal;
import java.util.Map;

import com.autovend.Card.CardData;
import com.autovend.external.CardIssuer;

public class CardPayment extends PaymentMethod {

    public CardPayment(CustomerSession currentSession) {
        super(currentSession);
    }

    private CardData cardData;
    public Map<String, CardIssuer> cardIssuers;

    public void processPayment(BigDecimal amount) {
        String cardIssuerName = cardData.getType();
        CardIssuer issuer = cardIssuers.get(cardIssuerName);
        if (issuer == null) {
            // System.out.println("Issuer not found");
        } else {
            int holdNumber = issuer.authorizeHold(cardData.getNumber(), amount);

            if (holdNumber == -1) {
                // System.out.println("Insufficient hold - try another payment method. ");
            } else {
                boolean transcationResult = issuer.postTransaction(cardData.getNumber(), holdNumber, amount);
                if (transcationResult == true) {
                    currentSession.addPayment(amount);

                } else {
                    // System.out.println("Transaction failed. ");

                }

            }

        }

    }

    @Override
    public String getPaymentMethodName() {
        // TODO Auto-generated method stub
        return "Card";
    }

    public void setCardData(CardData cardDataToSet) {
        this.cardData = cardDataToSet;
    }

}
