package utilcalc.core.model.output;

import java.math.BigDecimal;
import java.util.List;
import utilcalc.core.model.Payment;

public class TestOutputDataFactory {

    public static Payment createPayment() {

        return new Payment(
                "Leden - Prosinec",
                new BigDecimal("12"),
                new BigDecimal("500"),
                new BigDecimal("6000"));
    }

    public static HeatingPayment createHeatingPayment() {

        return new HeatingPayment(
                "Únor",
                new BigDecimal("0.5"),
                new BigDecimal("879.36"),
                new BigDecimal("10992"),
                new BigDecimal("0.16"));
    }

    public static List<Payment> createPayments() {
        Payment payment = createPayment();

        return List.of(payment);
    }

    public static List<HeatingPayment> createHeatingPayments() {
        HeatingPayment heatingPayment = createHeatingPayment();

        return List.of(heatingPayment);
    }

    public static DepositSection createDepositSection() {
        List<Payment> payments = createPayments();

        return new DepositSection("Zálohy", new BigDecimal("6000"), payments);
    }

    public static OtherFeeSection createOtherFeeSection() {
        List<Payment> payments = createPayments();

        return new OtherFeeSection("Ostatní", new BigDecimal("12000"), payments);
    }

    public static HeatingFeeSection createHeatingFeeSection() {
        List<HeatingPayment> heatingPayments = createHeatingPayments();

        return new HeatingFeeSection("Vytápění", new BigDecimal("8000"), heatingPayments);
    }
}
