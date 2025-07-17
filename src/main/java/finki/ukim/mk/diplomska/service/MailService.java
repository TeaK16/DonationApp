package finki.ukim.mk.diplomska.service;

public interface MailService {
    void sendDonationConfirmation(String toEmail, String subject, String body);
}
