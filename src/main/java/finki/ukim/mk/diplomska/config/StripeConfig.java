package finki.ukim.mk.diplomska.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @Value("${stripe.secretKey}")
    private String apiKey;

    @PostConstruct
    public void init(){

        Stripe.apiKey = apiKey;
    };
}
